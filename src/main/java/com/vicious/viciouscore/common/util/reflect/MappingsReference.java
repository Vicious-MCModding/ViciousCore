package com.vicious.viciouscore.common.util.reflect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to reference mappings in runtime outside of the dev env.
 */
public class MappingsReference {
    private static Map<Class<?>, Mapping> mappings = new HashMap<>();

    public static void addMapping(String deobf, Class<?> location, String obf) {
        if (mappings.containsKey(location)) {
            mappings.get(location).register(deobf, obf);
        } else {
            mappings.put(location, new Mapping().register(deobf, obf));
        }
    }

    public static Mapping getMapping(Class<?> clazz) {
        return mappings.get(clazz);
    }
    public static String toMCP(Class<?> clazz, String srg){
        String s = null;
        while(clazz != null && s == null){
            Mapping m = mappings.get(clazz);
            if(m != null) s = m.getDeobfuscated(srg);
            clazz = clazz.getSuperclass();
        }
        return s;
    }

    public static boolean hasMappingForClass(Class<?> clazz) {
        return mappings.containsKey(clazz);
    }

    public static class Mapping {
        private Map<String, String> obfdeobf = new HashMap<>();
        private Map<String, String> deobfobf = new HashMap<>();

        private Mapping() {
        }

        private Mapping register(String deobf, String obf) {
            obfdeobf.putIfAbsent(obf, deobf);
            deobfobf.putIfAbsent(deobf, obf);
            return this;
        }

        public String getObfuscated(String deobf) {
            return deobfobf.get(deobf);
        }

        public String getDeobfuscated(String obf) {
            return obfdeobf.get(obf);
        }

        public boolean hasSRG(String deobf) {
            return deobfobf.containsKey(deobf);
        }
        public boolean hasMCP(String srg) {
            return obfdeobf.containsKey(srg);
        }
    }

    static {
        init();

    }

