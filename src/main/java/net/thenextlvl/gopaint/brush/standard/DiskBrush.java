/*
 * goPaint is designed to simplify painting inside of Minecraft.
 * Copyright (C) Arcaniax-Development
 * Copyright (C) Arcaniax team and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package net.thenextlvl.gopaint.brush.standard;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.thenextlvl.gopaint.api.brush.PatternBrush;
import net.thenextlvl.gopaint.api.brush.pattern.BuildPattern;
import net.thenextlvl.gopaint.api.brush.setting.BrushSettings;
import net.thenextlvl.gopaint.api.model.GoPaintProvider;
import net.thenextlvl.gopaint.brush.pattern.ShufflePattern;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class DiskBrush extends PatternBrush {
    private final GoPaintProvider provider;

    public DiskBrush(GoPaintProvider provider) {
        super(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjFmMjgyNTBkMWU0MjBhNjUxMWIwMzk2NDg2OGZjYTJmNTYzN2UzYWJhNzlmNGExNjNmNGE4ZDYxM2JlIn19fQ==",
                Key.key("gopaint", "disk_brush")
        );
        this.provider = provider;
    }

    @Override
    public Component getName(Audience audience) {
        return provider.bundle().component("brush.name.disk", audience);
    }

    @Override
    public Component[] getDescription(Audience audience) {
        return new Component[]{
                provider.bundle().component("brush.description.disk.1", audience),
                provider.bundle().component("brush.description.disk.2", audience),
                provider.bundle().component("brush.description.disk.3", audience)
        };
    }

    @Override
    public Pattern buildPattern(EditSession session, BlockVector3 position, Player player, BrushSettings settings) {
        return new ShufflePattern(session, position, player, settings);
    }

    @Override
    public void build(EditSession session, BlockVector3 position, Pattern pattern, double size) throws MaxChangedBlocksException {
        if (!(pattern instanceof BuildPattern buildPattern)) return;
        switch (buildPattern.settings().getAxis()) {
            case X -> session.makeSphere(position, pattern, 0, size, size, true);
            case Y -> session.makeSphere(position, pattern, size, 0, size, true);
            case Z -> session.makeSphere(position, pattern, size, size, 0, true);
        }
    }
}
