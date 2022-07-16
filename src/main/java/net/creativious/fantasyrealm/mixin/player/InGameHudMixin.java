package net.creativious.fantasyrealm.mixin.player;

import com.mojang.blaze3d.systems.RenderSystem;
import net.creativious.fantasyrealm.levelingsystem.PlayerStatsManager;
import net.creativious.fantasyrealm.levelingsystem.interfaces.IPlayerStatsManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

/**
 * The type In game hud mixin.
 */
@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {

    private static final Identifier LEVEL_BACKGROUND = new Identifier("fantasyrealm:textures/gui/levelbackgroundtexture.png");
    @Shadow
    @Final
    @Mutable
    private final MinecraftClient client;
    @Shadow
    private int ticks;
    @Shadow
    private int scaledWidth;
    @Shadow
    private int scaledHeight;

    /**
     * Gets camera player.
     *
     * @return the camera player
     */
    @Shadow protected abstract PlayerEntity getCameraPlayer();

    /**
     * Gets text renderer.
     *
     * @return the text renderer
     */
    @Shadow public abstract TextRenderer getTextRenderer();

    @Shadow @Final private static Identifier WIDGETS_TEXTURE;


    /**
     * Instantiates a new In game hud mixin.
     *
     * @param client the client
     */
    public InGameHudMixin(MinecraftClient client) {
        this.client = client;
    }


    /**
     *
     * Renders the top left level indicator
     *
     * @param matrices
     * @param info
     */
    @Inject(method = "renderStatusBars", at = @At(value="TAIL"))
    private void renderStatusBarsMixin(MatrixStack matrices, CallbackInfo info) {
        PlayerEntity playerEntity = this.getCameraPlayer();
        if (playerEntity != null && !playerEntity.isInvulnerable()) {
            this.client.getProfiler().push("fantasyrealmLevelBar");
            PlayerStatsManager playerStatsManager = ((IPlayerStatsManager) playerEntity).getPlayerStatsManager(playerEntity);
            int displayLevel = playerStatsManager.getLevel();
            if (displayLevel > 9999) {
                displayLevel = 9999;
            }
            String string = "" + displayLevel;
            float x_pos = (0 + 18%(float) this.scaledWidth) - ((float) this.getTextRenderer().getWidth(string) / 2);
            float y_pos = (0 + 13%(float) this.scaledHeight);
            int box_x_pos = (10 % (this.scaledWidth));
            int box_y_pos = (10 % (this.scaledHeight));
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, LEVEL_BACKGROUND);
            float playerProgress = playerStatsManager.getLevelProgress();
            int barHeight = Math.round((playerProgress / 100) * 35);
            if (barHeight > 35) {
                barHeight = 35;
            } if (barHeight < 0) {
                barHeight = 0;
            }
            this.drawTexture(matrices, 0, 0, 0, 35, 35, 35, 70, 35);
            this.drawTexture(matrices, 0, 0, 35, 35, 35, barHeight, 70, 35);
            this.getTextRenderer().draw(matrices, string, (Float)(x_pos + 1), (Float)(y_pos), 0);
            this.getTextRenderer().draw(matrices, string, (Float)(x_pos - 1), (Float)(y_pos), 0);
            this.getTextRenderer().draw(matrices, string, (Float)(x_pos), (Float)(y_pos + 1), 0);
            this.getTextRenderer().draw(matrices, string, (Float)(x_pos), (Float)(y_pos - 1), 0);
            this.getTextRenderer().draw(matrices, string, (Float)(x_pos), (Float)(y_pos), 52479);

        }
    }

}
