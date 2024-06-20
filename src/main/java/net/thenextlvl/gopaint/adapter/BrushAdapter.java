package net.thenextlvl.gopaint.adapter;

import com.google.gson.*;
import core.annotation.MethodsReturnNotNullByDefault;
import core.annotation.ParametersAreNotNullByDefault;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.key.Key;
import net.thenextlvl.gopaint.api.brush.Brush;
import net.thenextlvl.gopaint.api.model.GoPaintProvider;

import java.lang.reflect.Type;

@RequiredArgsConstructor
@MethodsReturnNotNullByDefault
@ParametersAreNotNullByDefault
public class BrushAdapter implements JsonDeserializer<Brush>, JsonSerializer<Brush> {
    private final GoPaintProvider provider;

    @Override
    @SuppressWarnings("PatternValidation")
    public Brush deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        return provider.brushRegistry().getBrush(Key.key(element.getAsString())).orElseThrow(() ->
                new IllegalArgumentException("unknown brush: " + element.getAsString()));
    }

    @Override
    public JsonElement serialize(Brush brush, Type typ, JsonSerializationContext context) {
        return new JsonPrimitive(brush.key().asString());
    }
}
