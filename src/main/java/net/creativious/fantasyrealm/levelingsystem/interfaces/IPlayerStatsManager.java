package net.creativious.fantasyrealm.levelingsystem.interfaces;

import net.creativious.fantasyrealm.levelingsystem.PlayerStatsManager;
import net.minecraft.entity.player.PlayerEntity;

public interface IPlayerStatsManager {

    /**
     * Gets player stats manager.
     *
     * @param player the player
     * @return the player stats manager
     */
    public PlayerStatsManager getPlayerStatsManager(PlayerEntity player);
}
