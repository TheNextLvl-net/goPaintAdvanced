package net.thenextlvl.gopaint.brush;

import com.google.common.base.Preconditions;
import net.kyori.adventure.key.Key;
import net.thenextlvl.gopaint.GoPaintPlugin;
import net.thenextlvl.gopaint.api.brush.PatternBrush;
import net.thenextlvl.gopaint.api.brush.BrushRegistry;
import net.thenextlvl.gopaint.brush.standard.*;
import org.jspecify.annotations.NullMarked;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@NullMarked
public class CraftBrushRegistry implements BrushRegistry {
    private final List<PatternBrush> brushes = new LinkedList<>();

    public CraftBrushRegistry(GoPaintPlugin plugin) {
        registerBrush(new SphereBrush(plugin));
        registerBrush(new SprayBrush(plugin));
        registerBrush(new SplatterBrush(plugin));
        registerBrush(new DiskBrush(plugin));
        registerBrush(new BucketBrush(plugin));
        registerBrush(new AngleBrush(plugin));
        registerBrush(new OverlayBrush(plugin));
        registerBrush(new UnderlayBrush(plugin));
        registerBrush(new FractureBrush(plugin));
        registerBrush(new GradientBrush(plugin));
        registerBrush(new PaintBrush(plugin));
    }

    @Override
    public Stream<PatternBrush> getBrushes() {
        return brushes.stream().sorted();
    }

    @Override
    public boolean isRegistered(PatternBrush brush) {
        return brushes.contains(brush);
    }

    @Override
    public void registerBrush(PatternBrush brush) throws IllegalStateException {
        Preconditions.checkState(!isRegistered(brush), "Brush already registered");
        brushes.add(brush);
    }

    @Override
    public void unregisterBrush(PatternBrush brush) throws IllegalStateException {
        if (!brushes.remove(brush)) throw new IllegalStateException("Brush not registered");
    }

    @Override
    public Optional<PatternBrush> getBrush(Key key) {
        return brushes.stream()
                .filter(brush -> brush.key().equals(key))
                .findAny();
    }
}
