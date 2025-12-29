package net.thenextlvl.gopaint.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.gopaint.GoPaintPlugin;
import net.thenextlvl.gopaint.commands.brigadier.SimpleCommand;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class ToggleCommand extends SimpleCommand {
    private ToggleCommand(GoPaintPlugin plugin) {
        super(plugin, "toggle", null);
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(GoPaintPlugin plugin) {
        var command = new ToggleCommand(plugin);
        return command.create()
                .requires(stack -> stack.getSender() instanceof Player)
                .executes(command);
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();
        var settings = plugin.brushController().getBrushSettings(player);
        settings.setEnabled(!settings.isEnabled());
        var message = settings.isEnabled() ? "command.gopaint.brush.enabled"
                : "command.gopaint.brush.disabled";
        plugin.bundle().sendMessage(player, message);
        return SINGLE_SUCCESS;
    }
}
