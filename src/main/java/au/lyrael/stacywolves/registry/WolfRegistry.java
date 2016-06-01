package au.lyrael.stacywolves.registry;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.entity.wolf.IWolf;
import au.lyrael.stacywolves.item.ItemWolfPlacer;
import au.lyrael.stacywolves.utility.MetadataUtility;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraftforge.common.BiomeDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static au.lyrael.stacywolves.StacyWolves.*;
import static au.lyrael.stacywolves.registry.WolfType.NORMAL;
import static net.minecraftforge.common.BiomeDictionary.Type;
import static net.minecraftforge.common.BiomeDictionary.getBiomesForType;

public class WolfRegistry {

    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private final Map<String, Class<? extends IWolf>> classRegistryByName = new HashMap<>();
    private Map<String, BiomeGenBase> biomeNameMapping;
    private Map<WolfType, Map<BiomeGenBase, List<BiomeGenBase.SpawnListEntry>>> spawnRegistry = new HashMap<>();

    public void registerWolf(Class<? extends IWolf> WolfClass) {
        final WolfMetadata metadata = getMetadataFor(WolfClass);
        if (metadata != null) {
            registerWolfEntity(WolfClass, metadata);
        }
    }

    protected void registerModEntityWithEgg(Class parEntityClass, String parEntityName,
                                            int parEggColor, int parEggSpotsColor) {
        EntityRegistry.registerModEntity(parEntityClass, parEntityName, ++LAST_MOD_ENTITY_ID,
                INSTANCE, 80, 3, false);
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

        switch (entityName) {
            case "EntityCakeWolf":
                registerCakeWolf(entityName);
                break;
            default:
                // Assume that because we looked the class up from the registry that it was in fact properly registered and
                // has metadata.
                final WolfMetadata metadata = getMetadataFor(wolfClass);
                final List<WolfSpawn> wolfSpawns = Arrays.asList(metadata.spawns());

                for (WolfSpawn wolfSpawnAnnotation : wolfSpawns) {
                    final Set<BiomeGenBase> biomesToSpawn = buildBiomeList(wolfSpawnAnnotation.spawnBiomes());

                    for (BiomeGenBase biome : biomesToSpawn) {
                        final Integer probability = wolfSpawnAnnotation.probability();
                        this.addSpawn(entityName, probability, wolfSpawnAnnotation.min(), wolfSpawnAnnotation.max(), biome, metadata.type());
                    }
                }
                break;
        }
    }

    private void registerCakeWolf(String entityName) {
        final List<BiomeGenBase> villageSpawnBiomes = (List<BiomeGenBase>) MapGenVillage.villageSpawnBiomes;
        for (BiomeGenBase biome : villageSpawnBiomes) {
            this.addSpawn(entityName, 2, 1, 2, biome, NORMAL);
        }
    }

    private void addSpawn(String entityName, Integer probability, int min, int max, BiomeGenBase biome, WolfType type) {
        if(biome == null && type != null) {
            LOGGER.warn("Biome entry was null registering [{}] as [{}]!", entityName, type.name());
            return;
        } else if(biome != null && type == null)
        {
            LOGGER.warn("Wolf type was null registering [{}] in [{}]!", entityName, biome.biomeName);
            return;
        } else if (biome == null && type == null) {
            LOGGER.warn("Null biome AND type registering [{}]", entityName);
        }

        final Class<? extends IWolf> entityClass = getClassFor(entityName);
        List<BiomeGenBase.SpawnListEntry> spawnListForBiome = getSpawnsFor(biome, type);
        spawnListForBiome.add(new BiomeGenBase.SpawnListEntry(entityClass, probability, min, max));
        LOGGER.trace("Registered [{}] [{}] to spawn in [{}] with probability [{}] in packs of [{}]-[{}]", type.name(), entityName, biome.biomeName, probability, min, max);
    }

