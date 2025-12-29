package net.thenextlvl.gopaint.api.model;

import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.registry.BlockMaterial;

public record Block(BaseBlock base, BlockVector3 vector, Extent world) {
    public BlockMaterial material() {
        return base().getMaterial();
    }
}
