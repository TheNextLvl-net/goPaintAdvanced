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
package net.thenextlvl.gopaint.api.math;

import com.sk89q.worldedit.math.BlockVector3;

import java.util.HashSet;
import java.util.Set;

public class Sphere {

    public static Set<BlockVector3> getBlocksInRadius(BlockVector3 position, double radius) {
        var vectors = new HashSet<BlockVector3>();
        var loc1 = position.subtract((int) radius, (int) radius, (int) radius);
        var loc2 = position.add((int) radius, (int) radius, (int) radius);

        for (var x = loc1.x(); x <= loc2.x(); x++) {
            for (var y = loc1.y(); y <= loc2.y(); y++) {
                for (var z = loc1.z(); z <= loc2.z(); z++) {
                    var vector3 = BlockVector3.at(x, y, z);
                    if (vector3.distance(position) >= radius) continue;
                    vectors.add(vector3);
                }
            }
        }
        return vectors;
    }
}
