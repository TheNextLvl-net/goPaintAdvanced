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

import net.thenextlvl.gopaint.api.brush.PatternBrush;
import net.thenextlvl.gopaint.api.brush.setting.ItemBrushSettings;
import net.thenextlvl.gopaint.api.model.SurfaceMode;
import org.bukkit.Axis;
import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Random;

@NullMarked
public final class CraftItemBrushSettings implements ItemBrushSettings {
    private final PatternBrush brush;
    private final Material mask;
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

    public CraftItemBrushSettings(PatternBrush brush, Material mask, List<Material> blocks, Axis axis, SurfaceMode surfaceMode,
                           boolean maskEnabled, int brushSize, int chance, int thickness, int angleDistance,
                           int fractureStrength, int falloffStrength, int mixingStrength, double angleHeightDifference) {
        this.brush = brush;
        this.mask = mask;
        this.blocks = blocks;
        this.axis = axis;
        this.surfaceMode = surfaceMode;
        this.maskEnabled = maskEnabled;
        this.brushSize = brushSize;
        this.chance = chance;
        this.thickness = thickness;
        this.angleDistance = angleDistance;
        this.fractureStrength = fractureStrength;
        this.falloffStrength = falloffStrength;
        this.mixingStrength = mixingStrength;
        this.angleHeightDifference = angleHeightDifference;
    }

    @Override
    public Random getRandom() {
        return random;
    }

    public PatternBrush getBrush() {
        return this.brush;
    }

    @Override
    public Material getMask() {
        return this.mask;
    }

    @Override
    public List<Material> getBlocks() {
        return this.blocks;
    }

    @Override
    public Axis getAxis() {
        return this.axis;
    }

    @Override
    public SurfaceMode getSurfaceMode() {
        return this.surfaceMode;
    }

    @Override
    public boolean isMaskEnabled() {
        return this.maskEnabled;
    }

    @Override
    public int getBrushSize() {
        return this.brushSize;
    }

    @Override
    public int getChance() {
        return this.chance;
    }

    @Override
    public int getThickness() {
        return this.thickness;
    }

    @Override
    public int getAngleDistance() {
        return this.angleDistance;
    }

    @Override
    public int getFractureStrength() {
        return this.fractureStrength;
    }

    @Override
    public int getFalloffStrength() {
        return this.falloffStrength;
    }

    @Override
    public int getMixingStrength() {
        return this.mixingStrength;
    }

    @Override
    public double getAngleHeightDifference() {
        return this.angleHeightDifference;
    }
}
