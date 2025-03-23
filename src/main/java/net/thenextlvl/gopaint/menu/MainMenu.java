package net.thenextlvl.gopaint.menu;

import core.paper.gui.AbstractGUI;
import core.paper.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

@NullMarked
public class MainMenu extends AbstractGUI {
    private final PlayerBrushSettings settings;
    private final GoPaintPlugin plugin;

    private final Inventory inventory;

    public MainMenu(GoPaintPlugin plugin, PlayerBrushSettings settings, Player owner) {
        super(owner, plugin.bundle().component(owner, "menu.main.title"));
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
                .itemName(plugin.bundle().component(owner, "mask.block"))
                .lore(plugin.bundle().components(owner, "mask.block.description"))
                .item());
    }

    public void updateBlockPalette() {

        var placeholder = ItemBuilder.of(Material.BARRIER)
                .itemName(plugin.bundle().component(owner, "slot.empty"))
                .lore(plugin.bundle().components(owner, "slot.empty.description"))
                .item();
        IntStream.rangeClosed(46, 50)
                .filter(value -> settings.getBlocks().size() <= value - 46)
                .forEach(value -> inventory.setItem(value, placeholder));

        if (settings.getBlocks().isEmpty()) return;

        var chance = 100d / settings.getBlocks().size();
        var formatter = DecimalFormat.getInstance(owner.locale());
        formatter.setMaximumFractionDigits(2);

        for (var i = 0; i < settings.getBlocks().size(); i++) {
            inventory.setItem(i + 46, ItemBuilder.of(settings.getBlocks().get(i))
                    .amount(chance > 64 ? 1 : (int) chance)
                    .itemName(plugin.bundle().component(owner, "slot.set",
                            Placeholder.parsed("slot", String.valueOf(i + 1)),
                            Placeholder.parsed("chance", formatter.format(chance))))
                    .lore(plugin.bundle().components(owner, "slot.set.description"))
                    .item());
        }
    }

    public void updateSize() {
        inventory.setItem(14, ItemBuilder.of(Material.BROWN_MUSHROOM)
                .itemName(plugin.bundle().component(owner, "brush.size",
                        Placeholder.parsed("size", String.valueOf(settings.getBrushSize()))))
                .lore(plugin.bundle().components(owner, "brush.size.description"))
                .item());
    }

    public void updateFalloffStrength() {
        if (!(settings.getBrush() instanceof SplatterBrush)
            && !(settings.getBrush() instanceof PaintBrush)
            && !(settings.getBrush() instanceof GradientBrush))
            return;

        inventory.setItem(12, ItemBuilder.of(Material.BLAZE_POWDER)
                .itemName(plugin.bundle().component(owner, "brush.falloff",
                        Placeholder.parsed("strength", String.valueOf(settings.getFalloffStrength()))))
                .lore(plugin.bundle().components(owner, "brush.falloff.description"))
                .item());

        var placeholder = ItemBuilder.of(Material.WHITE_STAINED_GLASS_PANE).hideTooltip().item();
        inventory.setItem(3, placeholder);
        inventory.setItem(21, placeholder);
    }

    public void updateMixingStrength() {
        if (!(settings.getBrush() instanceof GradientBrush)) return;

        inventory.setItem(13, ItemBuilder.of(Material.MAGMA_CREAM)
                .itemName(plugin.bundle().component(owner, "brush.mixing",
                        Placeholder.parsed("strength", String.valueOf(settings.getMixingStrength()))))
                .lore(plugin.bundle().components(owner, "brush.mixing.description"))
                .item());

        var placeholder = ItemBuilder.of(Material.WHITE_STAINED_GLASS_PANE).hideTooltip().item();
        inventory.setItem(4, placeholder);
        inventory.setItem(22, placeholder);
    }

    public void updateFractureSettings() {
        if (!(settings.getBrush() instanceof FractureBrush)) return;

        inventory.setItem(12, ItemBuilder.of(Material.DAYLIGHT_DETECTOR)
                .itemName(plugin.bundle().component(owner, "brush.fracture",
                        Placeholder.parsed("distance", String.valueOf(settings.getFractureStrength()))))
                .lore(plugin.bundle().components(owner, "brush.fracture.description"))
                .item());

        var placeholder = ItemBuilder.of(Material.WHITE_STAINED_GLASS_PANE).hideTooltip().item();
        inventory.setItem(3, placeholder);
        inventory.setItem(21, placeholder);
    }

    public void updateAngleSettings() {
        if (!(settings.getBrush() instanceof AngleBrush)) return;

        inventory.setItem(12, ItemBuilder.of(Material.DAYLIGHT_DETECTOR)
                .itemName(plugin.bundle().component(owner, "brush.angle.distance",
                        Placeholder.parsed("distance", String.valueOf(settings.getAngleDistance()))))
                .lore(plugin.bundle().components(owner, "brush.angle.distance.description"))
                .item());
        inventory.setItem(13, ItemBuilder.of(Material.BLAZE_ROD)
                .itemName(plugin.bundle().component(owner, "brush.angle.maximum",
                        Placeholder.parsed("angle", String.valueOf(settings.getAngleHeightDifference()))))
                .lore(plugin.bundle().components(owner, "brush.angle.maximum.description"))
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
                .itemName(plugin.bundle().component(owner, "brush.thickness",
                        Placeholder.parsed("thickness", String.valueOf(settings.getThickness()))))
                .lore(plugin.bundle().components(owner, "brush.thickness.description"))
                .item());

        var placeholder = ItemBuilder.of(Material.WHITE_STAINED_GLASS_PANE).hideTooltip().item();
        inventory.setItem(3, placeholder);
        inventory.setItem(21, placeholder);
    }

    public void updateAxis() {
        if (!(settings.getBrush() instanceof DiskBrush)) return;

        inventory.setItem(12, ItemBuilder.of(Material.COMPASS)
                .itemName(plugin.bundle().component(owner, "brush.axis",
                        Placeholder.parsed("axis", settings.getAxis().name())))
                .lore(plugin.bundle().components(owner, "brush.axis.description"))
                .item());

        var placeholder = ItemBuilder.of(Material.WHITE_STAINED_GLASS_PANE).hideTooltip().item();
        inventory.setItem(3, placeholder);
        inventory.setItem(21, placeholder);
    }

    public void updateChance() {
        if (!(settings.getBrush() instanceof SprayBrush)) return;

        inventory.setItem(12, ItemBuilder.of(Material.GOLD_NUGGET)
                .itemName(plugin.bundle().component(owner, "brush.chance",
                        Placeholder.parsed("chance", String.valueOf(settings.getChance()))))
                .lore(plugin.bundle().components(owner, "brush.chance.description"))
                .item());

        var placeholder = ItemBuilder.of(Material.WHITE_STAINED_GLASS_PANE).hideTooltip().item();
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

        inventory.setItem(11, ItemBuilder.of(Material.PLAYER_HEAD)
                .profileValue(brush.getHeadValue())
                .itemName(plugin.bundle().component(owner, "brush.selection"))
                .lore(lore.toArray(new Component[]{}))
                .item());
    }

    public void updateToggle() {
        var state = settings.isEnabled()
                ? plugin.bundle().component(owner, "brush.state.enabled")
                : plugin.bundle().component(owner, "brush.state.disabled");

        inventory.setItem(10, ItemBuilder.of(plugin.config().brushConfig().defaultBrushType())
                .itemName(plugin.bundle().component(owner, "brush.toggle"))
                .lore(plugin.bundle().components(owner, "brush.toggle.description",
                        Placeholder.component("state", state)))
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

        var state = plugin.bundle().component(owner, settings.isMaskEnabled() ? "mask.enabled" : "mask.disabled")
                .color(settings.isMaskEnabled() ? NamedTextColor.GREEN : NamedTextColor.RED);

        inventory.setItem(15, ItemBuilder.of(icon)
                .itemName(plugin.bundle().component(owner, "mask.state"))
                .lore(plugin.bundle().components(owner, "mask.state.description",
                        Placeholder.component("state", state)))
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

        var mode = plugin.bundle().component(owner, settings.getSurfaceMode().translationKey())
                .color(switch (settings.getSurfaceMode()) {
                    case EXPOSED -> NamedTextColor.GREEN;
                    case DISABLED -> NamedTextColor.RED;
                    case VISIBLE -> NamedTextColor.GOLD;
                });

        inventory.setItem(16, ItemBuilder.of(icon)
                .itemName(plugin.bundle().component(owner, "surface.mode"))
                .lore(plugin.bundle().components(owner, "surface.mode.description",
                        Placeholder.component("mode", mode)))
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
