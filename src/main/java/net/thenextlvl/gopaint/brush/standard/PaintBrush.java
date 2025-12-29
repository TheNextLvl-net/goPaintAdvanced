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

import com.fastasyncworldedit.core.math.MutableBlockVector3;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.thenextlvl.gopaint.api.brush.PatternBrush;
import net.thenextlvl.gopaint.api.brush.setting.BrushSettings;
import net.thenextlvl.gopaint.api.math.Sphere;
import net.thenextlvl.gopaint.api.math.curve.BezierSpline;
import net.thenextlvl.gopaint.api.model.GoPaintProvider;
import net.thenextlvl.gopaint.brush.pattern.SplinePattern;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@NullMarked
public final class PaintBrush extends PatternBrush {
    private final Map<UUID, List<BlockVector3>> selectedPoints = new HashMap<>();
    private final GoPaintProvider provider;

    public PaintBrush(GoPaintProvider provider) {
        super(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODBiM2E5ZGZhYmVmYmRkOTQ5YjIxN2JiZDRmYTlhNDg2YmQwYzNmMGNhYjBkMGI5ZGZhMjRjMzMyZGQzZTM0MiJ9fX0=",
                Key.key("gopaint", "paint_brush")
        );
        this.provider = provider;
    }

    @Override
    public Component getName(Audience audience) {
        return provider.bundle().component("brush.name.paint", audience);
    }

    @Override
    public Component[] getDescription(Audience audience) {
        return new Component[]{
                provider.bundle().component("brush.description.paint.1", audience),
                provider.bundle().component("brush.description.paint.2", audience)
        };
    }

    @Override
    public Pattern buildPattern(EditSession session, BlockVector3 position, Player player, BrushSettings settings) {
        return new SplinePattern(session, position, player, settings);
    }

    @Override
    public void build(EditSession session, BlockVector3 position, Pattern original, double size) throws MaxChangedBlocksException {
        if (!(original instanceof SplinePattern pattern)) return;
        if (!(pattern.player() instanceof BukkitPlayer bukkit)) return;

        var id = pattern.player().getUniqueId();
        selectedPoints.computeIfAbsent(id, ignored -> new ArrayList<>()).add(position);

        if (!bukkit.getPlayer().isSneaking()) {
            provider.bundle().sendMessage(bukkit.getPlayer(), "brush.paint.point.set",
                    Formatter.number("x", position.x()),
                    Formatter.number("y", position.y()),
                    Formatter.number("z", position.z()),
                    Formatter.number("point", selectedPoints.get(id).size())
            );
            return;
        }

        if (selectedPoints.get(id).size() <= 1) return;

        var vectors = selectedPoints.remove(id);

        var first = vectors.getFirst();
        var settings = pattern.settings();

        Sphere.getBlocksInRadius(first, size).stream()
                .filter(vector3 -> {
                    var rate = getRate(settings.getFalloffStrength(), size, vector3, first);
                    return settings.getRandom().nextDouble() > rate;
                }).forEach(vector3 -> {
                    pattern.random(settings.getRandom().nextInt(settings.getBlocks().size()));

                    var curve = new LinkedList<MutableBlockVector3>();
                    curve.add(MutableBlockVector3.at(vector3.x(), vector3.y(), vector3.z()));
                    vectors.stream().skip(1).map(location -> MutableBlockVector3.at(
                            vector3.x() + location.x() - first.x(),
                            vector3.y() + location.y() - first.y(),
                            vector3.z() + location.z() - first.z()
                    )).forEach(curve::add);

                    var spline = new BezierSpline(curve);
                    var maxCount = (spline.getCurveLength() * 2.5) + 1;

                    for (var y = 0; y <= maxCount; y++) {
                        session.setBlock(spline.getPoint((y / maxCount) * (vectors.size() - 1)), pattern);
                    }
                });
    }

    private double getRate(double falloffStrength, double size, BlockVector3 vector3, BlockVector3 first) {
        var falloffFactor = (100.0 - falloffStrength) / 100.0;
        var numerator = vector3.distance(first) - size * falloffFactor;
        var denominator = size - size * falloffFactor;
        return numerator / denominator;
    }
}
