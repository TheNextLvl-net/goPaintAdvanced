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
import lombok.Setter;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Getter
@Setter
public class BezierSplineSegment {

    private final double[] lengths = new double[20];
    private Location p0, p1, p2, p3;
    private float a, b, c;
    private @Nullable Double xFlat, yFlat, zFlat;
    private Location r;
    private double curveLength;

    public BezierSplineSegment(Location p0, Location p3) {
        this.p0 = p0;
        this.p3 = p3;
        p1 = new Location(p0.getWorld(), 0, 0, 0);
        p2 = new Location(p0.getWorld(), 0, 0, 0);
        r = new Location(p0.getWorld(), 0, 0, 0);
    }

    public void setX(double xflat2) {
        p0.setX(xflat2);
        p1.setX(xflat2);
        p2.setX(xflat2);
        p3.setX(xflat2);
        xFlat = xflat2;
    }

    public void setY(double yflat2) {
        p0.setY(yflat2);
        p1.setY(yflat2);
        p2.setY(yflat2);
        p3.setY(yflat2);
        yFlat = yflat2;
    }

    public void setZ(double zflat2) {
        p0.setZ(zflat2);
        p1.setZ(zflat2);
        p2.setZ(zflat2);
        p3.setZ(zflat2);
        zFlat = zflat2;
    }

    public void calculateCurveLength() {
        Location current = p0.clone();
        double step = 0.05;
        lengths[0] = 0;
        Location temp;
        for (int i = 1; i < 20; i++) {
            temp = getPoint(i * step);
            lengths[i] = lengths[i - 1] + temp.distance(current);
            current = temp;
        }
        curveLength = lengths[19];
    }

    public Location getPoint(double f) {
        Location result = new Location(p0.getWorld(), 0, 0, 0);
        result.setX(Objects.requireNonNullElseGet(xFlat, () -> (Math.pow(1 - f, 3) * p0.getX()) + (3 * Math.pow(1 - f, 2) * f * p1.getX()) + (3 * (1 - f) * f * f * p2.getX()) + (Math.pow(f, 3) * p3.getX())));
        result.setY(Objects.requireNonNullElseGet(yFlat, () -> (Math.pow(1 - f, 3) * p0.getY()) + (3 * Math.pow(1 - f, 2) * f * p1.getY()) + (3 * (1 - f) * f * f * p2.getY()) + (Math.pow(f, 3) * p3.getY())));
        result.setZ(Objects.requireNonNullElseGet(zFlat, () -> (Math.pow(1 - f, 3) * p0.getZ()) + (3 * Math.pow(1 - f, 2) * f * p1.getZ()) + (3 * (1 - f) * f * f * p2.getZ()) + (Math.pow(f, 3) * p3.getZ())));
        return result;
    }
}
