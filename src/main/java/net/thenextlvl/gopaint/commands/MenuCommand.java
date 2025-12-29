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
class MenuCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> create(GoPaintPlugin plugin) {
        return Commands.literal("menu")
                .requires(stack -> stack.getSender() instanceof Player)
                .executes(context -> menu(context, plugin));
    }

    private static int menu(CommandContext<CommandSourceStack> context, GoPaintPlugin plugin) {
        var player = (Player) context.getSource().getSender();
        var settings = plugin.brushController().getBrushSettings(player);
        settings.getMainMenu().open();
        return Command.SINGLE_SUCCESS;
    }
}
