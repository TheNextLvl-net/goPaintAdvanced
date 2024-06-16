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
package net.thenextlvl.gopaint.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.thenextlvl.gopaint.GoPaintPlugin;
import net.thenextlvl.gopaint.api.brush.setting.PlayerBrushSettings;
import net.thenextlvl.gopaint.brush.setting.CraftPlayerBrushSettings;
import net.thenextlvl.gopaint.brush.standard.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class GUI {

    private static final GoPaintPlugin plugin = JavaPlugin.getPlugin(GoPaintPlugin.class);

    public static Inventory create(CraftPlayerBrushSettings pb) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("goPaint Menu", NamedTextColor.DARK_BLUE));
        update(inv, pb);
        return inv;
    }

    public static Inventory generateBrushes() {
        Inventory inv = Bukkit.createInventory(null, 27, Component.text("goPaint Brushes", NamedTextColor.DARK_BLUE));
        // FILLER
        formatDefault(inv);
        var brushes = plugin.brushRegistry().getBrushes();
        for (int index = 0; index < brushes.size(); index++) {
            var brush = brushes.get(index);
            inv.setItem(index, Items.createHead(brush.getHeadValue(), 1, "§6" + brush.getName(),
                    "\n§7Click to select\n\n§8" + brush.getDescription()
            ));
        }
        return inv;
    }

    private static void formatDefault(Inventory inventory) {
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            inventory.setItem(slot, Items.create(Material.GRAY_STAINED_GLASS_PANE, 1, "§7", ""));
        }
    }

    public static void update(Inventory inventory, PlayerBrushSettings settings) {
        var brush = settings.getBrush();

        // FILLER
        formatDefault(inventory);

        // goPaint toggle
        if (settings.isEnabled()) {
            inventory.setItem(1, Items.create(Material.LIME_STAINED_GLASS_PANE, 1, "§7", ""));
            inventory.setItem(10, Items.create(plugin.config().generic().defaultBrush(), 1, "§6goPaint Brush",
                    "§a§lEnabled\n\n§7Left click with item to export\n§7Right click to toggle"
            ));
            inventory.setItem(19, Items.create(Material.LIME_STAINED_GLASS_PANE, 1, "§7", ""));
        } else {
            inventory.setItem(1, Items.create(Material.RED_STAINED_GLASS_PANE, 1, "§7", ""));
            inventory.setItem(10, Items.create(plugin.config().generic().defaultBrush(), 1, "§6goPaint Brush",
                    "§c§lDisabled\n\n§7Left click with item to export\n§7Right click to toggle"
            ));
            inventory.setItem(19, Items.create(Material.RED_STAINED_GLASS_PANE, 1, "§7", ""));
        }

        // Brushes + Chance
        inventory.setItem(2, Items.create(Material.ORANGE_STAINED_GLASS_PANE, 1, "§7", ""));


        String clicks = "\n§7Shift click to select\n§7Click to cycle brush\n\n";

        inventory.setItem(11, Items.createHead(brush.getHeadValue(), 1, "§6Selected Brush type",
                clicks + plugin.brushController().getBrushLore(brush)
        ));
        inventory.setItem(20, Items.create(Material.ORANGE_STAINED_GLASS_PANE, 1, "§7", ""));

        // chance
        if (brush instanceof SprayBrush) {
            inventory.setItem(3, Items.create(Material.WHITE_STAINED_GLASS_PANE, 1, "§7", ""));
            inventory.setItem(12, Items.create(Material.GOLD_NUGGET, 1,
                    "§6Place chance: §e" + settings.getChance() + "%",
                    "\n§7Left click to increase\n§7Right click to decrease"
            ));
            inventory.setItem(21, Items.create(Material.WHITE_STAINED_GLASS_PANE, 1, "§7", ""));
        }

        // axis
        if (brush instanceof DiscBrush) {
            inventory.setItem(3, Items.create(Material.WHITE_STAINED_GLASS_PANE, 1, "§7", ""));
            inventory.setItem(12, Items.create(Material.COMPASS, 1,
                    "§6Axis: §e" + settings.getAxis(), "\n§7Click to change"
            ));
            inventory.setItem(21, Items.create(Material.WHITE_STAINED_GLASS_PANE, 1, "§7", ""));
        }


        // thickness
        if (brush instanceof OverlayBrush || brush instanceof UnderlayBrush) {
            inventory.setItem(3, Items.create(Material.WHITE_STAINED_GLASS_PANE, 1, "§7", ""));
            inventory.setItem(12, Items.create(Material.BOOK, 1,
                    "§6Layer Thickness: §e" + settings.getThickness(),
                    "\n§7Left click to increase\n§7Right click to decrease"
            ));
            inventory.setItem(21, Items.create(Material.WHITE_STAINED_GLASS_PANE, 1, "§7", ""));
        }

        // angle settings
        if (brush instanceof AngleBrush) {
            inventory.setItem(3, Items.create(Material.WHITE_STAINED_GLASS_PANE, 1, "§7", ""));
            inventory.setItem(12, Items.create(Material.DAYLIGHT_DETECTOR, 1,
                    "§6Angle Check Distance: §e" + settings.getAngleDistance(),
                    "\n§7Left click to increase\n§7Right click to decrease"
            ));
            inventory.setItem(21, Items.create(Material.WHITE_STAINED_GLASS_PANE, 1, "§7", ""));

            inventory.setItem(4, Items.create(Material.WHITE_STAINED_GLASS_PANE, 1, "§7", ""));
            inventory.setItem(13, Items.create(Material.BLAZE_ROD, 1,
                    "§6Maximum Angle: §e" + settings.getAngleHeightDifference() + "°",
                    "\n§7Left click to increase\n§7Right click to decrease\n§7Shift click to change by 15"
            ));
            inventory.setItem(22, Items.create(Material.WHITE_STAINED_GLASS_PANE, 1, "§7", ""));
        }

        // fracture settings
        if (brush instanceof FractureBrush) {
            inventory.setItem(3, Items.create(Material.WHITE_STAINED_GLASS_PANE, 1, "§7", ""));
            inventory.setItem(12, Items.create(Material.DAYLIGHT_DETECTOR, 1,
                    "§6Fracture Check Distance: §e" + settings.getFractureDistance(),
                    "\n§7Left click to increase\n§7Right click to decrease"
            ));
            inventory.setItem(21, Items.create(Material.WHITE_STAINED_GLASS_PANE, 1, "§7", ""));
        }

        // angle settings
        if (brush instanceof GradientBrush) {
            inventory.setItem(4, Items.create(Material.WHITE_STAINED_GLASS_PANE, 1, "§7", ""));
            inventory.setItem(13, Items.create(Material.MAGMA_CREAM, 1,
                    "§6Mixing Strength: §e" + settings.getMixingStrength() + "%",
                    "\n§7Left click to increase\n§7Right click to decrease"
            ));
            inventory.setItem(22, Items.create(Material.WHITE_STAINED_GLASS_PANE, 1, "§7", ""));
        }

        if (brush instanceof SplatterBrush || brush instanceof PaintBrush || brush instanceof GradientBrush) {
            inventory.setItem(3, Items.create(Material.WHITE_STAINED_GLASS_PANE, 1, "§7", ""));
            inventory.setItem(12, Items.create(Material.BLAZE_POWDER, 1,
                    "§6Falloff Strength: §e" + settings.getFalloffStrength() + "%",
                    "\n§7Left click to increase\n§7Right click to decrease"
            ));
            inventory.setItem(21, Items.create(Material.WHITE_STAINED_GLASS_PANE, 1, "§7", ""));
        }


        // Size
        inventory.setItem(5, Items.create(Material.WHITE_STAINED_GLASS_PANE, 1, "§7", ""));
        inventory.setItem(14, Items.create(Material.BROWN_MUSHROOM, 1,
                "§6Brush Size: §e" + settings.getSize(),
                "\n§7Left click to increase\n§7Right click to decrease\n§7Shift click to change by 10"
        ));
        inventory.setItem(23, Items.create(Material.WHITE_STAINED_GLASS_PANE, 1, "§7", ""));

        // Mask toggle
        if (settings.isMaskEnabled()) {
            inventory.setItem(6, Items.create(Material.LIME_STAINED_GLASS_PANE, 1, "§7", ""));
            inventory.setItem(15, Items.create(Material.JACK_O_LANTERN, 1,
                    "§6Mask",
                    "§a§lEnabled\n\n§7Click to toggle"
            ));
            inventory.setItem(24, Items.create(Material.LIME_STAINED_GLASS_PANE, 1, "§7", ""));
        } else {
            inventory.setItem(6, Items.create(Material.RED_STAINED_GLASS_PANE, 1, "§7", ""));
            inventory.setItem(15, Items.create(Material.CARVED_PUMPKIN, 1, "§6Mask", "§c§lDisabled\n\n§7Click to toggle"));
            inventory.setItem(24, Items.create(Material.RED_STAINED_GLASS_PANE, 1, "§7", ""));
        }

        // Surface Mode toggle
        addSurfaceModeSwitch(inventory, settings);

        // Place Block
        for (int x = 37; x <= 41; x++) {
            inventory.setItem(x, Items.create(Material.YELLOW_STAINED_GLASS_PANE, 1, "§7", ""));
        }
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

        // Mask Block
        inventory.setItem(43, Items.create(Material.YELLOW_STAINED_GLASS_PANE, 1, "§7", ""));
        inventory.setItem(52, Items.create(settings.getMask(), 1, "§6Current Mask", "\n§7Left click with a block to change"));
    }

    private static void addSurfaceModeSwitch(Inventory inventory, PlayerBrushSettings settings) {
        Material pane = switch (settings.getSurfaceMode()) {
            case DIRECT -> Material.LIME_STAINED_GLASS_PANE;
            case DISABLED -> Material.RED_STAINED_GLASS_PANE;
            case RELATIVE -> Material.ORANGE_STAINED_GLASS_PANE;
        };
        String color = switch (settings.getSurfaceMode()) {
            case DIRECT -> "§a";
            case DISABLED -> "§c";
            case RELATIVE -> "§6";
        };

        inventory.setItem(7, Items.create(pane, 1, "§7", ""));
        inventory.setItem(16, Items.create(Material.LIGHT_WEIGHTED_PRESSURE_PLATE, 1,
                "§6Surface Mode",
                color + "§l" + settings.getSurfaceMode().getName() + "\n\n§7Click to toggle"
        ));
        inventory.setItem(25, Items.create(pane, 1, "§7", ""));
    }

}
