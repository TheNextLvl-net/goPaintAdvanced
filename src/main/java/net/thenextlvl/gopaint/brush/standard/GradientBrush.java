/*
 * goPaint is designed to simplify painting inside of Minecraft.
 * Copyright (C) Arcaniax-Development
 * Copyright (C) Arcaniax team and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package net.thenextlvl.gopaint.brush.standard;

import com.sk89q.worldedit.math.BlockVector3;
import net.thenextlvl.gopaint.api.brush.Brush;
import net.thenextlvl.gopaint.api.brush.setting.BrushSettings;
import net.thenextlvl.gopaint.api.math.Sphere;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.stream.Stream;

public class GradientBrush extends Brush {
    public static final GradientBrush INSTANCE = new GradientBrush();

    public GradientBrush() {
        super(
                "Gradient Brush",
                "Creates gradients",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjA2MmRhM2QzYjhmMWZkMzUzNDNjYzI3OWZiMGZlNWNmNGE1N2I1YWJjNDMxZmJiNzhhNzNiZjJhZjY3NGYifX19",
                new NamespacedKey("gopaint", "gradient_brush")
        );
    }

    @Override
    public void paint(Location location, Player player, BrushSettings brushSettings) {
        performEdit(player, session -> {
            Stream<Block> blocks = Sphere.getBlocksInRadius(location, brushSettings.getBrushSize(), null, false);
            blocks.filter(block -> passesDefaultChecks(brushSettings, player, session, block))
                    .filter(block -> brushSettings.getRandom().nextDouble() > getRate(location, brushSettings, block))
                    .map(block -> BlockVector3.at(block.getX(), block.getY(), block.getZ()))
                    .forEach(vector3 -> {
                        int random = getRandom(brushSettings, vector3);
                        int index = Math.clamp(random, 0, brushSettings.getBlocks().size() - 1);
                        setBlock(session, vector3, brushSettings.getBlocks().get(index));
                    });
        });
    }

    private static int getRandom(BrushSettings brushSettings, BlockVector3 vector3) {
        var y = vector3.getY() - (brushSettings.getBrushSize() / 2d);
        var mixingStrength = brushSettings.getMixingStrength() / 100d;
        var random = brushSettings.getRandom().nextDouble() * 2 - 1;
        var size = (vector3.getY() - y) / brushSettings.getBrushSize() * brushSettings.getBlocks().size();
        return (int) (size + random * mixingStrength);
    }

    private static double getRate(Location location, BrushSettings brushSettings, Block block) {
        double size = (brushSettings.getBrushSize() / 2d) * ((100d - brushSettings.getFalloffStrength()) / 100d);
        return (block.getLocation().distance(location) - size) / ((brushSettings.getBrushSize() / 2d) - size);
    }
}
