package net.ayataka.automine.packet.impl;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.ayataka.automine.data.Data;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ConfigPacket implements IMessage {
    @Getter
    private Data data = new Data();

    public ConfigPacket(){

    }

    public ConfigPacket(Data data) {
        this.data = data;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.data.blacklistedTools.clear();
        this.data.isEnabledMining = buf.readBoolean();
        this.data.isEnabledCutting = buf.readBoolean();

        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            int length = buf.readInt();
            byte[] read = new byte[length];
            buf.readBytes(read);

            this.data.blacklistedTools.add(new String(read));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(data.isEnabledMining);
        buf.writeBoolean(data.isEnabledCutting);
        buf.writeInt(data.blacklistedTools.size());
        for (String tool : data.blacklistedTools) {
            buf.writeInt(tool.getBytes().length);
            buf.writeBytes(tool.getBytes());
        }
    }
}
