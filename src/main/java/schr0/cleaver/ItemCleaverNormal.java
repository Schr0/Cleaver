package schr0.cleaver;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
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

			if (target instanceof EntityLiving)
			{
				((EntityLiving) target).setCanPickUpLoot(false);
			}

			ArrayList<ItemStack> equipments = getEquipments(stack, target, attacker);

			if (!equipments.isEmpty())
			{
				for (ItemStack stackEquipment : equipments)
				{
					if (stackEquipment.isItemStackDamageable() && !stackEquipment.getItem().isDamaged(stackEquipment))
					{
						int usedDamage = random.nextInt((int) Math.round(attackAmmount) + 3);
						int stackAmount = (stackEquipment.getMaxDamage() - (stackEquipment.getMaxDamage() / usedDamage));

						stackEquipment.setItemDamage(Math.max(stackAmount, 0));
					}

					this.spawnEntityItem(stackEquipment, target);
				}

				target.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);

				return;
			}

			ArrayList<ItemStack> drops = getDrops(stack, target, attacker, random.nextInt(Math.max((100 - (int) attackAmmount * 10), 60)));

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

	private void spawnEntityItem(ItemStack itemStack, EntityLivingBase target)
	{
		World world = target.getEntityWorld();

		world.spawnEntity(new EntityItem(world, target.posX, target.posY + (double) 1.0, target.posZ, itemStack));
	}

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

	private static ArrayList<ItemStack> getDrops(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker, int rarityValue)
	{
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		World world = target.getEntityWorld();

		if (world.isRemote)
		{
			return drops;
		}

		ResourceLocation targetKey = EntityList.getKey(target);
		boolean isSmelting = (target.isBurning() || (0 < EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, stack)));
		boolean rarityHolo = (rarityValue <= 10);
		boolean rarityRare = ((10 < rarityValue) && (rarityValue <= 30));
		Random random = world.rand;

		// TODO /* ======================================== BOSS =====================================*/

		if (EntityList.getKey(EntityDragon.class).equals(targetKey))
		{
			return drops;
		}

		if (EntityList.getKey(EntityWither.class).equals(targetKey))
		{
			return drops;
		}

		// TODO /* ======================================== MONSTER =====================================*/

		if (EntityList.getKey(EntityBlaze.class).equals(targetKey))
		{
			if (rarityRare)
			{
				drops.add(new ItemStack(Items.BLAZE_ROD));
			}

			drops.add(new ItemStack(Items.BLAZE_POWDER));

			return drops;
		}

		if (EntityList.getKey(EntityCreeper.class).equals(targetKey))
		{
			drops.add(new ItemStack(Items.GUNPOWDER));

			return drops;
		}

		if (EntityList.getKey(EntityEnderman.class).equals(targetKey))
		{
			EntityEnderman enderman = (EntityEnderman) target;
			IBlockState state = enderman.getHeldBlockState();

			if (state != null)
			{
				Item block = Item.getItemFromBlock(state.getBlock());
				int meta = block.getHasSubtypes() ? state.getBlock().getMetaFromState(state) : 0;

				drops.add(new ItemStack(block, 1, meta));

				enderman.setHeldBlockState((IBlockState) null);
			}

			if (rarityRare)
			{
				if (isSmelting)
				{
					drops.add(new ItemStack(Items.ENDER_EYE));
				}
				else
				{
					drops.add(new ItemStack(Items.ENDER_PEARL));
				}
			}

			return drops;
		}

		if (EntityList.getKey(EntityEndermite.class).equals(targetKey))
		{
			return drops;
		}

		if (EntityList.getKey(EntityEvoker.class).equals(targetKey))
		{
			if (rarityHolo)
			{
				drops.add(new ItemStack(Items.TOTEM_OF_UNDYING));
			}

			if (rarityRare)
			{
				drops.add(new ItemStack(Items.EMERALD));
			}

			return drops;
		}

		if (EntityList.getKey(EntityGhast.class).equals(targetKey))
		{
			if (rarityRare)
			{
				drops.add(new ItemStack(Items.GHAST_TEAR));
			}

			drops.add(new ItemStack(Items.GUNPOWDER));

			return drops;
		}

		if (EntityList.getKey(EntityGuardian.class).equals(targetKey) || EntityList.getKey(EntityElderGuardian.class).equals(targetKey))
		{
			if (rarityRare)
			{
				switch (random.nextInt(3))
				{
					case 1 :

						int metaSalmon = ItemFishFood.FishType.SALMON.getMetadata();

						if (isSmelting)
						{
							drops.add(new ItemStack(Items.COOKED_FISH, 1, metaSalmon));
						}
						else
						{
							drops.add(new ItemStack(Items.FISH, 1, metaSalmon));
						}

						break;

					case 2 :

						drops.add(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()));

						break;

					case 3 :

						drops.add(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata()));

						break;

					default :

						int metaCod = ItemFishFood.FishType.COD.getMetadata();

						if (isSmelting)
						{
							drops.add(new ItemStack(Items.COOKED_FISH, 1, metaCod));
						}
						else
						{
							drops.add(new ItemStack(Items.FISH, 1, metaCod));
						}
				}
			}

			if (random.nextInt(100) < 40)
			{
				drops.add(new ItemStack(Items.PRISMARINE_CRYSTALS));
			}
			else
			{
				drops.add(new ItemStack(Items.PRISMARINE_SHARD));
			}

			return drops;
		}

		if (EntityList.getKey(EntityIllusionIllager.class).equals(targetKey))
		{
			return drops;
		}

		if (EntityList.getKey(EntityIronGolem.class).equals(targetKey))
		{
			if (rarityHolo)
			{
				drops.add(new ItemStack(Items.IRON_INGOT));
			}

			if (rarityRare)
			{
				drops.add(new ItemStack(Items.IRON_NUGGET));
			}

			drops.add(new ItemStack(Blocks.RED_FLOWER));

			return drops;
		}

		if (EntityList.getKey(EntityMagmaCube.class).equals(targetKey))
		{
			drops.add(new ItemStack(Items.MAGMA_CREAM));

			return drops;
		}

		if (EntityList.getKey(EntityPigZombie.class).equals(targetKey))
		{
			if (rarityHolo)
			{
				drops.add(new ItemStack(Items.GOLD_INGOT));
			}

			if (rarityRare)
			{
				drops.add(new ItemStack(Items.GOLD_NUGGET));
			}

			switch (random.nextInt(3))
			{
				case 1 :

					drops.add(new ItemStack(Items.CARROT));

					break;

				case 2 :

					drops.add(new ItemStack(Items.BAKED_POTATO));

					break;

				case 3 :

					drops.add(new ItemStack(Items.POISONOUS_POTATO));

					break;

				default :

					drops.add(new ItemStack(Items.ROTTEN_FLESH));
			}

			return drops;
		}

		if (EntityList.getKey(EntityPolarBear.class).equals(targetKey))
		{
			int metaSalmon = ItemFishFood.FishType.SALMON.getMetadata();

			if (isSmelting)
			{
				drops.add(new ItemStack(Items.COOKED_FISH, 1, metaSalmon));
			}
			else
			{
				drops.add(new ItemStack(Items.FISH, 1, metaSalmon));
			}

			return drops;
		}

		if (EntityList.getKey(EntityShulker.class).equals(targetKey))
		{
			return drops;
		}

		if (EntityList.getKey(EntitySilverfish.class).equals(targetKey))
		{
			return drops;
		}

		if (EntityList.getKey(EntitySkeleton.class).equals(targetKey))
		{
			if (rarityHolo)
			{
				drops.add(new ItemStack(Items.ARROW));
			}

			if (rarityRare)
			{
				drops.add(new ItemStack(Items.BONE));
			}

			drops.add(new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()));

			return drops;
		}

		if (EntityList.getKey(EntitySlime.class).equals(targetKey))
		{
			drops.add(new ItemStack(Items.SLIME_BALL));

			return drops;
		}

		if (EntityList.getKey(EntitySnowman.class).equals(targetKey))
		{
			drops.add(new ItemStack(Items.SNOWBALL));

			return drops;
		}

		if (EntityList.getKey(EntitySpider.class).equals(targetKey) || EntityList.getKey(EntityCaveSpider.class).equals(targetKey))
		{
			if (rarityRare)
			{
				if (isSmelting)
				{
					drops.add(new ItemStack(Items.FERMENTED_SPIDER_EYE));
				}
				else
				{
					drops.add(new ItemStack(Items.SPIDER_EYE));
				}
			}

			drops.add(new ItemStack(Items.STRING));

			return drops;
		}

		if (EntityList.getKey(EntityStray.class).equals(targetKey))
		{
			if (rarityHolo)
			{
				if (random.nextInt(100) < 40)
				{
					drops.add(new ItemStack(Items.TIPPED_ARROW, PotionType.REGISTRY.getIDForObject(PotionTypes.SLOWNESS)));
				}
				else
				{
					drops.add(new ItemStack(Items.ARROW));
				}
			}

			if (rarityRare)
			{
				drops.add(new ItemStack(Items.BONE));
			}

			drops.add(new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()));

			return drops;
		}

		if (EntityList.getKey(EntityVex.class).equals(targetKey))
		{
			return drops;
		}

		if (EntityList.getKey(EntityVindicator.class).equals(targetKey))
		{
			if (rarityRare)
			{
				drops.add(new ItemStack(Items.EMERALD));
			}

			return drops;
		}

		if (EntityList.getKey(EntityWitch.class).equals(targetKey))
		{
			if (rarityHolo)
			{
				if (random.nextInt(100) < 50)
				{
					drops.add(new ItemStack(Items.GLOWSTONE_DUST));
				}
				else
				{
					drops.add(new ItemStack(Items.REDSTONE));
				}
			}

			if (rarityRare)
			{
				if (random.nextInt(100) < 40)
				{
					drops.add(new ItemStack(Items.SPIDER_EYE));
				}
				else
				{
					drops.add(new ItemStack(Items.SUGAR));
				}
			}

			if (random.nextInt(100) < 40)
			{
				drops.add(new ItemStack(Items.GLASS_BOTTLE));
			}
			else
			{
				drops.add(new ItemStack(Items.STICK));
			}

			return drops;
		}

		if (EntityList.getKey(EntityWitherSkeleton.class).equals(targetKey))
		{
			if (rarityRare)
			{
				drops.add(new ItemStack(Items.BONE));
			}

			if (random.nextInt(100) < 40)
			{
				drops.add(new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()));
			}
			else
			{
				drops.add(new ItemStack(Items.COAL));
			}

			return drops;
		}

		if (EntityList.getKey(EntityZombie.class).equals(targetKey) || EntityList.getKey(EntityZombieVillager.class).equals(targetKey) || EntityList.getKey(EntityHusk.class).equals(targetKey) || EntityList.getKey(EntityGiantZombie.class).equals(targetKey))
		{
			if (rarityHolo)
			{
				drops.add(new ItemStack(Items.IRON_INGOT));
			}

			if (rarityRare)
			{
				drops.add(new ItemStack(Items.IRON_NUGGET));
			}

			switch (random.nextInt(3))
			{
				case 1 :

					drops.add(new ItemStack(Items.CARROT));

					break;

				case 2 :

					if (isSmelting)
					{
						drops.add(new ItemStack(Items.BAKED_POTATO));
					}
					else
					{
						drops.add(new ItemStack(Items.POTATO));
					}

					break;

				case 3 :

					drops.add(new ItemStack(Items.POISONOUS_POTATO));

					break;

				default :

					drops.add(new ItemStack(Items.ROTTEN_FLESH));
			}

			return drops;
		}

		// TODO /* ======================================== PASSIVE =====================================*/

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

		return drops;
	}

}
