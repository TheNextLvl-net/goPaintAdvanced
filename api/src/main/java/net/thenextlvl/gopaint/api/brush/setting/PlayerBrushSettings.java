package net.thenextlvl.gopaint.api.brush.setting;

import core.paper.gui.AbstractGUI;
import net.thenextlvl.gopaint.api.brush.Brush;
import net.thenextlvl.gopaint.api.model.MaskMode;
import net.thenextlvl.gopaint.api.model.PluginConfig;
import net.thenextlvl.gopaint.api.model.SurfaceMode;
import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.List;

/**
 * The PlayerBrushSettings interface extends the BrushSettings interface and the InventoryHolder interface.
 * It represents the configuration settings for a brush specifically used by a player.
 */
public interface PlayerBrushSettings extends BrushSettings {
    /**
     * Checks whether the brush is enabled.
     *
     * @return true if the brush is enabled, false if it is disabled
     */
    boolean isEnabled();

    /**
     * Enables or disables the brush.
     *
     * @param enabled true to enable the brush, false to disable it
     */
    void setEnabled(boolean enabled);

    /**
     * Adds a block to the block palette.
     *
     * @param type The Material type of the block to add.
     * @param slot The slot index in the block palette where the block should be added.
     */
    void addBlock(Material type, int slot);

    /**
     * Exports the brush settings visually onto the given ItemStack.
     *
     * @param itemStack The ItemStack to export the brush settings to.
     */
    void exportSettings(ItemStack itemStack);

    /**
     * Imports the item brush settings.
     *
     * @param settings The settings to import.
     */
    void importSettings(ItemBrushSettings settings);

    /**
     * Removes a block from the block palette at the specified slot index.
     *
     * @param slot The slot index in the block palette from which the block should be removed.
     */
    void removeBlock(int slot);

    void setChance(@Range(from = 10, to = 90) int chance);

    /**
     * @param thickness
     * @see PluginConfig.ThicknessConfig#maxThickness()
     */
    void setThickness(@Range(from = 1, to = Integer.MAX_VALUE) int thickness);

    /**
     * @param strength
     * @see PluginConfig.FractureConfig#maxFractureStrength()
     */
    void setFractureStrength(@Range(from = 1, to = Integer.MAX_VALUE) int strength);

    /**
     * @param distance
     * @see PluginConfig.AngleConfig#maxAngleDistance()
     */
    void setAngleDistance(@Range(from = 1, to = Integer.MAX_VALUE) int distance);

    void setFalloffStrength(@Range(from = 10, to = 90) int strength);

    void setMixingStrength(@Range(from = 10, to = 90) int strength);

    void setAngleHeightDifference(double difference);

    void setMaskMode(MaskMode maskMode);

    void setSurfaceMode(SurfaceMode surfaceMode);

    void setAxis(Axis axis);

    void setBlocks(List<Material> blocks);

    /**
     * Sets the brush for the player's brush settings.
     *
     * @param brush The brush to set.
     */
    void setBrush(Brush brush);

    /**
     * Sets the mask for the brush.
     * <p>
     * The mask determines whether the brush applies its effect only to blocks matching the material specified
     *
     * @param type The Material type to set as the mask.
     */
    void setMask(Material type);

    /**
     * Sets the size of the brush.
     * <p>
     * The size determines the area the brush will cover when applied.
     *
     * @param size The size of the brush. Must be a positive integer.
     * @see PluginConfig.BrushConfig#maxBrushSize()
     */
    void setBrushSize(@Range(from = 1, to = Integer.MAX_VALUE) int size);

    /**
     * Retrieves the main menu for the player.
     *
     * @return The main menu.
     */
    AbstractGUI getMainMenu();

    /**
     * Retrieves the brushes menu for the player.
     *
     * @return The brushes menu.
     */
    AbstractGUI getBrushesMenu();

    /**
     * Retrieves the next brush in the list of available brushes.
     *
     * @param brush The current brush, if null returns the first brush in the list.
     * @return The next brush in the list, or the first brush if the current brush is null.
     */
    Brush getNextBrush(@Nullable Brush brush);

    /**
     * Retrieves the previous brush in the list of available brushes.
     *
     * @param brush The current brush.
     * @return The previous brush in the list, or the first brush if the current brush is null.
     */
    Brush getPreviousBrush(@Nullable Brush brush);
}
