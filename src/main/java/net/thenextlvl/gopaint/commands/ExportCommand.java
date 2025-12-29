package net.thenextlvl.gopaint.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.gopaint.GoPaintPlugin;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
class ExportCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> create(GoPaintPlugin plugin) {
        return Commands.literal("export")
                .requires(stack -> stack.getSender() instanceof Player)
                .executes(context -> exportSettings(context, plugin));
    }

    private static int exportSettings(CommandContext<CommandSourceStack> context, GoPaintPlugin plugin) {
        var player = (Player) context.getSource().getSender();

        var mainHand = player.getInventory().getItemInMainHand();
        var settings = plugin.brushController().getBrushSettings(player);

        plugin.bundle().sendMessage(player, settings.exportSettings(mainHand) ?
                "command.gopaint.export.success" : "command.gopaint.export.failed");

        return Command.SINGLE_SUCCESS;
    }
}
