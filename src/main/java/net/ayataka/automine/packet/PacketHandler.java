package net.ayataka.automine.packet;

import net.ayataka.automine.Automine;
import net.ayataka.automine.config.GlobalConfig;
import net.ayataka.automine.packet.impl.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketHandler {
    private static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Automine.MOD_ID);

    public PacketHandler() {
        // Register (Client To Server)
        CHANNEL.registerMessage(TogglePacketHandler.class, TooglePacket.class, 0, Side.SERVER);
        CHANNEL.registerMessage(ConfigPacketHandler.class, ConfigPacket.class, 1, Side.SERVER);

        // Handshake (Server To Client)
        CHANNEL.registerMessage(HandshakePacketHandler.class, HandshakePacket.class, 2, Side.CLIENT);
    }

    @SideOnly(Side.CLIENT)
    public boolean[] sendTogglePacket(int target) {
        // Toggle and Save
        GlobalConfig.toggle(target == 0 ? GlobalConfig.isEnabledMining : GlobalConfig.isEnabledCutting);

        // Send packet
        CHANNEL.sendToServer(new TooglePacket(GlobalConfig.isEnabledMining.getBoolean(), GlobalConfig.isEnabledCutting.getBoolean()));

        return new boolean[]{GlobalConfig.isEnabledMining.getBoolean(), GlobalConfig.isEnabledCutting.getBoolean()};
    }

    public void sendHandshakeToClient(EntityPlayerMP player) {
        Automine.LOGGER.info("[Server] Player joined! Trying to send handshake.");
        CHANNEL.sendTo(new HandshakePacket(), player);
    }
}
