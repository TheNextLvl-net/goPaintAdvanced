package net.thenextlvl.gopaint.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.gopaint.GoPaintPlugin;
import net.thenextlvl.gopaint.api.model.GoPaintProvider;
import org.jspecify.annotations.NullMarked;

@NullMarked
class ReloadCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> create(GoPaintPlugin plugin) {
        return Commands.literal("reload")
                .requires(stack -> stack.getSender().hasPermission(GoPaintProvider.ADMIN_PERMISSION))
                .executes(context -> reload(context, plugin));
    }

    private static int reload(CommandContext<CommandSourceStack> context, GoPaintPlugin plugin) {
        var sender = context.getSource().getSender();
        plugin.reloadConfig();
        plugin.bundle().sendMessage(sender, "command.gopaint.reloaded");
        return Command.SINGLE_SUCCESS;
    }
}
