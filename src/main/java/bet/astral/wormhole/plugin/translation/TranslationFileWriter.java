package bet.astral.wormhole.plugin.translation;

import bet.astral.messenger.v2.translation.TranslationKey;
import bet.astral.messenger.v3.minecraft.paper.PaperMessenger;
import com.google.gson.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public final class TranslationFileWriter {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    private static File translationFile;
    public static void ensureTranslationsExist(PaperMessenger messenger, Class<?> translationClass, File file) {
        translationFile = file;

        JsonObject existing = null;

        if (translationFile.exists()) {
            try (Reader reader = new InputStreamReader(new FileInputStream(translationFile), StandardCharsets.UTF_8)) {
                existing = gson.fromJson(reader, JsonObject.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else {
            String resourcePath = "/" + file.getName();
            try (InputStream in = TranslationFileWriter.class.getResourceAsStream(resourcePath)) {
                if (in != null) {
                    try (Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                        existing = gson.fromJson(reader, JsonObject.class);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<TranslationKey> translations = messenger.fetchTranslations(translationClass);
        JsonObject defaults = new JsonObject();
        for (TranslationKey key : translations) {
            defaults.addProperty(key.getKey(), key.getKey());
        }
        JsonObject merged = deepMergeJson(existing, defaults);

        try {
            if (!translationFile.exists()) {
                translationFile.getParentFile().mkdirs();
                translationFile.createNewFile();
            }
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(translationFile), StandardCharsets.UTF_8)) {
                gson.toJson(merged, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static JsonObject deepMergeJson(JsonObject base, JsonObject defaults) {
        if (base == null) {
            return defaults.deepCopy();
        }

        for (Map.Entry<String, JsonElement> entry : defaults.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();

            if (!base.has(key)) {
                base.add(key, value);
            } else {
                JsonElement baseValue = base.get(key);
                if (baseValue.isJsonObject() && value.isJsonObject()) {
                    JsonObject merged = deepMergeJson(baseValue.getAsJsonObject(), value.getAsJsonObject());
                    base.add(key, merged);
                }
            }
        }
        return base;
    }
}
