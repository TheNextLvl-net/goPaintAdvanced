package net.thenextlvl.gopaint.menu;

import core.paper.gui.AbstractGUI;
import core.paper.item.ItemBuilder;
import lombok.Getter;
import net.thenextlvl.gopaint.GoPaintPlugin;
import net.thenextlvl.gopaint.api.brush.setting.PlayerBrushSettings;
import net.thenextlvl.gopaint.brush.standard.*;
import net.thenextlvl.gopaint.util.Items;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.stream.Collectors;
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
        var placeholder = Items.create(Material.GRAY_STAINED_GLASS_PANE);
        IntStream.of(3, 4, 12, 13, 21, 22).forEach(value -> inventory.setItem(value, placeholder));
    }

    public void updateMask() {
        inventory.setItem(52, Items.create(settings.getMask(), 1, "§6Current Mask", "\n§7Left click with a block to change"));
    }

    public void updateBlockPalette() {
        for (int x = 46; x <= 50; x++) {
            inventory.setItem(x, Items.create(Material.BARRIER, 1, "§cEmpty Slot", "\n§7Click with a block to set"));
        }
        int x = 46;
        int size = settings.getBlocks().size();
        int chance = size == 0 ? 0 : 100 / size;
        for (Material material : settings.getBlocks()) {
            if (chance > 64) {
                inventory.setItem(x, Items.create(material, 1,
                        "§aSlot " + (x - 45) + " §7" + chance + "%",
                        "\n§7Left click with a block to change\n§7Right click to clear"
                ));
            } else {
                inventory.setItem(x, Items.create(material, chance,
                        "§aSlot " + (x - 45) + " §7" + chance + "%",
                        "\n§7Left click with a block to change\n§7Right click to clear"
                ));
            }
            x++;
        }
    }

    public void updateSize() {
        inventory.setItem(14, Items.create(Material.BROWN_MUSHROOM, 1,
                "§6Brush Size: §e" + settings.getBrushSize(),
                "\n§7Left click to increase\n§7Right click to decrease\n§7Shift click to change by 10"
        ));
    }

    public void updateFalloffStrength() {
        var brush = settings.getBrush();
        if (brush instanceof SplatterBrush || brush instanceof PaintBrush || brush instanceof GradientBrush) {
            inventory.setItem(3, Items.create(Material.WHITE_STAINED_GLASS_PANE));
            inventory.setItem(12, Items.create(Material.BLAZE_POWDER, 1,
                    "§6Falloff Strength: §e" + settings.getFalloffStrength() + "%",
                    "\n§7Left click to increase\n§7Right click to decrease"
            ));
            inventory.setItem(21, Items.create(Material.WHITE_STAINED_GLASS_PANE));
        }
    }

    public void updateMixingStrength() {
        if (settings.getBrush() instanceof GradientBrush) {
            inventory.setItem(4, Items.create(Material.WHITE_STAINED_GLASS_PANE));
            inventory.setItem(13, Items.create(Material.MAGMA_CREAM, 1,
                    "§6Mixing Strength: §e" + settings.getMixingStrength() + "%",
                    "\n§7Left click to increase\n§7Right click to decrease"
            ));
            inventory.setItem(22, Items.create(Material.WHITE_STAINED_GLASS_PANE));
        }
    }

    public void updateFractureSettings() {
        if (settings.getBrush() instanceof FractureBrush) {
            inventory.setItem(3, Items.create(Material.WHITE_STAINED_GLASS_PANE));
            inventory.setItem(12, Items.create(Material.DAYLIGHT_DETECTOR, 1,
                    "§6Fracture Check Distance: §e" + settings.getFractureDistance(),
                    "\n§7Left click to increase\n§7Right click to decrease"
            ));
            inventory.setItem(21, Items.create(Material.WHITE_STAINED_GLASS_PANE));
        }
    }

    public void updateAngleSettings() {
        if (settings.getBrush() instanceof AngleBrush) {
            inventory.setItem(3, Items.create(Material.WHITE_STAINED_GLASS_PANE));
            inventory.setItem(12, Items.create(Material.DAYLIGHT_DETECTOR, 1,
                    "§6Angle Check Distance: §e" + settings.getAngleDistance(),
                    "\n§7Left click to increase\n§7Right click to decrease"
            ));
            inventory.setItem(21, Items.create(Material.WHITE_STAINED_GLASS_PANE));

            inventory.setItem(4, Items.create(Material.WHITE_STAINED_GLASS_PANE));
            inventory.setItem(13, Items.create(Material.BLAZE_ROD, 1,
                    "§6Maximum Angle: §e" + settings.getAngleHeightDifference() + "°",
                    "\n§7Left click to increase\n§7Right click to decrease\n§7Shift click to change by 15"
            ));
            inventory.setItem(22, Items.create(Material.WHITE_STAINED_GLASS_PANE));
        }
    }

    public void updateThickness() {
        if (settings.getBrush() instanceof OverlayBrush || settings.getBrush() instanceof UnderlayBrush) {
            inventory.setItem(3, Items.create(Material.WHITE_STAINED_GLASS_PANE));
            inventory.setItem(12, Items.create(Material.BOOK, 1,
                    "§6Layer Thickness: §e" + settings.getThickness(),
                    "\n§7Left click to increase\n§7Right click to decrease"
            ));
            inventory.setItem(21, Items.create(Material.WHITE_STAINED_GLASS_PANE));
        }
    }

    public void updateAxis() {
        if (settings.getBrush() instanceof DiscBrush) {
            inventory.setItem(3, Items.create(Material.WHITE_STAINED_GLASS_PANE));
            inventory.setItem(12, Items.create(Material.COMPASS, 1,
                    "§6Axis: §e" + settings.getAxis(), "\n§7Click to change"
            ));
            inventory.setItem(21, Items.create(Material.WHITE_STAINED_GLASS_PANE));
        }
    }

    public void updateChance() {
        if (settings.getBrush() instanceof SprayBrush) {
            inventory.setItem(3, Items.create(Material.WHITE_STAINED_GLASS_PANE));
            inventory.setItem(12, Items.create(Material.GOLD_NUGGET, 1,
                    "§6Place chance: §e" + settings.getChance() + "%",
                    "\n§7Left click to increase\n§7Right click to decrease"
            ));
            inventory.setItem(21, Items.create(Material.WHITE_STAINED_GLASS_PANE));
        }
    }

    public void updateBrushSelection() {
        var brush = settings.getBrush();

        var clicks = "\n§7Shift click to select\n§7Click to cycle brush\n\n";

        var lore = plugin.brushRegistry().getBrushes().map(current -> {
            if (current.equals(brush)) {
                return "§e" + current.getName() + "\n";
            } else {
                return "§8" + current.getName() + "\n";
            }
        }).collect(Collectors.joining());

        inventory.setItem(11, Items.createHead(brush.getHeadValue(), 1, "§6Selected Brush type", clicks + lore));
    }

    public void updateToggle() {
        if (settings.isEnabled()) {
            inventory.setItem(1, Items.create(Material.LIME_STAINED_GLASS_PANE));
            inventory.setItem(10, Items.create(plugin.config().brushConfig().defaultBrushType(), 1, "§6goPaint Brush",
                    "§a§lEnabled\n\n§7Left click with item to export\n§7Right click to toggle"
            ));
            inventory.setItem(19, Items.create(Material.LIME_STAINED_GLASS_PANE));
        } else {
            inventory.setItem(1, Items.create(Material.RED_STAINED_GLASS_PANE));
            inventory.setItem(10, Items.create(plugin.config().brushConfig().defaultBrushType(), 1, "§6goPaint Brush",
                    "§c§lDisabled\n\n§7Left click with item to export\n§7Right click to toggle"
            ));
            inventory.setItem(19, Items.create(Material.RED_STAINED_GLASS_PANE));
        }
    }

    public void updateMaskMode() {
        var pane = switch (settings.getMaskMode()) {
            case DISABLED -> Material.RED_STAINED_GLASS_PANE;
            case INTERFACE -> Material.LIME_STAINED_GLASS_PANE;
            case WORLDEDIT -> Material.ORANGE_STAINED_GLASS_PANE;
        };
        var color = switch (settings.getMaskMode()) {
            case DISABLED -> "§c";
            case INTERFACE -> "§a";
            case WORLDEDIT -> "§6";
        };
        var icon = switch (settings.getMaskMode()) {
            case DISABLED -> Material.CARVED_PUMPKIN;
            case INTERFACE -> Material.JACK_O_LANTERN;
            case WORLDEDIT -> Material.WOODEN_AXE;
        };

        inventory.setItem(6, Items.create(pane));
        inventory.setItem(15, Items.create(icon, 1,
                "§6Mask Mode",
                color + "§l" + settings.getMaskMode().getName() + "\n\n§7Click to cycle"
        ));
        inventory.setItem(24, Items.create(pane));
    }

    public void updateSurfaceMode() {
        var pane = switch (settings.getSurfaceMode()) {
            case DIRECT -> Material.LIME_STAINED_GLASS_PANE;
            case DISABLED -> Material.RED_STAINED_GLASS_PANE;
            case RELATIVE -> Material.ORANGE_STAINED_GLASS_PANE;
        };
        var color = switch (settings.getSurfaceMode()) {
            case DIRECT -> "§a";
            case DISABLED -> "§c";
            case RELATIVE -> "§6";
        };

        inventory.setItem(7, Items.create(pane));
        inventory.setItem(16, Items.create(Material.LIGHT_WEIGHTED_PRESSURE_PLATE, 1,
                "§6Surface Mode",
                color + "§l" + settings.getSurfaceMode().getName() + "\n\n§7Click to cycle"
        ));
        inventory.setItem(25, Items.create(pane));
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
