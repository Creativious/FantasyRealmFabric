package net.creativious.fantasyrealm.client.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.creativious.fantasyrealm.levelingsystem.PlayerStatsManager;
import net.creativious.fantasyrealm.levelingsystem.interfaces.IPlayerStatsManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


public class PlayerStatsGUI extends LightweightGuiDescription {

    public int scaledWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
    public int scaledHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();
    public PlayerStatsGUI() {

        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity playerEntity = (PlayerEntity) client.getCameraEntity();

        assert playerEntity != null;
        PlayerStatsManager playerStatsManager = ((IPlayerStatsManager) playerEntity).getPlayerStatsManager(playerEntity);

        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);

        root.setSize(this.scaledWidth/2, (this.scaledHeight / 2) + (this.scaledHeight / 4));

        String levelTextString = "Level: " + playerStatsManager.getLevel();
        WText levelText = new WText(Text.literal(levelTextString), 52479);

        root.add(levelText, (root.getWidth() / 2) - (client.textRenderer.getWidth(levelTextString) / 2), 2%(root.getHeight()), client.textRenderer.getWidth(levelTextString), client.textRenderer.fontHeight);

        String experienceTextString = playerStatsManager.calcCurrentExperienceBasedOnLevel() + "/" + playerStatsManager.neededExperienceForLevelUp();

        WText experienceText = new WText(Text.literal(experienceTextString), 65280);
        root.add(experienceText, (root.getWidth() / 2) - (client.textRenderer.getWidth(experienceTextString) / 2), levelText.getY() + 13%(root.getHeight()), client.textRenderer.getWidth(experienceTextString), client.textRenderer.fontHeight);


        root.setInsets(Insets.ROOT_PANEL);

        root.validate(this);
    }
}
