package au.lyrael.stacywolves.item;


import au.lyrael.stacywolves.entity.wolf.EntityWolfBase;
import au.lyrael.stacywolves.registry.WolfType;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.ForgeEventFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

import static au.lyrael.stacywolves.StacyWolves.CREATIVE_TAB;
import static au.lyrael.stacywolves.StacyWolves.MOD_ID;
import static net.minecraft.world.SpawnerAnimals.canCreatureTypeSpawnAtLocation;

public class ItemWolfSpawnForcer extends ItemStacyWolves {
	private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public ItemWolfSpawnForcer() {
		super();
		setHasSubtypes(false);
		maxStackSize = 64;
		setCreativeTab(CREATIVE_TAB);
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed.
	 * Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world,
									  EntityPlayer player) {
		if (world.isRemote || !player.capabilities.isCreativeMode || !WorldServer.class.isAssignableFrom(world.getClass())) {
			return stack;
		} else {
			try {
				final WorldServer worldServer = (WorldServer) world;
//				final SpawnerAnimals animalSpawner = ReflectionHelper.getPrivateValue(WorldServer.class, worldServer, "animalSpawner", "field_135059_Q");
				int numTries = 0;
				int numSpawned = 0;
				while (numSpawned == 0 && numTries++ < 10) {
					numSpawned = findChunksForSpawning(worldServer, true, true, true, player);
					if(numSpawned == 0) Thread.sleep(2);
				}
				LOGGER.debug("Force spawned [{}] wolves in [{}] tries", numSpawned, numTries);
			} catch (Exception e) {
				LOGGER.error("Failed to force spawn", e);
			}
			return stack;
		}
	}

	public int findChunksForSpawning(WorldServer worldServer, boolean hostile, boolean passive, boolean isTimeForPassive, EntityPlayer player) {
		final HashMap eligibleChunksForSpawning = new HashMap();
		boolean stop = false;
		if (!hostile && !passive) {
			return 0;
		} else {
			eligibleChunksForSpawning.clear();

			EntityPlayer entityplayer = player;
			int playerChunkX = MathHelper.floor_double(entityplayer.posX / 16.0D);
			int playerChunkZ = MathHelper.floor_double(entityplayer.posZ / 16.0D);
			final byte spawnRadius = 1;


			ChunkCoordIntPair playerChunkCoords = new ChunkCoordIntPair(playerChunkX, playerChunkZ);

			ChunkCoordinates spawnPoint = worldServer.getSpawnPoint();
			EnumCreatureType[] aenumcreaturetype = {WolfType.NORMAL.creatureType(), WolfType.SUBTERRANEAN.creatureType(), WolfType.MOB.creatureType(), WolfType.WATER.creatureType()};

			int totalEntitiesSpawned = 0;

			for (int creatureTypeIndex = 0; creatureTypeIndex < aenumcreaturetype.length; ++creatureTypeIndex) {
				EnumCreatureType enumcreaturetype = aenumcreaturetype[creatureTypeIndex];

				if ((!enumcreaturetype.getPeacefulCreature() || passive) && (enumcreaturetype.getPeacefulCreature() || hostile) && (!enumcreaturetype.getAnimal() || isTimeForPassive)) {

					final Vec3 playerPosition = player.getPosition(1f);
					ChunkPosition spawnPosition = selectRandomPositionInChunk(worldServer, playerChunkCoords.chunkXPos, playerChunkCoords.chunkZPos);
//					ChunkPosition spawnPosition = new ChunkPosition((int) playerPosition.xCoord,(int) playerPosition.yCoord,(int) playerPosition.zCoord);
					int blockBelowPlayer = findYBelow(playerPosition, worldServer);
					int spawnPosX = spawnPosition.chunkPosX;
					int spawnPosY = blockBelowPlayer;
					int spawnPosZ = spawnPosition.chunkPosZ;

					if(enumcreaturetype.getCreatureMaterial() == Material.water)
						spawnPosY += 2;

					if (!worldServer.getBlock(spawnPosX, spawnPosY, spawnPosZ).isNormalCube() && worldServer.getBlock(spawnPosX, spawnPosY, spawnPosZ).getMaterial() == enumcreaturetype.getCreatureMaterial()) {
						int numEntitiesSpawnedInChunk = 0;
						int numSpawnAttempts = 0;

						while (numSpawnAttempts < 3) {
							int spawnBlockChunkX = spawnPosX;
							int spawnBlockChunkY = spawnPosY;
							int spawnBlockChunkZ = spawnPosZ;
							final byte six = 6;
							BiomeGenBase.SpawnListEntry spawnlistentry = null;
							IEntityLivingData ientitylivingdata = null;
							int lessThanFourOne = 0;

							while (true) {
								if (lessThanFourOne < 4) {
									label103:
									{
										spawnBlockChunkX += worldServer.rand.nextInt(six) - worldServer.rand.nextInt(six);
										spawnBlockChunkY += worldServer.rand.nextInt(1) - worldServer.rand.nextInt(1);
										spawnBlockChunkZ += worldServer.rand.nextInt(six) - worldServer.rand.nextInt(six);

										if (canCreatureTypeSpawnAtLocation(enumcreaturetype, worldServer, spawnBlockChunkX, spawnBlockChunkY, spawnBlockChunkZ)) {
											final float spawnChunkX = (float) spawnBlockChunkX + 0.5F;
											final float spawnChunkY = (float) spawnBlockChunkY;
											final float spawnChunkZ = (float) spawnBlockChunkZ + 0.5F;

											if (true /*dont check that not near player worldServer.getClosestPlayer((double) spawnChunkX, (double) spawnChunkY, (double) spawnChunkZ, 24.0D) == null */) {

												if (getDistanceFromSpawn(spawnPoint, spawnChunkX, spawnChunkY, spawnChunkZ) >= 576.0F) {
													if (spawnlistentry == null) {
														spawnlistentry = worldServer.spawnRandomCreature(enumcreaturetype, spawnBlockChunkX, spawnBlockChunkY, spawnBlockChunkZ);

														if (spawnlistentry == null) {
															break label103;
														}
													}

													final EntityLiving entityliving;

													try {
														entityliving = (EntityLiving) spawnlistentry.entityClass.getConstructor(new Class[]{World.class}).newInstance(new Object[]{worldServer});
														if (EntityWolfBase.class.isAssignableFrom(entityliving.getClass())) {
															((EntityWolfBase) entityliving).setBypassThrottleAndProbability(true);
														} else {
															spawnlistentry = null;
															break label103;
														}
													} catch (Exception exception) {
														exception.printStackTrace();
														return totalEntitiesSpawned;
													}

													entityliving.setLocationAndAngles((double) spawnChunkX, (double) spawnChunkY, (double) spawnChunkZ, worldServer.rand.nextFloat() * 360.0F, 0.0F);

													Event.Result canSpawn = ForgeEventFactory.canEntitySpawn(entityliving, worldServer, spawnChunkX, spawnChunkY, spawnChunkZ);
													if (canSpawn == Event.Result.ALLOW || (canSpawn == Event.Result.DEFAULT && entityliving.getCanSpawnHere())) {
														++numEntitiesSpawnedInChunk;
														worldServer.spawnEntityInWorld(entityliving);
														if (!ForgeEventFactory.doSpecialSpawn(entityliving, worldServer, spawnChunkX, spawnChunkY, spawnChunkZ)) {
															ientitylivingdata = entityliving.onSpawnWithEgg(ientitylivingdata);
														}

														if (numSpawnAttempts >= ForgeEventFactory.getMaxSpawnPackSize(entityliving)) {
															stop = true;
														}
													}

													totalEntitiesSpawned += numEntitiesSpawnedInChunk;
													if (stop)
														return totalEntitiesSpawned;
												}
											}
										}

										++lessThanFourOne;
										continue;
									}
								}

								++numSpawnAttempts;
								break;
							}
						}
					}
				}
			}

			return totalEntitiesSpawned;
		}
	}

