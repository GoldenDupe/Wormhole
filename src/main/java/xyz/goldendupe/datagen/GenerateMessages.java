package xyz.goldendupe.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.messenger.Translations;

import java.io.*;
import java.util.List;

public class GenerateMessages {
	public Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	public void generate(@NotNull File folder) throws IOException {
		File messages = getOrCreate(new File(folder, "messages.json"));
		try {
			write(Translations.createDefaults(), messages);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public void write(@NotNull JsonObject json, File file){
		try {
			FileReader reader = new FileReader(file);
			JsonObject current = gson.fromJson(reader, JsonObject.class);
			if (current != null) {
				checkUntilJsonEnd(json, current);
			} else {
				current = json;
			}

			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			String jsonString = gson.toJson(current);
			writer.write(jsonString);
			writer.flush();
			writer.close();
			reader.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void checkUntilJsonEnd(@NotNull JsonObject json, JsonObject saving){
		List.copyOf(json.keySet()).forEach(val->{
			if (saving.get(val)==null){
				saving.add(val, json.get(val));
			} else if (saving.get(val).isJsonObject()){
				checkUntilJsonEnd(json, saving.get(val).getAsJsonObject());
			}
		});
	}

	public File getOrCreate(@NotNull File file) throws IOException {
		if (!file.exists()){
			if (!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
		}
		return file;
	}
}
