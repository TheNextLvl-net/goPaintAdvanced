package net.thenextlvl.gopaint.api.brush;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import net.kyori.adventure.key.Key;

public abstract class SpherePatternBrush extends PatternBrush {
    public SpherePatternBrush(final String headValue, final Key key) {
        super(headValue, key);
    }

    @Override
    public final void build(final EditSession session, final BlockVector3 position, final Pattern pattern, final double size) throws MaxChangedBlocksException {
        session.makeSphere(position, pattern, size, size, size, true);
    }
}
