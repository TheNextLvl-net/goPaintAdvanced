package net.thenextlvl.gopaint.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.gopaint.GoPaintPlugin;
import net.thenextlvl.gopaint.commands.suggestion.BrushSuggestionProvider;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
class BrushCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> create(GoPaintPlugin plugin) {
        return Commands.literal("brush")
                .then(Commands.argument("brush", ArgumentTypes.key())
                        .suggests(new BrushSuggestionProvider<>(plugin))
                        .requires(stack -> stack.getSender() instanceof Player)
                        .executes(context -> brush(context, plugin)));
    }

    private static int brush(CommandContext<CommandSourceStack> context, GoPaintPlugin plugin) {
        var player = (Player) context.getSource().getSender();
        var settings = plugin.brushController().getBrushSettings(player);
        var argument = context.getArgument("brush", Key.class);
        plugin.brushRegistry().getBrush(argument).ifPresentOrElse(brush -> {
            plugin.bundle().sendMessage(player, "brush.set",
                    Placeholder.component("brush", brush.getName(player)));
            settings.setBrush(brush);
        }, () -> plugin.bundle().sendMessage(player, "brush.unknown",
                Placeholder.parsed("input", argument.asString())));
        return Command.SINGLE_SUCCESS;
    }
}
