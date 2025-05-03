package net.thenextlvl.gopaint.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.thenextlvl.gopaint.GoPaintPlugin;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
class SizeCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> create(GoPaintPlugin plugin) {
        return Commands.literal("size")
                .requires(stack -> stack.getSender() instanceof Player)
                .then(Commands.argument("size", IntegerArgumentType.integer(1, 100))
                        .executes(context -> size(context, plugin)));
    }

    private static int size(CommandContext<CommandSourceStack> context, GoPaintPlugin plugin) {
        var player = (Player) context.getSource().getSender();
        var settings = plugin.brushController().getBrushSettings(player);
        settings.setBrushSize(context.getArgument("size", int.class));
        plugin.bundle().sendMessage(player, "command.gopaint.brush.size",
                Formatter.number("size", settings.getBrushSize()));
        return Command.SINGLE_SUCCESS;
    }
}
