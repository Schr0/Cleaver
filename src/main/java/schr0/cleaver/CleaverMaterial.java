package schr0.cleaver;

public enum CleaverMaterial
{

	NORMAL(500, -0.5F, 14);

	private final int maxUses;
	private final float damageVsEntity;
	private final int enchantability;

	private CleaverMaterial(int maxUses, float damageVsEntity, int enchantability)
	{
		this.maxUses = maxUses;
		this.damageVsEntity = damageVsEntity;
		this.enchantability = enchantability;
	}

	public int getMaxUses()
	{
		return this.maxUses;
	}

	public float getDamageVsEntity()
	{
		return this.damageVsEntity;
	}

	public int getEnchantability()
	{
		return this.enchantability;
	}

}
