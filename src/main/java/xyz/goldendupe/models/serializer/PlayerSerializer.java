package xyz.goldendupe.models.serializer;

import com.google.gson.*;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.models.GDChat;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.models.chatcolor.GDChatColor;
import xyz.goldendupe.models.impl.GDHome;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class PlayerSerializer implements JsonSerializer<GDPlayer>, JsonDeserializer<GDPlayer> {
	private final Gson gson = new GsonBuilder()
			.registerTypeAdapter(GDChatColor.class, new ChatColorSerializer())
			.registerTypeAdapter(GDHome.class, new HomeSerializer())
			.create();
	@Override
	public GDPlayer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject object = json.getAsJsonObject();
		UUID uniqueID = gson.fromJson(object.get("uniqueId"), UUID.class);
		JsonObject chat = object.getAsJsonObject("chat");
		GDChat chatMode = gson.fromJson(chat.get("mode"), GDChat.class);
		GDChatColor color = gson.fromJson(chat.get("color"), GDChatColor.class);
		JsonArray array = gson.fromJson(object.get("homes"), JsonArray.class);
		List<GDHome> homes = new LinkedList<>();
		for (JsonElement element : array){
			homes.add(gson.fromJson(element, GDHome.class));
		}
		JsonObject counter = object.getAsJsonObject("counter");
		int itemsGenerated = counter.get("items-generated").getAsInt();
		int itemsDuped = counter.get("items-duped").getAsInt();
		int timesDuped = counter.get("times-duped").getAsInt();

		JsonObject toggle = object.getAsJsonObject("toggles");
		boolean randomItems = toggle.get("random-items").getAsBoolean();
		boolean drop = toggle.get("random-items").getAsBoolean();
		boolean pickup = toggle.get("random-items").getAsBoolean();
		boolean bottles = toggle.get("random-items").getAsBoolean();
		boolean speed = toggle.get("random-items").getAsBoolean();
		boolean nightVision = toggle.get("random-items").getAsBoolean();
		boolean autoClearInventory = toggle.get("random-items").getAsBoolean();

		return new GDPlayer(
				GoldenDupe.instance(),
				uniqueID,
				chatMode,
				color,
				homes,
				itemsDuped,
				timesDuped,
				itemsGenerated,
				autoClearInventory,
				randomItems,
				drop,
				pickup,
				nightVision,
				bottles,
				speed
		);
	}

	@Override
	public JsonElement serialize(GDPlayer src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject object = new JsonObject();
		object.addProperty("uniqueId", src.uuid().toString());
		JsonObject counter = new JsonObject();
		counter.addProperty("items-generated",src.getGeneratedRandomItems());
		counter.addProperty("items-duped",src.getItemsDuped());
		counter.addProperty("times-duped",src.getTimesDuped());
		object.add("counter", counter);
		JsonObject chat = new JsonObject();
		chat.addProperty("mode",src.chat().name());
		chat.add("color", gson.toJsonTree(src.color()));
		object.add("chat", chat);
		JsonObject toggleInfo = new JsonObject();
		toggleInfo.addProperty("random-items", src.isToggleRandomItems());
		toggleInfo.addProperty("drop", src.isToggleDropItem());
		toggleInfo.addProperty("pickup", src.isTogglePickupItem());
		toggleInfo.addProperty("bottles", src.isTogglePotionBottles());
		toggleInfo.addProperty("speed", src.isToggleSpeed());
		toggleInfo.addProperty("night-vision", src.isToggleNightVision());
		toggleInfo.addProperty("auto-clear-inventory", src.isToggleAutoConfirmClearInventory());
		object.add("toggles", toggleInfo);

		JsonArray array = new JsonArray();
		for (GDHome home : src.getHomes().values()){
			array.add(gson.toJsonTree(home, GDHome.class));
		}
		object.add("homes", array);
		return object;
	}
}
