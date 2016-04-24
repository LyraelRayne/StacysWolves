package au.lyrael.stacywolves.registry;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.entity.wolf.EntityFireWolf;
import au.lyrael.stacywolves.entity.wolf.IWolf;
import au.lyrael.stacywolves.item.ItemWolfPlacer;
import au.lyrael.stacywolves.utility.MetadataUtility;
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

import static au.lyrael.stacywolves.StacyWolves.*;
import static net.minecraftforge.common.BiomeDictionary.Type;
import static net.minecraftforge.common.BiomeDictionary.getBiomesForType;

public class WolfRegistry {

    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private final Map<String, Class<? extends IWolf>> classRegistryByName = new HashMap<>();

    public void registerWolf(Class<? extends IWolf> WolfClass) {
        final WolfMetadata metadata = getMetadataFor(WolfClass);
        if(metadata != null) {
            registerWolfEntity(WolfClass, metadata);
        }
    }

    protected void registerModEntityWithEgg(Class parEntityClass, String parEntityName,
                                            int parEggColor, int parEggSpotsColor) {
        EntityRegistry.registerModEntity(parEntityClass, parEntityName, ++modEntityID,
                instance, 80, 3, false);
        Item itemSpawnEgg = new ItemWolfPlacer(parEntityName, parEggColor, parEggSpotsColor)
                .setUnlocalizedName("spawn_egg_" + parEntityName.toLowerCase())
                .setTextureName("minecraft:spawn_egg");
        GameRegistry.registerItem(itemSpawnEgg, itemSpawnEgg.getUnlocalizedName());
    }


    protected void registerWolfEntity(Class<? extends IWolf> WolfClass, WolfMetadata metadata) {
        if (EntityLiving.class.isAssignableFrom(WolfClass)) {
            classRegistryByName.put(metadata.name(), WolfClass);
            Class<? extends EntityLiving> livingClass = (Class<? extends EntityLiving>) WolfClass;
            LOGGER.debug("Registering Wolf class [{}] with metadata [{}]", WolfClass, MetadataUtility.toString(metadata));
            registerModEntityWithEgg(livingClass, metadata.name(), metadata.primaryColour(), metadata.secondaryColour());
        } else {
            LOGGER.warn("Attempted to register Wolf entity class [{}] which is not a subclass of EntityLiving. Skipping", WolfClass.getName());
        }
    }

    public void registerForSpawning(String entityName) {
        final Class<? extends IWolf> wolfClass = getClassFor(entityName);

        if (wolfClass == null) {
            LOGGER.warn("Attempted to register [{}] for spawning but it doesn't exist in the registry.", entityName);
            return;
        }

        // Assume that because we looked the class up from the registry that it was in fact properly registered and
        // has metadata.
        final WolfMetadata metadata = getMetadataFor(wolfClass);
        final List<WolfSpawn> WolfSpawns = Arrays.asList(metadata.spawns());

        for (WolfSpawn wolfSpawnAnnotation : WolfSpawns) {
            final List<BiomeGenBase> biomes = getMatchingBiomes(Arrays.asList(wolfSpawnAnnotation.biomeTypes()));
            biomes.removeAll(getMatchingBiomes(Arrays.asList(wolfSpawnAnnotation.biomeTypeBlacklist())));
            final List<String> biomeBlacklist = Arrays.asList(wolfSpawnAnnotation.biomeBlacklist());

            for (BiomeGenBase biome : biomes) {
                if(!biomeBlacklist.contains(biome.biomeName)) {
                    final Integer probability = wolfSpawnAnnotation.probability();
                    EntityRegistry.addSpawn(toModEntityName(entityName), wolfSpawnAnnotation.probability(), wolfSpawnAnnotation.min(), wolfSpawnAnnotation.max(), wolfSpawnAnnotation.creatureType(), biome);
                    LOGGER.trace("Registered [{}] to spawn in [{}] with probability [{}] in packs of [{}]-[{}]", metadata.name(), biome.biomeName, probability, wolfSpawnAnnotation.min(), wolfSpawnAnnotation.max());
                }
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
    public ArrayList<Class<? extends IWolf>> getRegisteredEntityClasses() {
        return new ArrayList<>(classRegistryByName.values());
    }

    public WolfMetadata getMetadataFor(Class<? extends IWolf> clazz) {
        final WolfMetadata annotation = clazz.getAnnotation(WolfMetadata.class);
        if (annotation instanceof WolfMetadata)
            return annotation;
        else {
            LOGGER.warn("Attempted to get Wolf metadata for un-annotated class [{}]", clazz);
            return null;
        }
    }

    @SuppressWarnings("unused")
    public WolfMetadata getMetadataFor(String entityName) {
        return getMetadataFor(getClassFor(entityName));
    }

    public WolfMetadata getMetadataFor(IWolf Wolf) {
        return getMetadataFor(Wolf.getClass());
    }

    public String getEntityNameFor(IWolf Wolf) {
        return getMetadataFor(Wolf).name();
    }

    public Class<? extends IWolf> getClassFor(String entityName) {
        return classRegistryByName.get(entityName);
    }
}
