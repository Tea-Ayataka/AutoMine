package net.ayataka.automine.packet.impl;

import net.ayataka.automine.Automine;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ConfigPacketHandler implements IMessageHandler<ConfigPacket, IMessage> {
    @Override
    public IMessage onMessage(ConfigPacket message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().player;

        // Synced task
        player.getServerWorld().addScheduledTask(() -> {
            // Sync!
            Automine.INSTANCE.getDataManager().getData(player).isEnabledMining = message.getData().isEnabledMining;
            Automine.INSTANCE.getDataManager().getData(player).isEnabledCutting = message.getData().isEnabledCutting;
            Automine.INSTANCE.getDataManager().getData(player).blacklistedTools = message.getData().blacklistedTools;

            Automine.LOGGER.info("[Server] Received settings. Synced.");
        });

        return null;
    }
}
