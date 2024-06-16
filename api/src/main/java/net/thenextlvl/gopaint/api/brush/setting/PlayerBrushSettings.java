package net.thenextlvl.gopaint.api.brush.setting;

import net.thenextlvl.gopaint.api.brush.Brush;
import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;

/**
 * The PlayerBrushSettings interface extends the BrushSettings interface and the InventoryHolder interface.
 * It represents the configuration settings for a brush specifically used by a player.
 */
public interface PlayerBrushSettings extends BrushSettings, InventoryHolder {
    /**
     * Checks whether the brush is enabled.
     *
     * @return true if the brush is enabled, false if it is disabled
     */
    boolean isEnabled();

    /**
     * Enables or disables the brush.
     *
     * @return true if the brush is enabled, false if it is disabled
     */
    boolean toggle();

    /**
     * Adds a block to the block palette.
     *
     * @param type The Material type of the block to add.
     * @param slot The slot index in the block palette where the block should be added.
     */
    void addBlock(Material type, int slot);

    /**
     * Cycle the axis of the brush.
     */
    void cycleAxis();

    /**
     * Cycle the selected brush backward.
     */
    void cycleBrushBackward();

    /**
     * Cycle the selected brush forward.
     */
    void cycleBrushForward();

    /**
     * Cycle the surface mode of the brush.
     */
    void cycleSurfaceMode();

    /**
     * Decreases the angle distance of the brush.
     * <p>
     * The angle distance determines the angular range in which the brush will affect blocks.
     * A smaller angle distance will result in a more focused application of the brush.
     */
    void decreaseAngleDistance();

    /**
     * Decreases the height difference between angles.
     * <p>
     * The angle height difference determines the vertical range in which the brush will affect blocks.
     * A smaller angle height difference will result in a more focused application of the brush.
     *
     * @param amount The amount by which to decrease the angle height difference.
     */
    void decreaseAngleHeightDifference(int amount);

    /**
     * Decreases the brush size by the given amount.
     *
     * @param amount The amount by which to decrease the brush size.
     */
    void decreaseBrushSize(int amount);

    /**
     * Decreases the chance of the brush.
     * <p>
     * This method decreases the likelihood that the brush will affect blocks within its range when applied.
     */
    void decreaseChance();

    /**
     * Decreases the falloff strength of the brush.
     * <p>
     * The falloff strength determines the rate at which the brush's effect diminishes
     * as it moves away from the center point. A lower falloff strength will result in
     * a more gradual diminishing of the brush's effect.
     */
    void decreaseFalloffStrength();

    /**
     * Decreases the fracture distance of the brush.
     *
     * <p>
     * The fracture distance determines the distance between fractured block replicas when the brush is applied.
     * A smaller fracture distance will result in more closely spaced replicas, resulting in a more dense pattern.
     */
    void decreaseFractureDistance();

    /**
     * Decreases the mixing strength of the brush.
     * <p>
     * The mixing strength determines the intensity of the brush's effect when applied.
     * A lower mixing strength will result in a less prominent effect.
     */
    void decreaseMixingStrength();

    /**
     * Decreases the thickness of the brush.
     * <p>
     * This method decreases the thickness of the brush, allowing it to cover a smaller area when applied.
     */
    void decreaseThickness();

    /**
     * Exports the brush settings visually onto the given ItemStack.
     *
     * @param itemStack The ItemStack to export the brush settings to.
     */
    void export(ItemStack itemStack);

    /**
     * Increases the angle distance of the brush.
     *
     * <p>
     * The angle distance determines the angular range in which the brush will affect blocks.
     * A larger angle distance will result in a broader application of the brush.
     */
    void increaseAngleDistance();

    /**
     * Increases the height difference between angles in the brush.
     *
     * <p>
     * The angle height difference determines the vertical range in which the brush will affect blocks.
     * A larger angle height difference will result in a broader application of the brush vertically.
     *
     * @param amount The amount by which to increase the angle height difference.
     */
    void increaseAngleHeightDifference(int amount);

    /**
     * Increases the size of the brush.
     *
     * <p>The size determines the area the brush will cover when applied.
     * This method increases the size of the brush by the specified amount.
     *
     * @param amount The amount by which to increase the brush size.
     */
    void increaseBrushSize(int amount);

    /**
     * Increases the chance of the brush.
     * <p>
     * This method increases the likelihood that the brush will affect blocks within its range when applied.
     */
    void increaseChance();

    /**
     * Increases the falloff strength of the brush.
     *
     * <p>
     * The falloff strength determines the rate at which the brush's effect diminishes
     * as it moves away from the center point. A lower falloff strength will result in
     * a more gradual diminishing of the brush's effect.
     */
    void increaseFalloffStrength();

    /**
     * Increases the fracture distance of the brush.
     * <p>
     * The fracture distance determines the distance between fractured block replicas when the brush is applied.
     * A larger fracture distance will result in more widely spaced replicas, resulting in a less dense pattern.
     */
    void increaseFractureDistance();

    /**
     * Increases the mixing strength of the brush.
     * <p>
     * The mixing strength determines the intensity of the brush's effect when applied.
     * A higher mixing strength will result in a more prominent effect.
     */
    void increaseMixingStrength();

    /**
     * Increases the thickness of the brush.
     * <p>
     * This method increases the thickness of the brush, allowing it to cover a larger area when applied.
     */
    void increaseThickness();

    /**
     * Removes a block from the block palette at the specified slot index.
     *
     * @param slot The slot index in the block palette from which the block should be removed.
     */
    void removeBlock(int slot);

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
     * @deprecated the mask-material is going to be replaced with a WorldEdit Mask
     */
    @Deprecated(since = "1.1.1")
    @ApiStatus.ScheduledForRemoval(inVersion = "1.2.0")
    void setMask(Material type);

    /**
     * Sets the size of the brush.
     * <p>
     * The size determines the area the brush will cover when applied.
     *
     * @param size The size of the brush. Must be a positive integer.
     */
    void setSize(int size);

    /**
     * Toggles the mask state of the brush.
     * <p>
     * The mask state determines whether the brush applies its effect only to blocks
     * matching the material specified by {@link PlayerBrushSettings#setMask(Material)}.
     * If the mask is enabled, the brush will only affect blocks of the specified material.
     * If the mask is disabled, the brush will affect blocks of any material.
     */
    void toggleMask();

    /**
     * Updates the inventory of the player brush settings.
     * <p>
     * This method should be called whenever there is a change
     * in the brush settings that requires updating the inventory display.
     */
    void updateInventory();
}
