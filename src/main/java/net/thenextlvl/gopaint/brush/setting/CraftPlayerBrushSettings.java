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
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.gopaint.GoPaintPlugin;
import net.thenextlvl.gopaint.api.brush.PatternBrush;
import net.thenextlvl.gopaint.api.brush.setting.ItemBrushSettings;
import net.thenextlvl.gopaint.api.brush.setting.PlayerBrushSettings;
import net.thenextlvl.gopaint.api.model.SurfaceMode;
import net.thenextlvl.gopaint.brush.standard.AngleBrush;
import net.thenextlvl.gopaint.brush.standard.DiskBrush;
import net.thenextlvl.gopaint.brush.standard.FractureBrush;
import net.thenextlvl.gopaint.brush.standard.GradientBrush;
import net.thenextlvl.gopaint.brush.standard.OverlayBrush;
import net.thenextlvl.gopaint.brush.standard.PaintBrush;
import net.thenextlvl.gopaint.brush.standard.SplatterBrush;
import net.thenextlvl.gopaint.brush.standard.SprayBrush;
import net.thenextlvl.gopaint.brush.standard.UnderlayBrush;
import net.thenextlvl.gopaint.menu.BrushesMenu;
import net.thenextlvl.gopaint.menu.MainMenu;
import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@NullMarked
public final class CraftPlayerBrushSettings implements PlayerBrushSettings {
    private static final Random random = new Random();

    private final GoPaintPlugin plugin;
    private final Player player;

    private boolean enabled;
    private int brushSize;
    private int chance;
    private int thickness;
    private int fractureStrength;
    private int angleDistance;
    private int falloffStrength;
    private int mixingStrength;
    private double angleHeightDifference;
    private Axis axis;
    private boolean maskEnabled;
    private SurfaceMode surfaceMode;

    private PatternBrush brush;
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
        maskEnabled = plugin.config().brushConfig().mask();
        enabled = plugin.config().brushConfig().enabledByDefault();
        chance = plugin.config().brushConfig().defaultChance();
        thickness = plugin.config().thicknessConfig().defaultThickness();
        fractureStrength = plugin.config().fractureConfig().defaultFractureStrength();
        angleDistance = plugin.config().angleConfig().defaultAngleDistance();
        angleHeightDifference = plugin.config().angleConfig().defaultAngleHeightDifference();
        falloffStrength = plugin.config().brushConfig().defaultFalloffStrength();
        mixingStrength = plugin.config().brushConfig().defaultMixingStrength();
        axis = plugin.config().brushConfig().defaultAxis();
        brushSize = plugin.config().brushConfig().defaultSize();
        mask = plugin.config().brushConfig().defaultMask();
        blocks.addAll(plugin.config().brushConfig().defaultBlocks());

