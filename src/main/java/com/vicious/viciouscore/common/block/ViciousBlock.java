package com.vicious.viciouscore.common.block;


import com.vicious.viciouscore.ViciousCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Hashtable;
import java.util.Random;


public class ViciousBlock extends Block {
    private final Hashtable<String, IProperty<?>> properties = new Hashtable<>();
    public Attributes attributes;

    public ViciousBlock(String name, Material materialIn) {
        super(materialIn);
        setCreativeTab(ViciousCore.TABVICIOUS);
        setUnlocalizedName(name);
        setRegistryName(name);

        this.attributes = new Attributes();

    }

    public ViciousBlock(String name) {
        this(name, Material.AIR);
    }

    @SuppressWarnings("unchecked") // SHUT
    public ViciousBlock addProperties(IProperty<?>... properties) {
        for (IProperty<?> property : properties) {
            this.properties.put(property.getName(), property);
        }
        // Reflections to update properties because cannot modify post createBlockState()
        // Internal properties
        System.out.println("add");
        return this;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        System.out.println("create");
        return super.createBlockState();
    }

    public ViciousBlock setPassableCondition(boolean condition) {
        IProperty<?> p = properties.get("passable");
        if (p != null) {
            PropertyBool prop = (PropertyBool) p;
            Boolean value = this.getDefaultState().getValue(prop);
            if (condition != value) this.getDefaultState().cycleProperty(prop);
        }
        return this;
    }

    public ViciousBlock setReplaceableCondition(boolean condition) {
        IProperty<?> p = properties.get("replaceable");
        if (p != null) {
            PropertyBool prop = (PropertyBool) p;
            Boolean value = this.getDefaultState().getValue(prop);
            if (condition != value) this.getDefaultState().cycleProperty(prop);
        }
        return this;
    }

    public ViciousBlock setRandomTickAction(Runnable r1) {
        super.setTickRandomly(true);
        attributes.setRunnable("udt", r1);
        return this;
    }

    public ViciousBlock setDisplayTickAction(Runnable r1) {
        attributes.setRunnable("rdt", r1);
        return this;
    }

    public ViciousBlock setOnBlockDestroyByPlayerAction(Runnable r1) {
        attributes.setRunnable("bdp", r1);
        return this;
    }

    public ViciousBlock setTickRate(int tickRate) {
        BlockIntegers.TICKRATE.value = tickRate;
        return this;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        PropertyBool passable = (PropertyBool) properties.get("passable");
        PropertyBool replaceable = (PropertyBool) properties.get("replaceable");
        if (passable != null && replaceable != null) return 3;
        if (replaceable != null) return 2;
        if (passable != null) return 1;
        return 0;
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        PropertyBool bool = (PropertyBool) properties.get("passable");
        return bool == null ? super.isReplaceable(worldIn, pos) : this.getDefaultState().getValue(bool);
    }

    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
        PropertyBool bool = (PropertyBool) properties.get("replaceable");
        return bool == null ? super.isReplaceable(worldIn, pos) : this.getDefaultState().getValue(bool);

    }

    @Override
    public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {
        return super.canCollideCheck(state, hitIfLiquid);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        attributes.runAction("udt", worldIn, state, pos, rand);
        super.updateTick(worldIn, pos, state, rand);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        attributes.runAction("rdt", worldIn, stateIn, pos, rand);
        super.randomDisplayTick(stateIn, worldIn, pos, rand);
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        attributes.runAction("bdp", worldIn, state, pos, null);
        super.onBlockDestroyedByPlayer(worldIn, pos, state);
    }

    @Override
    public int tickRate(World worldIn) {
        return BlockIntegers.TICKRATE.value == null ? super.tickRate(worldIn) : BlockIntegers.TICKRATE.value;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public int quantityDropped(Random random) {
        return super.quantityDropped(random);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return super.getItemDropped(state, rand, fortune);
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return super.damageDropped(state);
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return super.getBlockLayer();
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return super.canPlaceBlockOnSide(worldIn, pos, side);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        super.onEntityWalk(worldIn, pos, entityIn);
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        super.onBlockClicked(worldIn, pos, playerIn);
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        return super.getSilkTouchDrop(state);
    }

    @Override
    public int quantityDroppedWithBonus(int fortune, Random random) {
        return super.quantityDroppedWithBonus(fortune, random);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public String getLocalizedName() {
        return super.getLocalizedName();
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        super.getSubBlocks(itemIn, items);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        super.getDrops(drops, world, pos, state, fortune);
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        return super.rotateBlock(world, pos, axis);
    }

    @Override
    public boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        return super.canBeConnectedTo(world, pos, facing);
    }

    private enum BlockIntegers {
        TICKRATE;

        public Integer value;
    }

    public static class Attributes {
        private final Hashtable<String, Runnable> actions = new Hashtable<>();
        public World world;
        public IBlockState state;
        public BlockPos pos;
        public Random rand;

        private void setAttributes(World worldIn, IBlockState stateIn, BlockPos pos, Random rand) {
            this.world = worldIn;
            this.state = stateIn;
            this.pos = pos;
            this.rand = rand;
        }

        public void setRunnable(String name, Runnable r1) {
            actions.put(name, r1);
        }

        public void runAction(String name, World worldIn, IBlockState stateIn, BlockPos pos, Random rand) {
            Runnable action = actions.get(name);
            if (action != null) {
                this.setAttributes(worldIn, stateIn, pos, rand);
                try {
                    action.run();
                } catch (NullPointerException ignored) {
                }
            }
        }

        @Override
        public String toString() {
            return "ViciousBlockAttributes{" +
                    ", state=" + state +
                    ", pos=" + pos +
                    '}';
        }
    }
}
