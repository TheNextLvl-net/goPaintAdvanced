package net.thenextlvl.gopaint.version;

import net.thenextlvl.version.SemanticVersion;
import net.thenextlvl.version.modrinth.paper.PaperModrinthVersionChecker;
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