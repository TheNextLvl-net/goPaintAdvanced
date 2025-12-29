package net.thenextlvl.gopaint.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.gopaint.GoPaintPlugin;
import net.thenextlvl.gopaint.commands.brigadier.SimpleCommand;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class ImportCommand extends SimpleCommand {
    private ImportCommand(GoPaintPlugin plugin) {
        super(plugin, "import", null);
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(GoPaintPlugin plugin) {
        var command = new ImportCommand(plugin);
        return command.create()
                .requires(stack -> stack.getSender() instanceof Player)
                .executes(command);
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();

        var mainHand = player.getInventory().getItemInMainHand();
        var settings = plugin.brushController().getBrushSettings(player);
        var parsed = plugin.brushController().parseBrushSettings(mainHand);

        parsed.ifPresent(settings::importSettings);

        plugin.bundle().sendMessage(player, parsed.isPresent() ?
                "command.gopaint.import.success" : "command.gopaint.import.failed");

        return SINGLE_SUCCESS;
    }
}
