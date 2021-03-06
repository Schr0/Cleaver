package schr0.cleaver;

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

@SideOnly(Side.CLIENT)
public class MessageHandlerParticleEntity implements IMessageHandler<MessageParticleEntity, IMessage>
{

	@Override
	public IMessage onMessage(MessageParticleEntity message, MessageContext ctx)
	{
		World world = FMLClientHandler.instance().getClient().world;

		if (world != null)
		{
			Random random = world.rand;
			Entity entity = message.getEntity(world);

			switch (message.getParticleType())
			{
				case CleaverParticles.TARGET_CHOP :

					particleChop(world, entity, random);

					break;

				case CleaverParticles.TARGET_DISARMAMENT :

					particleDisarmament(world, entity, random);

					break;
			}
		}

		return (IMessage) null;
	}

	// TODO /* ======================================== MOD START =====================================*/

	private static void particleChop(World world, Entity entity, Random random)
	{
		double posX = entity.posX + (double) (random.nextFloat() * entity.width * 2.0F) - (double) entity.width;
		double posY = entity.posY + (double) (random.nextFloat() * entity.height);
		double posZ = entity.posZ + (double) (random.nextFloat() * entity.width * 2.0F) - (double) entity.width;

		world.spawnParticle(EnumParticleTypes.SWEEP_ATTACK, posX, posY, posZ, 0, 0, 0, new int[0]);
	}

	private static void particleDisarmament(World world, Entity entity, Random random)
	{
		double posX = entity.posX + (double) (random.nextFloat() * entity.width * 2.0F) - (double) entity.width;
		double posY = entity.posY + (double) (random.nextFloat() * entity.height);
		double posZ = entity.posZ + (double) (random.nextFloat() * entity.width * 2.0F) - (double) entity.width;
		double size = 0.85D;

		world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, posX, posY, posZ, size, 0, 0, new int[0]);
	}

}