	private int findYBelow(Vec3 playerPosition, WorldServer worldServer) {
		final int x = (int) playerPosition.xCoord;

		final int z = (int) playerPosition.zCoord;
		for (int y = (int) playerPosition.yCoord; y > 2; y--) {
			final Block block = worldServer.getBlock(x, y, z);
			if (block.isNormalCube())
				return y + 1;
		}
		return 2;
	}

	protected float getDistanceFromSpawn(ChunkCoordinates spawnPoint, float spawnChunkX, float spawnChunkY, float spawnChunkZ) {
		final float xFromSpawnPoint = spawnChunkX - (float) spawnPoint.posX;
		final float yFromSpawnPoint = spawnChunkY - (float) spawnPoint.posY;
		final float zFromSpawnPoint = spawnChunkZ - (float) spawnPoint.posZ;
		return xFromSpawnPoint * xFromSpawnPoint + yFromSpawnPoint * yFromSpawnPoint + zFromSpawnPoint * zFromSpawnPoint;
	}

	protected static ChunkPosition selectRandomPositionInChunk(World world, int chunkX, int chunkZ) {
		Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
		int xPos = chunkX * 16 + world.rand.nextInt(16);
		int zPos = chunkZ * 16 + world.rand.nextInt(16);
		int yPos = world.rand.nextInt(chunk == null ? world.getActualHeight() : chunk.getTopFilledSegment() + 16 - 1);
		return new ChunkPosition(xPos, yPos, zPos);
	}

	@Override
	public int getColorFromItemStack(ItemStack par1ItemStack, int parColorType) {
		return 0x000000;
	}

	@Override
	// Doing this override means that there is no localization for language
	// unless you specifically check for localization here and convert
	public String getItemStackDisplayName(ItemStack par1ItemStack) {
		return "Spawn Forcer";
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		super.registerIcons(par1IconRegister);
	}
}