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
import net.thenextlvl.gopaint.api.math.ConnectedBlocks;
import net.thenextlvl.gopaint.api.math.Sphere;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Stream;

public class BucketBrush extends Brush {
    public static final BucketBrush INSTANCE = new BucketBrush();

    public BucketBrush() {
        super(
                "Bucket Brush",
                "Paints connected blocks\n§8with the same block type",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTAxOGI0NTc0OTM5Nzg4YTJhZDU1NTJiOTEyZDY3ODEwNjk4ODhjNTEyMzRhNGExM2VhZGI3ZDRjOTc5YzkzIn19fQ==",
                new NamespacedKey("gopaint", "bucket_brush")
        );
    }

    @Override
    public void paint(Location location, Player player, BrushSettings brushSettings) {
        performEdit(player, session -> {
            List<Block> blocks = Sphere.getBlocksInRadius(location, brushSettings.getBrushSize(), null, false).toList();
            Stream<Block> connectedBlocks = ConnectedBlocks.getConnectedBlocks(location, blocks);
            connectedBlocks.filter(block -> passesDefaultChecks(brushSettings, player, session, block))
                    .map(block -> BlockVector3.at(block.getX(), block.getY(), block.getZ()))
                    .forEach(vector3 -> setBlock(session, vector3, brushSettings.getRandomBlock()));
        });
    }
}
