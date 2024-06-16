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
package net.thenextlvl.gopaint.utils.curve;

import lombok.Getter;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.OptionalDouble;

@Getter
public class BezierSpline {

    private final Vector[] knots;
    private final BezierSplineSegment[] segments;
    private final double curveLength;

    public BezierSpline(LinkedList<Vector> curve) {
        this.knots = curve.toArray(new Vector[0]);
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
    public Vector getPoint(double point) {
        if (point >= segments.length) {
            return getPoint(segments.length - 1, 1);
        } else {
            return getPoint((int) Math.floor(point), point - Math.floor(point));
        }
    }

    @Contract(pure = true)
    public Vector getPoint(int segmentIndex, double factor) {
        assert (segmentIndex < segments.length);
        assert (factor > 0 && factor <= 1);
        return segments[segmentIndex].getPoint(factor);
    }

    public void calculateControlPoints() {
        if (segments.length == 0) return;

        var xFlat = Arrays.stream(knots).allMatch(l -> l.getBlockX() == knots[0].getX())
                ? OptionalDouble.of(knots[0].getX()) : OptionalDouble.empty();
        var yFlat = Arrays.stream(knots).allMatch(l -> l.getBlockY() == knots[0].getY())
                ? OptionalDouble.of(knots[0].getY()) : OptionalDouble.empty();
        var zFlat = Arrays.stream(knots).allMatch(l -> l.getBlockZ() == knots[0].getZ())
                ? OptionalDouble.of(knots[0].getZ()) : OptionalDouble.empty();

        if (segments.length == 1) {
            var midpoint = new Vector(0, 0, 0);
            midpoint.setX((segments[0].getStartPoint().getX() + segments[0].getEndPoint().getX()) / 2);
            midpoint.setY((segments[0].getStartPoint().getY() + segments[0].getEndPoint().getY()) / 2);
            midpoint.setZ((segments[0].getStartPoint().getZ() + segments[0].getEndPoint().getZ()) / 2);
            segments[0].setIntermediatePoint1(midpoint);
            segments[0].setIntermediatePoint2(midpoint.clone());
        } else {
            segments[0].setCoefficient1(0);
            segments[0].setCoefficient2(2);
            segments[0].setCoefficient3(1);
            segments[0].getResult().setX(knots[0].getX() + 2 * knots[1].getX());
            segments[0].getResult().setY(knots[0].getY() + 2 * knots[1].getY());
            segments[0].getResult().setZ(knots[0].getZ() + 2 * knots[1].getZ());

            int n = knots.length - 1;
            float m;

            for (int i = 1; i < n - 1; i++) {
                segments[i].setCoefficient1(1);
                segments[i].setCoefficient2(4);
                segments[i].setCoefficient3(1);
                segments[i].getResult().setX(4 * knots[i].getX() + 2 * knots[i + 1].getX());
                segments[i].getResult().setY(4 * knots[i].getY() + 2 * knots[i + 1].getY());
                segments[i].getResult().setZ(4 * knots[i].getZ() + 2 * knots[i + 1].getZ());
            }

            segments[n - 1].setCoefficient1(2);
            segments[n - 1].setCoefficient2(7);
            segments[n - 1].setCoefficient3(0);
            segments[n - 1].getResult().setX(8 * knots[n - 1].getX() + knots[n].getX());
            segments[n - 1].getResult().setY(8 * knots[n - 1].getY() + knots[n].getY());
            segments[n - 1].getResult().setZ(8 * knots[n - 1].getZ() + knots[n].getZ());

            for (int i = 1; i < n; i++) {
                m = segments[i].getCoefficient1() / segments[i - 1].getCoefficient2();
                segments[i].setCoefficient2(segments[i].getCoefficient2() - m * segments[i - 1].getCoefficient3());
                segments[i].getResult().setX(segments[i].getResult().getX() - m * segments[i - 1].getResult().getX());
                segments[i].getResult().setY(segments[i].getResult().getY() - m * segments[i - 1].getResult().getY());
                segments[i].getResult().setZ(segments[i].getResult().getZ() - m * segments[i - 1].getResult().getZ());
            }

            segments[n - 1].getIntermediatePoint1().setX(segments[n - 1].getResult().getX() / segments[n - 1].getCoefficient2());
            segments[n - 1].getIntermediatePoint1().setY(segments[n - 1].getResult().getY() / segments[n - 1].getCoefficient2());
            segments[n - 1].getIntermediatePoint1().setZ(segments[n - 1].getResult().getZ() / segments[n - 1].getCoefficient2());

            for (int i = n - 2; i >= 0; i--) {
                segments[i].getIntermediatePoint1().setX((segments[i].getResult().getX() - segments[i].getCoefficient3() * segments[i + 1].getIntermediatePoint1().getX()) / segments[i].getCoefficient2());
                segments[i].getIntermediatePoint1().setY((segments[i].getResult().getY() - segments[i].getCoefficient3() * segments[i + 1].getIntermediatePoint1().getY()) / segments[i].getCoefficient2());
                segments[i].getIntermediatePoint1().setZ((segments[i].getResult().getZ() - segments[i].getCoefficient3() * segments[i + 1].getIntermediatePoint1().getZ()) / segments[i].getCoefficient2());
            }

            for (int i = 0; i < n - 1; i++) {
                segments[i].getIntermediatePoint2().setX(2 * knots[i + 1].getX() - segments[i + 1].getIntermediatePoint1().getX());
                segments[i].getIntermediatePoint2().setY(2 * knots[i + 1].getY() - segments[i + 1].getIntermediatePoint1().getY());
                segments[i].getIntermediatePoint2().setZ(2 * knots[i + 1].getZ() - segments[i + 1].getIntermediatePoint1().getZ());
            }
            segments[n - 1].getIntermediatePoint2().setX(0.5 * (knots[n].getX() + segments[n - 1].getIntermediatePoint1().getX()));
            segments[n - 1].getIntermediatePoint2().setY(0.5 * (knots[n].getY() + segments[n - 1].getIntermediatePoint1().getY()));
            segments[n - 1].getIntermediatePoint2().setZ(0.5 * (knots[n].getZ() + segments[n - 1].getIntermediatePoint1().getZ()));
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