    /**
     * Register MCP to SRG mappings for any class in MC.common that VCore reflects on.
     */
    private static void init(){
        addMapping("getMaxHealth", EntityLivingBase.class, "func_110138_aP");
        addMapping("getAbsorptionAmount", EntityLivingBase.class, "func_110139_bj");
        addMapping("getAttributeMap", EntityLivingBase.class, "func_110140_aT");
        addMapping("getCombatTracker", EntityLivingBase.class, "func_110142_aN");
        addMapping("getHealth", EntityLivingBase.class, "func_110143_aJ");
        addMapping("getLastAttackedEntity", EntityLivingBase.class, "func_110144_aD");
        addMapping("dismountEntity", EntityLivingBase.class, "func_110145_l");
        addMapping("updateDistance", EntityLivingBase.class, "func_110146_f");
        addMapping("applyEntityAttributes", EntityLivingBase.class, "func_110147_ax");
        addMapping("getEntityAttribute", EntityLivingBase.class, "func_110148_a");
        addMapping("setAbsorptionAmount", EntityLivingBase.class, "func_110149_m");
        addMapping("setLastAttackedEntity", EntityLivingBase.class, "func_130011_c");
        addMapping("getLastAttackedEntityTime", EntityLivingBase.class, "func_142013_aG");
        addMapping("getRevengeTimer", EntityLivingBase.class, "func_142015_aE");
        addMapping("canDropLoot", EntityLivingBase.class, "func_146066_aG");
        addMapping("sendEnterCombat", EntityLivingBase.class, "func_152111_bt");
        addMapping("sendEndCombat", EntityLivingBase.class, "func_152112_bu");
        addMapping("onKillCommand", EntityLivingBase.class, "func_174812_G");
        addMapping("resetPotionEffectMetadata", EntityLivingBase.class, "func_175133_bi");
        addMapping("getJumpUpwardsMotion", EntityLivingBase.class, "func_175134_bD");
        addMapping("updatePotionMetadata", EntityLivingBase.class, "func_175135_B");
        addMapping("markPotionsDirty", EntityLivingBase.class, "func_175136_bO");
        addMapping("fall", EntityLivingBase.class, "func_180430_e");
        addMapping("handleJumpLava", EntityLivingBase.class, "func_180466_bG");
        addMapping("setRenderYawOffset", EntityLivingBase.class, "func_181013_g");
        addMapping("getArmorInventoryList", EntityLivingBase.class, "func_184193_aE");
        addMapping("setItemStackToSlot", EntityLivingBase.class, "func_184201_a");
        addMapping("notifyDataManagerChange", EntityLivingBase.class, "func_184206_a");
        addMapping("dismountRidingEntity", EntityLivingBase.class, "func_184210_p");
        addMapping("updateFallState", EntityLivingBase.class, "func_184231_a");
        addMapping("playHurtSound", EntityLivingBase.class, "func_184581_c");
        addMapping("getItemStackFromSlot", EntityLivingBase.class, "func_184582_a");
        addMapping("canBlockDamageSource", EntityLivingBase.class, "func_184583_d");
        addMapping("updateItemUse", EntityLivingBase.class, "func_184584_a");
        addMapping("isActiveItemStackBlocking", EntityLivingBase.class, "func_184585_cz");
        addMapping("getHeldItem", EntityLivingBase.class, "func_184586_b");
        addMapping("isHandActive", EntityLivingBase.class, "func_184587_cr");
        addMapping("getFallSound", EntityLivingBase.class, "func_184588_d");
        addMapping("removePotionEffect", EntityLivingBase.class, "func_184589_d");
        addMapping("damageShield", EntityLivingBase.class, "func_184590_k");
        addMapping("getPrimaryHand", EntityLivingBase.class, "func_184591_cq");
        addMapping("getHeldItemOffhand", EntityLivingBase.class, "func_184592_cb");
        addMapping("areAllPotionsAmbient", EntityLivingBase.class, "func_184593_a");
        addMapping("frostWalk", EntityLivingBase.class, "func_184594_b");
        addMapping("attemptTeleport", EntityLivingBase.class, "func_184595_k");
        addMapping("removeActivePotionEffect", EntityLivingBase.class, "func_184596_c");
        addMapping("stopActiveHand", EntityLivingBase.class, "func_184597_cx");
        addMapping("setActiveHand", EntityLivingBase.class, "func_184598_c");
        addMapping("getActiveHand", EntityLivingBase.class, "func_184600_cs");
        addMapping("getHurtSound", EntityLivingBase.class, "func_184601_bQ");
        addMapping("resetActiveHand", EntityLivingBase.class, "func_184602_cy");
        addMapping("canBeHitWithPotion", EntityLivingBase.class, "func_184603_cC");
        addMapping("canGoThroughtTrapDoorOnLadder", EntityLivingBase.class, "func_184604_a");
        addMapping("getItemInUseCount", EntityLivingBase.class, "func_184605_cv");
        addMapping("playEquipSound", EntityLivingBase.class, "func_184606_a_");
        addMapping("getActiveItemStack", EntityLivingBase.class, "func_184607_cu");
        addMapping("updateActiveHand", EntityLivingBase.class, "func_184608_ct");
        addMapping("swingArm", EntityLivingBase.class, "func_184609_a");
        addMapping("dropLoot", EntityLivingBase.class, "func_184610_a");
        addMapping("setHeldItem", EntityLivingBase.class, "func_184611_a");
        addMapping("getItemInUseMaxCount", EntityLivingBase.class, "func_184612_cw");
        addMapping("isElytraFlying", EntityLivingBase.class, "func_184613_cA");
        addMapping("getHeldItemMainhand", EntityLivingBase.class, "func_184614_ca");
        addMapping("getDeathSound", EntityLivingBase.class, "func_184615_bR");
        addMapping("updateElytra", EntityLivingBase.class, "func_184616_r");
        addMapping("getLastDamageSource", EntityLivingBase.class, "func_189748_bU");
        addMapping("getWaterSlowDown", EntityLivingBase.class, "func_189749_co");
        addMapping("checkTotemDeathProtection", EntityLivingBase.class, "func_190628_d");
        addMapping("blockUsingShield", EntityLivingBase.class, "func_190629_c");
        addMapping("hasItemInSlot", EntityLivingBase.class, "func_190630_a");
        addMapping("attackable", EntityLivingBase.class, "func_190631_cK");
        addMapping("travel", EntityLivingBase.class, "func_191986_a");
        addMapping("getActivePotionMap", EntityLivingBase.class, "func_193076_bZ");
        addMapping("writeEntityToNBT", EntityLivingBase.class, "func_70014_b");
        addMapping("markVelocityChanged", EntityLivingBase.class, "func_70018_K");
        addMapping("onEntityUpdate", EntityLivingBase.class, "func_70030_z");
        addMapping("setSprinting", EntityLivingBase.class, "func_70031_b");
        addMapping("setRotationYawHead", EntityLivingBase.class, "func_70034_d");
        addMapping("readEntityFromNBT", EntityLivingBase.class, "func_70037_a");
        addMapping("canBeCollidedWith", EntityLivingBase.class, "func_70067_L");
        addMapping("onUpdate", EntityLivingBase.class, "func_70071_h_");
        addMapping("outOfWorld", EntityLivingBase.class, "func_70076_C");
        addMapping("getRotationYawHead", EntityLivingBase.class, "func_70079_am");
        addMapping("entityInit", EntityLivingBase.class, "func_70088_a");
        addMapping("isEntityAlive", EntityLivingBase.class, "func_70089_S");
        addMapping("attackEntityFrom", EntityLivingBase.class, "func_70097_a");
        addMapping("updateRidden", EntityLivingBase.class, "func_70098_U");
        addMapping("canBePushed", EntityLivingBase.class, "func_70104_M");
        addMapping("getSoundVolume", EntityLivingBase.class, "func_70599_aP");
        addMapping("setRevengeTarget", EntityLivingBase.class, "func_70604_c");
        addMapping("setHealth", EntityLivingBase.class, "func_70606_j");
        addMapping("isPlayerSleeping", EntityLivingBase.class, "func_70608_bn");
        addMapping("onDeathUpdate", EntityLivingBase.class, "func_70609_aI");
        addMapping("isMovementBlocked", EntityLivingBase.class, "func_70610_aX");
        addMapping("isServerWorld", EntityLivingBase.class, "func_70613_aW");
        addMapping("isOnLadder", EntityLivingBase.class, "func_70617_f_");
        addMapping("updateEntityActionState", EntityLivingBase.class, "func_70626_be");
        addMapping("dropFewItems", EntityLivingBase.class, "func_70628_a");
        addMapping("handleJumpWater", EntityLivingBase.class, "func_70629_bd");
        addMapping("isChild", EntityLivingBase.class, "func_70631_g_");
        addMapping("onLivingUpdate", EntityLivingBase.class, "func_70636_d");
        addMapping("setJumping", EntityLivingBase.class, "func_70637_d");
        addMapping("getRevengeTarget", EntityLivingBase.class, "func_70643_av");
        addMapping("isPotionActive", EntityLivingBase.class, "func_70644_a");
        addMapping("onDeath", EntityLivingBase.class, "func_70645_a");
        addMapping("getSoundPitch", EntityLivingBase.class, "func_70647_i");
        addMapping("canBreatheUnderwater", EntityLivingBase.class, "func_70648_aU");
        addMapping("getActivePotionEffects", EntityLivingBase.class, "func_70651_bq");
        addMapping("attackEntityAsMob", EntityLivingBase.class, "func_70652_k");
        addMapping("knockBack", EntityLivingBase.class, "func_70653_a");
        addMapping("getIdleTime", EntityLivingBase.class, "func_70654_ax");
        addMapping("applyArmorCalculations", EntityLivingBase.class, "func_70655_b");
        addMapping("getTotalArmorValue", EntityLivingBase.class, "func_70658_aO");
        addMapping("setAIMoveSpeed", EntityLivingBase.class, "func_70659_e");
        addMapping("getActivePotionEffect", EntityLivingBase.class, "func_70660_b");
        addMapping("isEntityUndead", EntityLivingBase.class, "func_70662_br");
        addMapping("jump", EntityLivingBase.class, "func_70664_aZ");
        addMapping("damageEntity", EntityLivingBase.class, "func_70665_d");
        addMapping("getCreatureAttribute", EntityLivingBase.class, "func_70668_bt");
        addMapping("renderBrokenItemStack", EntityLivingBase.class, "func_70669_a");
        addMapping("onNewPotionEffect", EntityLivingBase.class, "func_70670_a");
        addMapping("applyPotionDamageCalculations", EntityLivingBase.class, "func_70672_c");
        addMapping("clearActivePotions", EntityLivingBase.class, "func_70674_bp");
        addMapping("damageArmor", EntityLivingBase.class, "func_70675_k");
        addMapping("getLook", EntityLivingBase.class, "func_70676_i");
        addMapping("updatePotionEffects", EntityLivingBase.class, "func_70679_bo");
        addMapping("getRNG", EntityLivingBase.class, "func_70681_au");
        addMapping("decreaseAirSupply", EntityLivingBase.class, "func_70682_h");
        addMapping("isPlayer", EntityLivingBase.class, "func_70684_aJ");
        addMapping("canEntityBeSeen", EntityLivingBase.class, "func_70685_l");
        addMapping("isPotionApplicable", EntityLivingBase.class, "func_70687_e");
        addMapping("onFinishedPotionEffect", EntityLivingBase.class, "func_70688_c");
        addMapping("getAIMoveSpeed", EntityLivingBase.class, "func_70689_ay");
        addMapping("addPotionEffect", EntityLivingBase.class, "func_70690_d");
        addMapping("heal", EntityLivingBase.class, "func_70691_i");
        addMapping("getExperiencePoints", EntityLivingBase.class, "func_70693_a");
        addMapping("onChangedPotionEffect", EntityLivingBase.class, "func_70695_b");
        addMapping("onItemPickup", EntityLivingBase.class, "func_71001_a");
        addMapping("onItemUseFinish", EntityLivingBase.class, "func_71036_o");
        addMapping("dropEquipment", EntityLivingBase.class, "func_82160_b");
        addMapping("getArmSwingAnimationEnd", EntityLivingBase.class, "func_82166_i");
        addMapping("collideWithEntity", EntityLivingBase.class, "func_82167_n");
        addMapping("updateArmSwingProgress", EntityLivingBase.class, "func_82168_bl");
        addMapping("collideWithNearbyEntities", EntityLivingBase.class, "func_85033_bc");
        addMapping("setArrowCountInEntity", EntityLivingBase.class, "func_85034_r");
        addMapping("getArrowCountInEntity", EntityLivingBase.class, "func_85035_bI");
        addMapping("getAttackingEntity", EntityLivingBase.class, "func_94060_bK");
        addMapping("lastAttackedEntity", EntityLivingBase.class, "field_110150_bn");
        addMapping("absorptionAmount", EntityLivingBase.class, "field_110151_bq");
        addMapping("lastDamage", EntityLivingBase.class, "field_110153_bc");
        addMapping("onGroundSpeedFactor", EntityLivingBase.class, "field_110154_aX");
        addMapping("attributeMap", EntityLivingBase.class, "field_110155_d");
        addMapping("SPRINTING_SPEED_BOOST_ID", EntityLivingBase.class, "field_110156_b");
        addMapping("SPRINTING_SPEED_BOOST", EntityLivingBase.class, "field_110157_c");
        addMapping("swingProgressInt", EntityLivingBase.class, "field_110158_av");
        addMapping("lastAttackedEntityTime", EntityLivingBase.class, "field_142016_bo");
        addMapping("ticksSinceLastSwing", EntityLivingBase.class, "field_184617_aD");
        addMapping("prevLimbSwingAmount", EntityLivingBase.class, "field_184618_aE");
        addMapping("limbSwing", EntityLivingBase.class, "field_184619_aG");
        addMapping("prevBlockpos", EntityLivingBase.class, "field_184620_bC");
        addMapping("HAND_STATES", EntityLivingBase.class, "field_184621_as");
        addMapping("swingingHand", EntityLivingBase.class, "field_184622_au");
        addMapping("interpTargetX", EntityLivingBase.class, "field_184623_bh");
        addMapping("interpTargetY", EntityLivingBase.class, "field_184624_bi");
        addMapping("interpTargetZ", EntityLivingBase.class, "field_184625_bj");
        addMapping("interpTargetYaw", EntityLivingBase.class, "field_184626_bk");
        addMapping("activeItemStack", EntityLivingBase.class, "field_184627_bm");
        addMapping("activeItemStackUseCount", EntityLivingBase.class, "field_184628_bn");
        addMapping("ticksElytraFlying", EntityLivingBase.class, "field_184629_bo");
        addMapping("handInventory", EntityLivingBase.class, "field_184630_bs");
        addMapping("armorArray", EntityLivingBase.class, "field_184631_bt");
        addMapping("HEALTH", EntityLivingBase.class, "field_184632_c");
        addMapping("POTION_EFFECTS", EntityLivingBase.class, "field_184633_f");
        addMapping("HIDE_PARTICLES", EntityLivingBase.class, "field_184634_g");
        addMapping("ARROW_COUNT_IN_ENTITY", EntityLivingBase.class, "field_184635_h");
        addMapping("lastDamageSource", EntityLivingBase.class, "field_189750_bF");
        addMapping("lastDamageStamp", EntityLivingBase.class, "field_189751_bG");
        addMapping("LOGGER", EntityLivingBase.class, "field_190632_a");
        addMapping("moveForward", EntityLivingBase.class, "field_191988_bg");
        addMapping("moveVertical", EntityLivingBase.class, "field_70701_bs");
        addMapping("moveStrafing", EntityLivingBase.class, "field_70702_br");
        addMapping("isJumping", EntityLivingBase.class, "field_70703_bu");
        addMapping("randomYawVelocity", EntityLivingBase.class, "field_70704_bt");
        addMapping("idleTime", EntityLivingBase.class, "field_70708_bq");
        addMapping("interpTargetPitch", EntityLivingBase.class, "field_70709_bj");
        addMapping("activePotionsMap", EntityLivingBase.class, "field_70713_bf");
        addMapping("newPosRotationIncrements", EntityLivingBase.class, "field_70716_bi");
        addMapping("attackingPlayer", EntityLivingBase.class, "field_70717_bb");
        addMapping("recentlyHit", EntityLivingBase.class, "field_70718_bc");
        addMapping("arrowHitTimer", EntityLivingBase.class, "field_70720_be");
        addMapping("limbSwingAmount", EntityLivingBase.class, "field_70721_aZ");
        addMapping("deathTime", EntityLivingBase.class, "field_70725_aQ");
        addMapping("cameraPitch", EntityLivingBase.class, "field_70726_aT");
        addMapping("prevCameraPitch", EntityLivingBase.class, "field_70727_aS");
        addMapping("dead", EntityLivingBase.class, "field_70729_aU");
        addMapping("prevSwingProgress", EntityLivingBase.class, "field_70732_aI");
        addMapping("swingProgress", EntityLivingBase.class, "field_70733_aJ");
        addMapping("hurtTime", EntityLivingBase.class, "field_70737_aN");
        addMapping("maxHurtTime", EntityLivingBase.class, "field_70738_aO");
        addMapping("attackedAtYaw", EntityLivingBase.class, "field_70739_aP");
        addMapping("unused180", EntityLivingBase.class, "field_70741_aB");
        addMapping("scoreValue", EntityLivingBase.class, "field_70744_aE");
        addMapping("landMovementFactor", EntityLivingBase.class, "field_70746_aG");
        addMapping("jumpMovementFactor", EntityLivingBase.class, "field_70747_aH");
        addMapping("potionsNeedUpdate", EntityLivingBase.class, "field_70752_e");
        addMapping("revengeTarget", EntityLivingBase.class, "field_70755_b");
        addMapping("revengeTimer", EntityLivingBase.class, "field_70756_c");
        addMapping("prevRotationYawHead", EntityLivingBase.class, "field_70758_at");
        addMapping("rotationYawHead", EntityLivingBase.class, "field_70759_as");
        addMapping("prevRenderYawOffset", EntityLivingBase.class, "field_70760_ar");
        addMapping("renderYawOffset", EntityLivingBase.class, "field_70761_aq");
        addMapping("prevMovedDistance", EntityLivingBase.class, "field_70763_ax");
        addMapping("movedDistance", EntityLivingBase.class, "field_70764_aw");
        addMapping("prevOnGroundSpeedFactor", EntityLivingBase.class, "field_70768_au");
        addMapping("randomUnused2", EntityLivingBase.class, "field_70769_ao");
        addMapping("randomUnused1", EntityLivingBase.class, "field_70770_ap");
        addMapping("maxHurtResistantTime", EntityLivingBase.class, "field_70771_an");
        addMapping("jumpTicks", EntityLivingBase.class, "field_70773_bE");
        addMapping("isSwingInProgress", EntityLivingBase.class, "field_82175_bq");
        addMapping("_combatTracker", EntityLivingBase.class, "field_94063_bt");
        addMapping("getCurrentMoonPhaseFactor", World.class, "func_130001_d");
        addMapping("rayTraceBlocks", World.class, "func_147447_a");
        addMapping("addTileEntities", World.class, "func_147448_a");
        addMapping("updateBlocks", World.class, "func_147456_g");
        addMapping("markTileEntityForRemoval", World.class, "func_147457_a");
        addMapping("markBlockRangeForRenderUpdate", World.class, "func_147458_c");
        addMapping("isFlammableWithin", World.class, "func_147470_e");
        addMapping("getPlayerEntityByUUID", World.class, "func_152378_a");
        addMapping("isAirBlock", World.class, "func_175623_d");
        addMapping("getWorldType", World.class, "func_175624_G");
        addMapping("getTileEntity", World.class, "func_175625_s");
        addMapping("getStrongPower", World.class, "func_175627_a");
        addMapping("isAnyPlayerWithinRangeAt", World.class, "func_175636_b");
        addMapping("getRawLight", World.class, "func_175638_a");
        addMapping("isAreaLoaded", World.class, "func_175639_b");
        addMapping("isBlockPowered", World.class, "func_175640_z");
        addMapping("addBlockEvent", World.class, "func_175641_c");
        addMapping("getLightFor", World.class, "func_175642_b");
        addMapping("init", World.class, "func_175643_b");
        addMapping("getEntities", World.class, "func_175644_a");
        addMapping("getHeight", World.class, "func_175645_m");
        addMapping("markChunkDirty", World.class, "func_175646_b");
        addMapping("getEntitiesWithinAABB", World.class, "func_175647_a");
        addMapping("isAreaLoaded", World.class, "func_175648_a");
        addMapping("getDifficultyForLocation", World.class, "func_175649_E");
        addMapping("loadEntities", World.class, "func_175650_b");
        addMapping("getRedstonePower", World.class, "func_175651_c");
        addMapping("setSpawnPoint", World.class, "func_175652_B");
        addMapping("setLightFor", World.class, "func_175653_a");
        addMapping("updateBlockTick", World.class, "func_175654_a");
        addMapping("destroyBlock", World.class, "func_175655_b");
        addMapping("setBlockState", World.class, "func_175656_a");
        addMapping("getSkylightSubtracted", World.class, "func_175657_ab");
        addMapping("getDifficulty", World.class, "func_175659_aa");
        addMapping("isBlockModifiable", World.class, "func_175660_a");
        addMapping("getPlayers", World.class, "func_175661_b");
        addMapping("canBlockFreezeNoWater", World.class, "func_175662_w");
        addMapping("isAreaLoaded", World.class, "func_175663_a");
        addMapping("checkLight", World.class, "func_175664_x");
        addMapping("isBlockFullCube", World.class, "func_175665_u");
        addMapping("updateComparatorOutputLevel", World.class, "func_175666_e");
        addMapping("isBlockLoaded", World.class, "func_175667_e");
        addMapping("isBlockLoaded", World.class, "func_175668_a");
        addMapping("playBroadcastSound", World.class, "func_175669_a");
        addMapping("canBlockFreeze", World.class, "func_175670_e");
        addMapping("getLightFromNeighbors", World.class, "func_175671_l");
        addMapping("getTopSolidOrLiquidBlock", World.class, "func_175672_r");
        addMapping("getEntitiesInAABBexcluding", World.class, "func_175674_a");
        addMapping("canBlockFreezeWater", World.class, "func_175675_v");
        addMapping("getStrongPower", World.class, "func_175676_y");
        addMapping("isBlockNormalCube", World.class, "func_175677_d");
        addMapping("canSeeSky", World.class, "func_175678_i");
        addMapping("notifyLightSet", World.class, "func_175679_n");
        addMapping("isChunkLoaded", World.class, "func_175680_a");
        addMapping("unloadEntities", World.class, "func_175681_c");
        addMapping("scheduleUpdate", World.class, "func_175684_a");
        addMapping("notifyNeighborsOfStateChange", World.class, "func_175685_c");
        addMapping("isBlockIndirectlyGettingPowered", World.class, "func_175687_A");
        addMapping("spawnParticle", World.class, "func_175688_a");
        addMapping("setTileEntity", World.class, "func_175690_a");
        addMapping("isBlockTickPending", World.class, "func_175691_a");
        addMapping("setSkylightSubtracted", World.class, "func_175692_b");
        addMapping("getMapStorage", World.class, "func_175693_T");
        addMapping("getSpawnPoint", World.class, "func_175694_M");
        addMapping("notifyNeighborsOfStateExcept", World.class, "func_175695_a");
        addMapping("isWater", World.class, "func_175696_F");
        addMapping("isAreaLoaded", World.class, "func_175697_a");
        addMapping("setBlockToAir", World.class, "func_175698_g");
        addMapping("getLight", World.class, "func_175699_k");
        addMapping("addTileEntity", World.class, "func_175700_a");
        addMapping("isValid", World.class, "func_175701_a");
        addMapping("setLastLightningBolt", World.class, "func_175702_c");
        addMapping("markBlockRangeForRenderUpdate", World.class, "func_175704_b");
        addMapping("isAreaLoaded", World.class, "func_175706_a");
        addMapping("isAreaLoaded", World.class, "func_175707_a");
        addMapping("canSnowAt", World.class, "func_175708_f");
        addMapping("isSidePowered", World.class, "func_175709_b");
        addMapping("canBlockSeeSky", World.class, "func_175710_j");
        addMapping("isAreaLoaded", World.class, "func_175711_a");
        addMapping("getPendingBlockUpdates", World.class, "func_175712_a");
        addMapping("removeTileEntity", World.class, "func_175713_t");
        addMapping("getVillageCollection", World.class, "func_175714_ae");
        addMapping("sendBlockBreakProgress", World.class, "func_175715_c");
        addMapping("playEvent", World.class, "func_175718_b");
        addMapping("extinguishFire", World.class, "func_175719_a");
        addMapping("spawnParticle", World.class, "func_175720_a");
        addMapping("getLight", World.class, "func_175721_c");
        addMapping("notifyNeighborsRespectDebug", World.class, "func_175722_b");
        addMapping("getWorldBorder", World.class, "func_175723_af");
        addMapping("getLightBrightness", World.class, "func_175724_o");
        addMapping("getPrecipitationHeight", World.class, "func_175725_q");
        addMapping("getChunkFromBlockCoords", World.class, "func_175726_f");
        addMapping("isRainingAt", World.class, "func_175727_C");
        addMapping("getBiome", World.class, "func_180494_b");
        addMapping("getBlockState", World.class, "func_180495_p");
        addMapping("scheduleBlockUpdate", World.class, "func_180497_b");
        addMapping("playEvent", World.class, "func_180498_a");
        addMapping("checkLightFor", World.class, "func_180500_c");
        addMapping("setBlockState", World.class, "func_180501_a");
        addMapping("isBlockinHighHumidity", World.class, "func_180502_D");
        addMapping("setSeaLevel", World.class, "func_181544_b");
        addMapping("getSeaLevel", World.class, "func_181545_F");
        addMapping("playSound", World.class, "func_184133_a");
        addMapping("playSound", World.class, "func_184134_a");
        addMapping("sendPacketToServer", World.class, "func_184135_a");
        addMapping("getNearestPlayerNotCreative", World.class, "func_184136_b");
        addMapping("getClosestPlayer", World.class, "func_184137_a");
        addMapping("notifyBlockUpdate", World.class, "func_184138_a");
        addMapping("getNearestAttackablePlayer", World.class, "func_184139_a");
        addMapping("getGroundAboveSeaLevel", World.class, "func_184141_c");
        addMapping("getNearestAttackablePlayer", World.class, "func_184142_a");
        addMapping("collidesWithAnyBlock", World.class, "func_184143_b");
        addMapping("getCollisionBoxes", World.class, "func_184144_a");
        addMapping("isUpdateScheduled", World.class, "func_184145_b");
        addMapping("getLootTableManager", World.class, "func_184146_ak");
        addMapping("tickPlayers", World.class, "func_184147_l");
        addMapping("playSound", World.class, "func_184148_a");
        addMapping("playRecord", World.class, "func_184149_a");
        addMapping("getNearestAttackablePlayer", World.class, "func_184150_a");
        addMapping("immediateBlockTick", World.class, "func_189507_a");
        addMapping("getPendingTileEntityAt", World.class, "func_189508_F");
        addMapping("isOutsideBuildHeight", World.class, "func_189509_E");
        addMapping("getHeight", World.class, "func_189649_b");
        addMapping("updateObservingBlocksAt", World.class, "func_190522_c");
        addMapping("spawnAlwaysVisibleParticle", World.class, "func_190523_a");
        addMapping("neighborChanged", World.class, "func_190524_a");
        addMapping("getClosestPlayer", World.class, "func_190525_a");
        addMapping("isChunkGeneratedAt", World.class, "func_190526_b");
        addMapping("mayPlace", World.class, "func_190527_a");
        addMapping("findNearestStructure", World.class, "func_190528_a");
        addMapping("observedNeighborChanged", World.class, "func_190529_b");
        addMapping("isInsideWorldBorder", World.class, "func_191503_g");
        addMapping("getCollisionBoxes", World.class, "func_191504_a");
        addMapping("getHeight", World.class, "func_72800_K");
        addMapping("getThunderStrength", World.class, "func_72819_i");
        addMapping("getWorldTime", World.class, "func_72820_D");
        addMapping("setData", World.class, "func_72823_a");
        addMapping("getCelestialAngle", World.class, "func_72826_c");
        addMapping("checkBlockCollision", World.class, "func_72829_c");
        addMapping("tick", World.class, "func_72835_b");
        addMapping("spawnEntity", World.class, "func_72838_d");
        addMapping("getEntitiesWithinAABBExcludingEntity", World.class, "func_72839_b");
        addMapping("getUniqueDataId", World.class, "func_72841_b");
        addMapping("getBlockDensity", World.class, "func_72842_a");
        addMapping("setRandomSeed", World.class, "func_72843_D");
        addMapping("onEntityRemoved", World.class, "func_72847_b");
        addMapping("updateAllPlayersSleepingFlag", World.class, "func_72854_c");
        addMapping("checkNoEntityCollision", World.class, "func_72855_b");
        addMapping("findNearestEntityWithinAABB", World.class, "func_72857_a");
        addMapping("getSaveHandler", World.class, "func_72860_G");
        addMapping("getChunkProvider", World.class, "func_72863_F");
        addMapping("updateEntityWithOptionalForce", World.class, "func_72866_a");
        addMapping("getRainStrength", World.class, "func_72867_j");
        addMapping("updateEntity", World.class, "func_72870_g");
        addMapping("getEntitiesWithinAABB", World.class, "func_72872_a");
        addMapping("isMaterialInBB", World.class, "func_72875_a");
        addMapping("createExplosion", World.class, "func_72876_a");
        addMapping("setWorldTime", World.class, "func_72877_b");
        addMapping("newExplosion", World.class, "func_72885_a");
        addMapping("getClosestPlayerToEntity", World.class, "func_72890_a");
        addMapping("setAllowedSpawnTypes", World.class, "func_72891_a");
        addMapping("isRaining", World.class, "func_72896_J");
        addMapping("removeEntity", World.class, "func_72900_e");
        addMapping("rayTraceBlocks", World.class, "func_72901_a");
        addMapping("getSeed", World.class, "func_72905_C");
        addMapping("checkSessionLock", World.class, "func_72906_B");
        addMapping("countEntities", World.class, "func_72907_a");
        addMapping("isThundering", World.class, "func_72911_I");
        addMapping("getWorldInfo", World.class, "func_72912_H");
        addMapping("addWorldInfoToCrashReport", World.class, "func_72914_a");
        addMapping("isSpawnChunk", World.class, "func_72916_c");
        addMapping("checkNoEntityCollision", World.class, "func_72917_a");
        addMapping("handleMaterialAcceleration", World.class, "func_72918_a");
        addMapping("getPendingBlockUpdates", World.class, "func_72920_a");
        addMapping("onEntityAdded", World.class, "func_72923_a");
        addMapping("getPlayerEntityByName", World.class, "func_72924_a");
        addMapping("getCelestialAngleRadians", World.class, "func_72929_e");
        addMapping("rayTraceBlocks", World.class, "func_72933_a");
        addMapping("isDaytime", World.class, "func_72935_r");
        addMapping("updateEntities", World.class, "func_72939_s");
        addMapping("getActualHeight", World.class, "func_72940_L");
        addMapping("addWeatherEffect", World.class, "func_72942_c");
        addMapping("loadData", World.class, "func_72943_a");
        addMapping("calculateInitialWeather", World.class, "func_72947_a");
        addMapping("containsAnyLiquid", World.class, "func_72953_d");
        addMapping("addEventListener", World.class, "func_72954_a");
        addMapping("tickUpdates", World.class, "func_72955_a");
        addMapping("getBiomeProvider", World.class, "func_72959_q");
        addMapping("setEntityState", World.class, "func_72960_a");
        addMapping("initialize", World.class, "func_72963_a");
        addMapping("getChunkFromChunkCoords", World.class, "func_72964_e");
        addMapping("calculateInitialSkylight", World.class, "func_72966_v");
        addMapping("calculateSkylightSubtracted", World.class, "func_72967_a");
        addMapping("createChunkProvider", World.class, "func_72970_h");
        addMapping("removeEntityDangerously", World.class, "func_72973_f");
        addMapping("markBlocksDirtyVertical", World.class, "func_72975_g");
        addMapping("updateWeather", World.class, "func_72979_l");
        addMapping("getEntityByID", World.class, "func_73045_a");
        addMapping("getMinecraftServer", World.class, "func_73046_m");
        addMapping("getChunksLowestHorizon", World.class, "func_82734_g");
        addMapping("getGameRules", World.class, "func_82736_K");
        addMapping("getTotalWorldTime", World.class, "func_82737_E");
        addMapping("getCurrentDate", World.class, "func_83015_S");
        addMapping("getScoreboard", World.class, "func_96441_U");
        addMapping("processingLoadedTiles", World.class, "field_147481_N");
        addMapping("loadedTileEntityList", World.class, "field_147482_g");
        addMapping("tileEntitiesToBeRemoved", World.class, "field_147483_b");
        addMapping("addedTileEntityList", World.class, "field_147484_a");
        addMapping("worldBorder", World.class, "field_175728_M");
        addMapping("entitiesById", World.class, "field_175729_l");
        addMapping("tickableTileEntities", World.class, "field_175730_i");
        addMapping("seaLevel", World.class, "field_181546_a");
        addMapping("lootTable", World.class, "field_184151_B");
        addMapping("pathListener", World.class, "field_184152_t");
        addMapping("advancementManager", World.class, "field_191951_C");
        addMapping("functionManager", World.class, "field_193036_D");
        addMapping("villageCollection", World.class, "field_72982_D");
        addMapping("profiler", World.class, "field_72984_F");
        addMapping("spawnHostileMobs", World.class, "field_72985_G");
        addMapping("worldInfo", World.class, "field_72986_A");
        addMapping("findingSpawnPoint", World.class, "field_72987_B");
        addMapping("mapStorage", World.class, "field_72988_C");
        addMapping("spawnPeacefulMobs", World.class, "field_72992_H");
        addMapping("lightUpdateBlockList", World.class, "field_72994_J");
        addMapping("isRemote", World.class, "field_72995_K");
        addMapping("loadedEntityList", World.class, "field_72996_f");
        addMapping("unloadedEntityList", World.class, "field_72997_g");
        addMapping("scheduledUpdatesAreImmediate", World.class, "field_72999_e");
        addMapping("cloudColour", World.class, "field_73001_c");
        addMapping("prevRainingStrength", World.class, "field_73003_n");
        addMapping("rainingStrength", World.class, "field_73004_o");
        addMapping("updateLCG", World.class, "field_73005_l");
        addMapping("DIST_HASH_MAGIC", World.class, "field_73006_m");
        addMapping("weatherEffects", World.class, "field_73007_j");
        addMapping("skylightSubtracted", World.class, "field_73008_k");
        addMapping("playerEntities", World.class, "field_73010_i");
        addMapping("provider", World.class, "field_73011_w");
        addMapping("rand", World.class, "field_73012_v");
        addMapping("lastLightningBolt", World.class, "field_73016_r");
        addMapping("thunderingStrength", World.class, "field_73017_q");
        addMapping("prevThunderingStrength", World.class, "field_73018_p");
        addMapping("saveHandler", World.class, "field_73019_z");
        addMapping("chunkProvider", World.class, "field_73020_y");
        addMapping("eventListeners", World.class, "field_73021_x");
        addMapping("calendar", World.class, "field_83016_L");
        addMapping("worldScoreboard", World.class, "field_96442_D");
    }
}