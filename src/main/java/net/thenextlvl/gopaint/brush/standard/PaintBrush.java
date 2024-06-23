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
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.gopaint.api.brush.Brush;
import net.thenextlvl.gopaint.api.brush.setting.BrushSettings;
import net.thenextlvl.gopaint.api.math.Height;
import net.thenextlvl.gopaint.api.math.Sphere;
import net.thenextlvl.gopaint.api.math.curve.BezierSpline;
import net.thenextlvl.gopaint.api.model.GoPaintProvider;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

public class PaintBrush extends Brush {
    private final GoPaintProvider provider;

    public PaintBrush(GoPaintProvider provider) {
        super(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODBiM2E5ZGZhYmVmYmRkOTQ5YjIxN2JiZDRmYTlhNDg2YmQwYzNmMGNhYjBkMGI5ZGZhMjRjMzMyZGQzZTM0MiJ9fX0=",
                new NamespacedKey("gopaint", "paint_brush")
        );
        this.provider = provider;
    }

    @Override
    public Component getName(Audience audience) {
        return provider.bundle().component(audience, "brush.name.paint");
    }

    @Override
    public Component[] getDescription(Audience audience) {
        return provider.bundle().components(audience, "brush.description.paint");
    }

    private static final Map<UUID, List<Location>> selectedPoints = new HashMap<>();

    @Override
    public void paint(Location target, Player player, BrushSettings brushSettings) {
        List<Location> locations = selectedPoints.computeIfAbsent(player.getUniqueId(), ignored -> new ArrayList<>());
        locations.add(target);

        if (!player.isSneaking()) {
            provider.bundle().sendMessage(player, "brush.paint.point.set",
                    Placeholder.parsed("x", String.valueOf(target.getBlockX())),
                    Placeholder.parsed("y", String.valueOf(target.getBlockY())),
                    Placeholder.parsed("z", String.valueOf(target.getBlockZ())),
                    Placeholder.parsed("point", String.valueOf(locations.size()))
            );
            return;
        }

        selectedPoints.remove(player.getUniqueId());

        performEdit(player, session -> {
            var world = player.getWorld();
            Location first = locations.getFirst();
            Sphere.getBlocksInRadius(first, brushSettings.getBrushSize(), null, false)
                    .filter(block -> Height.getAverageHeightDiffAngle(block.getLocation(), 1) < 0.1
                                     || Height.getAverageHeightDiffAngle(block.getLocation(), brushSettings.getAngleDistance())
                                        < Math.tan(Math.toRadians(brushSettings.getAngleHeightDifference())))

                    .filter(block -> {
                        var rate = calculateRate(block, first, brushSettings);
                        return brushSettings.getRandom().nextDouble() > rate;
                    }).forEach(block -> {
                        var curve = new LinkedList<Vector>();
                        curve.add(new Vector(block.getX(), block.getY(), block.getZ()));
                        locations.stream().map(location -> new Vector(
                                block.getX() + location.getX() - first.getX(),
                                block.getY() + location.getY() - first.getY(),
                                block.getZ() + location.getZ() - first.getZ()
                        )).forEach(curve::add);

                        var spline = new BezierSpline(curve);
                        var maxCount = (spline.getCurveLength() * 2.5) + 1;

                        for (int y = 0; y <= maxCount; y++) {
                            var point = spline.getPoint((y / maxCount) * (locations.size() - 1)).toLocation(world).getBlock();

                            if (point.isEmpty() || !passesDefaultChecks(brushSettings, player, session, point)) {
                                continue;
                            }

                            var vector3 = BlockVector3.at(point.getX(), point.getY(), point.getZ());
                            setBlock(session, vector3, brushSettings.getRandomBlock());
                        }
                    });
        });
    }

    private double calculateRate(Block block, Location first, BrushSettings brushSettings) {
        double sizeHalf = brushSettings.getBrushSize() / 2.0;
        double falloffStrengthFactor = (100.0 - brushSettings.getFalloffStrength()) / 100.0;
        double numerator = block.getLocation().distance(first) - sizeHalf * falloffStrengthFactor;
        double denominator = sizeHalf - sizeHalf * falloffStrengthFactor;

        return numerator / denominator;
    }
}
