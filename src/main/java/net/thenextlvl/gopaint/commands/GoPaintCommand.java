package net.thenextlvl.gopaint.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.gopaint.GoPaintPlugin;
import net.thenextlvl.gopaint.api.model.GoPaintProvider;
import net.thenextlvl.gopaint.commands.brigadier.BrigadierCommand;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class GoPaintCommand extends BrigadierCommand {
    private GoPaintCommand(GoPaintPlugin plugin) {
        super(plugin, "gopaint", GoPaintProvider.USE_PERMISSION);
    }

    public static LiteralCommandNode<CommandSourceStack> create(GoPaintPlugin plugin) {
        return new GoPaintCommand(plugin).create()
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
