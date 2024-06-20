package net.thenextlvl.gopaint.brush.standard;

import com.sk89q.worldedit.math.BlockVector3;
import net.thenextlvl.gopaint.api.brush.Brush;
import net.thenextlvl.gopaint.api.brush.setting.BrushSettings;
import net.thenextlvl.gopaint.api.math.Sphere;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.stream.Stream;

public class UnderlayBrush extends Brush {
    public static final UnderlayBrush INSTANCE = new UnderlayBrush();

    public UnderlayBrush() {
        super(
                "Underlay Brush",
                "Only paints blocks\n§8that have no air above it",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzIzNDQ2OTkwZjU4YjY1M2FiNWYwZTdhZjNmZGM3NTYwOTEyNzVmNGMzYzJkZDQxYzdkODYyZGQzZjkyZTg0YSJ9fX0=",
                new NamespacedKey("gopaint", "underlay_brush")
        );
    }

    @Override
    public void paint(Location location, Player player, BrushSettings brushSettings) {
        performEdit(player, session -> {
            Stream<Block> blocks = Sphere.getBlocksInRadius(location, brushSettings.getBrushSize(), null, false);
            blocks.filter(block -> passesMaskCheck(brushSettings, session, block))
                    .filter(block -> isUnderlay(block, brushSettings.getThickness()))
                    .map(block -> BlockVector3.at(block.getX(), block.getY(), block.getZ()))
                    .forEach(vector3 -> setBlock(session, vector3, brushSettings.getRandomBlock()));
        });
    }

    private boolean isUnderlay(Block block, int thickness) {
        for (int i = 1; i <= thickness; i++) {
            if (!block.isSolid() || !block.getRelative(BlockFace.UP, i).isSolid()) {
                return false;
            }
        }
        return true;
    }

}
