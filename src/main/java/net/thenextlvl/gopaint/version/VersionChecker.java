package net.thenextlvl.gopaint.version;

import core.paper.version.PaperModrinthVersionChecker;
import core.version.SemanticVersion;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class VersionChecker extends PaperModrinthVersionChecker<SemanticVersion> {
    public VersionChecker(Plugin plugin) {
        super(plugin, "a2wQ6jIv");
    }

    @Override
    public SemanticVersion parseVersion(String version) {
        return SemanticVersion.parse(version);
    }
}