package net.thenextlvl.gopaint.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.gopaint.GoPaintPlugin;
import net.thenextlvl.gopaint.api.brush.Brush;
import net.thenextlvl.gopaint.api.model.GoPaintProvider;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class GoPaintCommand {
    private final GoPaintPlugin plugin;

    public void register() {
        var command = Commands.literal("gopaint")
                .requires(stack -> stack.getSender().hasPermission(GoPaintProvider.USE_PERMISSION))
                .then(Commands.literal("size")
                        .requires(stack -> stack.getSender() instanceof Player)
                        .then(Commands.argument("size", IntegerArgumentType.integer(1, 100))
                                .executes(this::size)))
                .then(Commands.literal("menu")
                        .requires(stack -> stack.getSender() instanceof Player)
                        .executes(this::menu))
                .then(Commands.literal("brush")
                        .then(Commands.argument("brush", ArgumentTypes.key())
                                .suggests((context, builder) -> {
                                    plugin.brushRegistry().getBrushes()
                                            .map(Brush::key)
                                            .map(Key::asString)
                                            .filter(key -> key.contains(builder.getRemaining()))
                                            .forEach(builder::suggest);
                                    return builder.buildFuture();
                                })
                                .requires(stack -> stack.getSender() instanceof Player)
                                .executes(this::brush)))
                .then(Commands.literal("toggle")
                        .requires(stack -> stack.getSender() instanceof Player)
                        .executes(this::toggle))
                .then(Commands.literal("reload")
                        .requires(stack -> stack.getSender().hasPermission(GoPaintProvider.ADMIN_PERMISSION))
                        .executes(this::reload))
                .build();
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS.newHandler(event ->
                event.registrar().register(command, List.of("gp"))));
    }

    private int brush(CommandContext<CommandSourceStack> context) {
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

    private int menu(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();
        var settings = plugin.brushController().getBrushSettings(player);
        settings.getMainMenu().open();
        return Command.SINGLE_SUCCESS;
    }

    private int size(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();
        var settings = plugin.brushController().getBrushSettings(player);
        settings.setBrushSize(context.getArgument("size", int.class));
        plugin.bundle().sendMessage(player, "command.gopaint.brush.size",
                Placeholder.parsed("size", String.valueOf(settings.getBrushSize())));
        return Command.SINGLE_SUCCESS;
    }

    private int toggle(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();
        var settings = plugin.brushController().getBrushSettings(player);
        settings.setEnabled(!settings.isEnabled());
        var message = settings.isEnabled() ? "command.gopaint.brush.enabled"
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
