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
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.thenextlvl.gopaint.api.brush.SpherePatternBrush;
import net.thenextlvl.gopaint.api.brush.setting.BrushSettings;
import net.thenextlvl.gopaint.api.model.GoPaintProvider;
import net.thenextlvl.gopaint.brush.pattern.SprayPattern;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class SprayBrush extends SpherePatternBrush {
    private final GoPaintProvider provider;

    public SprayBrush(GoPaintProvider provider) {
        super(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjg4MGY3NjVlYTgwZGVlMzcwODJkY2RmZDk4MTJlZTM2ZmRhODg0ODY5MmE4NDFiZWMxYmJkOWVkNTFiYTIyIn19fQ==",
                Key.key("gopaint", "spray_brush")
        );
        this.provider = provider;
    }

    @Override
    public Component getName(Audience audience) {
        return provider.bundle().component("brush.name.spray", audience);
    }

    @Override
    public Component[] getDescription(Audience audience) {
        return new Component[]{provider.bundle().component("brush.description.spray", audience)};
    }

    @Override
    public Pattern buildPattern(EditSession session, BlockVector3 position, Player player, BrushSettings settings) {
        return new SprayPattern(session, position, player, settings);
    }
}
