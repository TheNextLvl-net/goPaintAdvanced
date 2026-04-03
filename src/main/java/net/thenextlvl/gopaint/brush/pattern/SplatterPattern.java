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
public record SplatterPattern(
        EditSession session,
        BlockVector3 position,
        Player player,
        BrushSettings settings
) implements BuildPattern {

    @Override
    public boolean apply(final Extent extent, final BlockVector3 get, final BlockVector3 set) throws WorldEditException {
        if (settings().getRandom().nextDouble() <= getRate(set)) return false;
        return set.setBlock(extent, getRandomBlockState().withProperties(get.getBlock(extent)));
    }

    private double getRate(final BlockVector3 position) {
        final var size = settings().getBrushSize() / 2;
        final var falloff = (100 - settings().getFalloffStrength()) / 100d;
        return (position.distance(position()) - size * falloff) / (size - size * falloff);
    }
}
