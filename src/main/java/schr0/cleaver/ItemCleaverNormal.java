package schr0.cleaver;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import schr0.cleaver.api.CleaverMaterial;
import schr0.cleaver.api.ItemCleaver;

public class ItemCleaverNormal extends ItemCleaver
{

	public ItemCleaverNormal()
	{
		super(CleaverMaterial.NORMAL);
	}

	@Override
	public float getAttackAmmount(float rawAttackAmmount, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		return rawAttackAmmount;
	}

	@Override
	public boolean isCleaveTarget(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker, float attackAmmount)
	{
		int chance = Math.min((10 + (this.getSharpnessAmount(attackAmmount) * 10)), 80);

		return (target.getEntityWorld().rand.nextInt(100) < chance);
	}

	@Override
	public void onAttackTarget(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker, float attackAmmount, boolean isCleaveTarget)
	{
		stack.damageItem(1, attacker);

		if (isCleaveTarget)
		{
			World world = target.getEntityWorld();
			Random random = world.rand;

			if (target instanceof EntityLiving)
			{
				((EntityLiving) target).setCanPickUpLoot(false);
			}

			int used = random.nextInt(Math.min((3 + this.getSharpnessAmount(attackAmmount)), 7));
			ArrayList<ItemStack> equipments = getEquipmentsLivingBase(used, stack, target, attacker);

			if (!equipments.isEmpty())
			{
				for (ItemStack stackEquipment : equipments)
				{
					this.spawnEntityItem(stackEquipment, target);
				}

				target.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);

				return;
			}

			int rarity = random.nextInt(Math.max((100 - (this.getSharpnessAmount(attackAmmount) * 2)), 80));
			ArrayList<ItemStack> drops = getDropsLivingBase(rarity, stack, target, attacker);

			if (MinecraftForge.EVENT_BUS.post(new CleaverNormalEvent.CleaveDropsEvent(drops, rarity, stack, target, attacker)))
			{
				return;
			}

			if (!drops.isEmpty())
			{
				for (ItemStack stackDrop : drops)
				{
					this.spawnEntityItem(stackDrop, target);
				}

				target.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1.0F, 1.0F);
			}
		}
	}

	// TODO /* ======================================== MOD START =====================================*/

	private int getSharpnessAmount(float attackAmmount)
	{
		return (int) Math.round(attackAmmount);
	}

	private static ArrayList<ItemStack> getEquipmentsLivingBase(int used, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		ArrayList<ItemStack> equipments = new ArrayList<ItemStack>();

		if (target instanceof EntityLiving)
		{
			EntityLiving entityLiving = (EntityLiving) target;

			for (EntityEquipmentSlot equipmentSlot : EntityEquipmentSlot.values())
			{
				ItemStack stackEquipment = entityLiving.getItemStackFromSlot(equipmentSlot);

				if (!stackEquipment.isEmpty())
				{
					if (stackEquipment.isItemStackDamageable() && !stackEquipment.getItem().isDamaged(stackEquipment))
					{
						int stackAmount = (stackEquipment.getMaxDamage() - (stackEquipment.getMaxDamage() / used));

						stackEquipment.setItemDamage(Math.max(stackAmount, 0));
					}

					equipments.add(stackEquipment);

					entityLiving.setItemStackToSlot(equipmentSlot, ItemStack.EMPTY);

					return equipments;
				}
			}
		}

		return equipments;
	}

	private static ArrayList<ItemStack> getDropsLivingBase(int rarity, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		World world = target.getEntityWorld();

		if (world.isRemote)
		{
			return drops;
		}

		ResourceLocation targetKey = EntityList.getKey(target);

		// TODO /* ======================================== BOSS =====================================*/

		if (EntityList.getKey(EntityDragon.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsDragon(drops, rarity, stack, (EntityDragon) target, attacker);
		}

		if (EntityList.getKey(EntityWither.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsWither(drops, rarity, stack, (EntityWither) target, attacker);
		}

		// TODO /* ======================================== MONSTER =====================================*/

		if (EntityList.getKey(EntityBlaze.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsBlaze(drops, rarity, stack, (EntityBlaze) target, attacker);
		}

		if (EntityList.getKey(EntityCaveSpider.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsCaveSpider(drops, rarity, stack, (EntityCaveSpider) target, attacker);
		}

		if (EntityList.getKey(EntityCreeper.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsCreeper(drops, rarity, stack, (EntityCreeper) target, attacker);
		}

		if (EntityList.getKey(EntityElderGuardian.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsElderGuardian(drops, rarity, stack, (EntityElderGuardian) target, attacker);
		}

		if (EntityList.getKey(EntityEnderman.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsEnderman(drops, rarity, stack, (EntityEnderman) target, attacker);
		}

		if (EntityList.getKey(EntityEndermite.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsEndermite(drops, rarity, stack, (EntityEndermite) target, attacker);
		}

		if (EntityList.getKey(EntityEvoker.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsEvoker(drops, rarity, stack, (EntityEvoker) target, attacker);
		}

		if (EntityList.getKey(EntityGhast.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsGhast(drops, rarity, stack, (EntityGhast) target, attacker);
		}

		if (EntityList.getKey(EntityGiantZombie.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsGiantZombie(drops, rarity, stack, (EntityGiantZombie) target, attacker);
		}

		if (EntityList.getKey(EntityGuardian.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsGuardian(drops, rarity, stack, (EntityGuardian) target, attacker);
		}

		if (EntityList.getKey(EntityHusk.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsHusk(drops, rarity, stack, (EntityHusk) target, attacker);
		}

		if (EntityList.getKey(EntityIllusionIllager.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsIllusionIllager(drops, rarity, stack, (EntityIllusionIllager) target, attacker);
		}

		if (EntityList.getKey(EntityIronGolem.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsIronGolem(drops, rarity, stack, (EntityIronGolem) target, attacker);
		}

		if (EntityList.getKey(EntityMagmaCube.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsMagmaCube(drops, rarity, stack, (EntityMagmaCube) target, attacker);
		}

		if (EntityList.getKey(EntityPigZombie.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsPigZombie(drops, rarity, stack, (EntityPigZombie) target, attacker);
		}

		if (EntityList.getKey(EntityPolarBear.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsPolarBear(drops, rarity, stack, (EntityPolarBear) target, attacker);
		}

		if (EntityList.getKey(EntityShulker.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsShulker(drops, rarity, stack, (EntityShulker) target, attacker);
		}

		if (EntityList.getKey(EntitySilverfish.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsSilverfish(drops, rarity, stack, (EntitySilverfish) target, attacker);
		}

		if (EntityList.getKey(EntitySkeleton.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsSilverfish(drops, rarity, stack, (EntitySilverfish) target, attacker);
		}

		if (EntityList.getKey(EntitySlime.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsSlime(drops, rarity, stack, (EntitySlime) target, attacker);
		}

		if (EntityList.getKey(EntitySnowman.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsSnowman(drops, rarity, stack, (EntitySnowman) target, attacker);
		}

		if (EntityList.getKey(EntitySpider.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsSpider(drops, rarity, stack, (EntitySpider) target, attacker);
		}

		if (EntityList.getKey(EntityStray.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsStray(drops, rarity, stack, (EntityStray) target, attacker);
		}

		if (EntityList.getKey(EntityVex.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsVex(drops, rarity, stack, (EntityVex) target, attacker);
		}

		if (EntityList.getKey(EntityVindicator.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsVindicator(drops, rarity, stack, (EntityVindicator) target, attacker);
		}

		if (EntityList.getKey(EntityWitch.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsWitch(drops, rarity, stack, (EntityWitch) target, attacker);
		}

		if (EntityList.getKey(EntityWitherSkeleton.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsWitherSkeleton(drops, rarity, stack, (EntityWitherSkeleton) target, attacker);
		}

		if (EntityList.getKey(EntityZombie.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsZombie(drops, rarity, stack, (EntityZombie) target, attacker);
		}

		if (EntityList.getKey(EntityZombieVillager.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsZombieVillager(drops, rarity, stack, (EntityZombieVillager) target, attacker);
		}

		// TODO /* ======================================== PASSIVE =====================================*/

		if (EntityList.getKey(EntityBat.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsBat(drops, rarity, stack, (EntityBat) target, attacker);
		}

		if (EntityList.getKey(EntityChicken.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsChicken(drops, rarity, stack, (EntityChicken) target, attacker);
		}

		if (EntityList.getKey(EntityCow.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsCow(drops, rarity, stack, (EntityCow) target, attacker);
		}

		if (EntityList.getKey(EntityDonkey.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsDonkey(drops, rarity, stack, (EntityDonkey) target, attacker);
		}

		if (EntityList.getKey(EntityHorse.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsHorse(drops, rarity, stack, (EntityHorse) target, attacker);
		}

		if (EntityList.getKey(EntityLlama.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsLlama(drops, rarity, stack, (EntityLlama) target, attacker);
		}

		if (EntityList.getKey(EntityMooshroom.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsMooshroom(drops, rarity, stack, (EntityMooshroom) target, attacker);
		}

		if (EntityList.getKey(EntityMule.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsMule(drops, rarity, stack, (EntityMule) target, attacker);
		}

		if (EntityList.getKey(EntityOcelot.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsOcelot(drops, rarity, stack, (EntityOcelot) target, attacker);
		}

		if (EntityList.getKey(EntityParrot.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsParrot(drops, rarity, stack, (EntityParrot) target, attacker);
		}

		if (EntityList.getKey(EntityPig.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsPig(drops, rarity, stack, (EntityPig) target, attacker);
		}

		if (EntityList.getKey(EntityRabbit.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsRabbit(drops, rarity, stack, (EntityRabbit) target, attacker);
		}

		if (EntityList.getKey(EntitySheep.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsSheep(drops, rarity, stack, (EntitySheep) target, attacker);
		}

		if (EntityList.getKey(EntitySkeletonHorse.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsSkeletonHorse(drops, rarity, stack, (EntitySkeletonHorse) target, attacker);
		}

		if (EntityList.getKey(EntitySquid.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsSquid(drops, rarity, stack, (EntitySquid) target, attacker);
		}

		if (EntityList.getKey(EntityVillager.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsVillager(drops, rarity, stack, (EntityVillager) target, attacker);
		}

		if (EntityList.getKey(EntityWolf.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsWolf(drops, rarity, stack, (EntityWolf) target, attacker);
		}

		if (EntityList.getKey(EntityZombieHorse.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsZombieHorse(drops, rarity, stack, (EntityZombieHorse) target, attacker);
		}

		return drops;
	}

	private void spawnEntityItem(ItemStack itemStack, EntityLivingBase target)
	{
		World world = target.getEntityWorld();

		world.spawnEntity(new EntityItem(world, target.posX, target.posY + (double) 1.0, target.posZ, itemStack));
	}

}
