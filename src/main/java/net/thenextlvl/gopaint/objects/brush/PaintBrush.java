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
package net.thenextlvl.gopaint.objects.brush;

import core.i18n.file.ComponentBundle;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.gopaint.brush.BrushSettings;
import net.thenextlvl.gopaint.utils.Height;
import net.thenextlvl.gopaint.utils.Sphere;
import net.thenextlvl.gopaint.utils.curve.BezierSpline;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class PaintBrush extends Brush {

    private static final @NotNull String DESCRIPTION = "Paints strokes\n§8hold shift to end";
    private static final @NotNull String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODBiM2E5ZGZhYmVmYmRkOTQ5YjIxN2JiZDRmYTlhNDg2YmQwYzNmMGNhYjBkMGI5ZGZhMjRjMzMyZGQzZTM0MiJ9fX0=";
    private static final @NotNull String NAME = "Paint Brush";

    private final @NotNull ComponentBundle bundle;

    public PaintBrush(@NotNull ComponentBundle bundle) {
        super(NAME, DESCRIPTION, HEAD);
        this.bundle = bundle;
    }

    private static final HashMap<UUID, List<Location>> selectedPoints = new HashMap<>();

    @Override
    public void paint(
            @NotNull Location target,
            @NotNull Player player,
            @NotNull BrushSettings brushSettings
    ) {
        List<Location> locations = selectedPoints.computeIfAbsent(player.getUniqueId(), ignored -> new ArrayList<>());
        locations.add(target);

        if (!player.isSneaking()) {
            bundle.sendMessage(player, "brush.paint.point.set",
                    Placeholder.parsed("x", String.valueOf(target.getBlockX())),
                    Placeholder.parsed("y", String.valueOf(target.getBlockY())),
                    Placeholder.parsed("z", String.valueOf(target.getBlockZ())),
                    Placeholder.parsed("point", String.valueOf(locations.size()))
            );
            return;
        }

        selectedPoints.remove(player.getUniqueId());

        performEdit(player, session -> {
            Location first = locations.getFirst();
            Stream<Block> blocks = Sphere.getBlocksInRadius(first, brushSettings.size(), null, false);
            blocks.forEach(block -> {
                if (Height.getAverageHeightDiffAngle(block.getLocation(), 1) >= 0.1
                        && Height.getAverageHeightDiffAngle(block.getLocation(), brushSettings.angleDistance())
                        >= Math.tan(Math.toRadians(brushSettings.angleHeightDifference()))) {
                    return;
                }

                double rate = (block.getLocation().distance(first) - (brushSettings.size() / 2.0)
                        * ((100.0 - brushSettings.falloffStrength()) / 100.0)) / ((brushSettings.size() / 2.0)
                        - (brushSettings.size() / 2.0) * ((100.0 - brushSettings.falloffStrength()) / 100.0));

                if (brushSettings.random().nextDouble() <= rate) {
                    return;
                }

                LinkedList<Location> newCurve = new LinkedList<>();
                newCurve.add(block.getLocation());
                for (Location location : locations) {
                    newCurve.add(block.getLocation().clone().add(
                            location.getX() - first.getX(),
                            location.getY() - first.getY(),
                            location.getZ() - first.getZ()
                    ));
                }
                BezierSpline spline = new BezierSpline(newCurve);
                double maxCount = (spline.getCurveLength() * 2.5) + 1;
                for (int y = 0; y <= maxCount; y++) {
                    Block point = spline.getPoint((y / maxCount) * (locations.size() - 1)).getBlock();

                    if (point.isEmpty() || !passesDefaultChecks(brushSettings, player, point)) {
                        continue;
                    }

                    setBlock(session, point, brushSettings.randomBlock());
                }
            });
        });
    }

}
