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
import net.thenextlvl.gopaint.brush.PlayerBrushManager;
import net.thenextlvl.gopaint.command.GoPaintCommand;
import net.thenextlvl.gopaint.objects.other.PluginConfig;
import net.thenextlvl.gopaint.listeners.ConnectListener;
import net.thenextlvl.gopaint.listeners.InteractListener;
import net.thenextlvl.gopaint.listeners.InventoryListener;
import net.thenextlvl.gopaint.objects.other.SurfaceMode;
import org.bstats.bukkit.Metrics;
import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

@Accessors(fluent = true)
public class GoPaintPlugin extends JavaPlugin implements Listener {
    public static final String USE_PERMISSION = "gopaint.use";
    public static final String ADMIN_PERMISSION = "gopaint.admin";
    public static final String WORLD_BYPASS_PERMISSION = "gopaint.world.bypass";

    private final File translations = new File(getDataFolder(), "translations");
    private final @Getter ComponentBundle bundle = new ComponentBundle(translations, audience ->
            audience instanceof Player player ? player.locale() : Locale.US)
            .register("messages", Locale.US)
            .register("messages_german", Locale.GERMANY)
            .miniMessage(bundle -> MiniMessage.builder().tags(TagResolver.resolver(
                    TagResolver.standard(),
                    Placeholder.component("prefix", bundle.component(Locale.US, "prefix"))
            )).build());

    private final FileIO<PluginConfig> configFile = new GsonFile<>(IO.of(getDataFolder(), "config.json"), new PluginConfig(
            new PluginConfig.Generic(Material.FEATHER, 100, 10, 50, Axis.Y, 50, 50, new ArrayList<>(), true, true, Material.SPONGE, SurfaceMode.DIRECT),
            new PluginConfig.Thickness(1, 5),
            new PluginConfig.Angle(2, 5, 10, 40, 85),
            new PluginConfig.Fracture(2, 5)
    ), new GsonBuilder()
            .registerTypeAdapter(Material.class, MaterialAdapter.NotNull.INSTANCE)
            .setPrettyPrinting()
            .create()
    ).validate().save();

    private final @Getter PlayerBrushManager brushManager = new PlayerBrushManager(this);
    private final Metrics metrics = new Metrics(this, 22279);

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

    private void registerCommands() {
        new GoPaintCommand(this).register();
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new InventoryListener(this), this);
        Bukkit.getPluginManager().registerEvents(new InteractListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ConnectListener(brushManager()), this);
    }

    private boolean hasOriginalGoPaint() {
        return Bukkit.getPluginManager().getPlugin("goPaint") != this;
    }

    public PluginConfig config() {
        return configFile.getRoot();
    }
}
