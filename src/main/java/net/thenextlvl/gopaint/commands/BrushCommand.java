package net.thenextlvl.gopaint.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.gopaint.GoPaintPlugin;
import net.thenextlvl.gopaint.commands.brigadier.SimpleCommand;
import net.thenextlvl.gopaint.commands.suggestion.BrushSuggestionProvider;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class BrushCommand extends SimpleCommand {
    private BrushCommand(GoPaintPlugin plugin) {
        super(plugin, "brush", null);
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(GoPaintPlugin plugin) {
        var command = new BrushCommand(plugin);
        return command.create().then(Commands.argument("brush", ArgumentTypes.key())
                .suggests(new BrushSuggestionProvider<>(plugin))
                .requires(stack -> stack.getSender() instanceof Player)
                .executes(command));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();
        var settings = plugin.brushController().getBrushSettings(player);
        var argument = context.getArgument("brush", Key.class);
        plugin.brushRegistry().getBrush(argument).ifPresentOrElse(brush -> {
            plugin.bundle().sendMessage(player, "brush.set",
                    Placeholder.component("brush", brush.getName(player)));
            settings.setBrush(brush);
        }, () -> plugin.bundle().sendMessage(player, "brush.unknown",
                Placeholder.parsed("input", argument.asString())));
        return SINGLE_SUCCESS;
    }
}
