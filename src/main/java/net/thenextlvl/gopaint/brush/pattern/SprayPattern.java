package net.thenextlvl.gopaint.brush.pattern;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.math.BlockVector3;
import net.thenextlvl.gopaint.api.brush.pattern.BuildPattern;
import net.thenextlvl.gopaint.api.brush.setting.BrushSettings;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record SprayPattern(
        EditSession session,
        BlockVector3 position,
        Player player,
        BrushSettings settings
) implements BuildPattern {

    @Override
    public boolean apply(final Extent extent, final BlockVector3 get, final BlockVector3 set) throws WorldEditException {
        if (settings.getRandom().nextInt(100) >= settings.getChance()) return false;
        return set.setBlock(extent, getRandomBlockState().withProperties(get.getBlock(extent)));
    }
}
