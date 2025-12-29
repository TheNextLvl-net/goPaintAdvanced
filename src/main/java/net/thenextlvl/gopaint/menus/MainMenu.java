package net.thenextlvl.gopaint.menus;

import core.paper.gui.AbstractGUI;
import core.paper.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.gopaint.GoPaintPlugin;
import net.thenextlvl.gopaint.api.brush.setting.PlayerBrushSettings;
import net.thenextlvl.gopaint.brush.standard.AngleBrush;
import net.thenextlvl.gopaint.brush.standard.DiskBrush;
import net.thenextlvl.gopaint.brush.standard.FractureBrush;
import net.thenextlvl.gopaint.brush.standard.GradientBrush;
import net.thenextlvl.gopaint.brush.standard.OverlayBrush;
import net.thenextlvl.gopaint.brush.standard.PaintBrush;
import net.thenextlvl.gopaint.brush.standard.SplatterBrush;
import net.thenextlvl.gopaint.brush.standard.SprayBrush;
import net.thenextlvl.gopaint.brush.standard.UnderlayBrush;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

@NullMarked
public final class MainMenu extends AbstractGUI {
    private final PlayerBrushSettings settings;
    private final GoPaintPlugin plugin;

    private final Inventory inventory;

    public MainMenu(GoPaintPlugin plugin, PlayerBrushSettings settings, Player owner) {
        super(owner, plugin.bundle().component("menu.main.title", owner));
        this.inventory = plugin.getServer().createInventory(this, 6 * 9, title());
        this.settings = settings;
        this.plugin = plugin;

        formatDefault();
        updateToggle();
        updateBrush();
        updateSize();
        updateMaskToggle();
        updateSurfaceMode();
        updateBlockPalette();
        updateMask();
    }

    public void updateBrush() {
        resetSettingSlots();
        updateBrushSelection();
        updateChance();
        updateAxis();
        updateThickness();
        updateAngleSettings();
        updateFractureSettings();
        updateMixingStrength();
        updateFalloffStrength();
    }

