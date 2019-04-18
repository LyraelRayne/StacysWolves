package au.lyrael.stacywolves.registry;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.annotation.WolfSpawn;
import au.lyrael.stacywolves.annotation.WolfSpawnBiome;
import au.lyrael.stacywolves.entity.wolf.IWolf;
import au.lyrael.stacywolves.item.ItemWolfPlacer;
import au.lyrael.stacywolves.utility.MetadataUtility;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraftforge.common.BiomeDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static au.lyrael.stacywolves.StacyWolves.*;
import static au.lyrael.stacywolves.registry.WolfType.NORMAL;
import static net.minecraftforge.common.BiomeDictionary.Type;
import static net.minecraftforge.common.BiomeDictionary.getBiomes;

public class WolfRegistry {

    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private final Map<String, Class<? extends IWolf>> classRegistryByName = new HashMap<>();
    private Map<String, Biome> biomeNameMapping;
    private Map<WolfType, Map<Biome, List<Biome.SpawnListEntry>>> spawnRegistry = new HashMap<>();

    public void dumpRegistry() {
        LOGGER.info("Dumping spawn registry ===============================");
        for (Map.Entry<WolfType, Map<Biome, List<Biome.SpawnListEntry>>> entry : getSpawnRegistry().entrySet()) {
            final Map<Biome, List<Biome.SpawnListEntry>> value = entry.getValue();
            for(Map.Entry<Biome, List<Biome.SpawnListEntry>> entryInner: value.entrySet()) {
                final Biome biome = entryInner.getKey();
                LOGGER.info("[{}] -> [{}]", biome.getBiomeName(), entryInner.getValue());
            }

        }
        LOGGER.info("Spawn Registry Dump Ended ============================");
    }

    public void registerWolf(Class<? extends IWolf> WolfClass) {
        final WolfMetadata metadata = getMetadataFor(WolfClass);
        if (metadata != null) {
            registerWolfEntity(WolfClass, metadata);
        }
    }

