package net.thenextlvl.gopaint.commands.suggestion;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.kyori.adventure.key.Key;
import net.thenextlvl.gopaint.GoPaintPlugin;
import net.thenextlvl.gopaint.api.brush.PatternBrush;

import java.util.concurrent.CompletableFuture;

public class BrushSuggestionProvider<T> implements SuggestionProvider<T> {
    private final GoPaintPlugin plugin;

    public BrushSuggestionProvider(GoPaintPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext context, SuggestionsBuilder builder) {
        plugin.brushRegistry().getBrushes()
                .map(PatternBrush::key)
                .map(Key::asString)
                .filter(key -> key.contains(builder.getRemaining()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }
}
