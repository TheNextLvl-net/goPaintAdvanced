package net.thenextlvl.gopaint.version;

import core.paper.version.PaperHangarVersionChecker;
import core.version.SemanticVersion;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class VersionChecker extends PaperHangarVersionChecker<SemanticVersion> {
    public VersionChecker(Plugin plugin) {
        super(plugin, "TheNextLvl", "goPaintAdvanced");
    }

    @Override
    public @Nullable SemanticVersion parseVersion(String version) {
        return SemanticVersion.parse(version);
    }
}