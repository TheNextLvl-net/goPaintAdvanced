package net.thenextlvl.gopaint.api.brush.pattern;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockState;
import net.thenextlvl.gopaint.api.brush.setting.BrushSettings;
import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public interface BuildPattern extends Pattern {
    BlockVector3 position();

    EditSession session();

    Player player();

    BrushSettings settings();

    @Override
    default BaseBlock applyBlock(BlockVector3 position) {
        return player().getWorld().getFullBlock(position);
    }

    /**
     * Picks a random block from {@link BrushSettings#getBlocks()}.
     *
     * @return The default state of the randomly picked block.
     */
    default BlockState getRandomBlockState() {
        Material material;
        if (settings().getBlocks().size() == 1) {
            material = settings().getBlocks().getFirst();
        } else {
            var index = settings().getRandom().nextInt(settings().getBlocks().size());
            material = settings().getBlocks().get(index);
        }
        var type = BukkitAdapter.asBlockType(material);
        return Objects.requireNonNull(type).getDefaultState();
    }
}
