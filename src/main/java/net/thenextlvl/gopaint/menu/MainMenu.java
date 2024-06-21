package net.thenextlvl.gopaint.menu;

import core.paper.gui.AbstractGUI;
import core.paper.item.ItemBuilder;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.gopaint.GoPaintPlugin;
import net.thenextlvl.gopaint.api.brush.setting.PlayerBrushSettings;
import net.thenextlvl.gopaint.brush.standard.*;
import net.thenextlvl.gopaint.util.Items;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

public class MainMenu extends AbstractGUI {
    private final PlayerBrushSettings settings;
    private final GoPaintPlugin plugin;

    private final @Getter Inventory inventory;

    public MainMenu(GoPaintPlugin plugin, PlayerBrushSettings settings, Player owner) {
        super(owner, plugin.bundle().component(owner, "menu.main.title"));
        this.inventory = Bukkit.createInventory(this, 6 * 9, title());
        this.settings = settings;
        this.plugin = plugin;

        formatDefault();
        updateToggle();
        updateBrush();
        updateSize();
        updateMaskMode();
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
        var placeholder = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).hideTooltip(true);
        IntStream.of(3, 4, 12, 13, 21, 22).forEach(value -> inventory.setItem(value, placeholder));
    }

    public void updateMask() {
        inventory.setItem(52, new ItemBuilder(settings.getMask())
                .itemName(plugin.bundle().component(owner, "mask.block"))
                .lore(plugin.bundle().components(owner, "mask.block.description")));
    }

    public void updateBlockPalette() {

        var placeholder = new ItemBuilder(Material.BARRIER)
                .itemName(plugin.bundle().component(owner, "slot.empty"))
                .lore(plugin.bundle().components(owner, "slot.empty.description"));
        IntStream.rangeClosed(46, 50)
                .filter(value -> settings.getBlocks().size() <= value - 46)
                .forEach(value -> inventory.setItem(value, placeholder));

        if (settings.getBlocks().isEmpty()) return;

        var chance = 100d / settings.getBlocks().size();
        var formatter = DecimalFormat.getInstance(owner.locale());
        formatter.setMaximumFractionDigits(2);

        for (var i = 0; i < settings.getBlocks().size(); i++) {
            inventory.setItem(i + 46, new ItemBuilder(settings.getBlocks().get(i))
                    .amount(chance > 64 ? 1 : (int) chance)
                    .itemName(plugin.bundle().component(owner, "slot.set",
                            Placeholder.parsed("slot", String.valueOf(i + 1)),
                            Placeholder.parsed("chance", formatter.format(chance))))
                    .lore(plugin.bundle().components(owner, "slot.set.description")));
        }
    }

    public void updateSize() {
        inventory.setItem(14, new ItemBuilder(Material.BROWN_MUSHROOM)
                .itemName(plugin.bundle().component(owner, "brush.size",
                        Placeholder.parsed("size", String.valueOf(settings.getBrushSize()))))
                .lore(plugin.bundle().components(owner, "brush.size.description")));
    }

    public void updateFalloffStrength() {
        if (!(settings.getBrush() instanceof SplatterBrush)
            && !(settings.getBrush() instanceof PaintBrush)
            && !(settings.getBrush() instanceof GradientBrush))
            return;

        inventory.setItem(12, Items.create(Material.BLAZE_POWDER, 1,
                "§6Falloff Strength: §e" + settings.getFalloffStrength() + "%",
                "\n§7Left click to increase\n§7Right click to decrease"
        ));

        var placeholder = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).hideTooltip(true);
        inventory.setItem(3, placeholder);
        inventory.setItem(21, placeholder);
    }

    public void updateMixingStrength() {
        if (!(settings.getBrush() instanceof GradientBrush)) return;

        inventory.setItem(13, Items.create(Material.MAGMA_CREAM, 1,
                "§6Mixing Strength: §e" + settings.getMixingStrength() + "%",
                "\n§7Left click to increase\n§7Right click to decrease"
        ));

        var placeholder = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).hideTooltip(true);
        inventory.setItem(4, placeholder);
        inventory.setItem(22, placeholder);
    }

    public void updateFractureSettings() {
        if (!(settings.getBrush() instanceof FractureBrush)) return;

        inventory.setItem(12, Items.create(Material.DAYLIGHT_DETECTOR, 1,
                "§6Fracture Check Distance: §e" + settings.getFractureDistance(),
                "\n§7Left click to increase\n§7Right click to decrease"
        ));

        var placeholder = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).hideTooltip(true);
        inventory.setItem(3, placeholder);
        inventory.setItem(21, placeholder);
    }

    public void updateAngleSettings() {
        if (!(settings.getBrush() instanceof AngleBrush)) return;

        inventory.setItem(12, Items.create(Material.DAYLIGHT_DETECTOR, 1,
                "§6Angle Check Distance: §e" + settings.getAngleDistance(),
                "\n§7Left click to increase\n§7Right click to decrease"
        ));
        inventory.setItem(13, Items.create(Material.BLAZE_ROD, 1,
                "§6Maximum Angle: §e" + settings.getAngleHeightDifference() + "°",
                "\n§7Left click to increase\n§7Right click to decrease\n§7Shift click to change by 15"
        ));

        var placeholder = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).hideTooltip(true);
        inventory.setItem(3, placeholder);
        inventory.setItem(4, placeholder);
        inventory.setItem(22, placeholder);
        inventory.setItem(21, placeholder);
    }

    public void updateThickness() {
        if (!(settings.getBrush() instanceof OverlayBrush)
            && !(settings.getBrush() instanceof UnderlayBrush))
            return;

        inventory.setItem(12, Items.create(Material.BOOK, 1,
                "§6Layer Thickness: §e" + settings.getThickness(),
                "\n§7Left click to increase\n§7Right click to decrease"
        ));

        var placeholder = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).hideTooltip(true);
        inventory.setItem(3, placeholder);
        inventory.setItem(21, placeholder);
    }

    public void updateAxis() {
        if (!(settings.getBrush() instanceof DiscBrush)) return;

        inventory.setItem(12, Items.create(Material.COMPASS, 1,
                "§6Axis: §e" + settings.getAxis(), "\n§7Click to change"
        ));

        var placeholder = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).hideTooltip(true);
        inventory.setItem(3, placeholder);
        inventory.setItem(21, placeholder);
    }

    public void updateChance() {
        if (!(settings.getBrush() instanceof SprayBrush)) return;

        inventory.setItem(12, new ItemBuilder(Material.GOLD_NUGGET)
                .itemName(plugin.bundle().component(owner, "brush.chance",
                        Placeholder.parsed("chance", String.valueOf(settings.getChance()))))
                .lore(plugin.bundle().components(owner, "brush.chance.description")));

        var placeholder = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).hideTooltip(true);
        inventory.setItem(3, placeholder);
        inventory.setItem(21, placeholder);
    }

    public void updateBrushSelection() {
        var brush = settings.getBrush();

        var lore = new ArrayList<>(Arrays.asList(plugin.bundle().components(owner, "brush.selection.lore")));

        plugin.brushRegistry().getBrushes().map(current -> {
            var color = current.equals(brush) ? NamedTextColor.YELLOW : NamedTextColor.DARK_GRAY;
            return current.getName(owner).decoration(TextDecoration.ITALIC, false).color(color);
        }).forEach(lore::addLast);

        inventory.setItem(11, new ItemBuilder(Material.PLAYER_HEAD)
                .headValue(brush.getHeadValue())
                .itemName(plugin.bundle().component(owner, "brush.selection"))
                .lore(lore.toArray(new Component[]{})));
    }

    public void updateToggle() {
        var state = settings.isEnabled()
                ? plugin.bundle().component(owner, "brush.state.enabled")
                : plugin.bundle().component(owner, "brush.state.disabled");

        inventory.setItem(10, new ItemBuilder(plugin.config().brushConfig().defaultBrushType())
                .itemName(plugin.bundle().component(owner, "brush.toggle"))
                .lore(plugin.bundle().components(owner, "brush.toggle.description",
                        Placeholder.component("state", state))));

        var placeholder = new ItemBuilder(settings.isEnabled()
                ? Material.LIME_STAINED_GLASS_PANE
                : Material.RED_STAINED_GLASS_PANE
        ).hideTooltip(true);

        inventory.setItem(1, placeholder);
        inventory.setItem(19, placeholder);
    }

    public void updateMaskMode() {
        var icon = switch (settings.getMaskMode()) {
            case DISABLED -> Material.CARVED_PUMPKIN;
            case INTERFACE -> Material.JACK_O_LANTERN;
            case WORLDEDIT -> Material.WOODEN_AXE;
        };

        var mode = plugin.bundle().component(owner, settings.getMaskMode().translationKey())
                .color(switch (settings.getMaskMode()) {
                    case DISABLED -> NamedTextColor.RED;
                    case INTERFACE -> NamedTextColor.GREEN;
                    case WORLDEDIT -> NamedTextColor.GOLD;
                });

        inventory.setItem(15, new ItemBuilder(icon)
                .itemName(plugin.bundle().component(owner, "mask.mode"))
                .lore(plugin.bundle().components(owner, "mask.mode.description",
                        Placeholder.component("mode", mode)))
                .itemFlags(ItemFlag.HIDE_ATTRIBUTES));

        var placeholder = new ItemBuilder(switch (settings.getMaskMode()) {
            case DISABLED -> Material.RED_STAINED_GLASS_PANE;
            case INTERFACE -> Material.LIME_STAINED_GLASS_PANE;
            case WORLDEDIT -> Material.ORANGE_STAINED_GLASS_PANE;
        }).hideTooltip(true);

        inventory.setItem(6, placeholder);
        inventory.setItem(24, placeholder);
    }

    public void updateSurfaceMode() {
        var icon = switch (settings.getSurfaceMode()) {
            case DIRECT -> Material.LIGHT_WEIGHTED_PRESSURE_PLATE;
            case DISABLED -> Material.POLISHED_BLACKSTONE_PRESSURE_PLATE;
            case RELATIVE -> Material.HEAVY_WEIGHTED_PRESSURE_PLATE;
        };

        var mode = plugin.bundle().component(owner, settings.getSurfaceMode().translationKey())
                .color(switch (settings.getSurfaceMode()) {
                    case DIRECT -> NamedTextColor.GREEN;
                    case DISABLED -> NamedTextColor.RED;
                    case RELATIVE -> NamedTextColor.GOLD;
                });

        inventory.setItem(16, new ItemBuilder(icon)
                .itemName(plugin.bundle().component(owner, "surface.mode"))
                .lore(plugin.bundle().components(owner, "surface.mode.description",
                        Placeholder.component("mode", mode)))
                .itemFlags(ItemFlag.HIDE_ATTRIBUTES));

        var placeholder = new ItemBuilder(switch (settings.getSurfaceMode()) {
            case DIRECT -> Material.LIME_STAINED_GLASS_PANE;
            case DISABLED -> Material.RED_STAINED_GLASS_PANE;
            case RELATIVE -> Material.ORANGE_STAINED_GLASS_PANE;
        }).hideTooltip(true);

        inventory.setItem(7, placeholder);
        inventory.setItem(25, placeholder);
    }

    private void formatDefault() {
        var grayGlassPane = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).hideTooltip(true);
        var yellowGlassPane = new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).hideTooltip(true);
        var whiteGlassPane = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).hideTooltip(true);
        var orangeGlassPane = new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).hideTooltip(true);

        IntStream.range(0, inventory.getSize()).forEach(value -> inventory.setItem(value, grayGlassPane));

        IntStream.of(43, 37, 38, 39, 40, 41).forEach(value -> inventory.setItem(value, yellowGlassPane));

        inventory.setItem(5, whiteGlassPane);
        inventory.setItem(23, whiteGlassPane);

        inventory.setItem(2, orangeGlassPane);
        inventory.setItem(20, orangeGlassPane);
    }
}
