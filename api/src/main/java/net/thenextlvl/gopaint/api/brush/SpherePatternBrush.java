package net.thenextlvl.gopaint.api.brush;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class SpherePatternBrush extends PatternBrush {
    public SpherePatternBrush(String headValue, Key key) {
        super(headValue, key);
    }

    @Override
    public final void build(EditSession session, BlockVector3 position, Pattern pattern, double size) throws MaxChangedBlocksException {
        session.makeSphere(position, pattern, size, size, size, true);
    }
}
