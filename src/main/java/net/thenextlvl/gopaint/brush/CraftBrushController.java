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

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import net.thenextlvl.gopaint.GoPaintPlugin;
import net.thenextlvl.gopaint.api.brush.Brush;
import net.thenextlvl.gopaint.api.brush.BrushController;
import net.thenextlvl.gopaint.api.brush.setting.ItemBrushSettings;
import net.thenextlvl.gopaint.api.brush.setting.PlayerBrushSettings;
import net.thenextlvl.gopaint.brush.setting.CraftItemBrushSettings;
import net.thenextlvl.gopaint.brush.setting.CraftPlayerBrushSettings;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class CraftBrushController implements BrushController {
    private final Map<UUID, PlayerBrushSettings> playerBrushes = new HashMap<>();
    private final @Getter List<Brush> brushes;
    private final GoPaintPlugin plugin;

    public CraftBrushController(GoPaintPlugin plugin) {
        brushes = ImmutableList.of(
                new SphereBrush(),
                new SprayBrush(),
                new SplatterBrush(),
                new DiscBrush(),
                new BucketBrush(),
                new AngleBrush(),
                new OverlayBrush(),
                new UnderlayBrush(),
                new FractureBrush(),
                new GradientBrush(),
                new PaintBrush(plugin.bundle())
        );
        this.plugin = plugin;
    }

    @Override
    public PlayerBrushSettings getBrush(Player player) {
        return playerBrushes.computeIfAbsent(player.getUniqueId(), ignored -> new CraftPlayerBrushSettings(plugin));
    }

    @Override
    @SuppressWarnings("removal")
    public ItemBrushSettings parseBrushSettings(Brush brush, ItemMeta itemMeta) {
        return CraftItemBrushSettings.parse(brush, itemMeta);
    }

    @Override
    public String getBrushLore(Brush brush) {
        return brushes.stream().map(current -> {
            if (current.equals(brush)) {
                return "§e" + current.getName() + "\n";
            } else {
                return "§8" + current.getName() + "\n";
            }
        }).collect(Collectors.joining());
    }

    @Override
    public Optional<Brush> getBrushHandler(String name) {
        return brushes.stream()
                .filter(brush -> name.contains(brush.getName()))
                .findAny();
    }

    @Override
    public void removeBrush(Player player) {
        playerBrushes.remove(player.getUniqueId());
    }

    @Override
    public Brush cycleForward(@Nullable Brush brush) {
        if (brush == null) {
            return brushes.getFirst();
        }
        int next = brushes.indexOf(brush) + 1;
        if (next < brushes.size()) {
            return brushes.get(next);
        }
        return brushes.getFirst();
    }

    @Override
    public Brush cycleBackward(@Nullable Brush brush) {
        if (brush == null) {
            return brushes.getFirst();
        }
        int back = brushes.indexOf(brush) - 1;
        if (back >= 0) {
            return brushes.get(back);
        }
        return brushes.getLast();
    }
}
