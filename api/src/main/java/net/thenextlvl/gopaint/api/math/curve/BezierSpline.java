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
import lombok.Getter;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;

@Getter
public class BezierSpline {

    private final MutableBlockVector3[] knots;
    private final BezierSplineSegment[] segments;
    private final double curveLength;

    public BezierSpline(List<MutableBlockVector3> curve) {
        this.knots = curve.toArray(new MutableBlockVector3[0]);
        this.segments = new BezierSplineSegment[knots.length - 1];
        for (var segment = 0; segment < knots.length - 1; segment++) {
            segments[segment] = new BezierSplineSegment(knots[segment], knots[segment + 1]);
        }
        calculateControlPoints();
        this.curveLength = calculateLength();
    }

    @Contract(pure = true)
    public double calculateLength() {
        var length = this.curveLength;
        for (var segment : segments) {
            length += segment.getCurveLength();
        }
        return length;
    }

    @Contract(pure = true)
    public BlockVector3 getPoint(double point) {
        if (point >= segments.length) {
            return getPoint(segments.length - 1, 1);
        } else {
            return getPoint((int) Math.floor(point), point - Math.floor(point));
        }
    }

    @Contract(pure = true)
    public BlockVector3 getPoint(int segmentIndex, double factor) {
        assert (segmentIndex < segments.length);
        assert (factor > 0 && factor <= 1);
        return segments[segmentIndex].getPoint(factor);
    }

    public void calculateControlPoints() {
        if (segments.length == 0) return;

        var xFlat = Arrays.stream(knots).allMatch(l -> l.x() == knots[0].x())
                ? OptionalDouble.of(knots[0].x()) : OptionalDouble.empty();
        var yFlat = Arrays.stream(knots).allMatch(l -> l.y() == knots[0].y())
                ? OptionalDouble.of(knots[0].y()) : OptionalDouble.empty();
        var zFlat = Arrays.stream(knots).allMatch(l -> l.z() == knots[0].z())
                ? OptionalDouble.of(knots[0].z()) : OptionalDouble.empty();

        if (segments.length == 1) {
            var midpoint = new MutableBlockVector3(0, 0, 0);
            midpoint.mutX((segments[0].getStartPoint().x() + segments[0].getEndPoint().x()) / 2);
            midpoint.mutY((segments[0].getStartPoint().y() + segments[0].getEndPoint().y()) / 2);
            midpoint.mutZ((segments[0].getStartPoint().z() + segments[0].getEndPoint().z()) / 2);
            segments[0].setIntermediatePoint1(midpoint);
            segments[0].setIntermediatePoint2(new MutableBlockVector3(midpoint));
        } else {
            segments[0].setCoefficient1(0);
            segments[0].setCoefficient2(2);
            segments[0].setCoefficient3(1);
            segments[0].getResult().mutX(knots[0].x() + 2 * knots[1].x());
            segments[0].getResult().mutY(knots[0].y() + 2 * knots[1].y());
            segments[0].getResult().mutZ(knots[0].z() + 2 * knots[1].z());

            int n = knots.length - 1;
            float m;

            for (int i = 1; i < n - 1; i++) {
                segments[i].setCoefficient1(1);
                segments[i].setCoefficient2(4);
                segments[i].setCoefficient3(1);
                segments[i].getResult().mutX(4 * knots[i].x() + 2 * knots[i + 1].x());
                segments[i].getResult().mutY(4 * knots[i].y() + 2 * knots[i + 1].y());
                segments[i].getResult().mutZ(4 * knots[i].z() + 2 * knots[i + 1].z());
            }

            segments[n - 1].setCoefficient1(2);
            segments[n - 1].setCoefficient2(7);
            segments[n - 1].setCoefficient3(0);
            segments[n - 1].getResult().mutX(8 * knots[n - 1].x() + knots[n].x());
            segments[n - 1].getResult().mutY(8 * knots[n - 1].y() + knots[n].y());
            segments[n - 1].getResult().mutZ(8 * knots[n - 1].z() + knots[n].z());

            for (int i = 1; i < n; i++) {
                m = segments[i].getCoefficient1() / segments[i - 1].getCoefficient2();
                segments[i].setCoefficient2(segments[i].getCoefficient2() - m * segments[i - 1].getCoefficient3());
                segments[i].getResult().mutX(segments[i].getResult().x() - m * segments[i - 1].getResult().x());
                segments[i].getResult().mutY(segments[i].getResult().y() - m * segments[i - 1].getResult().y());
                segments[i].getResult().mutZ(segments[i].getResult().z() - m * segments[i - 1].getResult().z());
            }

            segments[n - 1].getIntermediatePoint1().mutX(segments[n - 1].getResult().x() / segments[n - 1].getCoefficient2());
            segments[n - 1].getIntermediatePoint1().mutY(segments[n - 1].getResult().y() / segments[n - 1].getCoefficient2());
            segments[n - 1].getIntermediatePoint1().mutZ(segments[n - 1].getResult().z() / segments[n - 1].getCoefficient2());

            for (int i = n - 2; i >= 0; i--) {
                segments[i].getIntermediatePoint1().mutX((segments[i].getResult().x() - segments[i].getCoefficient3() * segments[i + 1].getIntermediatePoint1().x()) / segments[i].getCoefficient2());
                segments[i].getIntermediatePoint1().mutY((segments[i].getResult().y() - segments[i].getCoefficient3() * segments[i + 1].getIntermediatePoint1().y()) / segments[i].getCoefficient2());
                segments[i].getIntermediatePoint1().mutZ((segments[i].getResult().z() - segments[i].getCoefficient3() * segments[i + 1].getIntermediatePoint1().z()) / segments[i].getCoefficient2());
            }

            for (int i = 0; i < n - 1; i++) {
                segments[i].getIntermediatePoint2().mutX(2 * knots[i + 1].x() - segments[i + 1].getIntermediatePoint1().x());
                segments[i].getIntermediatePoint2().mutY(2 * knots[i + 1].y() - segments[i + 1].getIntermediatePoint1().y());
                segments[i].getIntermediatePoint2().mutZ(2 * knots[i + 1].z() - segments[i + 1].getIntermediatePoint1().z());
            }
            segments[n - 1].getIntermediatePoint2().mutX(0.5 * (knots[n].x() + segments[n - 1].getIntermediatePoint1().x()));
            segments[n - 1].getIntermediatePoint2().mutY(0.5 * (knots[n].y() + segments[n - 1].getIntermediatePoint1().y()));
            segments[n - 1].getIntermediatePoint2().mutZ(0.5 * (knots[n].z() + segments[n - 1].getIntermediatePoint1().z()));
        }

        xFlat.ifPresent(value -> Arrays.stream(segments).forEach(segment -> segment.setX(value)));
        yFlat.ifPresent(value -> Arrays.stream(segments).forEach(segment -> segment.setY(value)));
        zFlat.ifPresent(value -> Arrays.stream(segments).forEach(segment -> segment.setZ(value)));
    }

    @Override
    public String toString() {
        return knots.length + " points.";
    }
}