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
class ImportCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> create(GoPaintPlugin plugin) {
        return Commands.literal("import")
                .requires(stack -> stack.getSender() instanceof Player)
                .executes(context -> importSettings(context, plugin));
    }

    private static int importSettings(CommandContext<CommandSourceStack> context, GoPaintPlugin plugin) {
        var player = (Player) context.getSource().getSender();

        var mainHand = player.getInventory().getItemInMainHand();
        var settings = plugin.brushController().getBrushSettings(player);
        var parsed = plugin.brushController().parseBrushSettings(mainHand);

        parsed.ifPresent(settings::importSettings);

        plugin.bundle().sendMessage(player, parsed.isPresent() ?
                "command.gopaint.import.success" : "command.gopaint.import.failed");

        return Command.SINGLE_SUCCESS;
    }
}