    private void resetSettingSlots() {
        var placeholder = ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).hideTooltip().item();
        IntStream.of(3, 4, 12, 13, 21, 22).forEach(value -> inventory.setItem(value, placeholder));
    }

    public void updateMask() {
        inventory.setItem(52, ItemBuilder.of(settings.getMask())
                .itemName(plugin.bundle().component("mask.block", owner))
                .lore(Component.empty(), plugin.bundle().component("mask.block.description", owner))
                .item());
    }

    public void updateBlockPalette() {

        var placeholder = ItemBuilder.of(Material.BARRIER)
                .itemName(plugin.bundle().component("slot.empty", owner))
                .lore(Component.empty(), plugin.bundle().component("slot.empty.description", owner))
                .item();
        IntStream.rangeClosed(46, 50)
                .filter(value -> settings.getBlocks().size() <= value - 46)
                .forEach(value -> inventory.setItem(value, placeholder));

        if (settings.getBlocks().isEmpty()) return;

        var chance = 100d / settings.getBlocks().size();

        for (var i = 0; i < settings.getBlocks().size(); i++) {
            inventory.setItem(i + 46, ItemBuilder.of(settings.getBlocks().get(i))
                    .amount(chance > 64 ? 1 : (int) chance)
                    .itemName(plugin.bundle().component("slot.set", owner,
                            Formatter.number("slot", i + 1),
                            Formatter.number("chance", chance)))
                    .lore(Component.empty(),
                            plugin.bundle().component("slot.set.description.left", owner),
                            plugin.bundle().component("slot.set.description.right", owner))
                    .item());
        }
    }

    public void updateSize() {
        inventory.setItem(14, ItemBuilder.of(Material.BROWN_MUSHROOM)
                .itemName(plugin.bundle().component("brush.size", owner,
                        Formatter.number("size", settings.getBrushSize())))
                .lore(Component.empty(),
                        plugin.bundle().component("brush.size.description.left", owner),
                        plugin.bundle().component("brush.size.description.right", owner),
                        plugin.bundle().component("brush.size.description.shift", owner))
                .item());
    }

    public void updateFalloffStrength() {
        if (!(settings.getBrush() instanceof SplatterBrush)
            && !(settings.getBrush() instanceof PaintBrush)
            && !(settings.getBrush() instanceof GradientBrush))
            return;

        inventory.setItem(12, ItemBuilder.of(Material.BLAZE_POWDER)
                .itemName(plugin.bundle().component("brush.falloff", owner,
                        Formatter.number("strength", settings.getFalloffStrength())))
                .lore(Component.empty(),
                        plugin.bundle().component("brush.falloff.description.left", owner),
                        plugin.bundle().component("brush.falloff.description.right", owner))
                .item());

        var placeholder = ItemBuilder.of(Material.WHITE_STAINED_GLASS_PANE).hideTooltip().item();
        inventory.setItem(3, placeholder);
        inventory.setItem(21, placeholder);
    }

    public void updateMixingStrength() {
        if (!(settings.getBrush() instanceof GradientBrush)) return;

        inventory.setItem(13, ItemBuilder.of(Material.MAGMA_CREAM)
                .itemName(plugin.bundle().component("brush.mixing", owner,
                        Formatter.number("strength", settings.getMixingStrength())))
                .lore(Component.empty(),
                        plugin.bundle().component("brush.mixing.description.left", owner),
                        plugin.bundle().component("brush.mixing.description.right", owner))
                .item());

        var placeholder = ItemBuilder.of(Material.WHITE_STAINED_GLASS_PANE).hideTooltip().item();
        inventory.setItem(4, placeholder);
        inventory.setItem(22, placeholder);
    }

    public void updateFractureSettings() {
        if (!(settings.getBrush() instanceof FractureBrush)) return;

        inventory.setItem(12, ItemBuilder.of(Material.DAYLIGHT_DETECTOR)
                .itemName(plugin.bundle().component("brush.fracture", owner,
                        Formatter.number("distance", settings.getFractureStrength())))
                .lore(Component.empty(),
                        plugin.bundle().component("brush.fracture.description.left", owner),
                        plugin.bundle().component("brush.fracture.description.right", owner))
                .item());

        var placeholder = ItemBuilder.of(Material.WHITE_STAINED_GLASS_PANE).hideTooltip().item();
        inventory.setItem(3, placeholder);
        inventory.setItem(21, placeholder);
    }

    public void updateAngleSettings() {
        if (!(settings.getBrush() instanceof AngleBrush)) return;

        inventory.setItem(12, ItemBuilder.of(Material.DAYLIGHT_DETECTOR)
                .itemName(plugin.bundle().component("brush.angle.distance", owner,
                        Formatter.number("distance", settings.getAngleDistance())))
                .lore(Component.empty(),
                        plugin.bundle().component("brush.angle.distance.description.left", owner),
                        plugin.bundle().component("brush.angle.distance.description.right", owner))
                .item());
        inventory.setItem(13, ItemBuilder.of(Material.BLAZE_ROD)
                .itemName(plugin.bundle().component("brush.angle.maximum", owner,
                        Formatter.number("angle", settings.getAngleHeightDifference())))
                .lore(Component.empty(),
                        plugin.bundle().component("brush.angle.maximum.description.left", owner),
                        plugin.bundle().component("brush.angle.maximum.description.right", owner),
                        plugin.bundle().component("brush.angle.maximum.description.shift", owner))
                .item());

        var placeholder = ItemBuilder.of(Material.WHITE_STAINED_GLASS_PANE).hideTooltip().item();
        inventory.setItem(3, placeholder);
        inventory.setItem(4, placeholder);
        inventory.setItem(22, placeholder);
        inventory.setItem(21, placeholder);
    }

    public void updateThickness() {
        if (!(settings.getBrush() instanceof OverlayBrush)
            && !(settings.getBrush() instanceof UnderlayBrush))
            return;

        inventory.setItem(12, ItemBuilder.of(Material.BOOK)
                .itemName(plugin.bundle().component("brush.thickness", owner,
                        Formatter.number("thickness", settings.getThickness())))
                .lore(Component.empty(),
                        plugin.bundle().component("brush.thickness.description.left", owner),
                        plugin.bundle().component("brush.thickness.description.right", owner))
                .item());

        var placeholder = ItemBuilder.of(Material.WHITE_STAINED_GLASS_PANE).hideTooltip().item();
        inventory.setItem(3, placeholder);
        inventory.setItem(21, placeholder);
    }

    public void updateAxis() {
        if (!(settings.getBrush() instanceof DiskBrush)) return;

        inventory.setItem(12, ItemBuilder.of(Material.COMPASS)
                .itemName(plugin.bundle().component("brush.axis", owner,
                        Placeholder.parsed("axis", settings.getAxis().name())))
                .lore(Component.empty(), plugin.bundle().component("brush.axis.description", owner))
                .item());

        var placeholder = ItemBuilder.of(Material.WHITE_STAINED_GLASS_PANE).hideTooltip().item();
        inventory.setItem(3, placeholder);
        inventory.setItem(21, placeholder);
    }

    public void updateChance() {
        if (!(settings.getBrush() instanceof SprayBrush)) return;

        inventory.setItem(12, ItemBuilder.of(Material.GOLD_NUGGET)
                .itemName(plugin.bundle().component("brush.chance", owner,
                        Formatter.number("chance", settings.getChance())))
                .lore(Component.empty(),
                        plugin.bundle().component("brush.chance.description.left", owner),
                        plugin.bundle().component("brush.chance.description.right", owner))
                .item());

        var placeholder = ItemBuilder.of(Material.WHITE_STAINED_GLASS_PANE).hideTooltip().item();
        inventory.setItem(3, placeholder);
        inventory.setItem(21, placeholder);
    }

    public void updateBrushSelection() {
        var brush = settings.getBrush();

        var lore = new ArrayList<>(Arrays.asList(
                Component.empty(),
                plugin.bundle().component("brush.selection.description.1", owner),
                plugin.bundle().component("brush.selection.description.2", owner),
                Component.empty()
        ));

        plugin.brushRegistry().getBrushes().map(current -> {
            var color = current.equals(brush) ? NamedTextColor.YELLOW : NamedTextColor.DARK_GRAY;
            return current.getName(owner).decoration(TextDecoration.ITALIC, false).color(color);
        }).forEach(lore::addLast);

        inventory.setItem(11, ItemBuilder.of(Material.PLAYER_HEAD)
                .profileValue(brush.getHeadValue())
                .itemName(plugin.bundle().component("brush.selection", owner))
                .lore(lore)
                .item());
    }

    public void updateToggle() {
        inventory.setItem(10, ItemBuilder.of(plugin.config().brushConfig().defaultBrushType())
                .itemName(plugin.bundle().component("brush.toggle", owner))
                .lore(plugin.bundle().component(settings.isEnabled() ? "brush.state.enabled" : "brush.state.disabled", owner),
                        Component.empty(),
                        plugin.bundle().component("brush.toggle.description.1", owner),
                        plugin.bundle().component("brush.toggle.description.2", owner),
                        plugin.bundle().component("brush.toggle.description.3", owner))
                .item());

        var placeholder = ItemBuilder.of(settings.isEnabled()
                ? Material.LIME_STAINED_GLASS_PANE
                : Material.RED_STAINED_GLASS_PANE
        ).hideTooltip().item();

        inventory.setItem(1, placeholder);
        inventory.setItem(19, placeholder);
    }

    public void updateMaskToggle() {
        var icon = settings.isMaskEnabled() ? Material.JACK_O_LANTERN : Material.CARVED_PUMPKIN;

        inventory.setItem(15, ItemBuilder.of(icon)
                .itemName(plugin.bundle().component("mask.state", owner))
                .lore(plugin.bundle().component(settings.isMaskEnabled() ? "mask.enabled" : "mask.disabled", owner),
                        Component.empty(), plugin.bundle().component("click.cycle", owner))
                .item());

        var placeholder = ItemBuilder.of(settings.isMaskEnabled()
                ? Material.LIME_STAINED_GLASS_PANE
                : Material.RED_STAINED_GLASS_PANE
        ).hideTooltip().item();

        inventory.setItem(6, placeholder);
        inventory.setItem(24, placeholder);
    }

    public void updateSurfaceMode() {
        var icon = switch (settings.getSurfaceMode()) {
            case EXPOSED -> Material.LIGHT_WEIGHTED_PRESSURE_PLATE;
            case DISABLED -> Material.POLISHED_BLACKSTONE_PRESSURE_PLATE;
            case VISIBLE -> Material.HEAVY_WEIGHTED_PRESSURE_PLATE;
        };

        inventory.setItem(16, ItemBuilder.of(icon)
                .itemName(plugin.bundle().component("surface.mode", owner))
                .lore(plugin.bundle().component(settings.getSurfaceMode().translationKey(), owner),
                        Component.empty(), plugin.bundle().component("surface.mode.description", owner))
                .item());

        var placeholder = ItemBuilder.of(switch (settings.getSurfaceMode()) {
            case EXPOSED -> Material.LIME_STAINED_GLASS_PANE;
            case DISABLED -> Material.RED_STAINED_GLASS_PANE;
            case VISIBLE -> Material.ORANGE_STAINED_GLASS_PANE;
        }).hideTooltip().item();

        inventory.setItem(7, placeholder);
        inventory.setItem(25, placeholder);
    }

    private void formatDefault() {
        var grayGlassPane = ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).hideTooltip().item();
        var yellowGlassPane = ItemBuilder.of(Material.YELLOW_STAINED_GLASS_PANE).hideTooltip().item();
        var whiteGlassPane = ItemBuilder.of(Material.WHITE_STAINED_GLASS_PANE).hideTooltip().item();
        var orangeGlassPane = ItemBuilder.of(Material.ORANGE_STAINED_GLASS_PANE).hideTooltip().item();

        IntStream.range(0, inventory.getSize()).forEach(value -> inventory.setItem(value, grayGlassPane));

        IntStream.of(43, 37, 38, 39, 40, 41).forEach(value -> inventory.setItem(value, yellowGlassPane));

        inventory.setItem(5, whiteGlassPane);
        inventory.setItem(23, whiteGlassPane);

        inventory.setItem(2, orangeGlassPane);
        inventory.setItem(20, orangeGlassPane);
    }

    public Inventory getInventory() {
        return this.inventory;
    }
}
