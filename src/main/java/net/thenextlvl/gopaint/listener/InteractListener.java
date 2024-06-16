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

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.TextComponent;
import net.thenextlvl.gopaint.GoPaintPlugin;
import net.thenextlvl.gopaint.api.brush.setting.BrushSettings;
import net.thenextlvl.gopaint.api.brush.setting.PlayerBrushSettings;
import net.thenextlvl.gopaint.brush.setting.CraftItemBrushSettings;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@RequiredArgsConstructor
public final class InteractListener implements Listener {
    private final GoPaintPlugin plugin;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission(GoPaintPlugin.USE_PERMISSION)) {
            return;
        }

        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }

        if (event.getAction().isLeftClick() && item.getType().equals(plugin.config().generic().defaultBrush())) {
            var brush = plugin.brushManager().getBrush(player);
            player.openInventory(brush.getInventory());
            event.setCancelled(true);
            return;
        }

        if (!event.getAction().isRightClick()) {
            return;
        }

        Location location;
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            Block targetBlock = player.getTargetBlockExact(250, FluidCollisionMode.NEVER);
            if (targetBlock == null) {
                return;
            }
            location = targetBlock.getLocation().clone();
        } else if (event.getClickedBlock() != null) {
            location = event.getClickedBlock().getLocation();
        } else {
            return;
        }

        if (!player.hasPermission(GoPaintPlugin.WORLD_BYPASS_PERMISSION)
            && plugin.config().generic().disabledWorlds().contains(location.getWorld().getName())) {
            return;
        }

        BrushSettings settings;

        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta != null && itemMeta.hasLore() && itemMeta.displayName() instanceof TextComponent name) {

            var brush = plugin.brushManager().getBrushHandler(name.content());

            //noinspection removal
            settings = brush.map(current -> CraftItemBrushSettings.parse(current, itemMeta)).orElse(null);
        } else if (item.getType().equals(plugin.config().generic().defaultBrush())) {
            settings = plugin.brushManager().getBrush(player);
        } else {
            return;
        }

        event.setCancelled(true);

        if (settings == null || settings.getBlocks().isEmpty()) {
            return;
        }

        if (!(settings instanceof PlayerBrushSettings playerSettings) || playerSettings.isEnabled()) {
            BukkitAdapter.adapt(player).runAction(
                    () -> settings.getBrush().paint(location, player, settings), false, true
            );
        } else plugin.bundle().sendMessage(player, "brush.disabled");
    }
}
