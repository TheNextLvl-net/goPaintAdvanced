package net.thenextlvl.gopaint.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.gopaint.GoPaintPlugin;
import net.thenextlvl.gopaint.api.model.GoPaintProvider;
import net.thenextlvl.gopaint.commands.brigadier.SimpleCommand;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class ReloadCommand extends SimpleCommand {
    private ReloadCommand(final GoPaintPlugin plugin) {
        super(plugin, "reload", GoPaintProvider.ADMIN_PERMISSION);
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(final GoPaintPlugin plugin) {
        final var command = new ReloadCommand(plugin);
        return command.create().executes(command);
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) {
        final var sender = context.getSource().getSender();
        plugin.reloadConfig();
        plugin.bundle().sendMessage(sender, "command.gopaint.reloaded");
        return SINGLE_SUCCESS;
    }
}
