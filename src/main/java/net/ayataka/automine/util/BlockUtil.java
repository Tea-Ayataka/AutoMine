package net.ayataka.automine.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockUtil {
    //private static long broke;
    public static void destroyNaturally(final BlockPos pos, final World world, final EntityPlayerMP player) {
        final IBlockState blockState = world.getBlockState(pos);

        /*broke++;
        if(broke % 5 == 0) {
            world.playEvent(2001, pos, Block.getStateId(blockState));
        }*/

        // Item durability
        ItemStack itemStack = player.getHeldItemMainhand();
        if (!ItemUtil.isEmpty(itemStack)) {
            itemStack.onBlockDestroyed(world, blockState, pos, player);
        }

        // Harvest
        blockState.getBlock().removedByPlayer(blockState, world, pos, player, true); // Set block to air
        blockState.getBlock().onBlockDestroyedByPlayer(world, pos, blockState); // Call events for other mod processing
        blockState.getBlock().harvestBlock(world, player, pos, blockState, world.getTileEntity(pos), player.getHeldItemMainhand()); // Drop

        // Drop exp
        int exp = blockState.getBlock().getExpDrop(blockState, world, pos, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, player.getHeldItemMainhand()));
        blockState.getBlock().dropXpOnBlockBreak(world, pos, exp);
    }

    public static boolean isToolEffective(final ItemStack itemStack, final IBlockState state) {
        if (ItemUtil.isEmpty(itemStack)) {
            return false;
        }

        return itemStack.getItem().getToolClasses(itemStack).stream().anyMatch(type -> type.equals(state.getBlock().getHarvestTool(state)));
    }
}
