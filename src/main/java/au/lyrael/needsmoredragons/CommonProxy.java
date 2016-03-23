package au.lyrael.needsmoredragons;

import au.lyrael.needsmoredragons.annotation.DragonMetadata;
import au.lyrael.needsmoredragons.entity.dragon.IDragon;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

import static au.lyrael.needsmoredragons.NeedsMoreDragons.MOD_ID;
import static au.lyrael.needsmoredragons.NeedsMoreDragons.dragonRegistry;

public class CommonProxy {

    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public void init() {

    }

    public void preInit(FMLPreInitializationEvent event) {

        registerDragonEntities(event);
    }

    private void registerDragonEntities(FMLPreInitializationEvent event) {
        final Set<ASMDataTable.ASMData> dragonMetadatas = event.getAsmData().getAll(DragonMetadata.class.getName());
        LOGGER.trace("Found [{}] dragon metadata entries. Registering Dragons!", dragonMetadatas.size());
        for (ASMDataTable.ASMData annotation : dragonMetadatas) {
            try {
                final Class<?> annotatedClass = getDragonClass(annotation);
                if (IDragon.class.isAssignableFrom(annotatedClass)) {
                    dragonRegistry.registerDragon((Class<? extends IDragon>) annotatedClass);
                } else {
                    LOGGER.warn("Annotated dragon class [{}] doesn't implement IDragon. Skipping.", annotatedClass);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private Class<?> getDragonClass(ASMDataTable.ASMData dragonMetadata) throws ClassNotFoundException {
        final String className = dragonMetadata.getClassName();
        return Class.forName(className);
    }


    public void postInit(FMLPostInitializationEvent event) {
        for (String dragonName : dragonRegistry.getRegisteredEntityNames()) {
            dragonRegistry.registerForSpawning(dragonName);
        }
    }
}
