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
    private ImportCommand(final GoPaintPlugin plugin) {
        super(plugin, "import", null);
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(final GoPaintPlugin plugin) {
        final var command = new ImportCommand(plugin);
        return command.create()
                .requires(stack -> stack.getSender() instanceof Player)
                .executes(command);
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) {
        final var player = (Player) context.getSource().getSender();

        final var mainHand = player.getInventory().getItemInMainHand();
        final var settings = plugin.brushController().getBrushSettings(player);
        final var parsed = plugin.brushController().parseBrushSettings(mainHand);

        parsed.ifPresent(settings::importSettings);

        plugin.bundle().sendMessage(player, parsed.isPresent() ?
                "command.gopaint.import.success" : "command.gopaint.import.failed");

        return SINGLE_SUCCESS;
    }
}
