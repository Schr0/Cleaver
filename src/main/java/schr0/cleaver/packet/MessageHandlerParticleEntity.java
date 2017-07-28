package schr0.cleaver.packet;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import schr0.cleaver.init.CleaverParticles;

@SideOnly(Side.CLIENT)
public class MessageHandlerParticleEntity implements IMessageHandler<MessageParticleEntity, IMessage>
{

	@Override
	public IMessage onMessage(MessageParticleEntity message, MessageContext ctx)
	{
		World world = FMLClientHandler.instance().getClient().world;
		Entity entity = message.getEntity(world);
		Random random = world.rand;

		if (entity != null)
		{
			switch (message.getParticleType())
			{
				case CleaverParticles.ENTITY_NORMAL_DISARMAMENT :

					particleNormalDisarmament(world, entity, random);

					break;

				case CleaverParticles.ENTITY_NORMAL_CLEAVE :

					particleNormalCleave(world, entity, random);

					break;

				case CleaverParticles.ENTITY_BLAZE_SHIELD :

					particleBlazeShield(world, entity, random);

					break;

				case CleaverParticles.ENTITY_BLAZE_CLEAVE :

					particleBlazeCleave(world, entity, random);

					break;

			}
		}

		return (IMessage) null;
	}

	// TODO /* ======================================== MOD START =====================================*/

	private static void particleNormalDisarmament(World world, Entity entity, Random random)
	{
		double size = 0.85D;
		world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, entity.posX + (double) (random.nextFloat() * entity.width * 2.0F) - (double) entity.width, entity.posY + (double) (random.nextFloat() * entity.height), entity.posZ + (double) (random.nextFloat() * entity.width * 2.0F) - (double) entity.width, size, 0.0D, 0.0D, new int[0]);
	}

	private static void particleNormalCleave(World world, Entity entity, Random random)
	{
		world.spawnParticle(EnumParticleTypes.SWEEP_ATTACK, entity.posX + (double) (random.nextFloat() * entity.width * 2.0F) - (double) entity.width, entity.posY + (double) (random.nextFloat() * entity.height), entity.posZ + (double) (random.nextFloat() * entity.width * 2.0F) - (double) entity.width, 0.0D, 0.0D, 0.0D, new int[0]);
	}

	private static void particleBlazeShield(World world, Entity entity, Random random)
	{
		for (int count = 0; count < 2; count++)
		{
			world.spawnParticle(EnumParticleTypes.FLAME, entity.posX + (double) (random.nextFloat() * entity.width * 2.0F) - (double) entity.width, entity.posY + (double) (random.nextFloat() * entity.height), entity.posZ + (double) (random.nextFloat() * entity.width * 2.0F) - (double) entity.width, 0.0D, 0.0D, 0.0D, new int[0]);
		}
	}

	private static void particleBlazeCleave(World world, Entity entity, Random random)
	{
		for (int count = 0; count < 20; count++)
		{
			world.spawnParticle(EnumParticleTypes.FLAME, entity.posX + (double) (random.nextFloat() * entity.width * 2.0F) - (double) entity.width, entity.posY + (double) (random.nextFloat() * entity.height), entity.posZ + (double) (random.nextFloat() * entity.width * 2.0F) - (double) entity.width, 0.0D, 0.0D, 0.0D, new int[0]);
		}
	}

}
