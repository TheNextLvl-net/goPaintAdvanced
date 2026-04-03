package net.thenextlvl.gopaint.brush.pattern;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockState;
import net.thenextlvl.gopaint.api.brush.pattern.BuildPattern;
import net.thenextlvl.gopaint.api.brush.setting.BrushSettings;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public record GradientPattern(
        EditSession session,
        BlockVector3 position,
        Player player,
        BrushSettings settings
) implements BuildPattern {

    @Override
    public boolean apply(final Extent extent, final BlockVector3 get, final BlockVector3 set) throws WorldEditException {
        if (settings().getRandom().nextDouble() <= getRate(set)) return false;
        return set.setBlock(extent, getRandomBlockState(set.y()).withProperties(get.getBlock(extent)));
    }

    public BlockState getRandomBlockState(final int altitude) {
        final var index = Math.clamp(getRandom(altitude), 0, settings().getBlocks().size() - 1);
        final var block = BukkitAdapter.asBlockType(settings().getBlocks().get(index));
        return Objects.requireNonNull(block).getDefaultState();
    }

    private int getRandom(final int altitude) {
        if (settings().getBlocks().size() == 1) return 1;
        final var y = position().y() - (settings().getBrushSize() / 2d);
        final var _y = (altitude - y) / settings().getBrushSize() * settings().getBlocks().size();
        return (int) (_y + (settings().getRandom().nextDouble() * 2 - 1) * (settings().getMixingStrength() / 100d));
    }

    private double getRate(final BlockVector3 position) {
        final var size = settings().getBrushSize() * ((100d - settings().getFalloffStrength()) / 100d);
        return (position.distance(position()) - size) / (settings().getBrushSize() - size);
    }
}
