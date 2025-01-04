package net.thenextlvl.gopaint.menu;

import core.paper.gui.PaginatedGUI;
import core.paper.item.ActionItem;
import core.paper.item.ItemBuilder;
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
public class BrushesMenu extends PaginatedGUI<GoPaintProvider, PatternBrush> {
    private final Pagination pagination = new Pagination(
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
        var placeholder = ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).hideTooltip();
        IntStream.range(0, getSize()).forEach(value -> setSlotIfAbsent(value, placeholder));
    }

    @Override
    public ActionItem constructItem(PatternBrush brush) {
        return ItemBuilder.of(Material.PLAYER_HEAD)
                .profileValue(brush.getHeadValue())
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

    @Override
    public Pagination getPagination() {
        return pagination;
    }
}
