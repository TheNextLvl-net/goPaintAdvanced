package net.thenextlvl.gopaint.brush;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.math.BlockVector3;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.gopaint.api.brush.Brush;
import net.thenextlvl.gopaint.api.brush.setting.BrushSettings;
import net.thenextlvl.gopaint.api.math.Surface;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor
abstract class CraftBrush implements Brush {
    private final String name, description, headValue;

    public void setBlock(EditSession session, Block block, Material material) throws MaxChangedBlocksException {
        BlockVector3 vector = BlockVector3.at(block.getX(), block.getY(), block.getZ());
        if (session.getMask() == null || session.getMask().test(vector)) {
            session.setBlock(vector, BukkitAdapter.asBlockType(material));
        }
    }

    public void performEdit(Player player, Consumer<EditSession> edit) {
        BukkitPlayer wrapped = BukkitAdapter.adapt(player);
        LocalSession localSession = WorldEdit.getInstance().getSessionManager().get(wrapped);
        try (EditSession editsession = localSession.createEditSession(wrapped)) {
            try {
                edit.accept(editsession);
            } finally {
                localSession.remember(editsession);
            }
        }
    }

    public boolean passesDefaultChecks(BrushSettings brushSettings, Player player, Block block) {
        return passesMaskCheck(brushSettings, block) && passesSurfaceCheck(brushSettings, player, block);
    }

    public boolean passesSurfaceCheck(BrushSettings brushSettings, Player player, Block block) {
        return Surface.isOnSurface(block, brushSettings.getSurfaceMode(), player.getLocation());
    }

    public boolean passesMaskCheck(BrushSettings brushSettings, Block block) {
        return !brushSettings.isMaskEnabled() || block.getType().equals(brushSettings.getMask());
    }
}
