package schr0.cleaver;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.entity.EntityAgeable;
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
		int chance = Math.min((10 + ((int) Math.round(attackAmmount) * 10)), 90);

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
			int cleaveAmmount = (int) Math.round(attackAmmount);

			if (target instanceof EntityLiving)
			{
				((EntityLiving) target).setCanPickUpLoot(false);
			}

			int usedDamage = Math.min(cleaveAmmount, 10);
			ArrayList<ItemStack> equipments = getEquipments((random.nextInt(usedDamage) + 3), stack, target, attacker);

			if (!equipments.isEmpty())
			{
				for (ItemStack stackEquipment : equipments)
				{
					this.spawnEntityItem(stackEquipment, target);
				}

				target.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);

				return;
			}

			int rarityValue = Math.max((100 - (cleaveAmmount * 2)), 80);
			ArrayList<ItemStack> drops = getDrops(random.nextInt(rarityValue), stack, target, attacker);

			if (!drops.isEmpty())
			{
				if (target instanceof EntityAgeable)
				{
					EntityAgeable entityAgeable = (EntityAgeable) target;

					if (entityAgeable.isChild())
					{
						drops.clear();
					}
				}

				if (MinecraftForge.EVENT_BUS.post(new CleaverNormalEvent.CleaveDropsEvent(drops, target, stack, attacker)))
				{
					return;
				}

				for (ItemStack stackDrop : drops)
				{
					this.spawnEntityItem(stackDrop, target);
				}

				target.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1.0F, 1.0F);

				return;
			}
		}
	}

	// TODO /* ======================================== MOD START =====================================*/

	private static ArrayList<ItemStack> getEquipments(int usedDamage, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
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
						int stackAmount = (stackEquipment.getMaxDamage() - (stackEquipment.getMaxDamage() / usedDamage));

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

	private static ArrayList<ItemStack> getDrops(int rarityValue, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
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
			return CleaverDropsVanilla.getDropsDragon(drops, rarityValue, stack, (EntityDragon) target, attacker);
		}

		if (EntityList.getKey(EntityWither.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsWither(drops, rarityValue, stack, (EntityWither) target, attacker);
		}

		// TODO /* ======================================== MONSTER =====================================*/

		if (EntityList.getKey(EntityBlaze.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsBlaze(drops, rarityValue, stack, (EntityBlaze) target, attacker);
		}

		if (EntityList.getKey(EntityCaveSpider.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsCaveSpider(drops, rarityValue, stack, (EntityCaveSpider) target, attacker);
		}

		if (EntityList.getKey(EntityCreeper.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsCreeper(drops, rarityValue, stack, (EntityCreeper) target, attacker);
		}

		if (EntityList.getKey(EntityElderGuardian.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsElderGuardian(drops, rarityValue, stack, (EntityElderGuardian) target, attacker);
		}

		if (EntityList.getKey(EntityEnderman.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsEnderman(drops, rarityValue, stack, (EntityEnderman) target, attacker);
		}

		if (EntityList.getKey(EntityEndermite.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsEndermite(drops, rarityValue, stack, (EntityEndermite) target, attacker);
		}

		if (EntityList.getKey(EntityEvoker.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsEvoker(drops, rarityValue, stack, (EntityEvoker) target, attacker);
		}

		if (EntityList.getKey(EntityGhast.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsGhast(drops, rarityValue, stack, (EntityGhast) target, attacker);
		}

		if (EntityList.getKey(EntityGiantZombie.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsGiantZombie(drops, rarityValue, stack, (EntityGiantZombie) target, attacker);
		}

		if (EntityList.getKey(EntityGuardian.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsGuardian(drops, rarityValue, stack, (EntityGuardian) target, attacker);
		}

		if (EntityList.getKey(EntityHusk.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsHusk(drops, rarityValue, stack, (EntityHusk) target, attacker);
		}

		if (EntityList.getKey(EntityIllusionIllager.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsIllusionIllager(drops, rarityValue, stack, (EntityIllusionIllager) target, attacker);
		}

		if (EntityList.getKey(EntityIronGolem.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsIronGolem(drops, rarityValue, stack, (EntityIronGolem) target, attacker);
		}

		if (EntityList.getKey(EntityMagmaCube.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsMagmaCube(drops, rarityValue, stack, (EntityMagmaCube) target, attacker);
		}

		if (EntityList.getKey(EntityPigZombie.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsPigZombie(drops, rarityValue, stack, (EntityPigZombie) target, attacker);
		}

		if (EntityList.getKey(EntityPolarBear.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsPolarBear(drops, rarityValue, stack, (EntityPolarBear) target, attacker);
		}

		if (EntityList.getKey(EntityShulker.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsShulker(drops, rarityValue, stack, (EntityShulker) target, attacker);
		}

		if (EntityList.getKey(EntitySilverfish.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsSilverfish(drops, rarityValue, stack, (EntitySilverfish) target, attacker);
		}

		if (EntityList.getKey(EntitySkeleton.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsSilverfish(drops, rarityValue, stack, (EntitySilverfish) target, attacker);
		}

		if (EntityList.getKey(EntitySlime.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsSlime(drops, rarityValue, stack, (EntitySlime) target, attacker);
		}

		if (EntityList.getKey(EntitySnowman.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsSnowman(drops, rarityValue, stack, (EntitySnowman) target, attacker);
		}

		if (EntityList.getKey(EntitySpider.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsSpider(drops, rarityValue, stack, (EntitySpider) target, attacker);
		}

		if (EntityList.getKey(EntityStray.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsStray(drops, rarityValue, stack, (EntityStray) target, attacker);
		}

		if (EntityList.getKey(EntityVex.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsVex(drops, rarityValue, stack, (EntityVex) target, attacker);
		}

		if (EntityList.getKey(EntityVindicator.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsVindicator(drops, rarityValue, stack, (EntityVindicator) target, attacker);
		}

		if (EntityList.getKey(EntityWitch.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsWitch(drops, rarityValue, stack, (EntityWitch) target, attacker);
		}

		if (EntityList.getKey(EntityWitherSkeleton.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsWitherSkeleton(drops, rarityValue, stack, (EntityWitherSkeleton) target, attacker);
		}

		if (EntityList.getKey(EntityZombie.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsZombie(drops, rarityValue, stack, (EntityZombie) target, attacker);
		}

		if (EntityList.getKey(EntityZombieVillager.class).equals(targetKey))
		{
			return CleaverDropsVanilla.getDropsZombieVillager(drops, rarityValue, stack, (EntityZombieVillager) target, attacker);
		}

		// TODO /* ======================================== PASSIVE =====================================*/
		/*
				if (EntityList.getKey(EntityBat.class).equals(targetKey))
				{
					return drops;
				}
		
				if (EntityList.getKey(EntityChicken.class).equals(targetKey))
				{
					if (rarityRare)
					{
						if (isSmelting)
						{
							drops.add(new ItemStack(Items.COOKED_CHICKEN));
						}
						else
						{
							drops.add(new ItemStack(Items.CHICKEN));
						}
					}
		
					drops.add(new ItemStack(Items.FEATHER));
		
					return drops;
				}
		
				if (EntityList.getKey(EntityCow.class).equals(targetKey))
				{
					if (rarityRare)
					{
						if (isSmelting)
						{
							drops.add(new ItemStack(Items.COOKED_BEEF));
						}
						else
						{
							drops.add(new ItemStack(Items.BEEF));
						}
					}
		
					drops.add(new ItemStack(Items.LEATHER));
		
					return drops;
				}
		
				if (EntityList.getKey(EntityHorse.class).equals(targetKey) || EntityList.getKey(EntityDonkey.class).equals(targetKey) || EntityList.getKey(EntityMule.class).equals(targetKey))
				{
					drops.add(new ItemStack(Items.LEATHER));
		
					return drops;
				}
		
				if (EntityList.getKey(EntityLlama.class).equals(targetKey))
				{
					drops.add(new ItemStack(Items.LEATHER));
		
					return drops;
				}
		
				if (EntityList.getKey(EntityMooshroom.class).equals(targetKey))
				{
					EntityMooshroom mooshroom = (EntityMooshroom) target;
		
					mooshroom.setDead();
					world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, mooshroom.posX, mooshroom.posY + (double) (mooshroom.height / 2.0F), mooshroom.posZ, 0.0D, 0.0D, 0.0D);
		
					EntityCow entitycow = new EntityCow(world);
					entitycow.setLocationAndAngles(mooshroom.posX, mooshroom.posY, mooshroom.posZ, mooshroom.rotationYaw, mooshroom.rotationPitch);
					entitycow.setHealth(mooshroom.getHealth());
					entitycow.renderYawOffset = mooshroom.renderYawOffset;
		
					if (mooshroom.hasCustomName())
					{
						entitycow.setCustomNameTag(mooshroom.getCustomNameTag());
					}
		
					world.spawnEntity(entitycow);
		
					drops.add(new ItemStack(Blocks.RED_MUSHROOM, 5));
		
					return drops;
				}
		
				if (EntityList.getKey(EntityOcelot.class).equals(targetKey))
				{
					return drops;
				}
		
				if (EntityList.getKey(EntityParrot.class).equals(targetKey))
				{
					drops.add(new ItemStack(Items.FEATHER));
		
					return drops;
				}
		
				if (EntityList.getKey(EntityPig.class).equals(targetKey))
				{
					EntityPig pig = (EntityPig) target;
		
					if (pig.getSaddled())
					{
						drops.add(new ItemStack(Items.SADDLE));
		
						pig.setSaddled(false);
					}
					else
					{
						if (rarityRare)
						{
							if (isSmelting)
							{
								drops.add(new ItemStack(Items.COOKED_PORKCHOP));
							}
							else
							{
								drops.add(new ItemStack(Items.PORKCHOP));
							}
						}
					}
		
					return drops;
				}
		
				if (EntityList.getKey(EntityRabbit.class).equals(targetKey))
				{
					if (rarityRare)
					{
						if (isSmelting)
						{
							drops.add(new ItemStack(Items.COOKED_RABBIT));
						}
						else
						{
							drops.add(new ItemStack(Items.RABBIT));
						}
					}
		
					drops.add(new ItemStack(Items.RABBIT_HIDE));
		
					return drops;
				}
		
				if (EntityList.getKey(EntitySheep.class).equals(targetKey))
				{
					EntitySheep sheep = (EntitySheep) target;
		
					if (sheep.getSheared())
					{
						sheep.setSheared(true);
		
						drops.add(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, sheep.getFleeceColor().getMetadata()));
					}
					else
					{
						if (rarityRare)
						{
							if (isSmelting)
							{
								drops.add(new ItemStack(Items.COOKED_MUTTON));
							}
							else
							{
								drops.add(new ItemStack(Items.MUTTON));
							}
						}
					}
		
					return drops;
				}
		
				if (EntityList.getKey(EntitySkeletonHorse.class).equals(targetKey))
				{
					if (rarityRare)
					{
						drops.add(new ItemStack(Items.BONE));
					}
		
					drops.add(new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()));
		
					return drops;
				}
		
				if (EntityList.getKey(EntitySquid.class).equals(targetKey))
				{
					drops.add(new ItemStack(Items.DYE, 1, EnumDyeColor.BLACK.getDyeDamage()));
		
					return drops;
				}
		
				if (EntityList.getKey(EntityVillager.class).equals(targetKey))
				{
					if (rarityHolo)
					{
						EntityVillager villager = (EntityVillager) target;
		
						if (attacker instanceof EntityPlayer)
						{
							EntityPlayer player = (EntityPlayer) attacker;
							MerchantRecipeList merchantrecipelist = ((EntityVillager) target).getRecipes(player);
		
							if (merchantrecipelist != null && !merchantrecipelist.isEmpty())
							{
								int merchantSize = random.nextInt(merchantrecipelist.size());
								MerchantRecipe merchantrecipe = (MerchantRecipe) merchantrecipelist.get(merchantSize);
								ItemStack stackMerchant = merchantrecipe.getItemToSell().copy();
		
								if (stackMerchant != null)
								{
									drops.add(stackMerchant);
								}
							}
						}
					}
		
					if (rarityRare)
					{
						drops.add(new ItemStack(Items.EMERALD));
					}
		
					return drops;
				}
		
				if (EntityList.getKey(EntityWolf.class).equals(targetKey))
				{
					return drops;
				}
		
				if (EntityList.getKey(EntityZombieHorse.class).equals(targetKey))
				{
					drops.add(new ItemStack(Items.ROTTEN_FLESH));
		
					return drops;
				}
		//*/

		return drops;
	}

	private void spawnEntityItem(ItemStack itemStack, EntityLivingBase target)
	{
		World world = target.getEntityWorld();

		world.spawnEntity(new EntityItem(world, target.posX, target.posY + (double) 1.0, target.posZ, itemStack));
	}

}
