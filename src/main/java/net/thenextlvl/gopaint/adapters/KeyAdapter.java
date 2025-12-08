package net.thenextlvl.gopaint.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Type;

@NullMarked
public final class KeyAdapter implements JsonSerializer<Key>, JsonDeserializer<Key> {
    @Override
    @SuppressWarnings("PatternValidation")
    public @Nullable Key deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        return element.isJsonPrimitive() ? Key.key(element.getAsString()) : null;
    }

    @Override
    public JsonElement serialize(@Nullable Key source, Type type, JsonSerializationContext context) {
        return source != null ? new JsonPrimitive(source.toString()) : JsonNull.INSTANCE;
    }
}
