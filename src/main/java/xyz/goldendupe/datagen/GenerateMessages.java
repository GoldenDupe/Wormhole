package xyz.goldendupe.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.messenger.Translations;

import java.io.*;

public class GenerateMessages implements Generate {
	public Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	public void generate(@NotNull File folder) throws IOException {
		File messages = getOrCreate(new File(folder, "messages.json"));
		try {
			write(Translations.createDefaults(), messages);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Gson getGson() {
		return gson;
	}
}
