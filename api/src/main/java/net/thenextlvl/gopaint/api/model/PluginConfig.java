package net.thenextlvl.gopaint.api.model;

import com.google.gson.annotations.SerializedName;
import net.kyori.adventure.key.Key;
import org.bukkit.Axis;
import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Set;

@NullMarked
public record PluginConfig(
        @SerializedName("brush") BrushConfig brushConfig,
        @SerializedName("thickness") ThicknessConfig thicknessConfig,
        @SerializedName("angle") AngleConfig angleConfig,
        @SerializedName("fracture") FractureConfig fractureConfig
) {

    public record BrushConfig(
            @SerializedName("default-brush-type") Material defaultBrushType,
            @SerializedName("default-brush") Key defaultBrush,
            @SerializedName("max-brush-size") int maxBrushSize,
            @SerializedName("default-size") int defaultSize,
            @SerializedName("default-chance") int defaultChance,
            @SerializedName("default-axis") Axis defaultAxis,
            @SerializedName("default-falloff-strength") int defaultFalloffStrength,
            @SerializedName("default-mixing-strength") int defaultMixingStrength,
            @SerializedName("disabled-worlds") Set<String> disabledWorlds,
            @SerializedName("enabled-by-default") boolean enabledByDefault,
            @SerializedName("default-mask") Material defaultMask,
            @SerializedName("mask") boolean mask,
            @SerializedName("surface-mode") SurfaceMode surfaceMode,
            @SerializedName("default-blocks") List<Material> defaultBlocks
    ) {
    }

    public record ThicknessConfig(
            @SerializedName("default-thickness") int defaultThickness,
            @SerializedName("max-thickness") int maxThickness
    ) {
    }

    public record AngleConfig(
            @SerializedName("default-angle-distance") int defaultAngleDistance,
            @SerializedName("max-angle-distance") int maxAngleDistance,
            @SerializedName("min-angle-height-difference") double minAngleHeightDifference,
            @SerializedName("default-angle-height-difference") double defaultAngleHeightDifference,
            @SerializedName("max-angle-height-difference") double maxAngleHeightDifference
    ) {
    }

    public record FractureConfig(
            @SerializedName("default-fracture-strength") int defaultFractureStrength,
            @SerializedName("max-fracture-strength") int maxFractureStrength
    ) {
    }
}
