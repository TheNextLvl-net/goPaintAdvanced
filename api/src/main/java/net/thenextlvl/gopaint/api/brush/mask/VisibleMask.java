package net.thenextlvl.gopaint.api.brush.mask;

import com.fastasyncworldedit.core.math.MutableVector3;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;

public record VisibleMask(Extent extent, Vector3 viewPoint) implements Mask {

    @Override
    public boolean test(final BlockVector3 vector) {

        final var location = new MutableVector3(
                vector.x(),
                vector.y(),
                vector.z()
        );

        final var distanceX = viewPoint().x() - location.x();
        final var distanceY = viewPoint().y() - location.y();
        final var distanceZ = viewPoint().z() - location.z();

        location.setComponents(
                location.x() + (distanceX > 1 ? 1 : distanceX > 0 ? 0.5 : 0),
                location.y() + (distanceY > 1 ? 1 : distanceY > 0 ? 0.5 : 0),
                location.z() + (distanceZ > 1 ? 1 : distanceZ > 0 ? 0.5 : 0)
        );

        final var distance = location.distance(viewPoint());
        for (var x = 1; x < distance; x++) {

            final var moveX = distanceX * (x / distance);
            final var moveY = distanceY * (x / distance);
            final var moveZ = distanceZ * (x / distance);

            final var point = location.add(moveX, moveY, moveZ).toBlockPoint();

            if (!extent().getBlock(point).getMaterial().isAir()) return false;
        }
        return true;
    }

    @Override
    public Mask copy() {
        return new VisibleMask(extent(), viewPoint());
    }
}
