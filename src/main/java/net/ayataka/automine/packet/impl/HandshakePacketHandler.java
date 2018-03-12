package net.ayataka.automine.packet.impl;

import net.ayataka.automine.Automine;
import net.ayataka.automine.config.GlobalConfig;
import net.ayataka.automine.data.Data;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;

// On Client
public class HandshakePacketHandler implements IMessageHandler<HandshakePacket, IMessage> {
    @Override
    public IMessage onMessage(HandshakePacket message, MessageContext ctx) {
        // Activate this mod
        Automine.isActivated = true;

        Automine.LOGGER.info("[Client] Received handshake. Sending settings.");

        // Send Config to Server
        Data data = new Data();

        data.isEnabledMining = GlobalConfig.isEnabledMining.getBoolean();
        data.isEnabledCutting = GlobalConfig.isEnabledCutting.getBoolean();
        data.blacklistedTools = Arrays.asList(GlobalConfig.blacklistedTools.getStringList());

        return new ConfigPacket(data);
    }
}
