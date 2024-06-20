package net.thenextlvl.gopaint.api.brush;

import java.util.List;
import net.kyori.adventure.key.Key;

import java.util.Optional;

/**
 * The BrushRegistry interface represents a registry for brushes.
 * It provides methods to access, register, and unregister brushes.
 */
public interface BrushRegistry {
    /**
     * Returns an immutable list of available brushes.
     *
     * @return The list of available brushes.
     */
    List<Brush> getBrushes();

    /**
     * Checks if a brush is registered in the BrushController.
     *
     * @param brush The brush to check if it is registered.
     * @return true if the brush is registered, false otherwise.
     */
    boolean isRegistered(Brush brush);

    /**
     * Registers a brush in the BrushManager.
     *
     * @param brush The brush to be registered.
     * @throws IllegalStateException if the brush is already registered.
     */
    void registerBrush(Brush brush) throws IllegalStateException;

    /**
     * Unregisters a brush from the Brush Controller.
     *
     * @param brush The brush to be unregistered.
     * @throws IllegalStateException if the brush is not registered.
     */
    void unregisterBrush(Brush brush) throws IllegalStateException;

    /**
     * Retrieves the brush associated with the provided NamespacedKey.
     *
     * @param key The NamespacedKey of the brush to retrieve.
     * @return An Optional containing the brush if found, or an empty Optional if not found.
     */
    Optional<Brush> getBrush(Key key);
}
