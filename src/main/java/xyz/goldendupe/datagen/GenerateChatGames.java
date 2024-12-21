package xyz.goldendupe.datagen;

import bet.astral.chatgamecore.game.builtin.true_or_false.TrueOrFalseChatGame;
import bet.astral.chatgamecore.messenger.GameTranslations;
import bet.astral.messenger.v2.translation.serializer.gson.TranslationGsonHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.messenger.chat.game.CopyFastestTranslations;
import xyz.goldendupe.messenger.chat.game.StringStatement;
import xyz.goldendupe.messenger.chat.game.TrueOrFalseTranslations;
import xyz.goldendupe.messenger.chat.game.UnscrambleTranslations;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class GenerateChatGames implements Generate {
    public Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping()
            .registerTypeAdapter(TrueOrFalseChatGame.Statement.class, new TrueOrFalseChatGame.StatementSerializer())
            .create();
    public void generate(@NotNull File folder) throws IOException {
        // Translations
        write(translations(GameTranslations.class),          getOrCreate(new File(folder, "root.json")));
        write(translations(TrueOrFalseTranslations.class),   getOrCreate(new File(folder, "true-or-false-translations.json")));
        write(translations(CopyFastestTranslations.class),   getOrCreate(new File(folder, "type-fastest-translations.json")));
        write(translations(UnscrambleTranslations.class),    getOrCreate(new File(folder, "unscramble-translations.json")));

        // Values
        write(asArray(createStatements(), gson),                                        getOrCreate(new File(folder, "true-or-false-values.json")));
        write(asArray(createStrings(CopyFastestTranslations.class, "CF"), gson),  getOrCreate(new File(folder, "type-fastest-values.json")));
        write(asArray(createStrings(UnscrambleTranslations.class, "U"), gson),    getOrCreate(new File(folder, "unscramble-values.json")));
    }
    private JsonObject translations(Class<?> clazz){
        return TranslationGsonHelper.getDefaults(clazz, MiniMessage.miniMessage(), gson);
    }

    @Override
    public Gson getGson() {
        return gson;
    }

    public JsonArray asArray(List<?> list, Gson gson){
        JsonArray jsonArray = new JsonArray();
        for (Object object : list){
            jsonArray.add(gson.toJsonTree(object));
        }
        return jsonArray;
    }

    private List<TrueOrFalseChatGame.Statement> createStatements(){
        List<TrueOrFalseChatGame.Statement> statements = new LinkedList<>();
        for (Field field : TrueOrFalseTranslations.class.getFields()){
            if (field.getName().startsWith("Q")) {
                try {
                    TrueOrFalseTranslations.Statement statement = (TrueOrFalseTranslations.Statement) field.get(null);
                    statements.add(statement.asStatement());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return statements;
    }
    private List<String> createStrings(Class<?> clazz, String prefix){
        List<String> strings = new LinkedList<>();
        for (Field field : clazz.getFields()){
            if (field.getName().startsWith(prefix)) {
                try {
                    StringStatement value = (StringStatement) field.get(null);
                    strings.add(value.getValue());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return strings;
    }
}

