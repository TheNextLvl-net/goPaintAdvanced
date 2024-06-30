package net.thenextlvl.gopaint.brush.pattern;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.thenextlvl.gopaint.api.brush.pattern.BuildPattern;
import net.thenextlvl.gopaint.api.brush.setting.BrushSettings;

import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Accessors(fluent = true, chain = false)
public class SplinePattern implements BuildPattern {
    private final EditSession session;
    private final BlockVector3 position;
    private final Player player;
    private final BrushSettings settings;

    private int random;

    @Override
    public boolean apply(Extent extent, BlockVector3 get, BlockVector3 set) throws WorldEditException {
        return set.setBlock(extent, getRandomBlockState());
    }

    @Override
    public BlockState getRandomBlockState() {
        var index = Math.clamp(random(), 0, settings().getBlocks().size() - 1);
        var block = BukkitAdapter.asBlockType(settings().getBlocks().get(index));
        return Objects.requireNonNull(block).getDefaultState();
    }
}
