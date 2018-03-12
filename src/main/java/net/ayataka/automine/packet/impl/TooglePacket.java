package net.ayataka.automine.packet.impl;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class TooglePacket implements IMessage {
    @Getter
    private boolean mine, cut;

    public TooglePacket(){

    }

    public TooglePacket(boolean mine, boolean cut) {
        this.mine = mine;
        this.cut = cut;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.mine = buf.readBoolean();
        this.cut = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.mine);
        buf.writeBoolean(this.cut);
    }
}
