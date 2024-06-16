package net.thenextlvl.gopaint.api.brush.setting;

import net.thenextlvl.gopaint.api.brush.Brush;
import net.thenextlvl.gopaint.api.model.SurfaceMode;
import org.bukkit.Axis;
import org.bukkit.Material;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Random;

/**
 * The BrushSettings interface defines the configuration settings for a brush. It provides methods to retrieve information
 * about the brush's axis, brush type, list of blocks, mask material, enabled status, surface mode, angle-height
 * difference, angle distance, chance, falloff strength, fracture distance, mixing strength, size and thickness.
 */
public interface BrushSettings {
    /**
     * Returns the axis used by the brush settings.
     *
     * @return the axis used by the brush settings
     */
    Axis getAxis();

    /**
     * Returns the brush used by the brush settings.
     *
     * @return The brush used by the brush settings.
     */
    Brush getBrush();

    /**
     * Returns the list of blocks used by the brush settings.
     *
     * @return the list of blocks used by the brush settings
     */
    List<Material> getBlocks();

    /**
     * Retrieves the mask material used by the brush settings.
     *
     * @return The mask material.
     * @deprecated the mask-material is going to be replaced with a WorldEdit Mask
     */
    @Deprecated(since = "1.1.0-SNAPSHOT")
    @ApiStatus.ScheduledForRemoval(inVersion = "1.2.0")
    Material getMask();

    /**
     * Checks if the mask is enabled.
     *
     * @return true if the mask is enabled, false otherwise.
     */
    boolean isMaskEnabled();

    /**
     * Returns the surface mode used by the brush settings.
     *
     * @return The surface mode used by the brush settings.
     */
    SurfaceMode getSurfaceMode();

    /**
     * Returns the angle-height difference used by the brush settings.
     *
     * @return The angle-height difference used by the brush settings.
     */
    double getAngleHeightDifference();

    /**
     * Returns the angle distance used by the brush settings.
     *
     * @return The angle distance used by the brush settings.
     */
    int getAngleDistance();

    /**
     * Returns the chance of the brush being applied to a block.
     *
     * @return The chance of the brush being applied to a block.
     */
    int getChance();

    /**
     * Returns the falloff strength used by the brush settings.
     *
     * @return The falloff strength used by the brush settings.
     */
    int getFalloffStrength();

    /**
     * Returns the fracture distance used by the brush settings.
     *
     * @return The fracture distance used by the brush settings.
     */
    int getFractureDistance();

    /**
     * Returns the mixing strength used by the brush settings.
     *
     * @return The mixing strength used by the brush settings.
     */
    int getMixingStrength();

    /**
     * Returns the size of the brush settings.
     *
     * @return The size of the brush settings.
     */
    int getSize();

    /**
     * Returns the thickness used by the brush settings.
     *
     * @return The thickness used by the brush settings.
     */
    int getThickness();

    /**
     * Picks a random block material from {@link #getBlocks()}.
     *
     * @return The randomly picked block material.
     */
    Material getRandomBlock();

    /**
     * The random number generator instance.
     *
     * @return a Random instance
     */
    Random getRandom();
}
