package au.lyrael.stacywolves;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.entity.wolf.IWolf;
import au.lyrael.stacywolves.item.ItemWolfFood;
import au.lyrael.stacywolves.registry.ItemRegistry;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static au.lyrael.stacywolves.StacyWolves.MOD_ID;
import static au.lyrael.stacywolves.StacyWolves.WOLF_REGISTRY;

public class CommonProxy {

    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public void init() {
        ItemRegistry.wolf_food.registerRecipes();
    }

    public void preInit(FMLPreInitializationEvent event) {

        registerWolfEntities(event);
        GameRegistry.registerItem(new ItemWolfFood(), ItemWolfFood.UNLOCALIZED_NAME);
    }

    private void registerWolfEntities(FMLPreInitializationEvent event) {
        final List<ASMDataTable.ASMData> wolfMetadatas = sortWolfMetadata(event.getAsmData().getAll(WolfMetadata.class.getName()));
        LOGGER.trace("Found [{}] wolf metadata entries. Registering Wolves!", wolfMetadatas.size());
        for (ASMDataTable.ASMData annotation : wolfMetadatas) {
            try {
                final Class<?> annotatedClass = getWolfClass(annotation);
                if (IWolf.class.isAssignableFrom(annotatedClass)) {
                    WOLF_REGISTRY.registerWolf((Class<? extends IWolf>) annotatedClass);
                } else {
                    LOGGER.warn("Annotated wolf class [{}] doesn't implement IWolf. Skipping.", annotatedClass);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private List<ASMDataTable.ASMData> sortWolfMetadata(Collection<ASMDataTable.ASMData> wolfMetadatas) {
        List<ASMDataTable.ASMData> result = new ArrayList<>(wolfMetadatas);

        Collections.sort(result, new Comparator<ASMDataTable.ASMData>() {
            @Override
            public int compare(ASMDataTable.ASMData first, ASMDataTable.ASMData second) {
                final String firstName = (String) first.getAnnotationInfo().get("name");
                final String secondName = (String) second.getAnnotationInfo().get("name");
                return firstName.compareTo(secondName);
            }
        });
        return result;
    }


    private Class<?> getWolfClass(ASMDataTable.ASMData wolfMetadata) throws ClassNotFoundException {
        final String className = wolfMetadata.getClassName();
        return Class.forName(className);
    }


    public void postInit(FMLPostInitializationEvent event) {
        for (String wolfName : WOLF_REGISTRY.getRegisteredEntityNames()) {
            WOLF_REGISTRY.registerForSpawning(wolfName);
        }
    }
}
