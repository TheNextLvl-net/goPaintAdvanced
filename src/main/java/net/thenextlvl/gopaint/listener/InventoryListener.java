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
package net.thenextlvl.gopaint.listener;

import lombok.RequiredArgsConstructor;
import net.thenextlvl.gopaint.GoPaintPlugin;
import net.thenextlvl.gopaint.api.model.MaskMode;
import net.thenextlvl.gopaint.api.model.SurfaceMode;
import net.thenextlvl.gopaint.brush.standard.*;
import net.thenextlvl.gopaint.menu.MainMenu;
import org.bukkit.Axis;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

@RequiredArgsConstructor
public final class InventoryListener implements Listener {
    private final GoPaintPlugin plugin;

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void menuClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        if (!(event.getView().getTopInventory().getHolder(true) instanceof MainMenu)) {
            return;
        }
        if (event.getView().getTopInventory() != event.getClickedInventory()) {
            if (event.getClick().isShiftClick() || event.getAction().equals(InventoryAction.COLLECT_TO_CURSOR)) {
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(true);

        if (event.getAction().equals(InventoryAction.NOTHING)) return;

        var settings = plugin.brushController().getBrushSettings(player);
        var itemType = event.getCursor().getType();

        if (event.getRawSlot() == 10 || event.getRawSlot() == 1 || event.getRawSlot() == 19) {

            if (event.getCursor().isEmpty()) {
                settings.setEnabled(!settings.isEnabled());
                return;
            }


            plugin.brushController().parseBrushSettings(event.getCursor())
                    .ifPresentOrElse(settings::importSettings, () -> {
                        if (itemType.equals(plugin.config().brushConfig().defaultBrushType())) return;
                        if (!itemType.isBlock()) settings.exportSettings(event.getCursor());
                    });

        } else if (event.getRawSlot() == 11 || event.getRawSlot() == 2 || event.getRawSlot() == 20) {
            if (event.getClick().equals(ClickType.LEFT)) {
                settings.setBrush(settings.getNextBrush(settings.getBrush()));
            } else if (event.getClick().equals(ClickType.RIGHT)) {
                settings.setBrush(settings.getPreviousBrush(settings.getBrush()));
            } else if (event.getClick().isShiftClick()) {
                settings.getBrushesMenu().open();
            }
        } else if (event.getRawSlot() == 12 || event.getRawSlot() == 3 || event.getRawSlot() == 21) {
            var brush = settings.getBrush();
            if (brush instanceof SprayBrush) {
                if (event.getClick().isLeftClick()) {
                    settings.setChance(settings.getChance() + 10);
                } else if (event.getClick().isRightClick()) {
                    settings.setChance(settings.getChance() - 10);
                }
            } else if (brush instanceof OverlayBrush || brush instanceof UnderlayBrush) {
                if (event.getClick().isLeftClick()) {
                    settings.setThickness(settings.getThickness() + 1);
                } else if (event.getClick().isRightClick()) {
                    settings.setThickness(settings.getThickness() - 1);
                }
            } else if (brush instanceof FractureBrush) {
                if (event.getClick().isLeftClick()) {
                    settings.setFractureStrength(settings.getFractureStrength() + 1);
                } else if (event.getClick().isRightClick()) {
                    settings.setFractureStrength(settings.getFractureStrength() - 1);
                }
            } else if (brush instanceof AngleBrush) {
                if (event.getClick().isLeftClick()) {
                    settings.setAngleDistance(settings.getAngleDistance() + 1);
                } else if (event.getClick().isRightClick()) {
                    settings.setAngleDistance(settings.getAngleDistance() - 1);
                }
            } else if (brush instanceof GradientBrush || brush instanceof PaintBrush
                       || brush instanceof SplatterBrush) {
                if (event.getClick().isLeftClick()) {
                    settings.setFalloffStrength(settings.getFalloffStrength() + 10);
                } else if (event.getClick().isRightClick()) {
                    settings.setFalloffStrength(settings.getFalloffStrength() - 10);
                }
            } else if (brush instanceof DiscBrush) {
                settings.setAxis(switch (settings.getAxis()) {
                    case X -> Axis.Y;
                    case Y -> Axis.Z;
                    case Z -> Axis.X;
                });
            }
        } else if (event.getRawSlot() == 13 || event.getRawSlot() == 4 || event.getRawSlot() == 22) {
            var brush = settings.getBrush();
            if (brush instanceof AngleBrush) {
                if (event.getClick().equals(ClickType.LEFT)) {
                    settings.setAngleHeightDifference(settings.getAngleHeightDifference() + 5);
                } else if (event.getClick().equals(ClickType.RIGHT)) {
                    settings.setAngleHeightDifference(settings.getAngleHeightDifference() - 5);
                } else if (event.getClick().equals(ClickType.SHIFT_LEFT)) {
                    settings.setAngleHeightDifference(settings.getAngleHeightDifference() + 15);
                } else if (event.getClick().equals(ClickType.SHIFT_RIGHT)) {
                    settings.setAngleHeightDifference(settings.getAngleHeightDifference() - 15);
                }
            } else if (brush instanceof GradientBrush) {
                if (event.getClick().isLeftClick()) {
                    settings.setMixingStrength(settings.getMixingStrength() + 10);
                } else if (event.getClick().isRightClick()) {
                    settings.setMixingStrength(settings.getMixingStrength() - 10);
                }
            }
        } else if (event.getRawSlot() == 14 || event.getRawSlot() == 5 || event.getRawSlot() == 23) {
            if (event.getClick().equals(ClickType.LEFT)) {
                settings.setBrushSize(settings.getBrushSize() + 1);
            } else if (event.getClick().equals(ClickType.RIGHT)) {
                settings.setBrushSize(settings.getBrushSize() - 1);
            } else if (event.getClick().equals(ClickType.SHIFT_LEFT)) {
                settings.setBrushSize(settings.getBrushSize() + 10);
            } else if (event.getClick().equals(ClickType.SHIFT_RIGHT)) {
                settings.setBrushSize(settings.getBrushSize() - 10);
            }
        } else if (event.getRawSlot() == 15 || event.getRawSlot() == 6 || event.getRawSlot() == 24) {
            settings.setMaskMode(switch (settings.getMaskMode()) {
                case INTERFACE -> MaskMode.WORLDEDIT;
                case WORLDEDIT -> MaskMode.DISABLED;
                case DISABLED -> MaskMode.INTERFACE;
            });
        } else if (event.getRawSlot() == 16 || event.getRawSlot() == 7 || event.getRawSlot() == 25) {
            settings.setSurfaceMode(switch (settings.getSurfaceMode()) {
                case DIRECT -> SurfaceMode.RELATIVE;
                case RELATIVE -> SurfaceMode.DISABLED;
                case DISABLED -> SurfaceMode.DIRECT;
            });
        } else if ((event.getRawSlot() >= 37 && event.getRawSlot() <= 41)
                   || (event.getRawSlot() >= 46 && event.getRawSlot() <= 50)) {
            int slot = event.getRawSlot() - (event.getRawSlot() >= 37 && event.getRawSlot() <= 41 ? 36 : 45);
            if (event.getClick().isLeftClick()) {
                if (!itemType.isSolid()) return;
                settings.addBlock(itemType, slot);
            } else if (event.getClick().isRightClick()) {
                settings.removeBlock(slot);
            }
        } else if (event.getRawSlot() == 43 || event.getRawSlot() == 52) {
            if (!itemType.isSolid()) return;
            settings.setMask(itemType);
        }
    }
}
