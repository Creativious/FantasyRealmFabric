package net.creativious.fantasyrealm.levelingsystem;


import net.minecraft.nbt.NbtCompound;

public class PlayerStatsManager {
    public int level;
    public float levelProgress;
    public int skillPoints;
    public int totalLevelExperience;


    public void readNbt(NbtCompound tag) {
        this.level = tag.getInt("Level");
        this.levelProgress = tag.getFloat("LevelProgress");
        this.totalLevelExperience = tag.getInt("TotalLevelExperience");
    }

    public void writeNbt(NbtCompound tag) {
        tag.putInt("Level", this.level);
        tag.putFloat("LevelProgress", this.levelProgress);
        tag.putInt("TotalLevelExperience", this.totalLevelExperience);
    }

    public int getLevel() {
        return this.level;
    }

    public float getLevelProgress() {
        return this.levelProgress;
    }

    public int getTotalLevelExperience() {
        return this.totalLevelExperience;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setLevelProgress(float levelProgress) {
        this.levelProgress = levelProgress;
    }

    public void setTotalLevelExperience(int totalLevelExperience) {
        this.totalLevelExperience = totalLevelExperience;
    }
}
