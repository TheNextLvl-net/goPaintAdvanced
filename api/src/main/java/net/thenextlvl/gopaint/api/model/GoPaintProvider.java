package net.thenextlvl.gopaint.api.model;

import core.i18n.file.ComponentBundle;
import net.thenextlvl.gopaint.api.brush.BrushController;
import net.thenextlvl.gopaint.api.brush.BrushRegistry;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface GoPaintProvider extends Plugin {
    String USE_PERMISSION = "gopaint.use";
    String ADMIN_PERMISSION = "gopaint.admin";
    String WORLD_BYPASS_PERMISSION = "gopaint.world.bypass";

    ComponentBundle bundle();

    BrushRegistry brushRegistry();

    BrushController brushController();

    PluginConfig config();
}
