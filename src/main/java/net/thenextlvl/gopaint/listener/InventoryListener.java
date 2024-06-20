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
import net.kyori.adventure.text.TextComponent;
import net.thenextlvl.gopaint.GoPaintPlugin;
import net.thenextlvl.gopaint.brush.standard.*;
import net.thenextlvl.gopaint.util.GUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

@RequiredArgsConstructor
public final class InventoryListener implements Listener {
    private final GoPaintPlugin plugin;

    @EventHandler(priority = EventPriority.LOWEST)
    public void menuClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        if (!(event.getView().title() instanceof TextComponent text) || !text.content().equals("goPaint Menu")) {
            return;
        }
        if (event.getView().getTopInventory() != event.getClickedInventory()) {
            if (event.getClick().isShiftClick() || event.getAction().equals(InventoryAction.COLLECT_TO_CURSOR)) {
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(true);
        var playerBrush = plugin.brushController().getBrushSettings(player);
        if (event.getRawSlot() == 10 || event.getRawSlot() == 1 || event.getRawSlot() == 19) {
            if (event.getClick().isLeftClick()) {
                if (!event.getCursor().getType().isBlock()) {
                    if (!event.getCursor().getType().equals(plugin.config().generic().defaultBrush())) {
                        playerBrush.export(event.getCursor());
                    }
                }
            } else if (event.getClick().isRightClick()) {
                playerBrush.toggle();
            }
        } else if (event.getRawSlot() == 11 || event.getRawSlot() == 2 || event.getRawSlot() == 20) {
            if (event.getClick().equals(ClickType.LEFT)) {
                playerBrush.cycleBrushForward();
            } else if (event.getClick().equals(ClickType.RIGHT)) {
                playerBrush.cycleBrushBackward();
            } else if (event.getClick().isShiftClick()) {
                player.openInventory(GUI.generateBrushes());
            }
        } else if (event.getRawSlot() == 12 || event.getRawSlot() == 3 || event.getRawSlot() == 21) {
            var brush = playerBrush.getBrush();
            if (brush instanceof SprayBrush) {
                if (event.getClick().isLeftClick()) {
                    playerBrush.increaseChance();
                } else if (event.getClick().isRightClick()) {
                    playerBrush.decreaseChance();
                }
            } else if (brush instanceof OverlayBrush || brush instanceof UnderlayBrush) {
                if (event.getClick().isLeftClick()) {
                    playerBrush.increaseThickness();
                } else if (event.getClick().isRightClick()) {
                    playerBrush.decreaseThickness();
                }
            } else if (brush instanceof FractureBrush) {
                if (event.getClick().isLeftClick()) {
                    playerBrush.increaseFractureDistance();
                } else if (event.getClick().isRightClick()) {
                    playerBrush.decreaseFractureDistance();
                }
            } else if (brush instanceof AngleBrush) {
                if (event.getClick().isLeftClick()) {
                    playerBrush.increaseAngleDistance();
                } else if (event.getClick().isRightClick()) {
                    playerBrush.decreaseAngleDistance();
                }
            } else if (brush instanceof GradientBrush || brush instanceof PaintBrush
                       || brush instanceof SplatterBrush) {
                if (event.getClick().isLeftClick()) {
                    playerBrush.increaseFalloffStrength();
                } else if (event.getClick().isRightClick()) {
                    playerBrush.decreaseFalloffStrength();
                }
            } else if (brush instanceof DiscBrush) {
                playerBrush.cycleAxis();
            }
        } else if (event.getRawSlot() == 13 || event.getRawSlot() == 4 || event.getRawSlot() == 22) {
            var brush = playerBrush.getBrush();
            if (brush instanceof AngleBrush) {
                if (event.getClick().equals(ClickType.LEFT)) {
                    playerBrush.increaseAngleHeightDifference(5);
                } else if (event.getClick().equals(ClickType.RIGHT)) {
                    playerBrush.decreaseAngleHeightDifference(5);
                } else if (event.getClick().equals(ClickType.SHIFT_LEFT)) {
                    playerBrush.increaseAngleHeightDifference(15);
                } else if (event.getClick().equals(ClickType.SHIFT_RIGHT)) {
                    playerBrush.decreaseAngleHeightDifference(15);
                }
            } else if (brush instanceof GradientBrush) {
                if (event.getClick().isLeftClick()) {
                    playerBrush.increaseMixingStrength();
                } else if (event.getClick().isRightClick()) {
                    playerBrush.decreaseMixingStrength();
                }
            }
        } else if (event.getRawSlot() == 14 || event.getRawSlot() == 5 || event.getRawSlot() == 23) {
            if (event.getClick().equals(ClickType.LEFT)) {
                playerBrush.increaseBrushSize(1);
            } else if (event.getClick().equals(ClickType.RIGHT)) {
                playerBrush.decreaseBrushSize(1);
            } else if (event.getClick().equals(ClickType.SHIFT_LEFT)) {
                playerBrush.increaseBrushSize(10);
            } else if (event.getClick().equals(ClickType.SHIFT_RIGHT)) {
                playerBrush.decreaseBrushSize(10);
            }
        } else if (event.getRawSlot() == 15 || event.getRawSlot() == 6 || event.getRawSlot() == 24) {
            playerBrush.cycleMaskMode();
        } else if (event.getRawSlot() == 16 || event.getRawSlot() == 7 || event.getRawSlot() == 25) {
            playerBrush.cycleSurfaceMode();
        } else if ((event.getRawSlot() >= 37 && event.getRawSlot() <= 41)
                   || (event.getRawSlot() >= 46 && event.getRawSlot() <= 50)) {
            int slot;
            if (event.getRawSlot() >= 37 && event.getRawSlot() <= 41) {
                slot = event.getRawSlot() - 36;
            } else {
                slot = event.getRawSlot() - 45;
            }
            if (event.getClick().isLeftClick()) {
                if (event.getCursor().getType().isBlock() && event.getCursor().getType().isSolid()) {
                    playerBrush.addBlock(event.getCursor().getType(), slot);
                }
            } else if (event.getClick().isRightClick()) {
                playerBrush.removeBlock(slot);
            }
        } else if (event.getRawSlot() == 43 || event.getRawSlot() == 52) {
            if (event.getClick().isLeftClick()) {
                if (event.getCursor().getType().isBlock() && event.getCursor().getType().isSolid()) {
                    playerBrush.setMask(event.getCursor().getType());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void menuBrushClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        if (!(event.getView().title() instanceof TextComponent title) || !title.content().equals("goPaint Brushes")) {
            return;
        }
    }
}
