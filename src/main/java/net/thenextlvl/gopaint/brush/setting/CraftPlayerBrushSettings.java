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

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.thenextlvl.gopaint.GoPaintPlugin;
import net.thenextlvl.gopaint.api.brush.Brush;
import net.thenextlvl.gopaint.api.brush.setting.PlayerBrushSettings;
import net.thenextlvl.gopaint.api.model.SurfaceMode;
import net.thenextlvl.gopaint.brush.*;
import net.thenextlvl.gopaint.util.GUI;
import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Getter
public final class CraftPlayerBrushSettings implements PlayerBrushSettings {
    private static final Random random = new Random();

    private final GoPaintPlugin plugin;

    private boolean maskEnabled;
    private boolean enabled;
    private int size;
    private int chance;
    private int thickness;
    private int fractureDistance;
    private int angleDistance;
    private int falloffStrength;
    private int mixingStrength;
    private double angleHeightDifference;
    private Axis axis;
    private SurfaceMode surfaceMode;

    private @Setter Brush brush;
    private Material mask;
    private final List<Material> blocks = new ArrayList<>();

    private final Inventory inventory;

    public CraftPlayerBrushSettings(GoPaintPlugin plugin) {
        this.plugin = plugin;

        surfaceMode = plugin.config().generic().surfaceMode();
        maskEnabled = plugin.config().generic().maskEnabled();
        enabled = plugin.config().generic().enabledByDefault();
        chance = plugin.config().generic().defaultChance();
        thickness = plugin.config().thickness().defaultThickness();
        fractureDistance = plugin.config().fracture().defaultFractureDistance();
        angleDistance = plugin.config().angle().defaultAngleDistance();
        angleHeightDifference = plugin.config().angle().defaultAngleHeightDifference();
        falloffStrength = plugin.config().generic().defaultFalloffStrength();
        mixingStrength = plugin.config().generic().defaultMixingStrength();
        axis = plugin.config().generic().defaultAxis();
        size = plugin.config().generic().defaultSize();
        mask = plugin.config().generic().defaultMask();
        brush = plugin.brushController().cycleForward(null);
        blocks.add(Material.STONE);
        inventory = GUI.create(this);
    }

    @Override
    public Random getRandom() {
        return random;
    }

    public Material getRandomBlock() {
        return getBlocks().get(random.nextInt(getBlocks().size()));
    }

    public void updateInventory() {
        GUI.update(inventory, this);
    }

    public void increaseFalloffStrength() {
        if (falloffStrength > 90) return;
        falloffStrength += 10;
        updateInventory();
    }

    public void decreaseFalloffStrength() {
        if (falloffStrength < 10) return;
        falloffStrength -= 10;
        updateInventory();
    }

    public void increaseMixingStrength() {
        if (mixingStrength > 90) return;
        mixingStrength += 10;
        updateInventory();
    }

    public void decreaseMixingStrength() {
        if (mixingStrength < 10) return;
        mixingStrength -= 10;
        updateInventory();
    }

    public void setMask(Material type) {
        mask = type;
        updateInventory();
    }

    public void addBlock(Material type, int slot) {
        if (blocks.size() < slot) blocks.add(type);
        else blocks.set(slot - 1, type);
        updateInventory();
    }

    public void removeBlock(int slot) {
        if (blocks.size() < slot) return;
        blocks.remove(slot - 1);
        updateInventory();
    }

    public void cycleBrushForward() {
        brush = plugin.brushController().cycleForward(brush);
        updateInventory();
    }

    public void cycleBrushBackward() {
        brush = plugin.brushController().cycleBackward(brush);
        updateInventory();
    }

    public void setSize(int size) {
        this.size = Math.clamp(size, 1, plugin.config().generic().maxSize());
        updateInventory();
    }

    public void increaseBrushSize(int amount) {
        size = Math.min(plugin.config().generic().maxSize(), size + amount);
        updateInventory();
    }

    public void decreaseBrushSize(int amount) {
        size = Math.max(1, size - amount);
        updateInventory();
    }

    public boolean toggle() {
        enabled = !enabled;
        updateInventory();
        return enabled;
    }

    public void increaseChance() {
        if (chance >= 90) return;
        chance += 10;
        updateInventory();
    }

