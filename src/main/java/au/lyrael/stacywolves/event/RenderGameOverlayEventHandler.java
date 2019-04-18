package au.lyrael.stacywolves.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.lyrael.stacywolves.entity.wolf.EntityEnderWolf;
import au.lyrael.stacywolves.entity.wolf.IWolf;
import au.lyrael.stacywolves.registry.WolfType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraftforge.client.event.RenderGameOverlayEvent;


public class RenderGameOverlayEventHandler
{
	@SubscribeEvent
	@SuppressWarnings("unused")
	public void handleTextEvent(RenderGameOverlayEvent.Text event)
	{
		final Minecraft minecraft = Minecraft.getMinecraft();
		if (minecraft.gameSettings.showDebugInfo)
		{
			StringBuilder wolfTextBuilder = new StringBuilder("[SW] Total wolves spawned: ");
			StringBuilder otherTextBuilder = new StringBuilder("[SW] Total non-wolves spawned: ");

			Map<WolfType, Integer> counts = new HashMap<>();
			Map<EnumCreatureType, Integer> otherCounts = new HashMap<>();
			int enderCount = 0;
			int enderManCount = 0;
			final List<Object> loadedEntityList = minecraft.theWorld.getLoadedEntityList();
			for (Object entity : loadedEntityList)
			{
				if (IWolf.class.isAssignableFrom(entity.getClass()))
				{
					final IWolf wolf = (IWolf) entity;
					final WolfType wolfType = wolf.getWolfType();
					counts.put(wolfType, nullSafeIncrement(counts.get(wolfType)));
				}
				else
				{
					if (EntityLiving.class.isAssignableFrom(entity.getClass()))
					{
						final EntityLiving entityLiving = (EntityLiving) entity;
						for (EnumCreatureType enumCreatureType : EnumCreatureType.values())
						{
							if (((EntityLiving) entity).isCreatureType(enumCreatureType, false))
							{
								otherCounts.put(enumCreatureType, nullSafeIncrement(otherCounts.get(EnumCreatureType.monster)));
							}
						}

					}
				}

				if (EntityEnderWolf.class.isAssignableFrom(entity.getClass()))
				{
					enderCount++;
				}

				if (EntityEnderman.class.isAssignableFrom(entity.getClass()))
				{
					enderManCount++;
				}
			}

			for (Map.Entry<WolfType, Integer> entry : counts.entrySet())
			{
				wolfTextBuilder.append(entry.getKey().name() + ": " + Integer.toString(entry.getValue()) + ", ");
			}

			for (Map.Entry<EnumCreatureType, Integer> entry : otherCounts.entrySet())
			{
				otherTextBuilder.append(entry.getKey().name() + ": " + Integer.toString(entry.getValue()) + ", ");
			}

			event.left.add("");

			event.left.add(wolfTextBuilder.toString() + "ENDER : " + Integer.toString(enderCount));
			event.left.add(otherTextBuilder.toString() + "ENDER : " + Integer.toString(enderManCount));
		}
	}

	protected int nullSafeIncrement(Integer count)
	{
		return count == null ? 1 : count + 1;
	}
}
