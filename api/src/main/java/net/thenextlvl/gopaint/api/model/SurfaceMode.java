package net.thenextlvl.gopaint.api.model;

import com.fastasyncworldedit.core.function.mask.SurfaceMask;
import net.kyori.adventure.translation.Translatable;
import net.thenextlvl.gopaint.api.brush.mask.VisibleMask;
import org.jspecify.annotations.NullMarked;

@NullMarked
public enum SurfaceMode implements Translatable {
    /**
     * This enumeration represents that surface mode is disabled.
     */
    DISABLED("surface.mode.disabled"),
    /**
     * This enumeration represents a more intuitive check.
     *
     * @see SurfaceMask
     */
    EXPOSED("surface.mode.exposed"),
    /**
     * This enumeration represents the original surface mode check.
     *
     * @see VisibleMask
     */
    VISIBLE("surface.mode.visible");

    private final String translationKey;

    SurfaceMode(String translationKey) {
        this.translationKey = translationKey;
    }

    @Override
    public String translationKey() {
        return translationKey;
    }
}
