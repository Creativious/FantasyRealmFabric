package net.creativious.fantasyrealm.levelingsystem.stats;

import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WText;
import io.github.cottonmc.cotton.gui.widget.data.Color;
import net.creativious.fantasyrealm.levelingsystem.PlayerStatsManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public abstract class Stat {
    public int level;
    public int experience;

    public String name = "Stat";

    public String display_name = "Stat";

    public int calcExperienceForLevel(int level) {
        return (int) (((level) * 500)^4)/15^2;
    }

    public String getName() {
        return this.name;
    }

    public int getLevel() {
        return this.level;
    }

    public int getExperience() {
        return this.experience;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void addLevels(int levels) {
        this.level += levels;
    }

    public void addExperience(int experience) {
        this.experience += experience;
        this.autoFixLevel();
    }

    /**
     * Writes NBT Data for the player
     *
     * I suggest not touching this one
     *
     * @param tag
     */
    public void writeNBT(@NotNull NbtCompound tag) {
        tag.putInt(this.name.toString() + "Level", this.level);
        tag.putInt(this.name.toString() + "Experience", this.experience);
    }

    /**
     * Reads NBT Data for the player
     *
     * I suggest not touching this one
     *
     * @param tag
     */
    public void readNBT(@NotNull NbtCompound tag) {
        this.level = tag.getInt(this.name.toString() + "Level");
        this.experience = tag.getInt(this.name.toString() + "Experience");
    }

    /**
     * Returns the experience that the current player needs to level up from their current level to the next excluding already gained xp.
     *
     * @return
     */
    public int neededExperienceForLevelUp() {
        return calcExperienceForLevel(this.level + 1);
    }

    public void readBuffer(@NotNull PacketByteBuf buf) {
        this.setLevel(buf.readInt()); // Stat Level
        this.setExperience(buf.readInt()); // Stat Experience

    }

    public void writeBuffer(@NotNull PacketByteBuf buf) {
        buf.writeInt(this.getLevel()); // Stat level
        buf.writeInt(this.getExperience()); // Stat Experience

    }

    public int calcTotalNeedExperienceForLevel(int level) {
        int subEXP = 0;
        for (int i = 0; i < level; i++) {
            subEXP += calcExperienceForLevel(i);
        }
        return subEXP;
    }

    public void autoFixLevel() {
        int imaginaryLevel = 0;
        for (int i = 0; this.experience > this.calcTotalNeedExperienceForLevel(i + 1); i++) {
            imaginaryLevel = i + 1;
        }
        this.level = imaginaryLevel;
    }

    public int calcCurrentExperienceBasedOnLevel() {
        int subEXP = 0;
        for (int i = 0; i < this.level; i++) {
            subEXP += calcExperienceForLevel(i);
        }
        return (this.experience - subEXP);
    }

    @Environment(EnvType.CLIENT)
    public void createStatGUI(WPlainPanel root, int y_pos, MinecraftClient client) {
        String labelString = display_name + ":";
        String levelString = "Level: " + Integer.toString(level);
        String experienceString = "Experience: " + Integer.toString(calcCurrentExperienceBasedOnLevel()) + "/" +  Integer.toString(neededExperienceForLevelUp());
        WText labelWidget = new WText(Text.literal(labelString), Color.CYAN_DYE.toRgb());
        root.add(labelWidget, 0, y_pos, client.textRenderer.getWidth(labelString), client.textRenderer.fontHeight);
        WText levelWidget = new WText(Text.literal(levelString), Color.BLUE.toRgb());
        root.add(levelWidget, client.textRenderer.getWidth(labelString) + 10%(root.getWidth()), labelWidget.getY(), client.textRenderer.getWidth(levelString), client.textRenderer.fontHeight);
        WText experienceWidget = new WText(Text.literal(experienceString), 561152);
        root.add(experienceWidget, client.textRenderer.getWidth(levelString) + 20%(root.getWidth()) + client.textRenderer.getWidth(labelString), levelWidget.getY(), client.textRenderer.getWidth(experienceString), client.textRenderer.fontHeight);
    }



}
