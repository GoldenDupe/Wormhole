package xyz.goldendupe.models.serializer;

import com.google.gson.*;
import xyz.goldendupe.models.impl.GDSpawn;

import java.lang.reflect.Type;

public class SpawnSerializer implements JsonSerializer<GDSpawn>, JsonDeserializer<GDSpawn> {
	private final static Gson gson = new Gson();
	@Override
	public GDSpawn deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		JsonObject object = jsonElement.getAsJsonObject();

		String permission = gson.fromJson("permission", String.class);
		String name = gson.fromJson(object.get("name"), String.class);
		String worldName = gson.fromJson(object.get("world"), String.class);
		double x = gson.fromJson(object.get("x"), double.class);
		double y = gson.fromJson(object.get("y"), double.class);
		double z = gson.fromJson(object.get("z"), double.class);
		float yaw = gson.fromJson(object.get("yaw"), float.class);
		float pitch = 90;

		return new GDSpawn(name, permission, x, y, z, yaw, worldName);
	}

	@Override
	public JsonElement serialize(GDSpawn spawn, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.add("permission", gson.toJsonTree(spawn.getPermission() != null ? spawn.getPermission() : "", String.class));
		jsonObject.add("name", gson.toJsonTree(spawn.getName(), String.class));
		jsonObject.add("world", gson.toJsonTree(spawn.getWorldName(), String.class));
		jsonObject.add("x", gson.toJsonTree(spawn.getX(), double.class));
		jsonObject.add("y", gson.toJsonTree(spawn.getY(), double.class));
		jsonObject.add("z", gson.toJsonTree(spawn.getZ(), double.class));
		jsonObject.add("yaw", gson.toJsonTree(spawn.getYaw(), float.class));
		return jsonObject;
	}
}
