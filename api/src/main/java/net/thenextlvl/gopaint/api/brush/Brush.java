package net.thenextlvl.gopaint.api.brush;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.math.BlockVector3;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import net.thenextlvl.gopaint.api.brush.setting.BrushSettings;
import net.thenextlvl.gopaint.api.math.Surface;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * This interface represents a brush used for painting blocks in a world.
 */
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class Brush implements Comparable<Brush>, Keyed {
    /**
     * Retrieves the base64 head value.
     */
    private final String headValue;
    /**
     * The key that identifies this brush
     */
    private final @Accessors(fluent = true) Key key;

    /**
     * Retrieves the localized name of this brush.
     *
     * @param audience The audience for whom the name is retrieved.
     * @return The localized name of the brush.
     */
    public abstract Component getName(Audience audience);

    /**
     * Retrieves the localized description of this brush.
     *
     * @param audience The audience for whom the description is retrieved.
     * @return The localized description of the brush.
     */
    public abstract Component[] getDescription(Audience audience);

    /**
     * Performs a painting action using the provided location, player, and brush settings.
     *
     * @param location      The location the painting action is performed.
     * @param player        The player who is performing the paint action.
     * @param brushSettings The brush settings to be applied while painting.
     * @see #performEdit(Player, Consumer)
     * @see #setBlock(EditSession, BlockVector3, Material)
     */
    public abstract void paint(Location location, Player player, BrushSettings brushSettings);

    /**
     * Sets the material of a block in an EditSession.
     *
     * @param session  The EditSession to perform the block update in.
     * @param vector3  The block to update.
     * @param material The material to set the block to.
     * @throws MaxChangedBlocksException If the maximum number of changed blocks is exceeded.
     */
    public void setBlock(EditSession session, BlockVector3 vector3, Material material) throws MaxChangedBlocksException {
        session.setBlock(vector3, BukkitAdapter.asBlockType(material));
    }

    /**
     * Performs an edit using WorldEdit's EditSession.
     * This method wraps the edit session in a try-with-resources block to ensure proper cleanup of resources.
     *
     * @param player The player performing the edit.
     * @param edit   A Consumer functional interface that defines the actions to be taken within the edit session.
     */
    public void performEdit(Player player, Consumer<EditSession> edit) {
        BukkitPlayer wrapped = BukkitAdapter.adapt(player);
        LocalSession localSession = WorldEdit.getInstance().getSessionManager().get(wrapped);
        try (EditSession editsession = localSession.createEditSession(wrapped)) {
            try {
                edit.accept(editsession);
            } finally {
                localSession.remember(editsession);
            }
        }
    }

    /**
     * Checks if a given block passes the default checks defined by the brush settings.
     *
     * @param brushSettings The brush settings to be checked against.
     * @param player        The player using the brush.
     * @param session       The EditSession object used for performing the checks.
     * @param block         The block being checked.
     * @return true if the block passes the default checks, false otherwise.
     */
    public boolean passesDefaultChecks(BrushSettings brushSettings, Player player, EditSession session, Block block) {
        return passesMaskCheck(brushSettings, session, block) && passesSurfaceCheck(brushSettings, player, block);
    }

    /**
     * Checks if a given block passes the surface check defined by the brush settings.
     *
     * @param brushSettings The brush settings to be checked against.
     * @param player        The player using the brush.
     * @param block         The block being checked.
     * @return true if the block passes the surface check, false otherwise.
     */
    public boolean passesSurfaceCheck(BrushSettings brushSettings, Player player, Block block) {
        return Surface.isOnSurface(block, brushSettings.getSurfaceMode(), player.getLocation());
    }

    /**
     * Checks if a given block passes the mask check defined by the brush settings.
     *
     * @param brushSettings The brush settings to be checked against.
     * @param session       The EditSession object used for performing the mask check.
     * @param block         The block being checked.
     * @return true if the block passes the mask check, false otherwise.
     */
    public boolean passesMaskCheck(BrushSettings brushSettings, EditSession session, Block block) {
        return switch (brushSettings.getMaskMode()) {
            case INTERFACE -> block.getType().equals(brushSettings.getMask());
            case WORLDEDIT -> session.getMask() == null || session.getMask().test(
                    BlockVector3.at(block.getX(), block.getY(), block.getZ())
            );
            case DISABLED -> true;
        };
    }

    @Override
    public int compareTo(@NotNull Brush brush) {
        return key().compareTo(brush.key());
    }
}