        mainMenu = new MainMenu(plugin, this, player);
    }

    @Override
    public Random getRandom() {
        return random;
    }

    @Override
    public void setMask(Material type) {
        mask = type;
        mainMenu.updateMask();
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        mainMenu.updateToggle();
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
    public void setBrush(PatternBrush brush) {
        this.brush = brush;
        mainMenu.updateBrush();
    }

    @Override
    public void setBrushSize(@Range(from = 1, to = Integer.MAX_VALUE) int size) {
        this.brushSize = Math.clamp(size, 1, plugin.config().brushConfig().maxBrushSize());
        mainMenu.updateSize();
    }

    @Override
    public AbstractGUI getBrushesMenu() {
        return new BrushesMenu(plugin, this, player);
    }

    @Override
    public void setChance(@Range(from = 10, to = 90) int chance) {
        this.chance = Math.clamp(chance, 10, 90);
        mainMenu.updateChance();
    }

    @Override
    public void setThickness(@Range(from = 1, to = Integer.MAX_VALUE) int thickness) {
        this.thickness = Math.clamp(thickness, 1, plugin.config().thicknessConfig().maxThickness());
        mainMenu.updateThickness();
    }

    @Override
    public void setAngleDistance(@Range(from = 1, to = Integer.MAX_VALUE) int distance) {
        this.angleDistance = Math.clamp(distance, 1, plugin.config().angleConfig().maxAngleDistance());
        mainMenu.updateAngleSettings();
    }

    @Override
    public void setFalloffStrength(@Range(from = 0, to = 100) int strength) {
        this.falloffStrength = Math.clamp(strength, 0, 100);
        mainMenu.updateFalloffStrength();
    }

    @Override
    public void setMixingStrength(@Range(from = 0, to = 100) int strength) {
        this.mixingStrength = Math.clamp(strength, 0, 100);
        mainMenu.updateMixingStrength();
    }

    @Override
    public void setAngleHeightDifference(double difference) {
        this.angleHeightDifference = Math.clamp(difference,
                plugin.config().angleConfig().minAngleHeightDifference(),
                plugin.config().angleConfig().maxAngleHeightDifference());
        mainMenu.updateAngleSettings();
    }

    @Override
    public void setMaskEnabled(boolean maskEnabled) {
        this.maskEnabled = maskEnabled;
        mainMenu.updateMaskToggle();
    }

    @Override
    public void setSurfaceMode(SurfaceMode surfaceMode) {
        this.surfaceMode = surfaceMode;
        mainMenu.updateSurfaceMode();
    }

    @Override
    public void setAxis(Axis axis) {
        this.axis = axis;
        mainMenu.updateAxis();
    }

    @Override
    public void setBlocks(List<Material> blocks) {
        this.blocks.clear();
        this.blocks.addAll(blocks);
        mainMenu.updateBlockPalette();
    }

    @Override
    public void setFractureStrength(@Range(from = 1, to = Integer.MAX_VALUE) int fractureStrength) {
        this.fractureStrength = Math.clamp(fractureStrength, 1, plugin.config().fractureConfig().maxFractureStrength());
        mainMenu.updateFractureSettings();
    }

    @Override
    public PatternBrush getNextBrush(@Nullable PatternBrush brush) {
        var brushes = plugin.brushRegistry().getBrushes().toList();
        if (brush == null) return brushes.getFirst();
        int next = brushes.indexOf(brush) + 1;
        if (next < brushes.size()) return brushes.get(next);
        return brushes.getFirst();
    }

    @Override
    public PatternBrush getPreviousBrush(@Nullable PatternBrush brush) {
        var brushes = plugin.brushRegistry().getBrushes().toList();
        if (brush == null) return brushes.getFirst();
        int back = brushes.indexOf(brush) - 1;
        if (back >= 0) return brushes.get(back);
        return brushes.getLast();
    }

    @Override
    public boolean exportSettings(ItemStack itemStack) {
        if (itemStack.getType().equals(plugin.config().brushConfig().defaultBrushType())) return false;
        if (itemStack.getType().isBlock()) return false;

        var lines = new ArrayList<Component>();
        lines.add(Component.empty());
        lines.add(plugin.bundle().component("brush.exported.size", player,
                Formatter.number("size", getBrushSize())));
        if (getBrush() instanceof SprayBrush) {
            lines.add(plugin.bundle().component("brush.exported.chance", player,
                    Formatter.number("chance", getChance())));
        } else if (getBrush() instanceof OverlayBrush || getBrush() instanceof UnderlayBrush) {
            lines.add(plugin.bundle().component("brush.exported.thickness", player,
                    Formatter.number("thickness", getThickness())));
        } else if (getBrush() instanceof DiskBrush) {
            lines.add(plugin.bundle().component("brush.exported.axis", player,
                    Placeholder.parsed("axis", getAxis().name())));
        } else if (getBrush() instanceof AngleBrush) {
            lines.add(plugin.bundle().component("brush.exported.angle.distance", player,
                    Formatter.number("distance", getAngleDistance())));
            lines.add(plugin.bundle().component("brush.exported.angle.height", player,
                    Formatter.number("height", getAngleHeightDifference())));
        } else if (getBrush() instanceof SplatterBrush || getBrush() instanceof PaintBrush) {
            lines.add(plugin.bundle().component("brush.exported.falloff", player,
                    Formatter.number("falloff", getFalloffStrength())));
        } else if (getBrush() instanceof GradientBrush) {
            lines.add(plugin.bundle().component("brush.exported.mixing", player,
                    Formatter.number("mixing", getMixingStrength())));
            lines.add(plugin.bundle().component("brush.exported.falloff", player,
                    Formatter.number("falloff", getFalloffStrength())));
        } else if (getBrush() instanceof FractureBrush) {
            lines.add(plugin.bundle().component("brush.exported.fracture", player,
                    Formatter.number("fracture", getFractureStrength())));
        }
        if (!blocks.isEmpty()) {
            var blocks = getBlocks().stream()
                    .map(Material::translationKey)
                    .map(Component::translatable)
                    .toList();
            lines.add(plugin.bundle().component("brush.exported.blocks", player,
                    Placeholder.component("blocks", Component.join(JoinConfiguration.commas(true), blocks))));
        }

        if (isMaskEnabled()) {
            lines.add(plugin.bundle().component("brush.exported.mask", player,
                    Placeholder.component("mask", Component.translatable(getMask().translationKey()))));
        }

        if (!getSurfaceMode().equals(SurfaceMode.DISABLED)) {
            var mode = plugin.bundle().component(getSurfaceMode().translationKey(), player);
            lines.add(plugin.bundle().component("brush.exported.surface-mode", player,
                    Placeholder.component("mode", mode.style(Style.empty()))));
        }

        itemStack.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
        itemStack.setData(DataComponentTypes.ITEM_NAME, plugin.bundle().component("brush.exported.name", player,
                Placeholder.component("brush", getBrush().getName(player))));
        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(lines));

        return itemStack.editPersistentDataContainer(container -> {
            container.set(new NamespacedKey("gopaint", "size"), PersistentDataType.INTEGER, getBrushSize());
            container.set(new NamespacedKey("gopaint", "chance"), PersistentDataType.INTEGER, getChance());
            container.set(new NamespacedKey("gopaint", "thickness"), PersistentDataType.INTEGER, getThickness());
            container.set(new NamespacedKey("gopaint", "fracture_strength"), PersistentDataType.INTEGER, getFractureStrength());
            container.set(new NamespacedKey("gopaint", "angle_distance"), PersistentDataType.INTEGER, getAngleDistance());
            container.set(new NamespacedKey("gopaint", "falloff_strength"), PersistentDataType.INTEGER, getFalloffStrength());
            container.set(new NamespacedKey("gopaint", "mixing_strength"), PersistentDataType.INTEGER, getMixingStrength());
            container.set(new NamespacedKey("gopaint", "angle_height_difference"), PersistentDataType.DOUBLE, getAngleHeightDifference());
            container.set(new NamespacedKey("gopaint", "axis"), PersistentDataType.STRING, getAxis().name());
            container.set(new NamespacedKey("gopaint", "mask_enabled"), PersistentDataType.BOOLEAN, isMaskEnabled());
            container.set(new NamespacedKey("gopaint", "surface_mode"), PersistentDataType.STRING, getSurfaceMode().name());
            container.set(new NamespacedKey("gopaint", "brush"), PersistentDataType.STRING, getBrush().key().asString());
            container.set(new NamespacedKey("gopaint", "mask"), PersistentDataType.STRING, getMask().key().asString());
            container.set(new NamespacedKey("gopaint", "blocks"), PersistentDataType.STRING, getBlocks().stream()
                    .map(Material::key)
                    .map(Key::asString)
                    .collect(Collectors.joining(",")));
        });
    }

    @Override
    public void importSettings(ItemBrushSettings settings) {
        setBrushSize(settings.getBrushSize());
        setChance(settings.getBrushSize());
        setThickness(settings.getThickness());
        setFractureStrength(settings.getFractureStrength());
        setAngleDistance(settings.getAngleDistance());
        setFalloffStrength(settings.getFalloffStrength());
        setMixingStrength(settings.getMixingStrength());
        setAngleHeightDifference(settings.getAngleHeightDifference());
        setAxis(settings.getAxis());
        setMaskEnabled(settings.isMaskEnabled());
        setSurfaceMode(settings.getSurfaceMode());
        setBrush(settings.getBrush());
        setMask(settings.getMask());
        setBlocks(settings.getBlocks());
    }

    public GoPaintPlugin getPlugin() {
        return this.plugin;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public int getBrushSize() {
        return this.brushSize;
    }

    public int getChance() {
        return this.chance;
    }

    public int getThickness() {
        return this.thickness;
    }

    public int getFractureStrength() {
        return this.fractureStrength;
    }

    public int getAngleDistance() {
        return this.angleDistance;
    }

    public int getFalloffStrength() {
        return this.falloffStrength;
    }

    public int getMixingStrength() {
        return this.mixingStrength;
    }

    public double getAngleHeightDifference() {
        return this.angleHeightDifference;
    }

    public Axis getAxis() {
        return this.axis;
    }

    public boolean isMaskEnabled() {
        return this.maskEnabled;
    }

    public SurfaceMode getSurfaceMode() {
        return this.surfaceMode;
    }

    public PatternBrush getBrush() {
        return this.brush;
    }

    public Material getMask() {
        return this.mask;
    }

    public List<Material> getBlocks() {
        return this.blocks;
    }

    public MainMenu getMainMenu() {
        return this.mainMenu;
    }
}
