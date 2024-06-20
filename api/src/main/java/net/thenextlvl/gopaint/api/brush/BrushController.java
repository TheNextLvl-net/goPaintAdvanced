package net.thenextlvl.gopaint.api.brush;

import net.thenextlvl.gopaint.api.brush.setting.ItemBrushSettings;
import net.thenextlvl.gopaint.api.brush.setting.PlayerBrushSettings;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * This interface controls the brush settings for each player.
 */
public interface BrushController {
    /**
     * Retrieves the brush settings for a specific player.
     *
     * @param player The player whose brush settings are being retrieved.
     * @return The brush settings for the specified player.
     */
    PlayerBrushSettings getBrushSettings(Player player);

    /**
     * Parses the brush settings from the provided item stack.
     *
     * @param itemStack The item stack to parse the brush settings from.
     * @return An optional containing the parsed brush settings, or empty if the item does not contain brush settings.
     */
    Optional<ItemBrushSettings> parseBrushSettings(ItemStack itemStack);

    /**
     * Parses the brush settings from the given brush and item metadata.
     *
     * @param brush    The brush used for painting.
     * @param itemMeta The metadata of the item.
     * @return The parsed brush settings.
     */
    ItemBrushSettings parseBrushSettings(Brush brush, ItemMeta itemMeta);

    /**
     * Removes the brush settings for a specific player.
     *
     * @param player The player whose brush settings are being removed.
     */
    void removeBrushSettings(Player player);
}
