package net.thenextlvl.gopaint.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.gopaint.GoPaintPlugin;
import net.thenextlvl.gopaint.commands.brigadier.SimpleCommand;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class MenuCommand extends SimpleCommand {
    private MenuCommand(final GoPaintPlugin plugin) {
        super(plugin, "menu", null);
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(final GoPaintPlugin plugin) {
        final var command = new MenuCommand(plugin);
        return command.create()
                .requires(stack -> stack.getSender() instanceof Player)
                .executes(command);
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) {
        final var player = (Player) context.getSource().getSender();
        final var settings = plugin.brushController().getBrushSettings(player);
        settings.getMainMenu().open();
        return SINGLE_SUCCESS;
    }
}
