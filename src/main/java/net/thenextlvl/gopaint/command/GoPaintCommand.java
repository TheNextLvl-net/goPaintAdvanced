package net.thenextlvl.gopaint.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.gopaint.GoPaintPlugin;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class GoPaintCommand {
    private final GoPaintPlugin plugin;

    public void register() {
        var command = Commands.literal("gopaint")
                .requires(stack -> stack.getSender().hasPermission(GoPaintPlugin.USE_PERMISSION))
                .then(Commands.literal("size")
                        .requires(stack -> stack.getSender() instanceof Player)
                        .then(Commands.argument("size", IntegerArgumentType.integer(1, 100))
                                .executes(this::size)))
                .then(Commands.literal("toggle")
                        .requires(stack -> stack.getSender() instanceof Player)
                        .executes(this::toggle))
                .then(Commands.literal("reload")
                        .requires(stack -> stack.getSender().hasPermission(GoPaintPlugin.ADMIN_PERMISSION))
                        .executes(this::reload))
                .build();
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS.newHandler(event ->
                event.registrar().register(command, List.of("gp"))));
    }

    private int size(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();
        var brush = plugin.brushController().getBrushSettings(player);
        brush.setSize(context.getArgument("size", int.class));
        plugin.bundle().sendMessage(player, "command.gopaint.brush.size",
                Placeholder.parsed("size", String.valueOf(brush.getSize())));
        return Command.SINGLE_SUCCESS;
    }

    private int toggle(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();
        var brush = plugin.brushController().getBrushSettings(player);
        var message = brush.toggle() ? "command.gopaint.brush.enabled"
                : "command.gopaint.brush.disabled";
        plugin.bundle().sendMessage(player, message);
        return Command.SINGLE_SUCCESS;
    }

    private int reload(CommandContext<CommandSourceStack> context) {
        var sender = context.getSource().getSender();
        plugin.reloadConfig();
        plugin.bundle().sendMessage(sender, "command.gopaint.reloaded");
        return Command.SINGLE_SUCCESS;
    }
}
