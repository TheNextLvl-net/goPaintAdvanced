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

import core.paper.gui.AbstractGUI;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.thenextlvl.gopaint.GoPaintPlugin;
import net.thenextlvl.gopaint.api.brush.Brush;
import net.thenextlvl.gopaint.api.brush.setting.PlayerBrushSettings;
import net.thenextlvl.gopaint.api.model.MaskMode;
import net.thenextlvl.gopaint.api.model.SurfaceMode;
import net.thenextlvl.gopaint.brush.standard.*;
import net.thenextlvl.gopaint.menu.BrushesMenu;
import net.thenextlvl.gopaint.menu.MainMenu;
import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Getter
public final class CraftPlayerBrushSettings implements PlayerBrushSettings {
    private static final Random random = new Random();

    private final GoPaintPlugin plugin;
    private final Player player;

    private boolean enabled;
    private int brushSize;
    private int chance;
    private int thickness;
    private int fractureDistance;
    private int angleDistance;
    private int falloffStrength;
    private int mixingStrength;
    private double angleHeightDifference;
    private Axis axis;
    private MaskMode maskMode;
    private SurfaceMode surfaceMode;

    private Brush brush;
    private Material mask;
    private final List<Material> blocks = new ArrayList<>();

    private final MainMenu mainMenu;

    public CraftPlayerBrushSettings(GoPaintPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;

        var defaultBrush = plugin.config().brushConfig().defaultBrush();
        brush = plugin.brushRegistry().getBrush(defaultBrush)
                .orElseThrow(() -> new IllegalArgumentException("Unknown default brush: " + defaultBrush.asString()));

        surfaceMode = plugin.config().brushConfig().surfaceMode();
        maskMode = plugin.config().brushConfig().maskMode();
        enabled = plugin.config().brushConfig().enabledByDefault();
        chance = plugin.config().brushConfig().defaultChance();
        thickness = plugin.config().thicknessConfig().defaultThickness();
        fractureDistance = plugin.config().fractureConfig().defaultFractureDistance();
        angleDistance = plugin.config().angleConfig().defaultAngleDistance();
        angleHeightDifference = plugin.config().angleConfig().defaultAngleHeightDifference();
        falloffStrength = plugin.config().brushConfig().defaultFalloffStrength();
        mixingStrength = plugin.config().brushConfig().defaultMixingStrength();
        axis = plugin.config().brushConfig().defaultAxis();
        brushSize = plugin.config().brushConfig().defaultSize();
        mask = plugin.config().brushConfig().defaultMask();
        blocks.add(Material.STONE);

        mainMenu = new MainMenu(plugin, this, player);
    }

    @Override
    public Random getRandom() {
        return random;
    }

    @Override
    public Material getRandomBlock() {
        return getBlocks().get(random.nextInt(getBlocks().size()));
    }

    @Override
    public void increaseFalloffStrength() {
        if (falloffStrength > 90) return;
        falloffStrength += 10;
        mainMenu.updateFalloffStrength();
    }

    @Override
    public void decreaseFalloffStrength() {
        if (falloffStrength < 10) return;
        falloffStrength -= 10;
        mainMenu.updateFalloffStrength();
    }

    @Override
    public void increaseMixingStrength() {
        if (mixingStrength > 90) return;
        mixingStrength += 10;
        mainMenu.updateMixingStrength();
    }

    @Override
    public void decreaseMixingStrength() {
        if (mixingStrength < 10) return;
        mixingStrength -= 10;
        mainMenu.updateMixingStrength();
    }

    @Override
    public void setMask(Material type) {
        mask = type;
        mainMenu.updateMask();
    }

    @Override
    public void addBlock(Material type, int slot) {
        if (blocks.size() < slot) blocks.add(type);
        else blocks.set(slot - 1, type);
        mainMenu.updateBlockPalette();
    }

    @Override
    public void removeBlock(int slot) {
        if (blocks.size() < slot) return;
        blocks.remove(slot - 1);
        mainMenu.updateBlockPalette();
    }

    @Override
    public void setBrush(Brush brush) {
        this.brush = brush;
        mainMenu.updateBrush();
    }

    @Override
    public void cycleBrushForward() {
        setBrush(cycleForward(brush));
    }

    @Override
    public void cycleBrushBackward() {
        setBrush(cycleBackward(brush));
    }

    @Override
    public void setBrushSize(int size) {
        this.brushSize = Math.clamp(size, 1, plugin.config().brushConfig().maxSize());
        mainMenu.updateSize();
    }

    @Override
    public AbstractGUI getBrushesMenu() {
        return new BrushesMenu(plugin, this, player);
    }

    @Override
    public void increaseBrushSize(int amount) {
        setBrushSize(getBrushSize() + amount);
    }

    @Override
    public void decreaseBrushSize(int amount) {
        setBrushSize(getBrushSize() - amount);
    }