    public List<BiomeGenBase.SpawnListEntry> getSpawnsFor(BiomeGenBase biome, WolfType type) {
        final Map<BiomeGenBase, List<BiomeGenBase.SpawnListEntry>> spawnRegistry = getSpawnsFor(type);
        if (spawnRegistry.get(biome) == null)
            spawnRegistry.put(biome, new ArrayList<BiomeGenBase.SpawnListEntry>());
        return spawnRegistry.get(biome);
    }

    public Map<BiomeGenBase, List<BiomeGenBase.SpawnListEntry>> getSpawnsFor(WolfType type) {
        if (getSpawnRegistry().get(type) == null)
            getSpawnRegistry().put(type, new HashMap<BiomeGenBase, List<BiomeGenBase.SpawnListEntry>>());
        return getSpawnRegistry().get(type);
    }

    private Set<BiomeGenBase> buildBiomeList(WolfSpawnBiome[] biomeSpecs) {
        Set<BiomeGenBase> result = new HashSet<>();

        for (WolfSpawnBiome biomeSpec : biomeSpecs) {
            final List<BiomeGenBase> baseBiomes = getBiomesMatchingAll(Arrays.asList(biomeSpec.requireBiomeTypes()));
            final List<BiomeGenBase> excludeBiomes = getBiomesMatchingAny(Arrays.asList(biomeSpec.excludeBiomeTypes()));
            final List<String> excludeBiomeNames = Arrays.asList(biomeSpec.excludeBiomeNames());

            baseBiomes.removeAll(excludeBiomes);
            for (BiomeGenBase biome : baseBiomes) {
                if (!excludeBiomeNames.contains(biome.biomeName))
                    result.add(biome);
            }

            for (String biomeName : biomeSpec.specificBiomes()) {
                Map<String, BiomeGenBase> biomeNameMapping = getBiomeNameMapping();
                result.add(biomeNameMapping.get(biomeName.toLowerCase()));
            }
        }
        return result;
    }

    protected List<BiomeGenBase> getBiomesMatchingAny(List<BiomeDictionary.Type> types) {
        if (types != null && types.size() > 0) {
            List<BiomeGenBase> result = new ArrayList<>();

            for (Type type : types) {
                List<BiomeGenBase> matchingBiomes = Arrays.asList(getBiomesForType(type));
                result.addAll(matchingBiomes);
            }
            return result;
        } else {
            return new ArrayList<>();
        }
    }

    protected List<BiomeGenBase> getBiomesMatchingAll(List<BiomeDictionary.Type> requiredTypes) {
        if (requiredTypes != null && requiredTypes.size() > 0) {
            // Get the biomes which match the first type
            List<BiomeGenBase> biomesToTry = Arrays.asList(getBiomesForType(requiredTypes.get(0)));
            // Create an empty list with a capacity of all of the found biomes.
            List<BiomeGenBase> matchingBiomes = new ArrayList<>(biomesToTry.size());

            // Try each biome to see if it has ALL of the types.
            for (BiomeGenBase biome : biomesToTry) {
                final List<Type> typesForBiome = Arrays.asList(BiomeDictionary.getTypesForBiome(biome));
                if (typesForBiome.containsAll(requiredTypes)) {
                    matchingBiomes.add(biome);
                }
            }
            // Trim down the list capacity to only the size of the matched biomes.
            ((ArrayList) matchingBiomes).trimToSize();
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

    protected Map<String, BiomeGenBase> getBiomeNameMapping() {
        if (this.biomeNameMapping == null) {
            this.biomeNameMapping = new HashMap<>();
            for (BiomeGenBase biome : Arrays.asList(BiomeGenBase.getBiomeGenArray())) {
                if (biome != null && biome.biomeName != null)
                    this.biomeNameMapping.put(biome.biomeName.toLowerCase(), biome);
            }
        }
        return this.biomeNameMapping;
    }

    public Map<WolfType, Map<BiomeGenBase, List<BiomeGenBase.SpawnListEntry>>> getSpawnRegistry() {
        return spawnRegistry;
    }
}
