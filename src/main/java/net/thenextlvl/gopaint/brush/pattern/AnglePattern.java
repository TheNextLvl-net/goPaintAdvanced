package net.thenextlvl.gopaint.brush.pattern;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.math.BlockVector3;
import net.thenextlvl.gopaint.api.brush.pattern.BuildPattern;
import net.thenextlvl.gopaint.api.brush.setting.BrushSettings;
import net.thenextlvl.gopaint.api.math.Height;
import net.thenextlvl.gopaint.api.model.Block;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record AnglePattern(
        EditSession session,
        BlockVector3 position,
        Player player,
        BrushSettings settings
) implements BuildPattern {

    @Override
    public boolean apply(Extent extent, BlockVector3 get, BlockVector3 set) throws WorldEditException {
        var block = new Block(applyBlock(get), set, extent);

        if (Height.getAverageHeightDiffAngle(block, 1) < 0.1) return false;
        if (Height.getAverageHeightDiffAngle(block, settings().getAngleDistance())
            >= Math.tan(Math.toRadians(settings().getAngleHeightDifference()))
        ) return false;

        return set.setBlock(extent, getRandomBlockState());
    }
}
