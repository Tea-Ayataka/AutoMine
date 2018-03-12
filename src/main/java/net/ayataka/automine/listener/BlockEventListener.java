package net.ayataka.automine.listener;

import net.ayataka.automine.Automine;
import net.ayataka.automine.config.GlobalConfig;
import net.ayataka.automine.miner.VeinMiner;
import net.ayataka.automine.miner.VeinMode;
import net.ayataka.automine.util.BlockUtil;
import net.ayataka.automine.util.OredictUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BlockEventListener {
    private final List<QueueInstance> queue = new ArrayList<>();

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPlayerBlockBreak(BlockEvent.BreakEvent event) {
        // Make server world only
        if (event.getWorld().isRemote) {
            return;
        }

        if (event.isCanceled() || event.getResult() == Event.Result.DENY) {
            return;
        }

        // Check sneak
        if (event.getPlayer().isSneaking()) {
            return;
        }

        // Check harvest level
        if (!BlockUtil.isToolEffective(event.getPlayer().getHeldItemMainhand(), event.getState())) {
            return;
        }

        // Check Blacklist
        //String blockName = Block.REGISTRY.getNameForObject(event.getState().getBlock()) + "";
        String toolName = Item.REGISTRY.getNameForObject(event.getPlayer().getHeldItemMainhand().getItem()) + "";
        if (Automine.INSTANCE.getDataManager().getData(event.getPlayer()).blacklistedTools.contains(toolName)) {
            return;
        }

        // Check Ore
        boolean isOre = OredictUtil.isOre(event.getState().getBlock()) && Automine.INSTANCE.getDataManager().getData(event.getPlayer()).isEnabledMining;
        boolean isLog = OredictUtil.isLog(event.getState().getBlock()) && Automine.INSTANCE.getDataManager().getData(event.getPlayer()).isEnabledCutting;

        if (!isOre && !isLog) {
            return;
        }

        VeinMiner veinMiner = new VeinMiner(event.getPos(), event.getWorld(), GlobalConfig.blockLimit.getInt(), isOre ? VeinMode.ORE : VeinMode.TREE);

        // Destroy broke block
        BlockUtil.destroyNaturally(event.getPos(), event.getWorld(), (EntityPlayerMP) event.getPlayer());

        // Initialize a queue instance
        QueueInstance queueInstance = new QueueInstance();

        // Destroy ores (Queue)
        for (BlockPos pos : veinMiner.result) {
            queueInstance.destroyQueue.add(new DestroyQueue(((EntityPlayerMP) event.getPlayer()), event.getWorld(), pos));
        }

        // Decay leafs (resultLeaf will be empty if VeinMiner is executed as ORE mode.) (MAX 1000)
        for (BlockPos pos : veinMiner.resultLeaf.stream().limit(1000).collect(Collectors.toList())) {
            queueInstance.decayQueue.add(new DestroyQueue(((EntityPlayerMP) event.getPlayer()), event.getWorld(), pos));
        }

        this.queue.add(queueInstance);
    }

    // Process 1 blocks per tick (20 block/s)
    @SubscribeEvent
    public void onServerTick(TickEvent.WorldTickEvent event) {
        for (QueueInstance queue : this.queue) {
            if (queue.destroyQueue.isEmpty()) {
                if (!queue.decayQueue.isEmpty()) {
                    queue.decayQueue.forEach(q -> q.world.scheduleBlockUpdate(q.pos, q.world.getBlockState(q.pos).getBlock(), 0, 0));
                    queue.decayQueue.clear();
                }
                continue;
            }

            List<DestroyQueue> chunk = queue.destroyQueue.stream().limit(1).collect(Collectors.toList());

            for (DestroyQueue destroyQueue : chunk) {
                BlockUtil.destroyNaturally(destroyQueue.pos, destroyQueue.world, destroyQueue.player);
            }

            queue.destroyQueue.removeAll(chunk);
        }

        // Handle memory-leak
        this.queue.removeIf(q -> q.destroyQueue.isEmpty() && q.decayQueue.isEmpty());
    }
}

class DestroyQueue {
    public final EntityPlayerMP player;
    public final World world;
    public final BlockPos pos;

    DestroyQueue(final EntityPlayerMP player, final World world, final BlockPos pos) {
        this.player = player;
        this.world = world;
        this.pos = pos;
    }
}

class QueueInstance {
    public final List<DestroyQueue> destroyQueue = new ArrayList<>();
    public final List<DestroyQueue> decayQueue = new ArrayList<>();
}