package net.creativious.fantasyrealm.levelingsystem;


import net.minecraft.nbt.NbtCompound;

public class PlayerStatsManager {
    public int overallLevel;
    public float levelProgress;
    public int skillPoints;
    public int totalLevelExperience;


    public void readNbt(NbtCompound tag) {
        this.overallLevel = tag.getInt("level");
    }

    public void writeNbt(NbtCompound tag) {
        tag.putInt("Level", this.overallLevel);
    }

    public int getLevel() {
        return this.overallLevel;
    }

    public void setLevel(int level) {
        this.overallLevel = level;
    }
}
