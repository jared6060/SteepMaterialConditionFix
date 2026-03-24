package io.github.jared6060.steepmaterialconditionfix.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.SurfaceRules;

@Mixin(targets = "net.minecraft.world.level.levelgen.SurfaceRules$Context$SteepMaterialCondition")
public class MixinSteepMaterialCondition extends SurfaceRules.LazyXZCondition {
    MixinSteepMaterialCondition(SurfaceRules.Context p_189594_) {
        super(p_189594_);
    }

    @Override
    protected boolean compute() {
        int i = this.context.blockX & 15;
        int j = this.context.blockZ & 15;
        int k = Math.max(j - 1, 0);
        int l = Math.min(j + 1, 15);
        ChunkAccess chunkaccess = this.context.chunk;
        int i1 = chunkaccess.getHeight(Heightmap.Types.WORLD_SURFACE_WG, i, k);
        int j1 = chunkaccess.getHeight(Heightmap.Types.WORLD_SURFACE_WG, i, l);

        // The fix detects positive and negative slopes rather than just positive slopes
        // along the x-axis.
        if (Math.abs(i1 - j1) >= 4)
            return true;

        int k1 = Math.max(i - 1, 0);
        int l1 = Math.min(i + 1, 15);
        int i2 = chunkaccess.getHeight(Heightmap.Types.WORLD_SURFACE_WG, k1, j);
        int j2 = chunkaccess.getHeight(Heightmap.Types.WORLD_SURFACE_WG, l1, j);

        // The fix detects positive and negative slopes rather than just positive slopes
        // along the z-axis.
        return Math.abs(i2 - j2) >= 4;
    }
}