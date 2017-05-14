package au.lyrael.stacywolves.entity;

import au.lyrael.stacywolves.annotation.WolfMetadata;

/**
 * Created by Lyrael on 14/05/2017.
 */
public interface SpawnWeights {
    int SPAWN_WEIGHT_RARE = 5;
    int SPAWN_WEIGHT_COMMON = 50;
    int SPAWN_WEIGHT_SUPER_RARE = 1;

    int SPAWN_WEIGHT_MOB_SUPER_RARE = 1;
    int SPAWN_WEIGHT_MOB_RARE = 5;
    int SPAWN_WEIGHT_MOB_COMMON = 15;

    int SPAWN_PROBABILITY_ALWAYS = WolfMetadata.MAX_SPAWN_PROBABILITY;
    int SPAWN_PROBABILITY_MOSTLY = 7 * (WolfMetadata.MAX_SPAWN_PROBABILITY / 8);
    int SPAWN_PROBABILITY_SOMETIMES = WolfMetadata.MAX_SPAWN_PROBABILITY / 2;
    int SPAWN_PROBABILITY_RARE = WolfMetadata.MAX_SPAWN_PROBABILITY / 8;
    int SPAWN_PROBABILITY_SUPER_RARE = WolfMetadata.MAX_SPAWN_PROBABILITY / 12;


}
