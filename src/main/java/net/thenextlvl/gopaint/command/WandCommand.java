package net.thenextlvl.gopaint.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.gopaint.GoPaintPlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
class WandCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> create(GoPaintPlugin plugin) {
        return Commands.literal("wand")
                .requires(stack -> stack.getSender() instanceof Player)
                .executes(context -> wand(context, plugin));
    }

    private static int wand(CommandContext<CommandSourceStack> context, GoPaintPlugin plugin) {
        var player = (Player) context.getSource().getSender();
        plugin.bundle().sendMessage(player, giveWand(player, plugin)
                ? "command.gopaint.wand.success"
                : "command.gopaint.wand.failed");
        return Command.SINGLE_SUCCESS;
    }

    private static boolean giveWand(Player player, GoPaintPlugin plugin) {
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
