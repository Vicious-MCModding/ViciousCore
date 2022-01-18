package com.vicious.viciouscore.common.entity.projectile;

import com.vicious.viciouscore.common.entity.GenericEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class GenericProjectileEntity extends GenericEntity {
    protected static final DataParameter<Integer> TICKSEXISTED = EntityDataManager.createKey(GenericProjectileEntity.class, DataSerializers.VARINT);
    private final int expiry;
    public int ticksExisted = 0;
    private Object source;
    //The distance from this entity's hitbox where a collision detection will occur.

    public GenericProjectileEntity(World world) {
        this(world, 0);
    }

    /**
     * Creates a projectile entity with a set expiry time in ticks. Once the entity has existed for the expiry, it will be killed and destroyed.
     */
    public GenericProjectileEntity(World world, int expirationTicks) {
        super(world);
        this.expiry = expirationTicks;
    }

    public GenericProjectileEntity(World world, int expirationTicks, Entity source) {
        this(world, expirationTicks);
        this.source = source;
        Vec3d dir = this.getDirection(source.rotationPitch, source.getRotationYawHead());
        this.motionX = dir.x;
        this.motionY = dir.y;
        this.motionZ = dir.z;
        /*this.updateHeading();

        this.setSize(this.projectile.size, this.projectile.size);
        this.setPosition(shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ);

        ItemAmmo ammo = AmmoRegistry.getInstance().getAmmo(this.projectile.item);
        if(ammo != null)
        {
            this.item = new ItemStack(ammo);
        }*/
    }


    /**
     * Override this to execute code when expiration occurs.
     */
    protected void onExpire() {
        //TODO:enable
        //if(!isDead) {
        // setDead();
        // }
    }

    @Override
    protected void entityInit() {
        dataManager.register(TICKSEXISTED, ticksExisted);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        ticksExisted++;
        if (ticksExisted >= expiry) onExpire();
    }

    @Override
    public boolean canBeCollidedWith() {
        return ticksExisted >= invincibilityTime();
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        ticksExisted = compound.getInteger("existed");
        if (!world.isRemote) {
            dataManager.set(TICKSEXISTED, ticksExisted);
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger("existed", ticksExisted);
    }

    private int invincibilityTime() {
        //All projectile entities have an invincibility time where they won't collide with other entities.
        return 5;
    }

    /**
     * Math shit.
     */

    /**
     * Get's a Vector's direction, adds some noise if told to.
     */
    private Vec3d getDirection(float pitch, float yaw, float noise) {
        if (noise == 0F) return getDirection(pitch, yaw);
        else return this.getVectorFromRotation(pitch - noise, yaw - noise);
    }

    private Vec3d getDirection(float pitch, float yaw) {
        return this.getVectorFromRotation(pitch, yaw);
    }

    private Vec3d getVectorFromRotation(float pitch, float yaw) {
        float toRadians = (float) (Math.PI / 180);
        float f = MathHelper.cos(-yaw * toRadians - (float) Math.PI);
        float f1 = MathHelper.sin(-yaw * toRadians - (float) Math.PI);
        float f2 = -MathHelper.cos(-pitch * toRadians);
        float f3 = MathHelper.sin(-pitch * toRadians);
        return new Vec3d(f1 * f2, f3, f * f2);
    }
}
