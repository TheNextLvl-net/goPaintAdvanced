package net.thenextlvl.gopaint.api.model;

import com.sk89q.worldedit.EditSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.gopaint.api.brush.Brush;
import net.thenextlvl.gopaint.api.brush.setting.BrushSettings;
import org.bukkit.block.Block;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum MaskMode {
    /**
     * This enumeration represents that no mask should be applied.
     */
    DISABLED("Disabled"),
    /**
     * This enumeration represents that the mask material defined in the interface should be applied
     *
     * @see Brush#passesMaskCheck(BrushSettings, EditSession, Block)
     */
    INTERFACE("Interface"),
    /**
     * This enumeration represents the complex mask defined by WorldEdit
     *
     * @see Brush#passesMaskCheck(BrushSettings, EditSession, Block)
     */
    WORLDEDIT("WorldEdit");

    private final String name;

    public static Optional<MaskMode> byName(String name) {
        return Arrays.stream(values())
                .filter(maskMode -> maskMode.getName().equals(name))
                .findAny();
    }
}
