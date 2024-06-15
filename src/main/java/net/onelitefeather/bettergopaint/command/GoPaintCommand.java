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
package net.onelitefeather.bettergopaint.command;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.onelitefeather.bettergopaint.BetterGoPaint;
import net.onelitefeather.bettergopaint.brush.PlayerBrush;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GoPaintCommand extends Command implements PluginIdentifiableCommand {

    private final BetterGoPaint plugin;

    public GoPaintCommand(BetterGoPaint main) {
        super("gopaint", "goPaint command", "/gp size|toggle|info|reload", List.of("gp"));
        plugin = main;
    }

    @Override
    public boolean execute(
            @NotNull final CommandSender sender,
            @NotNull final String commandLabel,
            final @NotNull String[] args
    ) {
        if (!(sender instanceof final Player p)) {
            return false;
        }
        PlayerBrush pb = plugin.getBrushManager().getBrush(p);
        if (!p.hasPermission(BetterGoPaint.USE_PERMISSION)) {
            plugin.bundle().sendMessage(p, "command.gopaint.permission");
            return true;
        }
        if (args.length == 0) {
            if (p.hasPermission(BetterGoPaint.ADMIN_PERMISSION)) {
                plugin.bundle().sendMessage(p, "command.gopaint.usage.admin");
                return true;
            }
            plugin.bundle().sendMessage(p, "command.gopaint.usage");
            return true;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("size")) {
                plugin.bundle().sendMessage(p, "command.gopaint.usage.size");
                return true;
            } else if (args[0].equalsIgnoreCase("toggle")) {
                if (pb.enabled()) {
                    pb.toggle();
                    plugin.bundle().sendMessage(p, "command.gopaint.brush.disabled");
                } else {
                    pb.toggle();
                    plugin.bundle().sendMessage(p, "command.gopaint.brush.enabled");
                }
                return true;
            } else if ((args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("r")) && p.hasPermission(
                    BetterGoPaint.ADMIN_PERMISSION)) {
                plugin.reloadConfig();
                plugin.bundle().sendMessage(p, "command.gopaint.reloaded");
                return true;
            } else if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("i")) {
                plugin.bundle().sendMessage(p, "command.gopaint.info.creator");
                plugin.bundle().sendMessage(p, "command.gopaint.info.link");
                return true;
            }
            if (p.hasPermission(BetterGoPaint.ADMIN_PERMISSION)) {
                plugin.bundle().sendMessage(p, "command.gopaint.usage.admin");
                return true;
            }
            plugin.bundle().sendMessage(p, "command.gopaint.usage");
            return true;
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("size") || args[0].equalsIgnoreCase("s")) {
                try {
                    int sizeAmount = Integer.parseInt(args[1]);
                    pb.setSize(sizeAmount);
                    plugin.bundle().sendMessage(p, "command.gopaint.brush.size",
                            Placeholder.parsed("size", String.valueOf(pb.size()))
                    );
                    return true;
                } catch (Exception e) {
                    plugin.bundle().sendMessage(p, "command.gopaint.usage.size");
                    return true;
                }
            }
            if (p.hasPermission(BetterGoPaint.ADMIN_PERMISSION)) {
                plugin.bundle().sendMessage(p, "command.gopaint.usage.admin");
                return true;
            }
            plugin.bundle().sendMessage(p, "command.gopaint.usage");
            return true;
        }
        return false;
    }

    @Override
    public @NotNull Plugin getPlugin() {
        return plugin;
    }

}
