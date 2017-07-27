package schr0.cleaver.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MessageParticlePosition implements IMessage
{

	private int blockPosX;
	private int blockPosY;
	private int blockPosZ;
	private int particleType;

	public MessageParticlePosition()
	{
		// none
	}

	public MessageParticlePosition(BlockPos pos, int particleType)
	{
		this.blockPosX = pos.getX();
		this.blockPosY = pos.getY();
		this.blockPosZ = pos.getZ();
		this.particleType = particleType;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.blockPosX = buf.readInt();
		this.blockPosY = buf.readInt();
		this.blockPosZ = buf.readInt();
		this.particleType = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(this.blockPosX);
		buf.writeInt(this.blockPosY);
		buf.writeInt(this.blockPosZ);
		buf.writeInt(this.particleType);
	}

	// TODO /* ======================================== MOD START =====================================*/

	public BlockPos getPosition(World world)
	{
		return new BlockPos(this.blockPosX, this.blockPosY, this.blockPosZ);
	}

	public int getParticleType()
	{
		return this.particleType;
	}

}
