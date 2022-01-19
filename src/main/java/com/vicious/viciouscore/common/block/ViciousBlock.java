package com.vicious.viciouscore.common.block;


import com.vicious.viciouscore.ViciousCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import static com.vicious.viciouscore.ViciousCore.logger;


public class ViciousBlock extends Block {
    public Hashtable<String, IProperty<?>> properties;
    public Attributes attributes;

    public ViciousBlock(String name, Material materialIn, IProperty<?>... properties) {
        super(addProperties(name, materialIn, properties));
        setCreativeTab(ViciousCore.TABVICIOUS);
        setUnlocalizedName(name);
        setRegistryName(name);

        this.attributes = new Attributes();
    }

    public ViciousBlock(String name) {
        this(name, Material.AIR);
    }

    // fuckery to get around Block being initialized first
    private static Material addProperties(String name, Material materialIn, IProperty<?>... props) {
        Hashtable<String, IProperty<?>> internal = new Hashtable<>();
        for (IProperty<?> property : props) {
            internal.put(property.getName(), property);
        }
        Attributes.lastAddedName = name;
        Attributes.properties.put(name, internal);
        return materialIn;
    }

    public ViciousBlock setPassableCondition(boolean condition) {
        return setPassableCondition(condition, null);
    }

    public ViciousBlock setPassableCondition(boolean condition, Runnable withAffect) {
        if (withAffect != null) attributes.setRunnable("passingEffect", withAffect);
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
        attributes.setRunnable("updateTick", r1);
        return this;
    }

    public ViciousBlock setDisplayTickAction(Runnable r1) {
        attributes.setRunnable("displayTick", r1);
        return this;
    }


    public ViciousBlock setHardness(double hardness) {
        super.setHardness((float) hardness);
        return this;
    }

    /**
     * Method to set the code which runs when {@link #onBlockDestroyedByPlayer} is called
     * <br>Called after a player destroys this Block - the posiiton pos may no longer hold the state indicated
     *
     * @param r1 code to run
     *           <ul>Avaliable Attributes:
     *              <li>world</li>
     *              <li>position</li>
     *              <li>state</li>
     * @return self
     */
    public ViciousBlock setOnBlockDestroyByPlayerAction(Runnable r1) {
        attributes.setRunnable("blockDestroyByPlayer", r1);
        return this;
    }

    /**
     * Method to set the code which runs when {@link #onBlockActivated} is called
     * <br>Called when the block is right-clicked by a player.
     *
     * @param r1 code to run
     *           <ul>Avaliable Attributes:
     *              <li>world</li>
     *              <li>position</li>
     *              <li>state</li>
     *              <li>player</li>
     *              <li>hand</li>
     *              <li>facing</li>
     *              <li>hitX</li>
     *              <li>hitY</li>
     *              <li>hitZ</li>
     * @return self
     */
    public ViciousBlock setOnBlockActivatedAction(Runnable r1) {
        attributes.setRunnable("blockActivated", r1);
        return this;
    }

    /**
     * Method to set the code which runs when {@link #onBlockClicked} is called
     *
     * @param r1 code to run
     *           <ul>Avaliable Attributes:
     *              <li>world</li>
     *              <li>position</li>
     *              <li>player</li>
     * @return self
     */
    public ViciousBlock setOnBlockLeftClickedAction(Runnable r1) {
        attributes.setRunnable("blockClicked", r1);
        return this;
    }

    /**
     * Method to set the code which runs when {@link #onEntityWalk} is called
     *
     * @param r1 code to run
     *           <ul>Avaliable Attributes:
     *              <li>world</li>
     *              <li>position</li>
     *              <li>entity</li>
     * @return self
     */
    public ViciousBlock setOnEntityWalkAction(Runnable r1) {
        attributes.setRunnable("entityWalk", r1);
        return this;
    }

    /**
     * Method to set the code which runs when {@link #getDrops} is called
     * <br>{@link #getDrops} should get a complete list of items dropped from this block.
     * <br> drops should be added to {@code attributes.drops}
     *
     * @param r1 code to run
     *           <ul>Avaliable Attributes:
     *              <li>world</li>
     *              <li>position</li>
     *              <li>state</li>
     *              <li>fortune</li>
     *              <li>drops</li>
     * @return self
     */
    public ViciousBlock addToDrops(Runnable r1) {
        attributes.setRunnable("addDrops", r1);
        return this;
    }


