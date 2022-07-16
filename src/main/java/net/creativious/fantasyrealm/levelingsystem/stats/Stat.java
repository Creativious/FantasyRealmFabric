package net.creativious.fantasyrealm.levelingsystem.stats;

import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.creativious.fantasyrealm.levelingsystem.PlayerStatsManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;

public abstract class Stat {
    public int level;
    public int experience;

    public String name = "Stat";

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

    @Environment(EnvType.CLIENT)
    public void createStatGUI(WPlainPanel root) {

    }



}
