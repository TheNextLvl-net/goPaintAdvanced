package net.thenextlvl.gopaint.menu;

import core.paper.gui.PagedGUI;
import core.paper.item.ActionItem;
import core.paper.item.ItemBuilder;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.thenextlvl.gopaint.api.brush.PatternBrush;
import net.thenextlvl.gopaint.api.brush.setting.PlayerBrushSettings;
import net.thenextlvl.gopaint.api.model.GoPaintProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.stream.IntStream;

@NullMarked
public class BrushesMenu extends PagedGUI<GoPaintProvider, PatternBrush> {
    private final @Getter Options options = new Options(
            IntStream.range(0, getSize() - 9).toArray(),
            getSize() - 6,
            getSize() - 4
    );
    private final PlayerBrushSettings settings;

    public BrushesMenu(GoPaintProvider plugin, PlayerBrushSettings settings, Player owner) {
        super(plugin, owner, plugin.bundle().component(owner, "menu.brushes.title"), 3);
        this.settings = settings;
        loadPage(0);
    }

    @Override
    public void formatDefault() {
        var placeholder = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).hideTooltip(true);
        IntStream.range(0, getSize()).forEach(value -> setSlotIfAbsent(value, placeholder));
    }

    @Override
    public ActionItem constructItem(PatternBrush brush) {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .headValue(brush.getHeadValue())
                .itemName(brush.getName(owner).color(NamedTextColor.GOLD))
                .lore(brush.getDescription(owner))
                .withAction(() -> {
                    settings.setBrush(brush);
                    settings.getMainMenu().open();
                });
    }

    @Override
    public Component getPageFormat(int page) {
        var key = page > getCurrentPage() ? "gui.page.next" : "gui.page.previous";
        return plugin.bundle().component(owner, key);
    }

    @Override
    public Collection<PatternBrush> getElements() {
        return plugin.brushRegistry().getBrushes().toList();
    }
}