    @Override
    public boolean toggle() {
        enabled = !enabled;
        mainMenu.updateToggle();
        return enabled;
    }

    @Override
    public void increaseChance() {
        if (chance >= 90) return;
        chance += 10;
        mainMenu.updateChance();
    }

    @Override
    public void decreaseChance() {
        if (chance <= 10) return;
        chance -= 10;
        mainMenu.updateChance();
    }

    @Override
    public void increaseThickness() {
        if (thickness >= plugin.config().thicknessConfig().maxThickness()) return;
        thickness += 1;
        mainMenu.updateThickness();
    }

    @Override
    public void decreaseThickness() {
        if (thickness <= 1) return;
        thickness -= 1;
        mainMenu.updateThickness();
    }

    @Override
    public void increaseAngleDistance() {
        if (angleDistance >= plugin.config().angleConfig().maxAngleDistance()) return;
        angleDistance += 1;
        mainMenu.updateAngleSettings();
    }

    @Override
    public void decreaseAngleDistance() {
        if (angleDistance <= 1) return;
        angleDistance -= 1;
        mainMenu.updateAngleSettings();
    }

    @Override
    public void increaseFractureDistance() {
        if (this.fractureDistance >= plugin.config().fractureConfig().maxFractureDistance()) return;
        this.fractureDistance += 1;
        mainMenu.updateFractureSettings();
    }

    @Override
    public void decreaseFractureDistance() {
        if (this.fractureDistance <= 1) return;
        this.fractureDistance -= 1;
        mainMenu.updateFractureSettings();
    }

    @Override
    public void increaseAngleHeightDifference(int amount) {
        var max = plugin.config().angleConfig().maxAngleHeightDifference();
        angleHeightDifference = Math.min(max, angleHeightDifference + amount);
        mainMenu.updateAngleSettings();
    }

    @Override
    public void decreaseAngleHeightDifference(int amount) {
        var min = plugin.config().angleConfig().minAngleHeightDifference();
        angleHeightDifference = Math.max(min, angleHeightDifference - amount);
        mainMenu.updateAngleSettings();
    }

    @Override
    public void cycleMaskMode() {
        maskMode = switch (maskMode) {
            case INTERFACE -> MaskMode.WORLDEDIT;
            case WORLDEDIT -> MaskMode.DISABLED;
            case DISABLED -> MaskMode.INTERFACE;
        };
        mainMenu.updateMaskMode();
    }

    @Override
    public void cycleSurfaceMode() {
        surfaceMode = switch (surfaceMode) {
            case DIRECT -> SurfaceMode.RELATIVE;
            case RELATIVE -> SurfaceMode.DISABLED;
            case DISABLED -> SurfaceMode.DIRECT;
        };
        mainMenu.updateSurfaceMode();
    }

    @Override
    public void cycleAxis() {
        axis = switch (axis) {
            case X -> Axis.Y;
            case Y -> Axis.Z;
            case Z -> Axis.X;
        };
        mainMenu.updateAxis();
    }

    @Override
    public Brush cycleForward(@Nullable Brush brush) {
        var brushes = plugin.brushRegistry().getBrushes().toList();
        if (brush == null) return brushes.getFirst();
        int next = brushes.indexOf(brush) + 1;
        if (next < brushes.size()) return brushes.get(next);
        return brushes.getFirst();
    }

    @Override
    public Brush cycleBackward(@Nullable Brush brush) {
        var brushes = plugin.brushRegistry().getBrushes().toList();
        if (brush == null) return brushes.getFirst();
        int back = brushes.indexOf(brush) - 1;
        if (back >= 0) return brushes.get(back);
        return brushes.getLast();
    }

    @Override
    public void export(ItemStack itemStack) {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("Size: " + brushSize);
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

        if (!getMaskMode().equals(MaskMode.DISABLED)) {
            lore.add("Mask Mode: " + getMaskMode().translationKey());
        }
        if (getMaskMode().equals(MaskMode.INTERFACE)) {
            lore.add("Mask: " + getMask().getKey().asMinimalString());
        }

        if (!getSurfaceMode().equals(SurfaceMode.DISABLED)) {
            lore.add("Surface Mode: " + getSurfaceMode().translationKey());
        }

        itemStack.editMeta(itemMeta -> {
            var diamond = Component.text(" ♦ ");
            itemMeta.displayName(diamond.append(getBrush().getName(player)).append(diamond)
                    .decoration(TextDecoration.ITALIC, false).color(NamedTextColor.AQUA));
            itemMeta.lore(lore.stream().map(string -> Component.text(string)
                            .decoration(TextDecoration.ITALIC, false)
                            .color(NamedTextColor.DARK_GRAY))
                    .toList());
            itemMeta.setEnchantmentGlintOverride(true);
        });
    }

}
