package schr0.cleaver;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

public class CleaverDamageSource extends DamageSource
{

	private EntityLivingBase attacker;

	public CleaverDamageSource(EntityLivingBase attacker)
	{
		super("cleaver");

		this.attacker = attacker;
	}

	public EntityLivingBase getAttacker()
	{
		return this.attacker;
	}

}
