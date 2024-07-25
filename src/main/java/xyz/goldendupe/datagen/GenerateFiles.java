package xyz.goldendupe.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.datagen.defaults.SavedDataDefault;
import xyz.goldendupe.datagen.defaults.SettingsDefault;
import xyz.goldendupe.models.GDSavedData;
import xyz.goldendupe.models.GDSettings;
import xyz.goldendupe.models.serializer.GlobalSaveSerializer;
import xyz.goldendupe.models.serializer.SettingsSerializer;

import java.io.*;

public class GenerateFiles implements Generate {
	public Gson gson = new GsonBuilder().registerTypeAdapter(GDSettings.class, new SettingsSerializer()).registerTypeAdapter(GDSavedData.class, new GlobalSaveSerializer()).setPrettyPrinting().disableHtmlEscaping().create();
	public void generate(@NotNull File folder) throws IOException {
		File config = getOrCreate(new File(folder, "config.json"));
		File data = getOrCreate(new File(folder, "global-data.json"));
		write(gson.toJsonTree(new SettingsDefault(), GDSettings.class).getAsJsonObject(), config);
		write(gson.toJsonTree(new SavedDataDefault(), GDSavedData.class).getAsJsonObject(), data);
	}

	@Override
	public Gson getGson() {
		return gson;
	}
}
