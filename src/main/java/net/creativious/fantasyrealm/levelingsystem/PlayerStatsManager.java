package net.creativious.fantasyrealm.levelingsystem;


import io.github.apace100.origins.Origins;
import io.github.apace100.origins.origin.Origin;
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


    /**
     * Reads NBT Data for the player
     *
     * I suggest not touching this one, except when you add a new stat
     *
     * @param tag
     */
    public void readNbt(NbtCompound tag) {
        this.level = tag.getInt("Level");
        this.totalLevelExperience = tag.getInt("TotalLevelExperience");
        blacksmithingStat.readNBT(tag);
        cookingStat.readNBT(tag);
    }

    /**
     * Writes NBT Data for the player
     *
     * I suggest not touching this one, except when you add a new stat
     *
     * @param tag
     */
    public void writeNbt(NbtCompound tag) {
        tag.putInt("Level", this.level);
        tag.putInt("TotalLevelExperience", this.totalLevelExperience);
        blacksmithingStat.writeNBT(tag);
        cookingStat.writeNBT(tag);

    }

    /**
     *
     * @return Level Integer
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Returns the experience that the current player needs to level up from their current level to the next excluding already gained xp.
     *
     * @return
     */
    public int neededExperienceForLevelUp() {
        return calcExperienceForLevel(this.level + 1);
    }

    /**
     * All the experience in total that the current player needs to have before they can level up
     *
     * @return
     */
    public int neededTotalExperienceForLevelUp() {
        int subEXP = 0;
        for (int i = 0; i < this.level; i++) {
            subEXP += calcExperienceForLevel(i);
        }
        return subEXP + neededExperienceForLevelUp();
    }

    /**
     * All the experience in total that one may need for the given level to level up.
     *
     * @param level
     * @return
     */
    public int calcTotalNeedExperienceForLevel(int level) {
        int subEXP = 0;
        for (int i = 0; i < level; i++) {
            subEXP += calcExperienceForLevel(i);
        }
        return subEXP;
    }

    /**
     * The needed experience for the individual level to level up
     *
     * @param level
     * @return
     */
    public int calcExperienceForLevel(int level) {
        return (int) (((level) * 500)^4)/15^2;
    }

    /**
     * Is an easier format for players to read as it doesn't go into the billions, think of it of a small form of the total xp.
     *
     * @return
     */
    public int calcCurrentExperienceBasedOnLevel() {
        int subEXP = 0;
        for (int i = 0; i < this.level; i++) {
            subEXP += calcExperienceForLevel(i);
        }
        return (this.totalLevelExperience - subEXP);
    }

    /**
     * Client side only helps with the percentage bar at the top left of the screen
     *
     * @return
     */
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

    /**
     * Running this will take the given total experience and will auto calculate what level the player should be at and then set them to it
     *
     */
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
        this.autoFixLevel();
    }

    public void setTotalLevelExperience(int totalLevelExperience) {
        this.totalLevelExperience = totalLevelExperience;
    }
}
