package net.thenextlvl.gopaint.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.thenextlvl.gopaint.GoPaintPlugin;
import net.thenextlvl.gopaint.commands.brigadier.SimpleCommand;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class SizeCommand extends SimpleCommand {
    private SizeCommand(GoPaintPlugin plugin) {
        super(plugin, "size", null);
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(GoPaintPlugin plugin) {
        var command = new SizeCommand(plugin);
        return command.create()
                .requires(stack -> stack.getSender() instanceof Player)
                .then(Commands.argument("size", IntegerArgumentType.integer(1, 100)).executes(command));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();
        var settings = plugin.brushController().getBrushSettings(player);
        settings.setBrushSize(context.getArgument("size", int.class));
        plugin.bundle().sendMessage(player, "command.gopaint.brush.size",
                Formatter.number("size", settings.getBrushSize()));
        return SINGLE_SUCCESS;
    }
}
