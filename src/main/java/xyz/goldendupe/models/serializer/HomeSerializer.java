package xyz.goldendupe.models.serializer;

import com.google.gson.*;
import xyz.goldendupe.models.impl.GDHome;

import java.lang.reflect.Type;
import java.util.UUID;

public class HomeSerializer implements JsonSerializer<GDHome>, JsonDeserializer<GDHome> {
	private final static Gson gson = new Gson();
	@Override
	public GDHome deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		JsonObject object = jsonElement.getAsJsonObject();

		UUID uniqueId = gson.fromJson(object.get("uniqueId"), UUID.class);
		String name = gson.fromJson(object.get("name"), String.class);
		String worldName = gson.fromJson(object.get("world"), String.class);
		double x = gson.fromJson(object.get("x"), double.class);
		double y = gson.fromJson(object.get("y"), double.class);
		double z = gson.fromJson(object.get("z"), double.class);
		float yaw = gson.fromJson(object.get("yaw"), float.class);
		float pitch = 90;

		return new GDHome(name, uniqueId, x, y, z, yaw, worldName);
	}

	@Override
	public JsonElement serialize(GDHome gdHome, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.add("uniqueId", gson.toJsonTree(gdHome.getUniqueId(), UUID.class));
		jsonObject.add("name", gson.toJsonTree(gdHome.getName(), String.class));
		jsonObject.add("world", gson.toJsonTree(gdHome.getWorldName(), String.class));
		jsonObject.add("x", gson.toJsonTree(gdHome.getX(), double.class));
		jsonObject.add("y", gson.toJsonTree(gdHome.getY(), double.class));
		jsonObject.add("z", gson.toJsonTree(gdHome.getZ(), double.class));
		jsonObject.add("yaw", gson.toJsonTree(gdHome.getYaw(), float.class));
		return jsonObject;
	}
}
