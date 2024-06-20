package net.thenextlvl.gopaint.model;

import com.google.gson.annotations.SerializedName;
import net.thenextlvl.gopaint.api.model.MaskMode;
import net.thenextlvl.gopaint.api.model.SurfaceMode;
import org.bukkit.Axis;
import org.bukkit.Material;

import java.util.List;

public record PluginConfig(
        @SerializedName("generic") Generic generic,
        @SerializedName("thickness") Thickness thickness,
        @SerializedName("angle") Angle angle,
        @SerializedName("fracture") Fracture fracture
) {

    public record Generic(
            @SerializedName("default-brush") Material defaultBrush,
            @SerializedName("max-size") int maxSize,
            @SerializedName("default-size") int defaultSize,
            @SerializedName("default-chance") int defaultChance,
            @SerializedName("default-axis") Axis defaultAxis,
            @SerializedName("default-falloff-strength") int defaultFalloffStrength,
            @SerializedName("default-mixing-strength") int defaultMixingStrength,
            @SerializedName("disabled-worlds") List<String> disabledWorlds,
            @SerializedName("enabled-by-default") boolean enabledByDefault,
            @SerializedName("default-mask") Material defaultMask,
            @SerializedName("mask-mode") MaskMode maskMode,
            @SerializedName("surface-mode") SurfaceMode surfaceMode
    ) {
    }

    public record Thickness(
            @SerializedName("default-thickness") int defaultThickness,
            @SerializedName("max-thickness") int maxThickness
    ) {
    }

    public record Angle(
            @SerializedName("default-angle-distance") int defaultAngleDistance,
            @SerializedName("max-angle-distance") int maxAngleDistance,
            @SerializedName("min-angle-height-difference") double minAngleHeightDifference,
            @SerializedName("default-angle-height-difference") double defaultAngleHeightDifference,
            @SerializedName("max-angle-height-difference") double maxAngleHeightDifference
    ) {
    }

    public record Fracture(
            @SerializedName("default-fracture-distance") int defaultFractureDistance,
            @SerializedName("max-fracture-distance") int maxFractureDistance
    ) {
    }
}
