package net.thenextlvl.gopaint.brush;

import com.google.common.base.Preconditions;
import net.kyori.adventure.key.Key;
import net.thenextlvl.gopaint.GoPaintPlugin;
import net.thenextlvl.gopaint.api.brush.Brush;
import net.thenextlvl.gopaint.api.brush.BrushRegistry;
import net.thenextlvl.gopaint.brush.standard.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class CraftBrushRegistry implements BrushRegistry {
    private final List<Brush> brushes = new LinkedList<>();

    public CraftBrushRegistry(GoPaintPlugin plugin) {
        registerBrush(SphereBrush.INSTANCE);
        registerBrush(SprayBrush.INSTANCE);
        registerBrush(SplatterBrush.INSTANCE);
        registerBrush(DiscBrush.INSTANCE);
        registerBrush(BucketBrush.INSTANCE);
        registerBrush(AngleBrush.INSTANCE);
        registerBrush(OverlayBrush.INSTANCE);
        registerBrush(UnderlayBrush.INSTANCE);
        registerBrush(FractureBrush.INSTANCE);
        registerBrush(GradientBrush.INSTANCE);
        registerBrush(new PaintBrush(plugin.bundle()));
    }

    @Override
    public Stream<Brush> getBrushes() {
        return brushes.stream().sorted();
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
