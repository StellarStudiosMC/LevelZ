package net.levelz.access;

public interface PlayerSyncAccess {

    void levelZ$syncStats(boolean syncDelay);

    void levelZ$addLevelExperience(int experience);

    void levelZ$levelUp(int levels, boolean deductXp, boolean ignoreMaxLevel);
}
