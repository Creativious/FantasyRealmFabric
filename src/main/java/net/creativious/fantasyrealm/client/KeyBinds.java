package net.creativious.fantasyrealm.client;

import net.creativious.fantasyrealm.client.gui.PlayerStatsGUI;
import net.creativious.fantasyrealm.client.gui.screens.ClientScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

/**
 * The type Key binds.
 */
@Environment(EnvType.CLIENT)
public class KeyBinds {

    private static KeyBinding playerStatsGUIKeybind;

    /**
     * Init.
     */
    public static void init() {
        playerStatsGUIKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fantasyrealm.playerstatsguiopen",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "category.fantasyrealm.all"
        ));

        playerStatsGUIEvent();

    }

    /**
     * Player stats gui event.
     */
    public static void playerStatsGUIEvent() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (playerStatsGUIKeybind.wasPressed()) {
                MinecraftClient.getInstance().setScreen(new ClientScreen(new PlayerStatsGUI()));
            }

        });
    }
}
