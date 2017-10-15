package schr0.cleaver.init;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import schr0.cleaver.Cleaver;
import schr0.cleaver.packet.particleentity.MessageHandlerParticleEntity;
import schr0.cleaver.packet.particleentity.MessageParticleEntity;
import schr0.cleaver.packet.particleposition.MessageHandlerParticlePosition;
import schr0.cleaver.packet.particleposition.MessageParticlePosition;

public class CleaverPacket
{

	public static final String CHANNEL_NAME = Cleaver.MOD_ID;
	public static final SimpleNetworkWrapper DISPATCHER = NetworkRegistry.INSTANCE.newSimpleChannel(CHANNEL_NAME);

	public static final int ID_PARTICLE_ENTITY = 0;
	public static final int ID_PARTICLE_POSITION = 1;

	public void registerMessages()
	{
		// none
	}

	@SideOnly(Side.CLIENT)
	public void registerClientMessages()
	{
		DISPATCHER.registerMessage(MessageHandlerParticleEntity.class, MessageParticleEntity.class, ID_PARTICLE_ENTITY, Side.CLIENT);
		DISPATCHER.registerMessage(MessageHandlerParticlePosition.class, MessageParticlePosition.class, ID_PARTICLE_POSITION, Side.CLIENT);
	}

}
