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

import net.thenextlvl.gopaint.api.model.Block;

public class Height {

    /**
     * Returns the nearest non-empty block height above or below the given block.
     *
     * @param block the block to find the nearest non-empty block for
     * @return the y-coordinate of the nearest non-empty block
     */
    public static int getNearestNonEmptyBlock(Block block) {
        if (block.material().isAir()) {
            for (var y = block.vector().getY(); y >= block.world().getMinY(); y--) {
                if (block.world().getBlock(block.vector().getX(), y, block.vector().getZ()).isAir()) continue;
                return y + 1;
            }
            return block.world().getMinY();
        } else {
            for (var y = block.vector().getY(); y <= block.world().getMaxY(); y++) {
                if (!block.world().getBlock(block.vector().getX(), y, block.vector().getZ()).isAir()) continue;
                return y;
            }
            return block.world().getMaxY();
        }
    }

    /**
     * Calculates the average height difference fracture of a block within a given distance.
     *
     * @param block    The block to calculate the average height difference fracture for.
     * @param height   The height to compare against.
     * @param distance The distance to consider when calculating the average height difference fracture.
     * @return The average height difference fracture of the block within the given distance.
     */
    public static double getAverageHeightDiffFracture(Block block, int height, int distance) {
        double totalHeight = 0;
        totalHeight += Math.abs(getNearestNonEmptyBlock(relative(block, distance, -distance))) - height;
        totalHeight += Math.abs(getNearestNonEmptyBlock(relative(block, distance, distance))) - height;
        totalHeight += Math.abs(getNearestNonEmptyBlock(relative(block, -distance, distance))) - height;
        totalHeight += Math.abs(getNearestNonEmptyBlock(relative(block, -distance, -distance))) - height;
        totalHeight += Math.abs(getNearestNonEmptyBlock(relative(block, 0, -distance))) - height;
        totalHeight += Math.abs(getNearestNonEmptyBlock(relative(block, 0, distance))) - height;
        totalHeight += Math.abs(getNearestNonEmptyBlock(relative(block, -distance, 0))) - height;
        totalHeight += Math.abs(getNearestNonEmptyBlock(relative(block, distance, 0))) - height;
        return (totalHeight / 8d) / distance;
    }

    private static Block relative(Block block, int x, int z) {
        var vector3 = block.vector().add(x, 0, z);
        return new Block(block.world().getFullBlock(vector3), vector3, block.world());
    }

    /**
     * Calculates the average height difference angle of a block within a given distance.
     *
     * @param block    The block to calculate the average height difference angle for.
     * @param distance The distance to consider when calculating the average height difference angle.
     * @return The average height difference angle of the block within the given distance.
     */
    public static double getAverageHeightDiffAngle(Block block, int distance) {
        var maxHeightDiff = 0;
        var maxHeightDiff2 = 0;
        var diff = Math.abs(getNearestNonEmptyBlock(relative(block, distance, -distance))
                            - getNearestNonEmptyBlock(relative(block, -distance, distance)));
        if (diff >= maxHeightDiff) {
            maxHeightDiff = diff;
            maxHeightDiff2 = maxHeightDiff;
        }
        diff = Math.abs(getNearestNonEmptyBlock(relative(block, distance, distance))
                        - getNearestNonEmptyBlock(relative(block, -distance, -distance)));
        if (diff > maxHeightDiff) {
            maxHeightDiff = diff;
            maxHeightDiff2 = maxHeightDiff;
        }
        diff = Math.abs(getNearestNonEmptyBlock(relative(block, distance, 0))
                        - getNearestNonEmptyBlock(relative(block, -distance, 0)));
        if (diff > maxHeightDiff) {
            maxHeightDiff = diff;
            maxHeightDiff2 = maxHeightDiff;
        }
        diff = Math.abs(getNearestNonEmptyBlock(relative(block, 0, -distance))
                        - getNearestNonEmptyBlock(relative(block, 0, distance)));
        if (diff > maxHeightDiff) {
            maxHeightDiff = diff;
            maxHeightDiff2 = maxHeightDiff;
        }

        return (maxHeightDiff2 + maxHeightDiff) / (distance * 2d);
    }
}
