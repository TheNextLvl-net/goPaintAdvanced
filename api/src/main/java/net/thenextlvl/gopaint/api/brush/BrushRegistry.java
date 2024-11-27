package net.thenextlvl.gopaint.api.brush;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * The BrushRegistry interface represents a registry for brushes.
 * It provides methods to access, register, and unregister brushes.
 */
@NullMarked
public interface BrushRegistry {
    /**
     * Returns a stream of registered brushes.
     *
     * @return The stream of available brushes.
     */
    Stream<PatternBrush> getBrushes();

    /**
     * Checks if a brush is registered in the BrushController.
     *
     * @param brush The brush to check if it is registered.
     * @return true if the brush is registered, false otherwise.
     */
    boolean isRegistered(PatternBrush brush);

    /**
     * Registers a brush in the BrushManager.
     *
     * @param brush The brush to be registered.
     * @throws IllegalStateException if the brush is already registered.
     */
    void registerBrush(PatternBrush brush) throws IllegalStateException;

    /**
     * Unregisters a brush from the Brush Controller.
     *
     * @param brush The brush to be unregistered.
     * @throws IllegalStateException if the brush is not registered.
     */
    void unregisterBrush(PatternBrush brush) throws IllegalStateException;

    /**
     * Retrieves the brush associated with the provided NamespacedKey.
     *
     * @param key The NamespacedKey of the brush to retrieve.
     * @return An Optional containing the brush if found, or an empty Optional if not found.
     */
    Optional<PatternBrush> getBrush(Key key);
}
