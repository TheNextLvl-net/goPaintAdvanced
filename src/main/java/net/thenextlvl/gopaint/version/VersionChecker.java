package net.thenextlvl.gopaint.version;

import net.thenextlvl.version.SemanticVersion;
import net.thenextlvl.version.modrinth.paper.PaperModrinthVersionChecker;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class VersionChecker extends PaperModrinthVersionChecker<SemanticVersion> {
    public VersionChecker(final Plugin plugin) {
        super(plugin, "a2wQ6jIv");
    }

    @Override
    public SemanticVersion parseVersion(final String version) {
        return SemanticVersion.parse(version);
    }
}