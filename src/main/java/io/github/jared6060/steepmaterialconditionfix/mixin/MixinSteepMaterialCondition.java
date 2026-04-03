package io.github.jared6060.steepmaterialconditionfix.mixin;

import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(targets = "net.minecraft.world.level.levelgen.SurfaceRules$Context$SteepMaterialCondition")
public class MixinSteepMaterialCondition extends SurfaceRules.LazyXZCondition {
    MixinSteepMaterialCondition(SurfaceRules.Context p_189594_) {
        super(p_189594_);
    }

    @Overwrite
    protected boolean compute() {
        ChunkAccess chunkAccess = this.context.chunk;

        int targetX = this.context.blockX & 15;
        int targetZ = this.context.blockZ & 15;

        int targetSouthZ = Math.max(targetZ - 1, 0);
        int targetNorthZ = Math.min(targetZ + 1, 15);

        int targetSouthY = chunkAccess.getHeight(Heightmap.Types.WORLD_SURFACE_WG, targetX, targetSouthZ);
        int targetNorthY = chunkAccess.getHeight(Heightmap.Types.WORLD_SURFACE_WG, targetX, targetNorthZ);

        if (Math.abs(targetSouthY - targetNorthY) >= 4) {
            return true;
        }

        int targetWestX = Math.max(targetX - 1, 0);
        int targetEastX = Math.min(targetX + 1, 15);

        int targetWestY = chunkAccess.getHeight(Heightmap.Types.WORLD_SURFACE_WG, targetWestX, targetZ);
        int targetEastY = chunkAccess.getHeight(Heightmap.Types.WORLD_SURFACE_WG, targetEastX, targetZ);

        return Math.abs(targetWestY - targetEastY) >= 4;
    }
}