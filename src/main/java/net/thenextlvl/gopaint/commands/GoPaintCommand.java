package net.thenextlvl.gopaint.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.gopaint.GoPaintPlugin;
import net.thenextlvl.gopaint.api.model.GoPaintProvider;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class GoPaintCommand {
    public static LiteralCommandNode<CommandSourceStack> create(GoPaintPlugin plugin) {
        return Commands.literal("gopaint")
                .requires(stack -> stack.getSender().hasPermission(GoPaintProvider.USE_PERMISSION))
                .then(BrushCommand.create(plugin))
                .then(ExportCommand.create(plugin))
                .then(ImportCommand.create(plugin))
                .then(MenuCommand.create(plugin))
                .then(ReloadCommand.create(plugin))
                .then(SizeCommand.create(plugin))
                .then(ToggleCommand.create(plugin))
                .then(WandCommand.create(plugin))
                .build();
    }
}
