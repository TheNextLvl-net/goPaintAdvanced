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
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.stream.Stream;

public class OverlayBrush extends CraftBrush {

    private static final String DESCRIPTION = "Only paints blocks\n§8that have air above it";
    private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGYzMWQ2Zjk2NTRmODc0ZWE5MDk3YWRlZWEwYzk2OTk2ZTc4ZTNmZDM3NTRmYmY5ZWJlOTYzYWRhZDliZTRjIn19fQ==";
    private static final String NAME = "Overlay Brush";

    public OverlayBrush() {
        super(NAME, DESCRIPTION, HEAD);
    }

    @Override
    public void paint(Location location, Player player, BrushSettings brushSettings) {
        performEdit(player, session -> {
            Stream<Block> blocks = Sphere.getBlocksInRadius(location, brushSettings.getSize(), null, false);
            blocks.filter(block -> passesMaskCheck(brushSettings, block))
                    .filter(block -> isOverlay(block, brushSettings.getThickness()))
                    .forEach(block -> setBlock(session, block, brushSettings.getRandomBlock()));
        });
    }

    private boolean isOverlay(Block block, int thickness) {
        for (int i = 1; i <= thickness; i++) {
            if (block.isSolid() && !block.getRelative(BlockFace.UP, i).isSolid()) {
                return true;
            }
        }
        return false;
    }
}
