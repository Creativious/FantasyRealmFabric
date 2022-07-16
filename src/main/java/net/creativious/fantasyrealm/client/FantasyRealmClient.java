package net.creativious.fantasyrealm.client;

import net.creativious.fantasyrealm.network.PlayerStatsClientPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

/**
 * The type Fantasy realm client.
 */
public class FantasyRealmClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        PlayerStatsClientPacket.init();
        KeyBinds.init();
    }
}
