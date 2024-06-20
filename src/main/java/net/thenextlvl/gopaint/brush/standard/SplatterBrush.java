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
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.stream.Stream;

public class SplatterBrush extends Brush {

    private static final String DESCRIPTION = "More chance when closer\n§8to the clicked point\n§8and configurable chance";
    private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzMzODI5MmUyZTY5ZjA5MDY5NGNlZjY3MmJiNzZmMWQ4Mzc1OGQxMjc0NGJiNmZmYzY4MzRmZGJjMWE5ODMifX19";
    private static final String NAME = "Splatter Brush";

    public SplatterBrush() {
        super(NAME, DESCRIPTION, HEAD);
    }

    @Override
    public void paint(Location location, Player player, BrushSettings brushSettings) {
        performEdit(player, session -> {
            Stream<Block> blocks = Sphere.getBlocksInRadius(location, brushSettings.getSize(), null, false);
            blocks.filter(block -> passesDefaultChecks(brushSettings, player, session, block))
                    .filter(block -> brushSettings.getRandom().nextDouble() > getRate(location, brushSettings, block))
                    .map(block -> BlockVector3.at(block.getX(), block.getY(), block.getZ()))
                    .forEach(vector3 -> setBlock(session, vector3, brushSettings.getRandomBlock()));
        });
    }

    private static double getRate(Location location, BrushSettings brushSettings, Block block) {
        var size = (double) brushSettings.getSize() / 2.0;
        var falloff = (100.0 - (double) brushSettings.getFalloffStrength()) / 100.0;
        return (block.getLocation().distance(location) - size * falloff) / (size - size * falloff);
    }
}
