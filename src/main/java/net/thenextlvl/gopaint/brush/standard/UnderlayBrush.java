package net.thenextlvl.gopaint.brush.standard;

import net.thenextlvl.gopaint.api.brush.setting.BrushSettings;
import net.thenextlvl.gopaint.api.math.Sphere;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.stream.Stream;

public class UnderlayBrush extends CraftBrush {

    private static final String DESCRIPTION = "Only paints blocks\n§8that have no air above it";
    private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzIzNDQ2OTkwZjU4YjY1M2FiNWYwZTdhZjNmZGM3NTYwOTEyNzVmNGMzYzJkZDQxYzdkODYyZGQzZjkyZTg0YSJ9fX0=";
    private static final String NAME = "Underlay Brush";

    public UnderlayBrush() {
        super(NAME, DESCRIPTION, HEAD);
    }

    @Override
    public void paint(Location location, Player player, BrushSettings brushSettings) {
        performEdit(player, session -> {
            Stream<Block> blocks = Sphere.getBlocksInRadius(location, brushSettings.getSize(), null, false);
            blocks.filter(block -> passesMaskCheck(brushSettings, block))
                    .filter(block -> isUnderlay(block, brushSettings.getThickness()))
                    .forEach(block -> setBlock(session, block, brushSettings.getRandomBlock()));
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
