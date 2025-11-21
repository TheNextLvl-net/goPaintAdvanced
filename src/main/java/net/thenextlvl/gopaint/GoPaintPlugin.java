package net.thenextlvl.gopaint;

import com.google.gson.GsonBuilder;
import core.file.FileIO;
import core.file.format.GsonFile;
import core.io.IO;
import core.paper.adapters.inventory.MaterialAdapter;
import core.paper.adapters.key.KeyAdapter;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.key.Key;
import net.thenextlvl.gopaint.api.brush.BrushController;
import net.thenextlvl.gopaint.api.brush.BrushRegistry;
import net.thenextlvl.gopaint.api.model.GoPaintProvider;
import net.thenextlvl.gopaint.api.model.PluginConfig;
import net.thenextlvl.gopaint.api.model.SurfaceMode;
import net.thenextlvl.gopaint.brush.CraftBrushController;
import net.thenextlvl.gopaint.brush.CraftBrushRegistry;
import net.thenextlvl.gopaint.command.GoPaintCommand;
import net.thenextlvl.gopaint.listener.ConnectListener;
import net.thenextlvl.gopaint.listener.InteractListener;
import net.thenextlvl.gopaint.listener.InventoryListener;
import net.thenextlvl.gopaint.version.VersionChecker;
import net.thenextlvl.i18n.ComponentBundle;
import org.bstats.bukkit.Metrics;
import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@NullMarked
public class GoPaintPlugin extends JavaPlugin implements GoPaintProvider {
    private final Key key = Key.key("gopaint_advanced", "translations");
    private final Path translations = getDataPath().resolve("translations");
    private final ComponentBundle bundle = ComponentBundle.builder(key, translations)
            .placeholder("prefix", "prefix")
            .resource("gopaint.properties", Locale.US)
            .resource("gopaint_german.properties", Locale.GERMANY)
            .build();

    private final BrushController brushController = new CraftBrushController(this);
    private final BrushRegistry brushRegistry = new CraftBrushRegistry(this);

    private final FileIO<PluginConfig> configFile = new GsonFile<>(IO.of(getDataFolder(), "config.json"), new PluginConfig(
            new PluginConfig.BrushConfig(Material.FEATHER, Key.key("gopaint", "sphere_brush"), 100, 10, 50,
                    Axis.Y, 50, 50, Set.of("disabled"), true, Material.SPONGE, true, SurfaceMode.EXPOSED,
                    List.of(Material.STONE)),
            new PluginConfig.ThicknessConfig(1, 5),
            new PluginConfig.AngleConfig(2, 5, 10, 40, 85),
            new PluginConfig.FractureConfig(2, 5)
    ), new GsonBuilder()
            .registerTypeAdapter(Material.class, new MaterialAdapter())
            .registerTypeAdapter(Key.class, new KeyAdapter.Kyori())
            .setPrettyPrinting()
            .create()
    ).validate().save();

    private final VersionChecker versionChecker = new VersionChecker(this);
    private final Metrics metrics = new Metrics(this, 22279);

    public GoPaintPlugin() {
        registerServices();
    }

    @Override
    public void onLoad() {
        versionChecker.checkVersion();
        warnTranslationChanges();
    }

    private void warnTranslationChanges() {
        if (!Files.isRegularFile(translations.resolve("messages.properties"))
            && !Files.isRegularFile(translations.resolve("messages_german.properties"))) return;
        getComponentLogger().warn("The translations for goPaintAdvanced had major backwards incompatible changes");
        getComponentLogger().warn("For this reason the 'messages' files got renamed to 'gopaint'");
        getComponentLogger().warn("If you made changes to your translations before, you have to do them again in the new 'gopaint' files");
        getComponentLogger().warn("This message will go away once you have deleted the old 'messages' files from '{}'", translations);
    }

    @Override
    public void onEnable() {
        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        metrics.shutdown();
    }

    @Override
    public void reloadConfig() {
        configFile.reload();
    }

    private void registerServices() {
        getServer().getServicesManager().register(BrushController.class, brushController(), this, ServicePriority.Highest);
        getServer().getServicesManager().register(BrushRegistry.class, brushRegistry(), this, ServicePriority.Highest);
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        getServer().getPluginManager().registerEvents(new InteractListener(this), this);
        getServer().getPluginManager().registerEvents(new ConnectListener(this), this);
    }

    private void registerCommands() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS.newHandler(event ->
                event.registrar().register(GoPaintCommand.create(this), List.of("gp"))));
    }

    public PluginConfig config() {
        return configFile.getRoot();
    }

    public ComponentBundle bundle() {
        return this.bundle;
    }

    public BrushController brushController() {
        return this.brushController;
    }

    public BrushRegistry brushRegistry() {
        return this.brushRegistry;
    }
}
