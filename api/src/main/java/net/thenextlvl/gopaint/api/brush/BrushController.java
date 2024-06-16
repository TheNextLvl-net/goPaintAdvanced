package net.thenextlvl.gopaint.api.brush;

import net.thenextlvl.gopaint.api.brush.setting.ItemBrushSettings;
import net.thenextlvl.gopaint.api.brush.setting.PlayerBrushSettings;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * This interface controls the brush selection for each player.
 */
public interface BrushController {
    /**
     * Retrieves the brush settings for a specific player.
     *
     * @param player The player whose brush settings are being retrieved.
     * @return The brush settings for the specified player.
     */
    PlayerBrushSettings getBrush(Player player);

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
     * Retrieves the lore for a specific brush. Each brush name is preceded by a color code
     * indicating whether it is the currently selected brush or not.
     *
     * @param brush The brush for which to retrieve the lore.
     * @return The lore for the specified brush.
     */
    String getBrushLore(Brush brush);

    /**
     * Retrieves the brush handler for the given name.
     *
     * @param name The name of the brush to look for.
     * @return An optional containing the brush handler, or empty if not found.
     */
    Optional<Brush> getBrushHandler(String name);

    /**
     * Removes the brush settings for a specific player.
     *
     * @param player The player whose brush settings are being removed.
     */
    void removeBrush(Player player);

    /**
     * Retrieves the next brush in the list of available brushes.
     *
     * @param brush The current brush, if null returns the first brush in the list.
     * @return The next brush in the list, or the first brush if the current brush is null.
     */
    Brush cycleForward(@Nullable Brush brush);

    /**
     * Retrieves the previous brush in the list of available brushes.
     *
     * @param brush The current brush.
     * @return The previous brush in the list, or the first brush if the current brush is null.
     */
    Brush cycleBackward(@Nullable Brush brush);
}