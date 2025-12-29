package net.thenextlvl.gopaint.commands.brigadier;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.gopaint.GoPaintPlugin;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class SimpleCommand extends BrigadierCommand implements Command<CommandSourceStack> {
    protected SimpleCommand(GoPaintPlugin plugin, String name, @Nullable String permission) {
        super(plugin, name, permission);
    }
}
