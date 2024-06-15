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
import core.i18n.file.ComponentBundle;
import lombok.Getter;
import net.thenextlvl.gopaint.objects.brush.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The PlayerBrushManager class manages the brush selection for each player.
 */
public class PlayerBrushManager {
    private final HashMap<UUID, PlayerBrush> playerBrushes = new HashMap<>();
    private final @Getter List<Brush> brushes;

    public PlayerBrushManager(ComponentBundle bundle) {
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
                new PaintBrush(bundle)
        );
    }

    /**
     * Retrieves the brush for the given player.
     *
     * @param player The player for which to retrieve the brush.
     * @return The brush for the specified player.
     */
    public PlayerBrush getBrush(Player player) {
        return playerBrushes.computeIfAbsent(player.getUniqueId(), ignored -> new PlayerBrush(this));
    }

    /**
     * Retrieves the lore for a specific brush. Each brush name is preceded by a color code
     * indicating whether it is the currently selected brush or not.
     *
     * @param brush The brush for which to retrieve the lore.
     * @return The lore for the specified brush.
     */
    public String getBrushLore(Brush brush) {
        return brushes.stream().map(current -> {
            if (current.equals(brush)) {
                return "§e" + current.getName() + "\n";
            } else {
                return "§8" + current.getName() + "\n";
            }
        }).collect(Collectors.joining());
    }

    /**
     * Retrieves the brush handler for the given name.
     *
     * @param name The name of the brush to look for.
     * @return An optional containing the brush handler, or empty if not found.
     */
    public Optional<Brush> getBrushHandler(String name) {
        return brushes.stream()
                .filter(brush -> name.contains(brush.getName()))
                .findAny();
    }

    /**
     * Removes the player from the {@link #playerBrushes} map.
     *
     * @param player The player who should be removed.
     */
    public void removeBrush(Player player) {
        playerBrushes.remove(player.getUniqueId());
    }

    /**
     * Retrieves the next brush in the list of available brushes.
     *
     * @param brush The current brush, if null returns the first brush in the list.
     * @return The next brush in the list, or the first brush if the current brush is null.
     */
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

    /**
     * Retrieves the previous brush in the list of available brushes.
     *
     * @param brush The current brush.
     * @return The previous brush in the list, or the first brush if the current brush is null.
     */
    public Brush cycleBack(@Nullable Brush brush) {
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
