package net.thenextlvl.gopaint.api.brush.mask;

import com.fastasyncworldedit.core.math.MutableVector3;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;

public record VisibleMask(Extent extent, Vector3 viewPoint) implements Mask {

    @Override
    public boolean test(BlockVector3 vector) {

        var location = new MutableVector3(
                vector.getX(),
                vector.getY(),
                vector.getZ()
        );

        var distanceX = viewPoint().getX() - location.getX();
        var distanceY = viewPoint().getY() - location.getY();
        var distanceZ = viewPoint().getZ() - location.getZ();

        location.setComponents(
                location.getX() + (distanceX > 1 ? 1 : distanceX > 0 ? 0.5 : 0),
                location.getY() + (distanceY > 1 ? 1 : distanceY > 0 ? 0.5 : 0),
                location.getZ() + (distanceZ > 1 ? 1 : distanceZ > 0 ? 0.5 : 0)
        );

        var distance = location.distance(viewPoint());
        for (var x = 1; x < distance; x++) {

            var moveX = distanceX * (x / distance);
            var moveY = distanceY * (x / distance);
            var moveZ = distanceZ * (x / distance);

            var point = location.add(moveX, moveY, moveZ).toBlockPoint();

            if (!extent().getBlock(point).getMaterial().isAir()) return false;
        }
        return true;
    }

    @Override
    public Mask copy() {
        return new VisibleMask(extent(), viewPoint());
    }
}
