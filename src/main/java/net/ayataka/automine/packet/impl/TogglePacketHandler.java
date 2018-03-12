package net.ayataka.automine.packet.impl;

import net.ayataka.automine.Automine;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TogglePacketHandler implements IMessageHandler<TooglePacket, IMessage> {
    @Override
    public IMessage onMessage(TooglePacket message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().player;

        // Synced
        player.getServerWorld().addScheduledTask(() -> {
            // Set
            Automine.INSTANCE.getDataManager().getData(player).isEnabledMining = message.isMine();
            Automine.INSTANCE.getDataManager().getData(player).isEnabledCutting = message.isCut();
        });

        return null;
    }
}
