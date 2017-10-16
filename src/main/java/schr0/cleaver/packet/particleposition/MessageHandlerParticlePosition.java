package schr0.cleaver.packet.particleposition;

import java.util.Random;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import schr0.cleaver.init.CleaverParticles;

public class MessageHandlerParticlePosition implements IMessageHandler<MessageParticlePosition, IMessage>
{

	@Override
	public IMessage onMessage(MessageParticlePosition message, MessageContext ctx)
	{
		World world = FMLClientHandler.instance().getClient().world;

		if (world != null)
		{
			Random random = world.rand;
			BlockPos blockPos = message.getPosition(world);

			if (!blockPos.equals(BlockPos.ORIGIN))
			{
				switch (message.getParticleType())
				{
					case CleaverParticles.POSITION_BLAZE_SMELTING :

						particleBlazeSmelting(world, blockPos, random);

						break;
				}
			}
		}

		return (IMessage) null;
	}

	// TODO /* ======================================== MOD START =====================================*/

	private static void particleBlazeSmelting(World world, BlockPos pos, Random random)
	{
		for (int count = 0; count < 20; count++)
		{
			float randomPos = 0.5F;
			double posXdrop = (double) pos.getX() + ((random.nextFloat() * randomPos) + (double) (1.0F - randomPos) * 0.5D);
			double posYdrop = (double) pos.getY() + ((random.nextFloat() * randomPos) + (double) (1.0F - randomPos) * 0.5D);
			double posZdrop = (double) pos.getZ() + ((random.nextFloat() * randomPos) + (double) (1.0F - randomPos) * 0.5D);

			world.spawnParticle(EnumParticleTypes.FLAME, posXdrop, posYdrop, posZdrop, 0.0D, 0.0D, 0.0D, new int[0]);
		}
	}

}
