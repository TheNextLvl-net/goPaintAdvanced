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
package net.thenextlvl.gopaint.brush;

import net.thenextlvl.gopaint.api.brush.setting.BrushSettings;
import net.thenextlvl.gopaint.api.math.Sphere;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.stream.Stream;

public class GradientBrush extends CraftBrush {

    private static final String DESCRIPTION = "Creates gradients";
    private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjA2MmRhM2QzYjhmMWZkMzUzNDNjYzI3OWZiMGZlNWNmNGE1N2I1YWJjNDMxZmJiNzhhNzNiZjJhZjY3NGYifX19";
    private static final String NAME = "Gradient Brush";

    public GradientBrush() {
        super(NAME, DESCRIPTION, HEAD);
    }

    @Override
    public void paint(Location location, Player player, BrushSettings brushSettings) {
        performEdit(player, session -> {
            double y = location.getBlockY() - (brushSettings.getSize() / 2d);
            Stream<Block> blocks = Sphere.getBlocksInRadius(location, brushSettings.getSize(), null, false);
            blocks.filter(block -> passesDefaultChecks(brushSettings, player, block)).filter(block -> {
                double rate = (block.getLocation().distance(location) - (brushSettings.getSize() / 2d)
                        * ((100d - brushSettings.getFalloffStrength()) / 100d))
                        / ((brushSettings.getSize() / 2d) - (brushSettings.getSize() / 2d)
                                                            * ((100d - brushSettings.getFalloffStrength()) / 100d));

                return brushSettings.getRandom().nextDouble() > rate;
            }).forEach(block -> {
                int random = (int) (((block.getLocation().getBlockY() - y) / brushSettings.getSize()
                        * brushSettings.getBlocks().size()) + (brushSettings.getRandom().nextDouble() * 2 - 1)
                                                              * (brushSettings.getMixingStrength() / 100d));

                int index = Math.clamp(random, 0, brushSettings.getBlocks().size() - 1);

                setBlock(session, block, brushSettings.getBlocks().get(index));
            });
        });
    }
}
