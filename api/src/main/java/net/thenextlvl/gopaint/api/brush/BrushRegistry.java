package net.thenextlvl.gopaint.api.brush;

import java.util.List;

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
}
