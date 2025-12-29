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

import com.fastasyncworldedit.core.math.MutableBlockVector3;
import com.sk89q.worldedit.math.BlockVector3;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public class BezierSplineSegment {

    private final MutableBlockVector3 startPoint;
    private final MutableBlockVector3 endPoint;

    private MutableBlockVector3 intermediatePoint1 = MutableBlockVector3.at(0, 0, 0);
    private MutableBlockVector3 intermediatePoint2 = MutableBlockVector3.at(0, 0, 0);

    private float coefficient1;
    private float coefficient2;
    private float coefficient3;

    private @Nullable Double xFlat, yFlat, zFlat;

    private MutableBlockVector3 result = MutableBlockVector3.at(0, 0, 0);

    public BezierSplineSegment(MutableBlockVector3 startPoint, MutableBlockVector3 endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public void setX(double xFlat) {
        startPoint.mutX(xFlat);
        intermediatePoint1.mutX(xFlat);
        intermediatePoint2.mutX(xFlat);
        endPoint.mutX(xFlat);
        this.xFlat = xFlat;
    }

    public void setY(double yFlat) {
        startPoint.mutY(yFlat);
        intermediatePoint1.mutY(yFlat);
        intermediatePoint2.mutY(yFlat);
        endPoint.mutY(yFlat);
        this.yFlat = yFlat;
    }

    public void setZ(double zFlat) {
        startPoint.mutZ(zFlat);
        intermediatePoint1.mutZ(zFlat);
        intermediatePoint2.mutZ(zFlat);
        endPoint.mutZ(zFlat);
        this.zFlat = zFlat;
    }

    @Contract(pure = true)
    public double getCurveLength() {
        BlockVector3 current = startPoint;
        var lengths = new double[20];
        for (int i = 1; i < lengths.length; i++) {
            var point = getPoint(i * 0.05);
            lengths[i] = lengths[i - 1] + point.distance(current);
            current = point;
        }
        return lengths[lengths.length - 1];
    }

    @Contract(pure = true)
    public BlockVector3 getPoint(double factor) {
        var x = Objects.requireNonNullElseGet(xFlat, () -> calculatePoint(
                factor, startPoint.x(), intermediatePoint1.x(), intermediatePoint2.x(), endPoint.x()
        ));
        var y = Objects.requireNonNullElseGet(yFlat, () -> calculatePoint(
                factor, startPoint.y(), intermediatePoint1.y(), intermediatePoint2.y(), endPoint.y()
        ));
        var z = Objects.requireNonNullElseGet(zFlat, () -> calculatePoint(
                factor, startPoint.z(), intermediatePoint1.z(), intermediatePoint2.z(), endPoint.z()
        ));
        return BlockVector3.at(x, y, z);
    }

    @Contract(pure = true)
    private double calculatePoint(double factor, double startPoint, double intermediatePoint1, double intermediatePoint2, double endPoint) {
        return (Math.pow(1 - factor, 3) * startPoint) + (3 * Math.pow(1 - factor, 2) * factor * intermediatePoint1)
               + (3 * (1 - factor) * factor * factor * intermediatePoint2) + (Math.pow(factor, 3) * endPoint);
    }

    public MutableBlockVector3 getStartPoint() {
        return startPoint;
    }

    public MutableBlockVector3 getEndPoint() {
        return endPoint;
    }

    public MutableBlockVector3 getResult() {
        return result;
    }

    public MutableBlockVector3 getIntermediatePoint1() {
        return intermediatePoint1;
    }

    public MutableBlockVector3 getIntermediatePoint2() {
        return intermediatePoint2;
    }

    public float getCoefficient1() {
        return coefficient1;
    }

    public float getCoefficient2() {
        return coefficient2;
    }

    public float getCoefficient3() {
        return coefficient3;
    }

    public @Nullable Double getXFlat() {
        return xFlat;
    }

    public @Nullable Double getYFlat() {
        return yFlat;
    }

    public @Nullable Double getZFlat() {
        return zFlat;
    }

    public void setResult(MutableBlockVector3 result) {
        this.result = result;
    }

    public void setIntermediatePoint1(MutableBlockVector3 intermediatePoint1) {
        this.intermediatePoint1 = intermediatePoint1;
    }

    public void setIntermediatePoint2(MutableBlockVector3 intermediatePoint2) {
        this.intermediatePoint2 = intermediatePoint2;
    }

    public void setCoefficient1(float coefficient1) {
        this.coefficient1 = coefficient1;
    }

    public void setCoefficient2(float coefficient2) {
        this.coefficient2 = coefficient2;
    }

    public void setCoefficient3(float coefficient3) {
        this.coefficient3 = coefficient3;
    }

    public void setXFlat(@Nullable Double xFlat) {
        this.xFlat = xFlat;
    }

    public void setYFlat(@Nullable Double yFlat) {
        this.yFlat = yFlat;
    }

    public void setZFlat(@Nullable Double zFlat) {
        this.zFlat = zFlat;
    }
}