package net.thenextlvl.gopaint.api.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.kyori.adventure.translation.Translatable;
import net.thenextlvl.gopaint.api.math.Surface;
import org.bukkit.Location;
import org.bukkit.block.Block;

@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public enum SurfaceMode implements Translatable {
    /**
     * This enumeration represents a more intuitive check.
     *
     * @see Surface#isDirectlyOnSurface(Block)
     */
    DIRECT("surface.mode.direct"),
    /**
     * This enumeration represents that surface mode is disabled.
     *
     * @see Surface#isOnSurface(Block, SurfaceMode, Location)
     */
    DISABLED("surface.mode.disabled"),
    /**
     * This enumeration represents the original surface mode check.
     *
     * @see Surface#isRelativelyOnSurface(Block, Location)
     */
    RELATIVE("surface.mode.relative");

    private final String translationKey;
}
