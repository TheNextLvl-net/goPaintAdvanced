package net.thenextlvl.gopaint.brush.pattern;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BaseBlock;
import net.thenextlvl.gopaint.api.brush.pattern.BuildPattern;
import net.thenextlvl.gopaint.api.brush.setting.BrushSettings;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record OverlayPattern(
        EditSession session,
        BlockVector3 position,
        Player player,
        BrushSettings settings
) implements BuildPattern {

    @Override
    public boolean apply(Extent extent, BlockVector3 get, BlockVector3 set) throws WorldEditException {
        var block = applyBlock(get);
        if (!isOverlay(get, block)) return false;
        return set.setBlock(extent, getRandomBlockState().withProperties(block.toBlockState()));
    }

    private boolean isOverlay(BlockVector3 position, BaseBlock block) {
        for (var i = 1; i <= settings().getThickness(); i++) {
            if (!block.getMaterial().isMovementBlocker()) continue;
            if (position.getStateRelativeY(player().getWorld(), i).getMaterial().isMovementBlocker()) continue;
            return true;
        }
        return false;
    }
}
