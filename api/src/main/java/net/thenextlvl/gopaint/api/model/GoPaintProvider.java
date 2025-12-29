package net.thenextlvl.gopaint.api.model;

import net.thenextlvl.gopaint.api.brush.BrushController;
import net.thenextlvl.gopaint.api.brush.BrushRegistry;
import net.thenextlvl.i18n.ComponentBundle;
import org.bukkit.plugin.Plugin;

public interface GoPaintProvider extends Plugin {
    String USE_PERMISSION = "gopaint.use";
    String ADMIN_PERMISSION = "gopaint.admin";
    String WORLD_BYPASS_PERMISSION = "gopaint.world.bypass";

    ComponentBundle bundle();

    BrushRegistry brushRegistry();

    BrushController brushController();

    PluginConfig config();
}
