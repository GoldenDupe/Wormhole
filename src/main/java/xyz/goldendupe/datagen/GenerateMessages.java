package xyz.goldendupe.datagen;

import bet.astral.messenger.v2.translation.serializer.gson.TranslationGsonHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.messenger.Translations;

import java.io.*;

public class GenerateMessages implements Generate {
	public Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	public void generate(@NotNull File folder) throws IOException {
		File messages = getOrCreate(new File(folder, "messages.json"));
        write(TranslationGsonHelper.getDefaults(Translations.class, MiniMessage.miniMessage(), gson), messages);
    }

	@Override
	public Gson getGson() {
		return gson;
	}
}
