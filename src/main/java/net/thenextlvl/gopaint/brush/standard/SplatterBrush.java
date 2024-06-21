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
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.thenextlvl.gopaint.api.brush.Brush;
import net.thenextlvl.gopaint.api.brush.setting.BrushSettings;
import net.thenextlvl.gopaint.api.math.Sphere;
import net.thenextlvl.gopaint.api.model.GoPaintProvider;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.stream.Stream;

public class SplatterBrush extends Brush {
    private final GoPaintProvider provider;

    public SplatterBrush(GoPaintProvider provider) {
        super(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzMzODI5MmUyZTY5ZjA5MDY5NGNlZjY3MmJiNzZmMWQ4Mzc1OGQxMjc0NGJiNmZmYzY4MzRmZGJjMWE5ODMifX19",
                new NamespacedKey("gopaint", "splatter_brush")
        );
        this.provider = provider;
    }

    @Override
    public Component getName(Audience audience) {
        return provider.bundle().component(audience, "brush.name.splatter");
    }

    @Override
    public Component[] getDescription(Audience audience) {
        return provider.bundle().components(audience, "brush.description.splatter");
    }

    @Override
    public void paint(Location location, Player player, BrushSettings brushSettings) {
        performEdit(player, session -> {
            Stream<Block> blocks = Sphere.getBlocksInRadius(location, brushSettings.getBrushSize(), null, false);
            blocks.filter(block -> passesDefaultChecks(brushSettings, player, session, block))
                    .filter(block -> brushSettings.getRandom().nextDouble() > getRate(location, brushSettings, block))
                    .map(block -> BlockVector3.at(block.getX(), block.getY(), block.getZ()))
                    .forEach(vector3 -> setBlock(session, vector3, brushSettings.getRandomBlock()));
        });
    }

    private static double getRate(Location location, BrushSettings brushSettings, Block block) {
        var size = (double) brushSettings.getBrushSize() / 2.0;
        var falloff = (100.0 - (double) brushSettings.getFalloffStrength()) / 100.0;
        return (block.getLocation().distance(location) - size * falloff) / (size - size * falloff);
    }
}
