package net.thenextlvl.gopaint.brush.standard;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.thenextlvl.gopaint.api.brush.SpherePatternBrush;
import net.thenextlvl.gopaint.api.brush.setting.BrushSettings;
import net.thenextlvl.gopaint.api.model.GoPaintProvider;
import net.thenextlvl.gopaint.brush.pattern.UnderlayPattern;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class UnderlayBrush extends SpherePatternBrush {
    private final GoPaintProvider provider;

    public UnderlayBrush(GoPaintProvider provider) {
        super(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzFlNTY1YzFlMDVhODIzZDgxNjMwMjY4N2E5OGQ1ZmUyZDA2NmFhMTkxNDMzNjg4NDRhMGM0MzAyNzYyNDljMyJ9fX0=",
                Key.key("gopaint", "underlay_brush")
        );
        this.provider = provider;
    }

    @Override
    public Component getName(Audience audience) {
        return provider.bundle().component("brush.name.underlay", audience);
    }

    @Override
    public Component[] getDescription(Audience audience) {
        return new Component[]{
                provider.bundle().component("brush.description.underlay.1", audience),
                provider.bundle().component("brush.description.underlay.2", audience)
        };
    }

    @Override
    public Pattern buildPattern(EditSession session, BlockVector3 position, Player player, BrushSettings settings) {
        return new UnderlayPattern(session, position, player, settings);
    }
}
