package schr0.cleaver;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

public class CleaverDamageSource extends DamageSource
{

	private static final String DAMAGE_TYPE = Cleaver.MOD_ID;

	private EntityLivingBase attacker;

	public CleaverDamageSource(EntityLivingBase attacker)
	{
		super(DAMAGE_TYPE);

		this.attacker = attacker;
	}

	public EntityLivingBase getAttacker()
	{
		return this.attacker;
	}

}
