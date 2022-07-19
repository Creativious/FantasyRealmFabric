package net.creativious.fantasyrealm.client.gui.screens;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.creativious.fantasyrealm.client.gui.CoinBagGUI;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class CoinBagScreen extends CottonInventoryScreen<CoinBagGUI> {
    public CoinBagScreen(CoinBagGUI gui, PlayerEntity player, Text title) {
        super(gui, player, title);

    }
}
