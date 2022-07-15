package net.creativious.fantasyrealm.levelingsystem.interfaces;

import net.creativious.fantasyrealm.levelingsystem.PlayerStatsManager;
import net.minecraft.entity.player.PlayerEntity;

public interface IPlayerStatsManager {

    public PlayerStatsManager getPlayerStatsManager(PlayerEntity player);
}
