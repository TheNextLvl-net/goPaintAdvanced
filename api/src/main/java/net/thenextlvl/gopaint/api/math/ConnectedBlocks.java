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

import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.block.BlockFace;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConnectedBlocks {

    private static final BlockFace[] faces = new BlockFace[]{
            BlockFace.NORTH,
            BlockFace.SOUTH,
            BlockFace.EAST,
            BlockFace.WEST,
            BlockFace.UP,
            BlockFace.DOWN,
    };

    /**
     * Returns a stream of connected blocks starting from a given location, based on a list of blocks.
     * Only blocks of the same type as the start block are considered.
     *
     * @param vector3 the starting location
     * @param blocks  the list of blocks to check for connectivity
     * @return a stream of connected blocks
     */
    public static Stream<BlockVector3> getConnectedBlocks(Extent world, BlockVector3 vector3, Set<BlockVector3> blocks) {
        var startBlock = world.getFullBlock(vector3);
        var connected = new HashSet<BlockVector3>();
        var toCheck = new LinkedList<BlockVector3>();

        toCheck.add(vector3);
        connected.add(vector3);

        while (!toCheck.isEmpty() && connected.size() < blocks.size()) {
            var current = toCheck.poll();
            var neighbors = Arrays.stream(faces)
                    .map(face -> current.add(face.getModX(), face.getModY(), face.getModZ()))
                    .filter(relative -> !connected.contains(relative))
                    .filter(blocks::contains)
                    .filter(relative -> world.getBlock(relative).getMaterial().equals(startBlock.getMaterial()))
                    .collect(Collectors.toSet());

            connected.addAll(neighbors);
            toCheck.addAll(neighbors);
        }

        return connected.stream();
    }
}