    protected void registerModEntityWithEgg(Class parEntityClass, String parEntityName,
                                            int parEggColor, int parEggSpotsColor) {
        // TODO Make entities great again.

//        EntityRegistry.registerModEntity(parEntityClass, parEntityName, ++LAST_MOD_ENTITY_ID,
//                INSTANCE, 80, 3, false);
//        Item itemSpawnEgg = new ItemWolfPlacer(parEntityName, parEggColor, parEggSpotsColor)
//                .setUnlocalizedName("spawn_egg_" + parEntityName.toLowerCase())
//                .setTextureName("minecraft:spawn_egg");
//        GameRegistry.registerItem(itemSpawnEgg, itemSpawnEgg.getUnlocalizedName());
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
            case "EntityBookshelfWolf":
            case "EntityCakeWolf":
                registerVillageWolf(entityName);
                break;
            default:
                // Assume that because we looked the class up from the registry that it was in fact properly registered and
                // has metadata.
                final WolfMetadata metadata = getMetadataFor(wolfClass);
                final List<WolfSpawn> wolfSpawns = Arrays.asList(metadata.spawns());

                for (WolfSpawn wolfSpawnAnnotation : wolfSpawns) {
                    final Set<Biome> biomesToSpawn = buildBiomeList(wolfSpawnAnnotation.spawnBiomes());

                    for (Biome biome : biomesToSpawn) {
                        this.addSpawn(entityName, wolfSpawnAnnotation.weight(), wolfSpawnAnnotation.min(), wolfSpawnAnnotation.max(), biome, metadata.type());
                    }
                }
                break;
        }
    }

    private void registerVillageWolf(String entityName) {
        registerVillageWolf(entityName, 2, 1, 2);
    }

    private void registerVillageWolf(String entityName, int weight, int min, int max) {
        final List<Biome> villageSpawnBiomes = MapGenVillage.VILLAGE_SPAWN_BIOMES;
        for (Biome biome : villageSpawnBiomes) {
            this.addSpawn(entityName, weight, min, max, biome, NORMAL);
        }
    }

    private void addSpawn(String entityName, Integer weight, int min, int max, Biome biome, WolfType type) {
        if (biome == null && type != null) {
            LOGGER.warn("Biome entry was null registering [{}] as [{}]!", entityName, type.name());
            return;
        } else if (biome != null && type == null) {
            LOGGER.warn("Wolf type was null registering [{}] in [{}]!", entityName, biome.biomeName);
            return;
        } else if (biome == null && type == null) {
            LOGGER.warn("Null biome AND type registering [{}]", entityName);
        }

        final Class<? extends IWolf> entityClass = getClassFor(entityName);
        List<Biome.SpawnListEntry> spawnListForBiome = getSpawnsFor(biome, type);
        spawnListForBiome.add(new Biome.SpawnListEntry((Class<? extends EntityLiving>)entityClass, weight, min, max));
        LOGGER.trace("Registered [{}] [{}] to spawn in [{}] with weight [{}] in packs of [{}]-[{}]", type.name(), entityName, biome.getBiomeName(), weight, min, max);
    }

    public List<Biome.SpawnListEntry> getSpawnsFor(Biome biome, WolfType type) {
        final Map<Biome, List<Biome.SpawnListEntry>> spawnRegistry = getSpawnsFor(type);
        if (spawnRegistry.get(biome) == null)
            spawnRegistry.put(biome, new ArrayList<Biome.SpawnListEntry>());
        return spawnRegistry.get(biome);
    }

    public Map<Biome, List<Biome.SpawnListEntry>> getSpawnsFor(WolfType type) {
        if (getSpawnRegistry().get(type) == null)
            getSpawnRegistry().put(type, new HashMap<Biome, List<Biome.SpawnListEntry>>());
        return getSpawnRegistry().get(type);
    }

    private Set<Biome> buildBiomeList(WolfSpawnBiome[] biomeSpecs) {
        Set<Biome> result = new HashSet<>();

        for (WolfSpawnBiome biomeSpec : biomeSpecs) {
            final List<Biome> baseBiomes = getBiomesMatchingAll(Arrays.asList(biomeSpec.requireBiomeTypes()));
            final List<Biome> excludeBiomes = getBiomesMatchingAny(Arrays.asList(biomeSpec.excludeBiomeTypes()));
            final List<String> excludeBiomeNames = Arrays.asList(biomeSpec.excludeBiomeNames());

            baseBiomes.removeAll(excludeBiomes);
            for (Biome biome : baseBiomes) {
                if (!excludeBiomeNames.contains(biome.biomeName))
                    result.add(biome);
            }

            for (String biomeName : biomeSpec.specificBiomes()) {
                Map<String, Biome> biomeNameMapping = getBiomeNameMapping();
                result.add(biomeNameMapping.get(biomeName.toLowerCase()));
            }
        }
        return result;
    }

    protected List<Biome> getBiomesMatchingAny(List<BiomeDictionary.Type> types) {
        if (types != null && types.size() > 0) {
            List<Biome> result = new ArrayList<>();

            for (Type type : types) {
                List<Biome> matchingBiomes = Arrays.asList(getBiomes(type));
                result.addAll(matchingBiomes);
            }
            return result;
        } else {
            return new ArrayList<>();
        }
    }

    protected List<Biome> getBiomesMatchingAll(List<BiomeDictionary.Type> requiredTypes) {
        if (requiredTypes != null && requiredTypes.size() > 0) {
            // Get the biomes which match the first type
            List<Biome> biomesToTry = Arrays.asList(getBiomes(requiredTypes.get(0)));
            // Create an empty list with a capacity of all of the found biomes.
            List<Biome> matchingBiomes = new ArrayList<>(biomesToTry.size());

            // Try each biome to see if it has ALL of the types.
            for (Biome biome : biomesToTry) {
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

    protected Map<String, Biome> getBiomeNameMapping() {
        if (this.biomeNameMapping == null) {
            this.biomeNameMapping = new HashMap<>();
            for (Biome biome : Arrays.asList(Biome.getBiomeGenArray())) {
                if (biome != null && biome.biomeName != null)
                    this.biomeNameMapping.put(biome.biomeName.toLowerCase(), biome);
            }
        }
        return this.biomeNameMapping;
    }

    public Map<WolfType, Map<Biome, List<Biome.SpawnListEntry>>> getSpawnRegistry() {
        return spawnRegistry;
    }
}
