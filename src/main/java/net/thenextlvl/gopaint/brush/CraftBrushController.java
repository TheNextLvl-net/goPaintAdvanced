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

import net.kyori.adventure.key.Key;
import net.thenextlvl.gopaint.GoPaintPlugin;
import net.thenextlvl.gopaint.api.brush.BrushController;
import net.thenextlvl.gopaint.api.brush.setting.ItemBrushSettings;
import net.thenextlvl.gopaint.api.brush.setting.PlayerBrushSettings;
import net.thenextlvl.gopaint.api.model.SurfaceMode;
import net.thenextlvl.gopaint.brush.setting.CraftItemBrushSettings;
import net.thenextlvl.gopaint.brush.setting.CraftPlayerBrushSettings;
import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@NullMarked
public class CraftBrushController implements BrushController {
    private final Map<UUID, PlayerBrushSettings> playerBrushes = new HashMap<>();
    private final GoPaintPlugin plugin;

    public CraftBrushController(GoPaintPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public PlayerBrushSettings getBrushSettings(Player player) {
        return playerBrushes.computeIfAbsent(player.getUniqueId(), ignored ->
                new CraftPlayerBrushSettings(plugin, player));
    }

    @Override
    public Optional<ItemBrushSettings> parseBrushSettings(ItemStack itemStack) {
        var container = itemStack.getPersistentDataContainer();

        var brushSize = container.get(new NamespacedKey("gopaint", "size"), PersistentDataType.INTEGER);
        var maskEnabled = container.get(new NamespacedKey("gopaint", "mask_enabled"), PersistentDataType.BOOLEAN);

        var surfaceMode = Optional.ofNullable(container.get(new NamespacedKey("gopaint", "surface_mode"), PersistentDataType.STRING))
                .map(SurfaceMode::valueOf)
                .orElse(null);

        var brush = Optional.ofNullable(container.get(new NamespacedKey("gopaint", "brush"), PersistentDataType.STRING))
                .map(Key::key)
                .flatMap(plugin.brushRegistry()::getBrush)
                .orElse(null);

        if (brushSize == null || maskEnabled == null || surfaceMode == null || brush == null)
            return Optional.empty();

        var chance = container.getOrDefault(new NamespacedKey("gopaint", "chance"), PersistentDataType.INTEGER, 0);
        var thickness = container.getOrDefault(new NamespacedKey("gopaint", "thickness"), PersistentDataType.INTEGER, 0);
        var fractureStrength = container.getOrDefault(new NamespacedKey("gopaint", "fracture_strength"), PersistentDataType.INTEGER, 0);
        var angleDistance = container.getOrDefault(new NamespacedKey("gopaint", "angle_distance"), PersistentDataType.INTEGER, 0);
        var falloffStrength = container.getOrDefault(new NamespacedKey("gopaint", "falloff_strength"), PersistentDataType.INTEGER, 0);
        var mixingStrength = container.getOrDefault(new NamespacedKey("gopaint", "mixing_strength"), PersistentDataType.INTEGER, 0);

        var angleHeightDifference = container.getOrDefault(new NamespacedKey("gopaint", "angle_height_difference"), PersistentDataType.DOUBLE, 0d);

        var axis = Optional.ofNullable(container.get(new NamespacedKey("gopaint", "axis"), PersistentDataType.STRING))
                .map(Axis::valueOf)
                .orElse(Axis.Y);
        var mask = Optional.ofNullable(container.get(new NamespacedKey("gopaint", "mask"), PersistentDataType.STRING))
                .map(Material::matchMaterial)
                .orElseThrow();
        var blocks = Optional.ofNullable(container.get(new NamespacedKey("gopaint", "blocks"), PersistentDataType.STRING))
                .map(string -> string.split(","))
                .stream()
                .flatMap(Arrays::stream)
                .map(Material::matchMaterial)
                .filter(Objects::nonNull)
                .toList();

        return Optional.of(CraftItemBrushSettings.builder()
                .brushSize(brushSize)
                .maskEnabled(maskEnabled)
                .surfaceMode(surfaceMode)
                .brush(brush)
                .chance(chance)
                .thickness(thickness)
                .fractureStrength(fractureStrength)
                .angleDistance(angleDistance)
                .falloffStrength(falloffStrength)
                .mixingStrength(mixingStrength)
                .angleHeightDifference(angleHeightDifference)
                .axis(axis)
                .mask(mask)
                .blocks(blocks).build());
    }

    @Override
    public void removeBrushSettings(Player player) {
        playerBrushes.remove(player.getUniqueId());
    }
}
