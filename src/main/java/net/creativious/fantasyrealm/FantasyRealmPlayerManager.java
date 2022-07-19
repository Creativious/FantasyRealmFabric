package net.creativious.fantasyrealm;

import net.creativious.fantasyrealm.levelingsystem.PlayerStatsManager;
import net.minecraft.nbt.NbtCompound;

public class FantasyRealmPlayerManager {

    public static PlayerStatsManager playerStatsManager = new PlayerStatsManager();

    public void readNBT(NbtCompound tag) {
        playerStatsManager.readNbt(tag);
    }

    public void writeNBT(NbtCompound tag) {
        playerStatsManager.writeNbt(tag);
    }

    public PlayerStatsManager getPlayerStatsManager() {
        return playerStatsManager;
    }

}