    public void decreaseChance() {
        if (chance <= 10) return;
        chance -= 10;
        updateInventory();
    }

    public void increaseThickness() {
        if (thickness >= plugin.config().thickness().maxThickness()) return;
        thickness += 1;
        updateInventory();
    }

    public void decreaseThickness() {
        if (thickness <= 1) return;
        thickness -= 1;
        updateInventory();
    }

    public void increaseAngleDistance() {
        if (angleDistance >= plugin.config().angle().maxAngleDistance()) return;
        angleDistance += 1;
        updateInventory();
    }

    public void decreaseAngleDistance() {
        if (angleDistance <= 1) return;
        angleDistance -= 1;
        updateInventory();
    }

    public void increaseFractureDistance() {
        if (this.fractureDistance >= plugin.config().fracture().maxFractureDistance()) return;
        this.fractureDistance += 1;
        updateInventory();
    }

    public void decreaseFractureDistance() {
        if (this.fractureDistance <= 1) return;
        this.fractureDistance -= 1;
        updateInventory();
    }

    public void increaseAngleHeightDifference(int amount) {
        var max = plugin.config().angle().maxAngleHeightDifference();
        angleHeightDifference = Math.min(max, angleHeightDifference + amount);
        updateInventory();
    }

    public void decreaseAngleHeightDifference(int amount) {
        var min = plugin.config().angle().minAngleHeightDifference();
        angleHeightDifference = Math.max(min, angleHeightDifference - amount);
        updateInventory();
    }

    public void toggleMask() {
        maskEnabled = !maskEnabled;
        updateInventory();
    }

    public void cycleSurfaceMode() {
        surfaceMode = switch (surfaceMode) {
            case DIRECT -> SurfaceMode.RELATIVE;
            case RELATIVE -> SurfaceMode.DISABLED;
            case DISABLED -> SurfaceMode.DIRECT;
        };
        updateInventory();
    }

    public void cycleAxis() {
        axis = switch (axis) {
            case X -> Axis.Y;
            case Y -> Axis.Z;
            case Z -> Axis.X;
        };
        updateInventory();
    }

    public void export(ItemStack itemStack) {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("Size: " + size);
        if (getBrush() instanceof SprayBrush) {
            lore.add("Chance: " + getChance() + "%");
        } else if (getBrush() instanceof OverlayBrush || getBrush() instanceof UnderlayBrush) {
            lore.add("Thickness: " + getThickness());
        } else if (getBrush() instanceof DiscBrush) {
            lore.add("Axis: " + getAxis().name());
        } else if (getBrush() instanceof AngleBrush) {
            lore.add("AngleDistance: " + getAngleDistance());
            lore.add("AngleHeightDifference: " + getAngleHeightDifference());
        } else if (getBrush() instanceof SplatterBrush) {
            lore.add("Falloff: " + getFalloffStrength());
        } else if (getBrush() instanceof GradientBrush) {
            lore.add("Mixing: " + getMixingStrength());
            lore.add("Falloff: " + getFalloffStrength());
        } else if (getBrush() instanceof FractureBrush) {
            lore.add("FractureDistance: " + getFractureDistance());
        }
        lore.add("Blocks: " + (getBlocks().isEmpty() ? "none" : getBlocks().stream()
                .map(Material::getKey)
                .map(NamespacedKey::asMinimalString)
                .collect(Collectors.joining(", "))));

        if (isMaskEnabled()) {
            lore.add("Mask: " + getMask().getKey().asMinimalString());
        }
        if (!getSurfaceMode().equals(SurfaceMode.DISABLED)) {
            lore.add("Surface Mode: " + surfaceMode.getName());
        }

        itemStack.editMeta(itemMeta -> {
            itemMeta.displayName(Component.text(" ♦ " + getBrush().getName() + " ♦ ", NamedTextColor.AQUA)
                    .style(Style.style(TextDecoration.ITALIC.withState(false))));
            itemMeta.lore(lore.stream().map(string -> Component.text(string).style(Style
                    .style(TextDecoration.ITALIC.withState(false))
                    .color(NamedTextColor.DARK_GRAY))).toList());
            itemMeta.addEnchant(Enchantment.INFINITY, 1, false);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        });
    }

}
