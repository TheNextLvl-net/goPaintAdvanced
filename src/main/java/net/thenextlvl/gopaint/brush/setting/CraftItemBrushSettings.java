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
package net.thenextlvl.gopaint.brush.setting;

import lombok.Builder;
import lombok.Getter;
import net.thenextlvl.gopaint.api.brush.Brush;
import net.thenextlvl.gopaint.api.brush.setting.ItemBrushSettings;
import net.thenextlvl.gopaint.api.model.MaskMode;
import net.thenextlvl.gopaint.api.model.SurfaceMode;
import org.bukkit.Axis;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

@Getter
@Builder(builderClassName = "Builder")
public final class CraftItemBrushSettings implements ItemBrushSettings {
    private final Brush brush;
    private final @Nullable Material mask;
    private final List<Material> blocks;
    private final Axis axis;
    private final SurfaceMode surfaceMode;
    private final boolean maskEnabled;
    private final int brushSize;
    private final int chance;
    private final int thickness;
    private final int angleDistance;
    private final int fractureStrength;
    private final int falloffStrength;
    private final int mixingStrength;
    private final double angleHeightDifference;

    private static final Random random = new Random();

    @Override
    public Material getRandomBlock() {
        return getBlocks().get(getRandom().nextInt(getBlocks().size()));
    }

    @Override
    public Random getRandom() {
        return random;
    }
}
