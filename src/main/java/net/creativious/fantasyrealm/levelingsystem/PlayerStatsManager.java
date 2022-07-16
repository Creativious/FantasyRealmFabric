package net.creativious.fantasyrealm.levelingsystem;


import net.creativious.fantasyrealm.levelingsystem.stats.BlacksmithingStat;
import net.creativious.fantasyrealm.levelingsystem.stats.CookingStat;
import net.creativious.fantasyrealm.levelingsystem.stats.Stat;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public class PlayerStatsManager {
    public int level;
    public int skillPoints;
    public int totalLevelExperience;

    public Stat blacksmithingStat = new BlacksmithingStat();
    public Stat cookingStat = new CookingStat();


    public void readNbt(NbtCompound tag) {
        this.level = tag.getInt("Level");
        this.totalLevelExperience = tag.getInt("TotalLevelExperience");
        blacksmithingStat.readNBT(tag);
        cookingStat.readNBT(tag);
    }

    public void writeNbt(NbtCompound tag) {
        tag.putInt("Level", this.level);
        tag.putInt("TotalLevelExperience", this.totalLevelExperience);
        blacksmithingStat.writeNBT(tag);
        cookingStat.writeNBT(tag);

    }

    public int getLevel() {
        return this.level;
    }

    public int neededExperienceForLevelUp() {
        return calcExperienceForLevel(this.level + 1);
    }

    public int neededTotalExperienceForLevelUp() {
        int subEXP = 0;
        for (int i = 0; i < this.level; i++) {
            subEXP += calcExperienceForLevel(i);
        }
        return subEXP + neededExperienceForLevelUp();
    }

    public int calcTotalNeedExperienceForLevel(int level) {
        int subEXP = 0;
        for (int i = 0; i < level; i++) {
            subEXP += calcExperienceForLevel(i);
        }
        return subEXP;
    }

    public int calcExperienceForLevel(int level) {
        return (int) (((level) * 500)^4)/15^2;
    }

    public int calcCurrentExperienceBasedOnLevel() {
        int subEXP = 0;
        for (int i = 0; i < this.level; i++) {
            subEXP += calcExperienceForLevel(i);
        }
        return (this.totalLevelExperience - subEXP);
    }

    public float calcLevelProgress() {
        return (((float) calcCurrentExperienceBasedOnLevel()/neededExperienceForLevelUp()) * 100);
    }

    public float getLevelProgress() {
        return calcLevelProgress();
    }

    public int getTotalLevelExperience() {
        return this.totalLevelExperience;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void autoFixLevel() {
        int imaginaryLevel = 0;
        for (int i = 0; this.totalLevelExperience > this.calcTotalNeedExperienceForLevel(i+1); i++) {
            imaginaryLevel = i + 1;
        }
        this.level = imaginaryLevel;
    }

    public void addLevels(int levels) {
        this.level += levels;
    }

    public void addExperience(int experience) {
        this.totalLevelExperience += experience;
    }

    public void setTotalLevelExperience(int totalLevelExperience) {
        this.totalLevelExperience = totalLevelExperience;
    }
}
