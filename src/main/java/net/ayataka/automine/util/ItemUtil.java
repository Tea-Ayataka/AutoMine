package net.ayataka.automine.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemUtil {
    public static boolean isEmpty(ItemStack itemStack) {
        return itemStack == null || itemStack == new ItemStack((Item) null);
    }
}
