package au.lyrael.needsmoredragons.registry;

import au.lyrael.needsmoredragons.annotation.DragonMetadata;
import au.lyrael.needsmoredragons.annotation.DragonSpawn;
import au.lyrael.needsmoredragons.entity.dragon.IDragon;
import au.lyrael.needsmoredragons.item.ItemDragonPlacer;
import au.lyrael.needsmoredragons.utility.MetadataUtility;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static au.lyrael.needsmoredragons.NeedsMoreDragons.*;
import static net.minecraftforge.common.BiomeDictionary.*;

public class DragonRegistry {

    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private final Map<String, Class<? extends IDragon>> classRegistryByName = new HashMap<>();

    public void registerDragon(Class<? extends IDragon> dragonClass) {
        final DragonMetadata metadata = getMetadataFor(dragonClass);
        if(metadata != null) {
            registerDragonEntity(dragonClass, metadata);
        }
    }

    protected void registerModEntityWithEgg(Class parEntityClass, String parEntityName,
                                            int parEggColor, int parEggSpotsColor) {
        EntityRegistry.registerModEntity(parEntityClass, parEntityName, ++modEntityID,
                instance, 80, 3, false);
        Item itemSpawnEgg = new ItemDragonPlacer(parEntityName, parEggColor, parEggSpotsColor)
                .setUnlocalizedName("spawn_egg_" + parEntityName.toLowerCase())
                .setTextureName("minecraft:spawn_egg");
        GameRegistry.registerItem(itemSpawnEgg, itemSpawnEgg.getUnlocalizedName());
    }


    protected void registerDragonEntity(Class<? extends IDragon> dragonClass, DragonMetadata metadata) {
        if (EntityLiving.class.isAssignableFrom(dragonClass)) {
            classRegistryByName.put(metadata.name(), dragonClass);
            Class<? extends EntityLiving> livingClass = (Class<? extends EntityLiving>) dragonClass;
            LOGGER.debug("Registering dragon class [{}] with metadata [{}]", dragonClass, MetadataUtility.toString(metadata));
            registerModEntityWithEgg(livingClass, metadata.name(), metadata.primaryColour(), metadata.secondaryColour());
        } else {
            LOGGER.warn("Attempted to register dragon entity class [{}] which is not a subclass of EntityLiving. Skipping", dragonClass.getName());
        }
    }

    public void registerForSpawning(String entityName) {
        final Class<? extends IDragon> dragonClass = getClassFor(entityName);

        if (dragonClass == null) {
            LOGGER.warn("Attempted to register [{}] for spawning but it doesn't exist in the registry.", entityName);
            return;
        }

        // Assume that because we looked the class up from the registry that it was in fact properly registered and
        // has metadata.
        final DragonMetadata metadata = getMetadataFor(dragonClass);
        final List<DragonSpawn> dragonSpawns = Arrays.asList(metadata.spawns());

        for (DragonSpawn dragonSpawn : dragonSpawns) {
            final List<BiomeGenBase> biomes = getMatchingBiomes(Arrays.asList(dragonSpawn.biomeType()));
            for (BiomeGenBase biome : biomes) {
                final Integer probability = dragonSpawn.probability();
                EntityRegistry.addSpawn(toModEntityName(entityName), dragonSpawn.probability(), dragonSpawn.min(), dragonSpawn.max(), EnumCreatureType.creature, biome);
                LOGGER.trace("Registered [{}] to spawn in [{}] with probability [{}] in packs of [{}]-[{}]", metadata.name(), biome.biomeName, probability, dragonSpawn.min(), dragonSpawn.max());
            }
        }
    }

    private List<BiomeGenBase> getMatchingBiomes(List<BiomeDictionary.Type> requiredTypes) {
        if(requiredTypes != null && requiredTypes.size() > 0) {
            // Get the biomes which match the first type
            List<BiomeGenBase> biomesToTry = Arrays.asList(getBiomesForType(requiredTypes.get(0)));
            // Create an empty list with a capacity of all of the found biomes.
            List<BiomeGenBase> matchingBiomes = new ArrayList<>(biomesToTry.size());

            // Try each biome to see if it has ALL of the types.
            for (BiomeGenBase biome : biomesToTry) {
                final List<Type> typesForBiome = Arrays.asList(BiomeDictionary.getTypesForBiome(biome));
                if(typesForBiome.containsAll(requiredTypes))
                {
                    matchingBiomes.add(biome);
                }
            }
            // Trim down the list capacity to only the size of the matched biomes.
            ((ArrayList)matchingBiomes).trimToSize();
            return matchingBiomes;
        } else {
            // Return empty list if there were no required types to begin with.
            return new ArrayList<>();
        }
    }

    private String toModEntityName(String entityName) {
        return MOD_ID + "." + entityName;
    }

    public List<String> getRegisteredEntityNames() {
        return new ArrayList<>(classRegistryByName.keySet());
    }

    @SuppressWarnings("unused")
    public ArrayList<Class<? extends IDragon>> getRegisteredEntityClasses() {
        return new ArrayList<>(classRegistryByName.values());
    }

    public DragonMetadata getMetadataFor(Class<? extends IDragon> clazz) {
        final DragonMetadata annotation = clazz.getAnnotation(DragonMetadata.class);
        if (annotation instanceof DragonMetadata)
            return annotation;
        else {
            LOGGER.warn("Attempted to get dragon metadata for un-annotated class [{}]", clazz);
            return null;
        }
    }

    @SuppressWarnings("unused")
    public DragonMetadata getMetadataFor(String entityName) {
        return getMetadataFor(getClassFor(entityName));
    }

    public DragonMetadata getMetadataFor(IDragon dragon) {
        return getMetadataFor(dragon.getClass());
    }

    public String getEntityNameFor(IDragon dragon) {
        return getMetadataFor(dragon).name();
    }

    public Class<? extends IDragon> getClassFor(String entityName) {
        return classRegistryByName.get(entityName);
    }
}