    /**
     * Set what kind of render layer this block will have when rendered
     * <br> Will defualt to {@link Block#getBlockLayer()}
     *
     * @param type BlockRenderLayer type to render as. See {@link BlockRenderLayer}
     * @return self
     */
    public ViciousBlock setBlockRenderLayerType(BlockRenderLayer type) {
        attributes.blockRenderLayer = type;
        return this;
    }

    /**
     * Method to set the code which runs when {@link #canPlaceBlockAt} is called
     * <br> Checks if this block can be placed exactly at the given position.
     *
     * @param r1 code to run
     *           <br> Will defualt to {@link Block#canPlaceBlockAt} if canPlaceAt is not set in {@code attributes.bAttr}
     *           <ul>Avaliable Attributes:
     *              <li>world</li>
     *              <li>position</li>
     * @return self
     */
    public ViciousBlock setBlockCanPlaceAtCondition(Runnable r1) {
        attributes.setRunnable("canPlaceAt", r1);
        return this;
    }

    /**
     * Method to set the code which runs when {@link #canPlaceBlockOnSide} is called
     * <br> Check whether this Block can be placed at pos, while aiming at the specified side of an adjacent block
     *
     * @param r1 code to run
     *           <br> Will defualt to {@link Block#canPlaceBlockOnSide} if canPlaceBlockOnSide is not set in {@code attributes.bAttr}
     *           <ul>Avaliable Attributes:
     *              <li>world</li>
     *              <li>position</li>
     *              <li>facing</li>
     * @return self
     */
    public ViciousBlock setBlockPlaceOnSideCondition(Runnable r1) {
        attributes.setRunnable("placeOnSide", r1);
        return this;
    }

    /**
     * Set which item drops when the block is harvested
     * <br> Defaults to {@link Block#getItemDropped}
     *
     * @param item Item to drop
     * @return self
     */
    public ViciousBlock setItemDropped(Item item) {
        attributes.droppedItem = item;
        return this;
    }

    /**
     * Set the number of items to drop on block destruction with random additonal number.
     * <br> Defaults to 1
     *
     * @param quantity  number of items
     * @param randRange range for max number of random item drops (inclusive: 0 -> inclusive: randRange)
     * @return self
     */
    public ViciousBlock setQuantityDropped(int quantity, int randRange) {
        attributes.iAttr.put("quantity", quantity);
        attributes.iAttr.put("randRange", randRange);
        return this;
    }

    /**
     * Set the number of items to drop on block destruction.
     * <br> Defaults to 1
     *
     * @param quantity number of items
     * @return self
     */
    public ViciousBlock setQuantityDropped(int quantity) {
        return setQuantityDropped(quantity, 0);
    }

    /**
     * Method to set the code which runs when {@link #quantityDroppedWithBonus} is called
     *
     * @param r1 code to run
     *           <br> Will default to {@link Block#quantityDroppedWithBonus} if dropsFromFortune is not assigned in {@code attributes.iAttr}
     *           <ul>Avaliable Attributes:
     *              <li>fortune</li>
     *              <li>random</li>
     * @return self
     */
    public ViciousBlock addDropsFromFortune(Runnable r1) {
        attributes.setRunnable("dropsFromFortune", r1);
        return this;
    }

    /**
     * Method to set the code which runs when {@link #onBlockAdded} is called
     * <br> Called after block is set in Chunk data but before TileEntity is set
     *
     * @param r1 code to run
     *           <ul>Avaliable Attributes:
     *              <li>world</li>
     *              <li>position</li>
     *              <li>state</li>
     * @return self
     */
    public ViciousBlock setOnBlockAddedAction(Runnable r1) {
        attributes.setRunnable("blockAdded", r1);
        return this;
    }

    /**
     * Method to set the code which runs when {@link #breakBlock} is called
     * <br> Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     *
     * @param r1 code to run
     *           <ul>Avaliable Attributes:
     *              <li>world</li>
     *              <li>position</li>
     *              <li>state</li>
     * @return self
     */
    public ViciousBlock setOnBreakBlockAction(Runnable r1) {
        attributes.setRunnable("breakBlock", r1);
        return this;
    }

    /**
     * Method to set the code which runs when {@link #harvestBlock} is called
     * <br> Spawns the block's drops in the world. By the time this is called the Block has possibly been set to air via {@link Block#removedByPlayer}
     *
     * @param r1 code to run
     *           <ul>Avaliable Attributes:
     *              <li>world</li>
     *              <li>position</li>
     *              <li>player</li>
     *              <li>state</li>
     *              <li>stack</li>
     *              <li>tile</li>
     * @return self
     */
    public ViciousBlock setHarvestBlockAction(Runnable r1) {
        attributes.setRunnable("harvestBlock", r1);
        return this;
    }

