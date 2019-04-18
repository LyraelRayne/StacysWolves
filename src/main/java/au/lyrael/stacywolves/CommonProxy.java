package au.lyrael.stacywolves;

import au.lyrael.stacywolves.annotation.WolfMetadata;
import au.lyrael.stacywolves.blocks.BlockWolfsbaneTorch;
import au.lyrael.stacywolves.client.gui.StacysWolvesGuiHandler;
import au.lyrael.stacywolves.entity.wolf.IWolf;
import au.lyrael.stacywolves.item.ItemWolfClicker;
import au.lyrael.stacywolves.item.ItemWolfFood;
import au.lyrael.stacywolves.item.ItemWolfSpawnForcer;
import au.lyrael.stacywolves.item.block.ItemBlockStacyWolves;
import au.lyrael.stacywolves.registry.ItemRegistry;
import au.lyrael.stacywolves.tileentity.TileEntityWolfsbane;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static au.lyrael.stacywolves.StacyWolves.*;
import static au.lyrael.stacywolves.item.ItemWolfClicker.WOLF_CLICKER_NAME;
import static au.lyrael.stacywolves.item.ItemWolfFood.WOLF_FOOD_NAME;
import static au.lyrael.stacywolves.item.ItemWolfSpawnForcer.WOLF_SPAWN_FORCER_NAME;
import static au.lyrael.stacywolves.utility.LanguageHelper.setupColors;

public class CommonProxy {

    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public void preInit(FMLPreInitializationEvent event) {
        registerWolfEntities(event);
        registerItemsAndBlocks();
        setupColors();
    }

    public void init() {
        registerRecipes();
//        ItemRegistry.wolf_food.registerOreDict();
        GameRegistry.registerTileEntity(TileEntityWolfsbane.class, "Wolfsbane");
        NetworkRegistry.INSTANCE.registerGuiHandler(StacyWolves.INSTANCE, new StacysWolvesGuiHandler());
    }

    public void postInit(FMLPostInitializationEvent event) {
        for (String wolfName : WOLF_REGISTRY.getRegisteredEntityNames()) {
            WOLF_REGISTRY.registerForSpawning(wolfName);
        }
    }

    protected void registerRecipes() {
        ItemRegistry.wolf_food.registerRecipes();
        ItemRegistry.wolfsbane_torch.registerRecipes();
        ItemRegistry.wolf_clicker.registerRecipes();
    }


    protected void registerItemsAndBlocks() {
        // TODO Item registering must be JSON too?
//        GameRegistry.registerItem(new ItemWolfFood(), WOLF_FOOD_NAME);
        registerWolfsBaneTorch();
//        GameRegistry.registerItem(new ItemWolfTransporter(), WOLF_TRANSPORTER_NAME);
//        GameRegistry.registerItem(new ItemWolfSpawnForcer(), WOLF_SPAWN_FORCER_NAME);
//        GameRegistry.registerItem(new ItemWolfClicker(), WOLF_CLICKER_NAME);
    }

    private void registerWolfsBaneTorch() {
        final Block blockWolfsbaneTorch = new BlockWolfsbaneTorch();
        // TODO Block registering must be JSON too?
//        GameRegistry.registerBlock(blockWolfsbaneTorch, ItemBlockStacyWolves.class, blockWolfsbaneTorch.getUnlocalizedName());
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
}
