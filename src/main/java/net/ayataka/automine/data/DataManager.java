package net.ayataka.automine.data;

import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataManager {
    private final Map<UUID, Data> dataMap = new HashMap<>();

    public Data getData(final EntityPlayer player) {
        return this.getData(player.getUniqueID());
    }

    public Data getData(final UUID uuid) {
        if (!dataMap.containsKey(uuid)) {
            this.dataMap.put(uuid, new Data());
        }

        return dataMap.getOrDefault(uuid, null);
    }
}
