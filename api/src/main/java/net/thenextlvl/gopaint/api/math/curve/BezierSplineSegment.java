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
package net.thenextlvl.gopaint.api.math.curve;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
public class BezierSplineSegment {

    private final Vector startPoint;
    private final Vector endPoint;

    private Vector intermediatePoint1 = new Vector(0, 0, 0);
    private Vector intermediatePoint2 = new Vector(0, 0, 0);

    private float coefficient1;
    private float coefficient2;
    private float coefficient3;

    private @Nullable Double xFlat, yFlat, zFlat;

    private Vector result = new Vector(0, 0, 0);

    public void setX(double xFlat) {
        startPoint.setX(xFlat);
        intermediatePoint1.setX(xFlat);
        intermediatePoint2.setX(xFlat);
        endPoint.setX(xFlat);
        this.xFlat = xFlat;
    }

    public void setY(double yFlat) {
        startPoint.setY(yFlat);
        intermediatePoint1.setY(yFlat);
        intermediatePoint2.setY(yFlat);
        endPoint.setY(yFlat);
        this.yFlat = yFlat;
    }

    public void setZ(double zFlat) {
        startPoint.setZ(zFlat);
        intermediatePoint1.setZ(zFlat);
        intermediatePoint2.setZ(zFlat);
        endPoint.setZ(zFlat);
        this.zFlat = zFlat;
    }

    @Contract(pure = true)
    public double getCurveLength() {
        var current = startPoint.clone();
        var lengths = new double[20];
        for (int i = 1; i < lengths.length; i++) {
            var point = getPoint(i * 0.05);
            lengths[i] = lengths[i - 1] + point.distance(current);
            current = point;
        }
        return lengths[lengths.length - 1];
    }

    @Contract(pure = true)
    public Vector getPoint(double factor) {
        var x = Objects.requireNonNullElseGet(xFlat, () -> calculatePoint(
                factor, startPoint.getX(), intermediatePoint1.getX(), intermediatePoint2.getX(), endPoint.getX()
        ));
        var y = Objects.requireNonNullElseGet(yFlat, () -> calculatePoint(
                factor, startPoint.getY(), intermediatePoint1.getY(), intermediatePoint2.getY(), endPoint.getY()
        ));
        var z = Objects.requireNonNullElseGet(zFlat, () -> calculatePoint(
                factor, startPoint.getZ(), intermediatePoint1.getZ(), intermediatePoint2.getZ(), endPoint.getZ()
        ));
        return new Vector(x, y, z);
    }

    @Contract(pure = true)
    private double calculatePoint(double factor, double startPoint, double intermediatePoint1, double intermediatePoint2, double endPoint) {
        return (Math.pow(1 - factor, 3) * startPoint) + (3 * Math.pow(1 - factor, 2) * factor * intermediatePoint1)
               + (3 * (1 - factor) * factor * factor * intermediatePoint2) + (Math.pow(factor, 3) * endPoint);
    }
}
