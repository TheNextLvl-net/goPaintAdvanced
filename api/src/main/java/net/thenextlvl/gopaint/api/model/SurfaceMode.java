package net.thenextlvl.gopaint.api.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.gopaint.api.math.Surface;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum SurfaceMode {
    /**
     * This enumeration represents a more intuitive check.
     *
     * @see Surface#isDirectlyOnSurface(Block)
     */
    DIRECT("Direct"),
    /**
     * This enumeration represents that surface mode is disabled.
     *
     * @see Surface#isOnSurface(Block, SurfaceMode, Location)
     */
    DISABLED("Disabled"),
    /**
     * This enumeration represents the original surface mode check.
     *
     * @see Surface#isRelativelyOnSurface(Block, Location)
     */
    RELATIVE("Relative");

    private final String name;

    public static Optional<SurfaceMode> byName(String name) {
        return Arrays.stream(values())
                .filter(surfaceMode -> surfaceMode.getName().equals(name))
                .findAny();
    }
}
