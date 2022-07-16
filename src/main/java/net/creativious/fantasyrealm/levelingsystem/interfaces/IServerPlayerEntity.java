package net.creativious.fantasyrealm.levelingsystem.interfaces;

public interface IServerPlayerEntity {

    public int syncedPlayerStatsLevel = -1;

    /**
     * DEPRECATED WILL BE REMOVED SOON
     *
     * The constant syncedPlayerStatsLevelProgress.
     */
    @Deprecated
    public float syncedPlayerStatsLevelProgress = -1;

    public float syncedPlayerStatsTotalExperience = -1;
}
