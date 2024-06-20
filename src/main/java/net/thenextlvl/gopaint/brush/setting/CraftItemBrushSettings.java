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
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Getter
@Builder(builderClassName = "Builder")
public final class CraftItemBrushSettings implements ItemBrushSettings {
    private final Brush brush;
    private final @Nullable Material mask;
    private final List<Material> blocks;
    private final Axis axis;
    private final SurfaceMode surfaceMode;
    private final MaskMode maskMode;
    private final int brushSize;
    private final int chance;
    private final int thickness;
    private final int angleDistance;
    private final int fractureDistance;
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

    @Deprecated(forRemoval = true, since = "1.1.1")
    public static ItemBrushSettings parse(Brush brush, ItemMeta itemMeta) {
        var builder = builder()
                .maskMode(MaskMode.DISABLED)
                .surfaceMode(SurfaceMode.DISABLED)
                .brush(brush);
        Optional.ofNullable(itemMeta.getLore()).ifPresent(lore -> lore.stream()
                .map(line -> line.replace("§8", ""))
                .forEach(line -> {
                    if (line.startsWith("Size: ")) {
                        builder.brushSize(Integer.parseInt(line.substring(6)));
                    } else if (line.startsWith("Chance: ")) {
                        builder.chance(Integer.parseInt(line.substring(8, line.length() - 1)));
                    } else if (line.startsWith("Thickness: ")) {
                        builder.thickness(Integer.parseInt(line.substring(11)));
                    } else if (line.startsWith("Axis: ")) {
                        builder.axis(Axis.valueOf(line.substring(6).toUpperCase()));
                    } else if (line.startsWith("FractureDistance: ")) {
                        builder.fractureDistance(Integer.parseInt(line.substring(18)));
                    } else if (line.startsWith("AngleDistance: ")) {
                        builder.angleDistance(Integer.parseInt(line.substring(15)));
                    } else if (line.startsWith("AngleHeightDifference: ")) {
                        builder.angleHeightDifference(Double.parseDouble(line.substring(23)));
                    } else if (line.startsWith("Mixing: ")) {
                        builder.mixingStrength(Integer.parseInt(line.substring(8)));
                    } else if (line.startsWith("Falloff: ")) {
                        builder.falloffStrength(Integer.parseInt(line.substring(9)));
                    } else if (line.startsWith("Blocks: ")) {
                        builder.blocks(Arrays.stream(line.substring(8).split(", "))
                                .map(Material::matchMaterial)
                                .filter(Objects::nonNull)
                                .toList());
                    } else if (line.startsWith("Mask: ")) {
                        builder.mask(Material.matchMaterial(line.substring(6)));
                    } else if (line.startsWith("Mask Mode: ")) {
                        MaskMode.byName(line.substring(11)).ifPresent(builder::maskMode);
                    } else if (line.startsWith("Surface Mode: ")) {
                        SurfaceMode.byName(line.substring(14)).ifPresent(builder::surfaceMode);
                    }
                }));
        return builder.build();
    }

}
