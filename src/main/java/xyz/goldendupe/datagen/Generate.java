package xyz.goldendupe.datagen;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.List;

public interface Generate {
	Gson getGson();
	default void write(@NotNull JsonObject json, File file){
		try {
			FileReader reader = new FileReader(file);
			JsonElement element = getGson().fromJson(reader, JsonElement.class);
			JsonObject current;
			if (element instanceof JsonNull || element == null){
				current = null;
			} else {
				current = getGson().fromJson(reader, JsonObject.class);
			}

			if (current != null) {
				current = checkUntilJsonEnd(json, current);
			} else {
				current = json;
			}

			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(getGson().toJson(current));
			writer.flush();
			writer.close();
			reader.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	default JsonObject checkUntilJsonEnd(@Nullable JsonObject json, JsonObject saving){
		if (json==null){
			return null;
		}
		List.copyOf(json.keySet()).forEach(val->{
			if (saving.get(val)==null){
				saving.add(val, json.get(val));
			} else if (saving.get(val).isJsonObject()){
				JsonObject object;
				if (json.get(val)==null||json.get(val).isJsonNull()||!json.get(val).isJsonObject()){
					object = new JsonObject();
				} else {
					object = json.get(val).getAsJsonObject();
				}
				JsonElement element = checkUntilJsonEnd(object, saving.get(val).getAsJsonObject());
				saving.add(val, element);
			}
		});
		return saving;
	}

	default File getOrCreate(@NotNull File file) throws IOException {
		if (!file.exists()){
			if (!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
		}
		return file;
	}
}
