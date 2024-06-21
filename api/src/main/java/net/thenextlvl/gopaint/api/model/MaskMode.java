package net.thenextlvl.gopaint.api.model;

import com.sk89q.worldedit.EditSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.kyori.adventure.translation.Translatable;
import net.thenextlvl.gopaint.api.brush.Brush;
import net.thenextlvl.gopaint.api.brush.setting.BrushSettings;
import org.bukkit.block.Block;

@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public enum MaskMode implements Translatable {
    /**
     * This enumeration represents that no mask should be applied.
     */
    DISABLED("mask.mode.disabled"),
    /**
     * This enumeration represents that the mask material defined in the interface should be applied
     *
     * @see Brush#passesMaskCheck(BrushSettings, EditSession, Block)
     */
    INTERFACE("mask.mode.interface"),
    /**
     * This enumeration represents the complex mask defined by WorldEdit
     *
     * @see Brush#passesMaskCheck(BrushSettings, EditSession, Block)
     */
    WORLDEDIT("mask.mode.worldedit");

    private final String translationKey;
}
