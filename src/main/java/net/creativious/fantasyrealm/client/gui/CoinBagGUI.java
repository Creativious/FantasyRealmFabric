package net.creativious.fantasyrealm.client.gui;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.creativious.fantasyrealm.FantasyRealm;
import net.creativious.fantasyrealm.registration.items.CoinBagItem;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;

public class CoinBagGUI extends SyncedGuiDescription {
    private static final int INVENTORY_SIZE = 1;

    public CoinBagGUI(int syncID, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ScreenHandlerType.GENERIC_3X3,syncID, playerInventory, getBlockInventory(context, INVENTORY_SIZE), getBlockPropertyDelegate(context));

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(300, 200);
        root.setInsets(Insets.ROOT_PANEL);

        WItemSlot itemSlot = WItemSlot.of(blockInventory, 0);
        root.add(itemSlot, 4, 1);

        root.add(this.createPlayerInventoryPanel(), 0, 3);

        root.validate(this);
    }
}
