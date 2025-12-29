package net.thenextlvl.gopaint.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.gopaint.GoPaintPlugin;
import net.thenextlvl.gopaint.commands.brigadier.SimpleCommand;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class ExportCommand extends SimpleCommand {
    private ExportCommand(GoPaintPlugin plugin) {
        super(plugin, "export", null);
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(GoPaintPlugin plugin) {
        var command = new ExportCommand(plugin);
        return command.create()
                .requires(stack -> stack.getSender() instanceof Player)
                .executes(command);
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();

        var mainHand = player.getInventory().getItemInMainHand();
        var settings = plugin.brushController().getBrushSettings(player);

        plugin.bundle().sendMessage(player, settings.exportSettings(mainHand) ?
                "command.gopaint.export.success" : "command.gopaint.export.failed");

        return SINGLE_SUCCESS;
    }
}
