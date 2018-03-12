package net.ayataka.automine.key;

import net.ayataka.automine.Automine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class KeyHandler {
    private List<KeyBinding> keyBindings = new ArrayList<>();

    public KeyHandler() {
        this.keyBindings.add(new KeyBinding("key.automine_mine.desc", Keyboard.KEY_N, "key.automine.category"));
        this.keyBindings.add(new KeyBinding("key.automine_cut.desc", Keyboard.KEY_M, "key.automine.category"));

        this.keyBindings.forEach(ClientRegistry::registerKeyBinding);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        for (int i = 0; i < 2; i++) {
            if (!this.keyBindings.get(i).isPressed())
                continue;

            // error
            if (!Automine.isActivated) {
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString("\u00A7[\u00A7eAutoMine\u00A77] \u00A7c" + I18n.format("msg.automine.invalid")));
                return;
            }

            boolean[] results = Automine.INSTANCE.getPacketHandler().sendTogglePacket(i);
            if (results != null) {
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString("\u00A77[\u00A7eAutoMine\u00A77] " + I18n.format("msg.automine.mining") + ": " + (results[0] ? "\u00A7aON\u00A77" : "\u00A7cOFF\u00A77") + " " + I18n.format("msg.automine.cutting") + ": " + (results[1] ? "\u00A7aON" : "\u00A7cOFF")));
            }
        }
    }
}
