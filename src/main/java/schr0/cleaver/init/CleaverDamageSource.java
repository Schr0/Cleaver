package schr0.cleaver.init;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import schr0.cleaver.Cleaver;

public class CleaverDamageSource extends DamageSource
{

	public static final String DAMAGE_TYPE = Cleaver.MOD_ID + ".cleaver";

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