    /**
     * Method to set the code which runs when {@link #onEntityCollidedWithBlock} is called
     *
     * @param r1 code to run
     *           <ul>Avaliable Attributes:
     *              <li>world</li>
     *              <li>position</li>
     *              <li>state</li>
     *              <li>entity</li>
     * @return self
     */
    public ViciousBlock setOnEntityCollideWithBlockAction(Runnable r1) {
        attributes.setRunnable("entityCollide", r1);
        return this;
    }

    /**
     * Method to set the ItemStack that is dropped when block is mined with silk touch.
     * <br> Overwritten by {@link #setSilkTouchDrop(Runnable)}
     *
     * @param stack ItemStack to drop when mined with silk touch
     * @return self
     */
    public ViciousBlock setSilkTouchDrop(ItemStack stack) {
        attributes.silkTouchDrop = stack;
        return this;
    }

    /**
     * Method to set the ItemStack that is dropped when block is mined with silk touch.
     * <br> Overwrites {@link #setSilkTouchDrop(ItemStack)}
     *
     * @param r1 code to run
     *           <br>If {@code attributes.silkTouchDrop} is not assigned default drop will be assigned to {@link Block#getSilkTouchDrop}
     *           <ul>Avaliable Attributes:
     *              <li>state</li>
     * @return self
     */
    public ViciousBlock setSilkTouchDrop(Runnable r1) {
        attributes.setRunnable("silkTouchDrop", r1);
        return this;
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getMetaFromState(IBlockState state) {
        StringBuilder bin = new StringBuilder();
        List<PropertyInteger> propertyIntegers = new ArrayList<>();
        List<PropertyEnum<?>> propertyEnums = new ArrayList<>();
        for (IProperty<?> property : properties.values()) {
            if (property instanceof PropertyBool) bin.append(state.getValue((PropertyBool) property) ? "0" : "1");
            else if (property instanceof PropertyInteger) propertyIntegers.add((PropertyInteger) property);
            else if (property instanceof PropertyEnum<?>) propertyEnums.add((PropertyEnum<?>) property);
        }
        String value = bin.toString();
        if (value.equals("")) return 0;
        int meta = Integer.parseInt(value, 2) + 1;
        for (PropertyInteger property : propertyIntegers) meta += state.getValue(property);
        for (PropertyEnum<?> property : propertyEnums) {
            Enum<?> e = state.getValue(property);
            if (e instanceof EnumDyeColor) meta += ((EnumDyeColor) e).getMetadata();
        }
        this.attributes.iAttr.put("meta", meta);
        return meta;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        PropertyBool bool = (PropertyBool) properties.get("passable");
        return bool == null ? super.isReplaceable(worldIn, pos) : this.getDefaultState().getValue(bool);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
        PropertyBool bool = (PropertyBool) properties.get("replaceable");
        return bool == null ? super.isReplaceable(worldIn, pos) : this.getDefaultState().getValue(bool);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {
        return super.canCollideCheck(state, hitIfLiquid);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        attributes.runAction("updateTick",
                new Attributes.RunActionParams(attributes)
                        .world(worldIn)
                        .pos(pos)
                        .state(state)
                        .rand(rand));
        super.updateTick(worldIn, pos, state, rand);
    }

    @SideOnly(Side.CLIENT)
    @Override
    @ParametersAreNonnullByDefault
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        attributes.runAction("displayTick",
                new Attributes.RunActionParams(attributes)
                        .world(worldIn)
                        .pos(pos)
                        .state(stateIn)
                        .rand(rand));
        super.randomDisplayTick(stateIn, worldIn, pos, rand);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        attributes.runAction("blockDestroyByPlayer",
                new Attributes.RunActionParams(attributes)
                        .world(worldIn)
                        .pos(pos)
                        .state(state));
        super.onBlockDestroyedByPlayer(worldIn, pos, state);
    }

    @Override
    @ParametersAreNonnullByDefault
    public int tickRate(World worldIn) {
        return attributes.iAttr.getOrDefault("tickRate", super.tickRate(worldIn));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        attributes.runAction("blockAdded",
                new Attributes.RunActionParams(attributes)
                        .world(worldIn)
                        .pos(pos)
                        .state(state));
        super.onBlockAdded(worldIn, pos, state);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        attributes.runAction("breakBlock",
                new Attributes.RunActionParams(attributes)
                        .world(worldIn)
                        .pos(pos)
                        .state(state));
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    @ParametersAreNonnullByDefault
    public int quantityDropped(Random random) {
        int rand = attributes.iAttr.getOrDefault("randRange", 0) == 0 ? 0 : random.nextInt(attributes.iAttr.get("randRange"));
        return attributes.iAttr.getOrDefault("quantityDropped", super.quantityDropped(random) + rand);
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return getOrDefault(attributes.droppedItem, super.getItemDropped(state, rand, fortune));
    }

    @Override
    @ParametersAreNonnullByDefault
    public int damageDropped(IBlockState state) {
        return attributes.iAttr.getOrDefault("meta", 0);
    }

    @Override
    @Nonnull
    public BlockRenderLayer getBlockLayer() {
        return getOrDefault(attributes.blockRenderLayer, super.getBlockLayer());
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        attributes.runAction("placeOnSide",
                new Attributes.RunActionParams(attributes)
                        .world(worldIn)
                        .pos(pos)
                        .facing(side));
        if (!attributes.bAttr.containsKey("canPlaceBlockOnSide")) {
            logger.warn(String.format("%s: \"canPlaceBlockOnSide\" not assigned in boolean attributes. Defaulting to Block.canPlaceBlockOnSide", this.getLocalizedName()));
            attributes.bAttr.put("canPlaceBlockOnSide", super.canPlaceBlockOnSide(worldIn, pos, side));
        }
        return attributes.bAttr.get("canPlaceBlockOnSide");
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        attributes.runAction("placeBlockAt",
                new Attributes.RunActionParams(attributes)
                        .world(worldIn)
                        .pos(pos));
        if (!attributes.bAttr.containsKey("canPlaceAt")) {
            logger.warn(String.format("%s: \"canPlaceAt\" not assigned in boolean attributes. Defaulting to Block.canPlaceAt", this.getLocalizedName()));
            attributes.bAttr.put("canPlaceAt", super.canPlaceBlockAt(worldIn, pos));
        }
        return attributes.bAttr.get("canPlaceAt");
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        attributes.runAction("blockActivated",
                new Attributes.RunActionParams(attributes)
                        .world(worldIn)
                        .pos(pos)
                        .state(state)
                        .player(playerIn)
                        .hand(hand)
                        .facing(facing)
                        .hitCoords(hitX, hitY, hitX));
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        attributes.runAction("entityWalk",
                new Attributes.RunActionParams(attributes)
                        .world(worldIn)
                        .pos(pos)
                        .entity(entityIn));
        super.onEntityWalk(worldIn, pos, entityIn);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        attributes.runAction("blockClicked",
                new Attributes.RunActionParams(attributes)
                        .world(worldIn)
                        .pos(pos)
                        .player(playerIn));
        super.onBlockClicked(worldIn, pos, playerIn);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        attributes.runAction("entityCollide",
                new Attributes.RunActionParams(attributes)
                        .world(worldIn)
                        .pos(pos)
                        .state(state)
                        .entity(entityIn));
        super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        attributes.runAction("harvestBlock",
                new Attributes.RunActionParams(attributes)
                        .world(worldIn)
                        .player(player)
                        .pos(pos)
                        .state(state)
                        .tile(te)
                        .stack(stack));
        super.harvestBlock(worldIn, player, pos, state, te, stack);
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        attributes.runAction("silkTouchDrop",
                new Attributes.RunActionParams(attributes)
                        .state(state));
        if (attributes.silkTouchDrop == null) {
            logger.warn(String.format("%s: \"silkTouchDrop\" not assigned in attributes. Defaulting to Block.getSilkTouchDrop", this.getLocalizedName()));
            attributes.silkTouchDrop = super.getSilkTouchDrop(state);
        }
        return attributes.silkTouchDrop;
    }

    @Override
    @ParametersAreNonnullByDefault
    public int quantityDroppedWithBonus(int fortune, Random random) {
        attributes.runAction("dropsFromFortune",
                new Attributes.RunActionParams(attributes)
                        .fortune(fortune)
                        .rand(random));
        if (!attributes.iAttr.containsKey("dropsFromFortune")) {
            logger.warn(String.format("%s: \"dropsFromFortune\" not assigned in integer attributes. Defaulting to Block.quantityDroppedWithBonus", this.getLocalizedName()));
            attributes.iAttr.put("dropsFromFortune", super.quantityDroppedWithBonus(fortune, random));
        }
        return attributes.iAttr.get("dropsFromFortune");
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    @Nonnull
    public String getLocalizedName() {
        return super.getLocalizedName();
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        super.getSubBlocks(itemIn, items);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        this.properties = Attributes.properties.get(Attributes.lastAddedName);
        IProperty<?>[] pArr = new IProperty<?>[properties.size()];
        int i = 0;
        for (IProperty<?> property : properties.values()) {
            pArr[i] = property;
            i++;
        }
        return new BlockStateContainer(this, pArr);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        attributes.runAction("addDrops",
                new Attributes.RunActionParams(attributes)
                        .drops(drops)
                        .world((World) world)
                        .pos(pos)
                        .state(state)
                        .fortune(fortune));
        super.getDrops(drops, world, pos, state, fortune);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        return super.rotateBlock(world, pos, axis);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        return super.canBeConnectedTo(world, pos, facing);
    }

    private <T> T getOrDefault(T a, T b) {
        return a == null ? b : a;
    }

    public static class Attributes {
        // storing all properties for workaround in createBlockState();
        private static final Hashtable<String, Hashtable<String, IProperty<?>>> properties = new Hashtable<>();
        private static String lastAddedName;
        private final Hashtable<String, Runnable> actions = new Hashtable<>();
        private final Hashtable<String, Boolean> bAttr = new Hashtable<>();
        private final Hashtable<String, Integer> iAttr = new Hashtable<>();
        private final Hashtable<String, String> sAttr = new Hashtable<>();
        public World world;
        public IBlockState state;
        public BlockPos pos;
        public Random rand;
        public EntityPlayer player;
        public Entity entity;
        public TileEntity tile;
        public ItemStack stack;
        public EnumHand hand;
        public EnumFacing facing;
        public Float hitX;
        public Float hitY;
        public Float hitZ;
        public NonNullList<ItemStack> drops;
        public Integer fortune;
        public ItemStack silkTouchDrop;
        private BlockRenderLayer blockRenderLayer;
        private Item droppedItem;

        public Object set(String key, Object value) {
            if (value instanceof Boolean) return bAttr.put(key, (Boolean) value);
            else if (value instanceof Integer) return iAttr.put(key, (Integer) value);
            else if (value instanceof String) return sAttr.put(key, (String) value);
            return null;
        }

        public void setRunnable(String name, Runnable r1) {
            actions.put(name, r1);
        }

        public void runAction(String name, RunActionParams params) {
            if (params.attributes == null) logger.warn(String.format("%s action has no set parameters", name));
            Runnable action = actions.get(name);
            if (action != null) {
                try {
                    action.run();
                } catch (NullPointerException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

        private static class RunActionParams {
            private final Attributes attributes;

            public RunActionParams(Attributes attributes) {
                this.attributes = attributes;
            }

            public RunActionParams world(World world) {
                this.attributes.world = world;
                return this;
            }

            public RunActionParams state(IBlockState state) {
                this.attributes.state = state;
                return this;
            }

            public RunActionParams pos(BlockPos pos) {
                this.attributes.pos = pos;
                return this;
            }

            public RunActionParams rand(Random rand) {
                this.attributes.rand = rand;
                return this;
            }

            public RunActionParams player(EntityPlayer player) {
                this.attributes.player = player;
                return this;
            }

            public RunActionParams entity(Entity entity) {
                this.attributes.entity = entity;
                return this;
            }

            public RunActionParams hand(EnumHand hand) {
                this.attributes.hand = hand;
                return this;
            }

            public RunActionParams facing(EnumFacing facing) {
                this.attributes.facing = facing;
                return this;
            }

            public RunActionParams tile(TileEntity tile) {
                this.attributes.tile = tile;
                return this;
            }

            public RunActionParams stack(ItemStack stack) {
                this.attributes.stack = stack;
                return this;
            }

            public RunActionParams hitCoords(Float hitX, Float hitY, Float hitZ) {
                this.attributes.hitX = hitX;
                this.attributes.hitY = hitY;
                this.attributes.hitZ = hitZ;
                return this;
            }

            public RunActionParams drops(NonNullList<ItemStack> drops) {
                this.attributes.drops = drops;
                return this;
            }

            public RunActionParams fortune(Integer fortune) {
                this.attributes.fortune = fortune;
                return this;
            }
        }
    }

}
