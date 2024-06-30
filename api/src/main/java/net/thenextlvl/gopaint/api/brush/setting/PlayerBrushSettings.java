package net.thenextlvl.gopaint.api.brush.setting;

import core.paper.gui.AbstractGUI;
import net.thenextlvl.gopaint.api.brush.PatternBrush;
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

    /**
     * Sets the placement chance of the brush.
     *
     * @param chance The placement chance of the brush.
     */
    void setChance(@Range(from = 10, to = 90) int chance);

    /**
     * Sets the thickness of the brush.
     *
     * @param thickness The thickness to set
     * @see PluginConfig.ThicknessConfig#maxThickness()
     */
    void setThickness(@Range(from = 1, to = Integer.MAX_VALUE) int thickness);

    /**
     * Sets the fracture strength of the brush.
     *
     * @param strength The fracture strength to set
     * @see PluginConfig.FractureConfig#maxFractureStrength()
     */
    void setFractureStrength(@Range(from = 1, to = Integer.MAX_VALUE) int strength);

    /**
     * Sets the angle distance of the brush.
     *
     * @param distance The angle distance to set
     * @see PluginConfig.AngleConfig#maxAngleDistance()
     */
    void setAngleDistance(@Range(from = 1, to = Integer.MAX_VALUE) int distance);

    /**
     * Sets the falloff strength of the brush.
     *
     * @param strength The falloff strength to set.
     */
    void setFalloffStrength(@Range(from = 10, to = 90) int strength);

    /**
     * Sets the mixing strength of the brush.
     * This determines how strongly the brush blends colors between adjacent blocks.
     *
     * @param strength The mixing strength to set.
     */
    void setMixingStrength(@Range(from = 10, to = 90) int strength);

    /**
     * Sets the angle height difference of the brush.
     * The angle height difference specifies the difference in height between adjacent blocks when applying the brush.
     *
     * @param difference The angle height difference to set.
     * @see PluginConfig.AngleConfig#minAngleHeightDifference()
     * @see PluginConfig.AngleConfig#maxAngleHeightDifference()
     */
    void setAngleHeightDifference(double difference);

    /**
     * Sets whether the mask is enabled or disabled for the brush.
     *
     * @param enabled true to enable the mask, false to disable it
     */
    void setMaskEnabled(boolean enabled);

    /**
     * Sets the surface mode of the brush.
     *
     * @param surfaceMode The surface mode to set for the brush.
     */
    void setSurfaceMode(SurfaceMode surfaceMode);

    /**
     * Sets the axis for the brush.
     *
     * @param axis The axis to set for the brush.
     */
    void setAxis(Axis axis);

    /**
     * Sets the list of blocks used by the brush settings.
     *
     * @param blocks The list of blocks to set.
     */
    void setBlocks(List<Material> blocks);

    /**
     * Sets the brush for the player's brush settings.
     *
     * @param brush The brush to set.
     */
    void setBrush(PatternBrush brush);

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
    PatternBrush getNextBrush(@Nullable PatternBrush brush);

    /**
     * Retrieves the previous brush in the list of available brushes.
     *
     * @param brush The current brush.
     * @return The previous brush in the list, or the first brush if the current brush is null.
     */
    PatternBrush getPreviousBrush(@Nullable PatternBrush brush);
}
