package net.levelz.access;

import java.util.Map;
import java.util.UUID;

public interface PlayerListAccess {

    public int levelZ$getLevel();

    public void levelZ$setLevel(int level);

    public Map<UUID, Integer> getLevelMap();

}
