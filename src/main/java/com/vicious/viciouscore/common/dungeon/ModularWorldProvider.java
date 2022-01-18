package com.vicious.viciouscore.common.dungeon;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkGeneratorFlat;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Supplier;

public class ModularWorldProvider extends WorldProvider {
    private final DimensionType TYPE;
    private final int ID;
    private final String NAME;
    private final Supplier<IChunkGenerator> generator = () -> new ChunkGeneratorFlat(this.world, 0, false, "");

    public ModularWorldProvider(DimensionType type, int id, String name) {
        this.TYPE = type;
        this.ID = id;
        this.NAME = name;
    }

    @Override
    public void init() {
        //TODO add biome Provider
        setDimension(ID);
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        return generator.get();
    }

    @Override
    public boolean isSurfaceWorld() {
        return false;
    }

    @Override
    public boolean canRespawnHere() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getCloudHeight() {
        return 0F;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isSkyColored() {
        return true;
    }

    @Override
    public int getAverageGroundLevel() {
        return 128;
    }

    @Override
    public String getSaveFolder() {
        return NAME;
    }

    @Override
    public Vec3d getSkyColor(Entity cameraEntity, float partialTicks) {
        return new Vec3d(0, 0, 0);
    }

    @Override
    public boolean canDoRainSnowIce(Chunk chunk) {
        return false;
    }

    @Override
    public DimensionType getDimensionType() {
        return TYPE;
    }
}
