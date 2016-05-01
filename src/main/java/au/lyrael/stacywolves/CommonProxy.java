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

import java.util.Map;
import java.util.Set;

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
        final Set<ASMDataTable.ASMData> wolfMetadatas = event.getAsmData().getAll(WolfMetadata.class.getName());
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
