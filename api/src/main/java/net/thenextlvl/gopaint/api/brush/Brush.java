package net.thenextlvl.gopaint.api.brush;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import net.thenextlvl.gopaint.api.brush.setting.BrushSettings;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

/**
 * This interface represents a brush used for painting blocks in a world.
 */
public interface Brush {
    /**
     * Retrieves the name of the brush.
     *
     * @return The name of the brush.
     */
    String getName();

    /**
     * Retrieves the description of the brush.
     *
     * @return The description of the brush.
     */
    String getDescription();

    /**
     * Retrieves the base64 head value.
     *
     * @return The base64 head value.
     */
    String getHeadValue();

    /**
     * Performs a painting action using the provided location, player, and brush settings.
     *
     * @param location      The location the painting action is performed.
     * @param player        The player who is performing the paint action.
     * @param brushSettings The brush settings to be applied while painting.
     */
    void paint(Location location, Player player, BrushSettings brushSettings);

    /**
     * Sets the material of a block in an EditSession.
     *
     * @param session  The EditSession to perform the block update in.
     * @param block    The block to update.
     * @param material The material to set the block to.
     * @throws MaxChangedBlocksException If the maximum number of changed blocks is exceeded.
     */
    void setBlock(EditSession session, Block block, Material material) throws MaxChangedBlocksException;

    /**
     * Performs an edit using WorldEdit's EditSession.
     * This method wraps the edit session in a try-with-resources block to ensure proper cleanup of resources.
     *
     * @param player The player performing the edit.
     * @param edit   A Consumer functional interface that defines the actions to be taken within the edit session.
     */
    void performEdit(Player player, Consumer<EditSession> edit);

    /**
     * Checks if a given block passes the default checks defined by the brush settings.
     *
     * @param brushSettings The brush settings to be checked against.
     * @param player        The player using the brush.
     * @param block         The block being checked.
     * @return true if the block passes all the default checks, false otherwise.
     */
    boolean passesDefaultChecks(BrushSettings brushSettings, Player player, Block block);

    /**
     * Checks if a given block passes the surface check defined by the brush settings.
     *
     * @param brushSettings The brush settings to be checked against.
     * @param player        The player using the brush.
     * @param block         The block being checked.
     * @return true if the block passes the surface check, false otherwise.
     */
    boolean passesSurfaceCheck(BrushSettings brushSettings, Player player, Block block);

    /**
     * Checks if a given block passes the mask check defined by the brush settings.
     *
     * @param brushSettings The brush settings to be checked against.
     * @param block         The block being checked.
     * @return true if the block passes the mask check, false otherwise.
     */
    boolean passesMaskCheck(BrushSettings brushSettings, Block block);
}
