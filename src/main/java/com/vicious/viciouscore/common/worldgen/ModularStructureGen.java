package com.vicious.viciouscore.common.worldgen;

import com.vicious.viciouscore.common.util.WorldUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class ModularStructureGen extends MapGenStructure {
    /**
     * Determines whether a structure should generate here. returns the chunk ground level for the center block.
     */
    private final Predicate<BlockPos> canSpawnPredicator = (v) -> false;

    @Override
    public String getStructureName() {
        return null;
    }

    @Nullable
    @Override
    public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean findUnexplored) {
        return null;
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        BlockPos ground = WorldUtil.getNextChunkGround(world, new BlockPos(chunkX, 256, chunkZ));
        return canSpawnPredicator.test(ground);
    }

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        return new Start(chunkX, chunkZ);
    }

    public class Start extends StructureStart {
        public Start() {
        }

        public Start(int chunkX, int chunkZ) {
            super(chunkX, chunkZ);

           /* EnumFacing direction = Randomizer.randomOfOptions(EnumFacing.HORIZONTALS);
            int minY = settings.minY == 0 ? 17 : settings.minY;
            int maxY = settings.maxY == 0 ? 37 : settings.maxY;
            int y = random.nextInt(maxY - minY + 1) + minY;
            BlockPos.MutableBlockPos startingPos = new BlockPos.MutableBlockPos((chunkX << 4) + 2, y, (chunkZ << 4) + 2);

            // Entrypoint
            MineshaftPiece entryPoint = new VerticalEntrance(
                    0,
                    -1,
                    random,
                    startingPos,
                    direction,
                    settings
            );

            this.components.add(entryPoint);

            // Build room component. This also populates the children list, effectively building the entire mineshaft.
            // Note that no blocks are actually placed yet.
            entryPoint.buildComponent(entryPoint, this.components, random);

            // Expand bounding box to encompass all children*/
            this.updateBoundingBox();
        }
    }
}
