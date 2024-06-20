package net.thenextlvl.gopaint.brush;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import net.thenextlvl.gopaint.GoPaintPlugin;
import net.thenextlvl.gopaint.api.brush.Brush;
import net.thenextlvl.gopaint.api.brush.BrushRegistry;
import net.thenextlvl.gopaint.brush.standard.*;

import java.util.ArrayList;
import java.util.List;

public class CraftBrushRegistry implements BrushRegistry {
    private final List<Brush> brushes = new ArrayList<>(List.of(
            new SphereBrush(),
            new SprayBrush(),
            new SplatterBrush(),
            new DiscBrush(),
            new BucketBrush(),
            new AngleBrush(),
            new OverlayBrush(),
            new UnderlayBrush(),
            new FractureBrush(),
            new GradientBrush()
    ));

    public CraftBrushRegistry(GoPaintPlugin plugin) {
        brushes.add(new PaintBrush(plugin.bundle()));
    }

    @Override
    public List<Brush> getBrushes() {
        return ImmutableList.copyOf(brushes);
    }

    @Override
    public boolean isRegistered(Brush brush) {
        return brushes.contains(brush);
    }

    @Override
    public void registerBrush(Brush brush) throws IllegalStateException {
        Preconditions.checkState(!isRegistered(brush), "Brush already registered");
        brushes.add(brush);
    }

    @Override
    public void unregisterBrush(Brush brush) throws IllegalStateException {
        if (!brushes.remove(brush)) throw new IllegalStateException("Brush not registered");
    }

    @Override
    public Optional<Brush> getBrush(Key key) {
        return brushes.stream()
                .filter(brush -> brush.key().equals(key))
                .findAny();
    }
}
