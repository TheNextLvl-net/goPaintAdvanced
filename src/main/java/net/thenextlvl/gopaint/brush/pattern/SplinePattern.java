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
public class SplinePattern implements BuildPattern {
    private final EditSession session;
    private final BlockVector3 position;
    private final Player player;
    private final BrushSettings settings;

    private int random;

    public SplinePattern(EditSession session, BlockVector3 position, Player player, BrushSettings settings) {
        this.session = session;
        this.position = position;
        this.player = player;
        this.settings = settings;
    }

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

    public EditSession session() {
        return this.session;
    }

    public BlockVector3 position() {
        return this.position;
    }

    public Player player() {
        return this.player;
    }

    public BrushSettings settings() {
        return this.settings;
    }

    public int random() {
        return this.random;
    }

    public void random(int random) {
        this.random = random;
    }
}
