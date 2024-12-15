package xyz.goldendupe.database;

import com.google.gson.*;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.models.impl.GDSpawn;
import xyz.goldendupe.models.serializer.SpawnSerializer;

import java.io.*;
import java.util.*;

/**
 * Spawns can actually be stored in a json file so we're doing that
 */
public class SpawnDatabase {
	private final GoldenDupe goldenDupe;
	private final Gson GSON = new GsonBuilder()
			.registerTypeAdapter(GDSpawn.class, new SpawnSerializer())
			.setPrettyPrinting()
			.create();
	private final Map<String, GDSpawn> spawns = new HashMap<>();

	public SpawnDatabase(GoldenDupe goldenDupe) {
		this.goldenDupe = goldenDupe;
	}

	public GDSpawn get(String name){
		return spawns.get(name.toLowerCase());
	}
	public void delete(String name){
		spawns.remove(name.toLowerCase());
		recreateData();
	}
	public void create(GDSpawn gdSpawn){
		spawns.put(gdSpawn.getName().toLowerCase(), gdSpawn);
		recreateData();
	}
	public boolean exists(String spawn){
		return spawns.get(spawn.toLowerCase()) != null;
	}

	private void recreateData(){
		Collection<GDSpawn> spawns = this.spawns.values();

		String asJson = GSON.toJson(spawns);

		File file = new File(goldenDupe.getDataFolder(), "spawns.json");
		if (file.exists()) {
			file.delete();
			if (spawns.isEmpty()){
				return;
			}
		}
		try {
			file.createNewFile();
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(asJson);
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void load(){
		File file = new File(goldenDupe.getDataFolder(), "spawns.json");
		if (!file.exists()){
			return;
		}
		try {
			FileReader fileReader = new FileReader(file);
			JsonArray jsonArray = JsonParser.parseReader(fileReader).getAsJsonArray();
			for (JsonElement element : jsonArray){
				GDSpawn spawn = GSON.fromJson(element, GDSpawn.class);
				this.spawns.put(spawn.getName().toLowerCase(), spawn);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
