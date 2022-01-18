package com.vicious.viciouscore.common.entity.living;

import com.vicious.viciouscore.common.entity.projectile.GenericProjectileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class GenericEntityMob extends EntityMob {
    private final float hitRange = 0F;

    public GenericEntityMob(World worldIn) {
        super(worldIn);
    }

    protected Entity getEntityCollision() {
        Vec3d vec31 = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d vec3 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        List<Entity> entities = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(this.motionX, this.motionY, this.motionZ).grow(hitRange, hitRange, hitRange));
        double d0 = 0.0D;

        for (Entity possibleCollision : entities) {
            if (possibleCollision.canBeCollidedWith() && this.canBeCollidedWith()) {
                AxisAlignedBB axisalignedbb1 = possibleCollision.getEntityBoundingBox().grow(hitRange, hitRange, hitRange);
                RayTraceResult traceResult = axisalignedbb1.calculateIntercept(vec31, vec3);
                //Null means a collision somehow didn't occur (usually this means the hitrange is too small).
                if (traceResult != null) {
                    double distance = vec31.distanceTo(traceResult.hitVec);
                    if (distance < d0) {
                        return possibleCollision;
                    }
                }
            }
        }
        return null;
    }

    //Returns only player collisions.
    protected EntityPlayer getPlayerEntityCollision() {
        Entity entity = getEntityCollision();
        return entity instanceof EntityPlayer ? (EntityPlayer) entity : null;
    }

    //Returns only collisions that aren't the same type as this entity.
    protected Entity getNonSameEntityCollision() {
        Entity entity = getEntityCollision();
        return entity instanceof GenericProjectileEntity ? null : entity;
    }

    //Returns only collisions that are the same type as this entity.
    protected Entity getSameEntityCollision() {
        Entity entity = getEntityCollision();
        return entity instanceof GenericProjectileEntity ? entity : null;
    }

    protected Entity getTarget() {
        for (Entity entity : this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(4D))) {
            if (entity instanceof EntityPlayer) return entity;
        }
        return null;
    }
}
