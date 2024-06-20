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
package net.thenextlvl.gopaint.util;

import core.paper.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public final class Items {

    public static ItemBuilder create(Material material, int amount, String name, String lore) {
        var itemStack = new ItemBuilder(material, amount);
        itemStack.editMeta(itemMeta -> {
            if (!lore.isEmpty()) {
                String[] loreListArray = lore.split("\n");
                List<Component> loreList = new ArrayList<>();
                for (String s : loreListArray) {
                    loreList.add(Component.text(s));
                }
                itemMeta.lore(loreList);
            }
            if (!name.isEmpty()) {
                itemMeta.displayName(Component.text(name));
            }
        });
        return itemStack;
    }

    public static ItemBuilder create(Material material) {
        return new ItemBuilder(material).hideTooltip(true);
    }

    public static ItemBuilder createHead(String texture, int amount, String name, String lore) {
        return create(Material.PLAYER_HEAD, amount, name, lore).headValue(texture);
    }
}
