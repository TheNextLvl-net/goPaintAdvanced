package net.thenextlvl.gopaint.api.brush;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.command.tool.brush.Brush;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import net.thenextlvl.gopaint.api.brush.setting.BrushSettings;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * This interface represents a brush used for painting blocks in a world.
 */
@NullMarked
public abstract class PatternBrush implements Comparable<PatternBrush>, Keyed, Brush {
    /**
     * Retrieves the base64 head value.
     */
    private final String headValue;
    /**
     * The key that identifies this brush
     */
    private final Key key;

    public PatternBrush(String headValue, Key key) {
        this.headValue = headValue;
        this.key = key;
    }

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
     * Builds a pattern for the brush based on the provided parameters.
     *
     * @param session  The EditSession to build the pattern for.
     * @param position The position of the block where the pattern is located.
     * @param player   The player using the brush.
     * @param settings The brush settings to be used for building.
     * @return The built pattern.
     */
    public abstract Pattern buildPattern(EditSession session, BlockVector3 position, Player player, BrushSettings settings);

    /**
     * Builds a pattern in the specified EditSession at the given position with the provided size.
     *
     * @param session  The EditSession to build the pattern in.
     * @param position The position of the center block of the pattern.
     * @param pattern  The pattern.
     * @param size     The size of the pattern.
     * @throws MaxChangedBlocksException If the maximum number of changed blocks is exceeded.
     */
    @Override
    public abstract void build(EditSession session, BlockVector3 position, Pattern pattern, double size) throws MaxChangedBlocksException;

    public String getHeadValue() {
        return headValue;
    }

    @Override
    public Key key() {
        return key;
    }

    @Override
    public int compareTo(@NonNull PatternBrush brush) {
        return key().compareTo(brush.key());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PatternBrush that = (PatternBrush) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key);
    }

    @Override
    public String toString() {
        return "PatternBrush{" +
               "key=" + key +
               '}';
    }
}
