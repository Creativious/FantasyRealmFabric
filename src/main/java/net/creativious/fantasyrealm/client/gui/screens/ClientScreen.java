package net.creativious.fantasyrealm.client.gui.screens;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * The type Client screen.
 */
@Environment(EnvType.CLIENT)
public class ClientScreen extends CottonClientScreen {
    /**
     * Instantiates a new Client screen.
     *
     * @param description the description
     */
    public ClientScreen(GuiDescription description) {
        super(description);
    }
}
