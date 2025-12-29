package net.thenextlvl.gopaint.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.gopaint.GoPaintPlugin;
import net.thenextlvl.gopaint.commands.brigadier.SimpleCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class WandCommand extends SimpleCommand {
    private WandCommand(GoPaintPlugin plugin) {
        super(plugin, "wand", null);
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(GoPaintPlugin plugin) {
        var command = new WandCommand(plugin);
        return command.create()
                .requires(stack -> stack.getSender() instanceof Player)
                .executes(command);
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();
        plugin.bundle().sendMessage(player, giveWand(player)
                ? "command.gopaint.wand.success"
                : "command.gopaint.wand.failed");
        return SINGLE_SUCCESS;
    }

    private boolean giveWand(Player player) {
        var type = plugin.config().brushConfig().defaultBrushType();

        var inventory = player.getInventory();
        var first = inventory.first(type);

        if (first != -1) {
            if (inventory.getHeldItemSlot() == first) return true;

            if (first >= 0 && first <= 8) {
                inventory.setHeldItemSlot(first);
                return true;
            }

            var item = inventory.getItem(first);

            inventory.setItem(first, inventory.getItemInMainHand());
            inventory.setItemInMainHand(item);

            return true;
        }

        if (inventory.getItemInMainHand().isEmpty()) {
            inventory.setItemInMainHand(new ItemStack(type));
            return true;
        }

        var empty = inventory.firstEmpty();
        if (empty == -1) return false;

        inventory.setItem(empty, inventory.getItemInMainHand());
        inventory.setItemInMainHand(new ItemStack(type));
        return true;
    }
}
