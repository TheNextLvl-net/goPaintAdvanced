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
public record UnderlayPattern(
        EditSession session,
        BlockVector3 position,
        Player player,
        BrushSettings settings
) implements BuildPattern {

    @Override
    public boolean apply(final Extent extent, final BlockVector3 get, final BlockVector3 set) throws WorldEditException {
        final var block = applyBlock(get);
        if (!isUnderlay(get, block)) return false;
        return set.setBlock(extent, getRandomBlockState().withProperties(block.toBlockState()));
    }

    private boolean isUnderlay(final BlockVector3 position, final BaseBlock block) {
        for (var i = 1; i <= settings().getThickness(); i++) {
            if (!block.getMaterial().isMovementBlocker()) return false;
            if (!position.getStateRelativeY(player().getWorld(), i).getMaterial().isMovementBlocker()) return false;
        }
        return true;
    }
}
