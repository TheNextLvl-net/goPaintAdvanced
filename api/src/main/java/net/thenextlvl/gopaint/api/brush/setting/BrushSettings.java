package net.thenextlvl.gopaint.api.brush.setting;

import com.fastasyncworldedit.core.function.mask.SingleBlockTypeMask;
import com.fastasyncworldedit.core.function.mask.SurfaceMask;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.function.mask.ExistingBlockMask;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.MaskIntersection;
import net.thenextlvl.gopaint.api.brush.PatternBrush;
import net.thenextlvl.gopaint.api.brush.mask.VisibleMask;
import net.thenextlvl.gopaint.api.model.SurfaceMode;
import org.bukkit.Axis;
import org.bukkit.Material;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
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
    PatternBrush getBrush();

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
     */
    Material getMask();

    /**
     * Determines whether the mask is enabled or not.
     *
     * @return true if the mask is enabled, false otherwise
     */
    boolean isMaskEnabled();

    /**
     * Retrieves the WorldEdit mask for the given session according to the brush settings.
     *
     * @param session The session used for retrieving the mask.
     * @return The WorldEdit mask
     */
    default Mask getMask(EditSession session) {
        var mask = Optional.ofNullable(session.getMask())
                .orElseGet(() -> new ExistingBlockMask(session.getWorld()));
        return isMaskEnabled() ? Optional.of(getMask())
                .map(BukkitAdapter::asBlockType)
                .map(blockType -> new SingleBlockTypeMask(session.getWorld(), blockType))
                .map(single -> MaskIntersection.of(single, mask))
                .orElse(mask) : mask;
    }

    default @Nullable Mask getSurfaceMask(Player player) {
        return switch (getSurfaceMode()) {
            case VISIBLE -> new VisibleMask(player.getWorld(), player.getLocation().add(0, 1.5, 0));
            case EXPOSED -> new SurfaceMask(player.getWorld());
            case DISABLED -> null;
        };
    }

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
    int getFractureStrength();

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
    int getBrushSize();

    /**
     * Returns the thickness used by the brush settings.
     *
     * @return The thickness used by the brush settings.
     */
    int getThickness();

    /**
     * The random number generator instance.
     *
     * @return a Random instance
     */
    Random getRandom();
}
