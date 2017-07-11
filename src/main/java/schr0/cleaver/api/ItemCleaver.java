package schr0.cleaver.api;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ItemCleaver extends Item
{

	private float attackDamage;
	private int enchantability;

	public ItemCleaver(CleaverMaterial material)
	{
		this.maxStackSize = 1;
		this.setMaxDamage(material.getMaxUses());
		this.attackDamage = material.getDamageVsEntity();
		this.enchantability = material.getEnchantability();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
	{
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == EntityEquipmentSlot.MAINHAND)
		{
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Cleaver modifier", (double) this.attackDamage, 0));
		}

		return multimap;
	}

	@Override
	public int getItemEnchantability()
	{
		return this.enchantability;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
	{
		if (enchantment.type == EnumEnchantmentType.WEAPON)
		{
			return true;
		}

		return super.canApplyAtEnchantingTable(stack, enchantment);
	}

	@Override
	public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player)
	{
		return false;
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn)
	{
		return (blockIn.getBlock() == Blocks.WEB);
	}

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state)
	{
		Block block = state.getBlock();

		if (block == Blocks.WEB)
		{
			return 15.0F;
		}
		else
		{
			Material material = state.getMaterial();

			return (material != Material.PLANTS) && (material != Material.VINE) && (material != Material.CORAL) && (material != Material.LEAVES) && (material != Material.GOURD) ? 1.0F : 1.5F;
		}
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
	{
		if ((double) state.getBlockHardness(worldIn, pos) != 0.0D)
		{
			stack.damageItem(2, entityLiving);
		}

		return true;
	}

	// TODO /* ======================================== MOD START =====================================*/

	/**
	 * @param rawAttackAmmount
	 * @param stack
	 * @param target
	 * @param attacker
	 * @return
	 */
	public abstract float getAttackAmmount(float rawAttackAmmount, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker);

	/**
	 * @param stack
	 * @param target
	 * @param attacker
	 * @param attackAmmount
	 * @return
	 */
	public abstract boolean isCleaveTarget(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker, float attackAmmount);

	/**
	 * @param stack
	 * @param target
	 * @param attacker
	 * @param attackAmmount
	 * @param isCleaveTarget
	 */
	public abstract void onAttackTarget(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker, float attackAmmount, boolean isCleaveTarget);

}