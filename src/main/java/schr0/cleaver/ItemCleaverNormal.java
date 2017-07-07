package schr0.cleaver;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
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
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class ItemCleaverNormal extends ItemCleaver
{

	public ItemCleaverNormal()
	{
		super(CleaverMaterial.NORMAL);
	}

	@Override
	public float getAttackAmmount(float attackAmmount, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		return attackAmmount;
	}

	@Override
	public boolean isCleaveTarget(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker, float attackAmmount)
	{
		int cleaveChance = Math.min((10 + ((int) Math.round(attackAmmount) * 10)), 90);

		return (target.getEntityWorld().rand.nextInt(100) < cleaveChance);
	}

	@Override
	public void onAttackTarget(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker, float attackAmmount, boolean isCleaveTarget)
	{
		stack.damageItem(1, attacker);

		if (isCleaveTarget)
		{
			if (target instanceof EntityLiving)
			{
				((EntityLiving) target).setCanPickUpLoot(false);
			}

			World world = target.getEntityWorld();
			ArrayList<ItemStack> equipments = getEquipments(stack, target, attacker);
			ArrayList<ItemStack> drops = getDrops(stack, target, attacker);

			if (!equipments.isEmpty())
			{
				for (ItemStack stackEquipment : equipments)
				{
					if (stackEquipment.isItemStackDamageable() && !stackEquipment.getItem().isDamaged(stackEquipment))
					{
						int usedAmount = (world.rand.nextInt((int) Math.round(attackAmmount)) + 3);
						int stackDamage = (stackEquipment.getMaxDamage() - (stackEquipment.getMaxDamage() / usedAmount));

						stackEquipment.setItemDamage(Math.max(stackDamage, 0));
					}

					world.spawnEntity(new EntityItem(world, target.posX, target.posY + (double) 1.0, target.posZ, stackEquipment));
				}

				target.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);

				return;
			}

			if (!drops.isEmpty())
			{
				for (ItemStack stackDrop : drops)
				{
					world.spawnEntity(new EntityItem(world, target.posX, target.posY + (double) 1.0, target.posZ, stackDrop));
				}

				target.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1.0F, 1.0F);

				return;
			}
		}
	}

	// TODO /* ======================================== MOD START =====================================*/

	private static ArrayList<ItemStack> getEquipments(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
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
					equipments.add(stackEquipment);

					entityLiving.setItemStackToSlot(equipmentSlot, ItemStack.EMPTY);

					return equipments;
				}
			}
		}

		return equipments;
	}

	private static ArrayList<ItemStack> getDrops(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		World world = target.getEntityWorld();

		if (world.isRemote)
		{
			return drops;
		}

		Random random = world.rand;
		boolean isSmelting = (target.isBurning() || (0 < EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, stack)));
		ResourceLocation targetKey = EntityList.getKey(target);

		// TODO /* ======================================== BOSS =====================================*/

		if (EntityList.getKey(EntityDragon.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityWither.class).equals(targetKey))
		{
			// none
		}

		// TODO /* ======================================== MONSTER =====================================*/

		if (EntityList.getKey(EntityBlaze.class).equals(targetKey))
		{
			drops.add(new ItemStack(Items.BLAZE_POWDER));
		}

		if (EntityList.getKey(EntityCaveSpider.class).equals(targetKey))
		{
			drops.add(new ItemStack(Items.STRING));
		}

		if (EntityList.getKey(EntityCreeper.class).equals(targetKey))
		{
			drops.add(new ItemStack(Items.GUNPOWDER));
		}

		if (EntityList.getKey(EntityElderGuardian.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityEnderman.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityEndermite.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityEvoker.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityGhast.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityGiantZombie.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityGuardian.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityHusk.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityIllusionIllager.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityIronGolem.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityMagmaCube.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityPigZombie.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityPolarBear.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityShulker.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntitySilverfish.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntitySkeleton.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntitySlime.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntitySnowman.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntitySpider.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityStray.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityVex.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityVindicator.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityWitch.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityWitherSkeleton.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityZombie.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityZombieVillager.class).equals(targetKey))
		{
			// none
		}

		// TODO /* ======================================== PASSIVE =====================================*/

		if (EntityList.getKey(EntityBat.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityChicken.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityCow.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityDonkey.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityHorse.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityLlama.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityMooshroom.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityMule.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityOcelot.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityParrot.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityPig.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityRabbit.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntitySheep.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntitySkeletonHorse.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntitySquid.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityVillager.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityWolf.class).equals(targetKey))
		{
			// none
		}

		if (EntityList.getKey(EntityZombieHorse.class).equals(targetKey))
		{
			// none
		}

		if (MinecraftForge.EVENT_BUS.post(new CleaverNormalEvent.CleaveDropsEvent(drops, target, stack, attacker)))
		{
			return (new ArrayList<ItemStack>());
		}

		return drops;
	}

}
