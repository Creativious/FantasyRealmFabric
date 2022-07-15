package net.creativious.fantasyrealm.client;

import net.creativious.fantasyrealm.network.PlayerStatsClientPacket;
import net.fabricmc.api.ClientModInitializer;

public class FantasyRealmClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PlayerStatsClientPacket.init();
    }
}
