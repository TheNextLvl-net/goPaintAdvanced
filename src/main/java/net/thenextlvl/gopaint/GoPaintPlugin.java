package net.thenextlvl.gopaint;

import com.google.gson.GsonBuilder;
import core.file.FileIO;
import core.file.format.GsonFile;
import core.i18n.file.ComponentBundle;
import core.io.IO;
import core.paper.adapters.inventory.MaterialAdapter;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.thenextlvl.gopaint.adapter.BrushAdapter;
import net.thenextlvl.gopaint.api.brush.Brush;
import net.thenextlvl.gopaint.api.brush.BrushController;
import net.thenextlvl.gopaint.api.brush.BrushRegistry;
import net.thenextlvl.gopaint.api.model.GoPaintProvider;
import net.thenextlvl.gopaint.api.model.MaskMode;
import net.thenextlvl.gopaint.api.model.PluginConfig;
import net.thenextlvl.gopaint.api.model.SurfaceMode;
import net.thenextlvl.gopaint.brush.CraftBrushController;
import net.thenextlvl.gopaint.brush.CraftBrushRegistry;
import net.thenextlvl.gopaint.brush.standard.SphereBrush;
import net.thenextlvl.gopaint.command.GoPaintCommand;
import net.thenextlvl.gopaint.listener.ConnectListener;
import net.thenextlvl.gopaint.listener.InteractListener;
import net.thenextlvl.gopaint.listener.InventoryListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

@Accessors(fluent = true)
public class GoPaintPlugin extends JavaPlugin implements GoPaintProvider {

    private final File translations = new File(getDataFolder(), "translations");
    private final @Getter ComponentBundle bundle = new ComponentBundle(translations, audience ->
            audience instanceof Player player ? player.locale() : Locale.US)
            .register("messages", Locale.US)
            .register("messages_german", Locale.GERMANY)
            .miniMessage(bundle -> MiniMessage.builder().tags(TagResolver.resolver(
                    TagResolver.standard(),
                    Placeholder.component("prefix", bundle.component(Locale.US, "prefix"))
            )).build());

    private final @Getter BrushController brushController = new CraftBrushController(this);
    private final @Getter BrushRegistry brushRegistry = new CraftBrushRegistry(this);

    private final FileIO<PluginConfig> configFile = new GsonFile<>(IO.of(getDataFolder(), "config.json"), new PluginConfig(
            new PluginConfig.BrushConfig(Material.FEATHER, SphereBrush.INSTANCE, 100, 10, 50, Axis.Y, 50, 50,
                    new ArrayList<>(), true, Material.SPONGE, MaskMode.INTERFACE, SurfaceMode.DIRECT),
            new PluginConfig.ThicknessConfig(1, 5),
            new PluginConfig.AngleConfig(2, 5, 10, 40, 85),
            new PluginConfig.FractureConfig(2, 5)
    ), new GsonBuilder()
            .registerTypeAdapter(Material.class, MaterialAdapter.NotNull.INSTANCE)
            .registerTypeAdapter(Brush.class, new BrushAdapter(this))
            .setPrettyPrinting()
            .create()
    ).validate().save();

    private final Metrics metrics = new Metrics(this, 22279);

    public GoPaintPlugin() {
        registerServices();
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
        Bukkit.getServicesManager().register(BrushController.class, brushController(), this, ServicePriority.Highest);
        Bukkit.getServicesManager().register(BrushRegistry.class, brushRegistry(), this, ServicePriority.Highest);
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new InventoryListener(this), this);
        Bukkit.getPluginManager().registerEvents(new InteractListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ConnectListener(this), this);
    }

    private void registerCommands() {
        new GoPaintCommand(this).register();
    }

    public PluginConfig config() {
        return configFile.getRoot();
    }
}
