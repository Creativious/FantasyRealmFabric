package net.creativious.fantasyrealm;

import net.creativious.fantasyrealm.network.PlayerStatsServerPacket;
import net.fabricmc.api.ModInitializer;

import java.util.logging.LogManager;
import java.util.logging.Logger;

public class FantasyRealm implements ModInitializer {
    public static final String MOD_ID = "fantasyrealm";
    public static final Logger LOGGER = LogManager.getLogManager().getLogger(MOD_ID);
    @Override
    public void onInitialize() {
        PlayerStatsServerPacket.init();
    }
}
