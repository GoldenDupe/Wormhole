package xyz.goldendupe.models.serializer;

import com.google.gson.*;
import xyz.goldendupe.models.chatcolor.Color;
import xyz.goldendupe.models.chatcolor.GDChatColor;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ChatColorSerializer implements JsonSerializer<GDChatColor>, JsonDeserializer<GDChatColor> {
	@Override
	public GDChatColor deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		JsonObject object = jsonElement.getAsJsonObject();
		Map<Integer, Color> colors = new HashMap<>();
		JsonArray colorsArray = object.get("colors").getAsJsonArray();
		for (int i = 0; i < 9; i++){
			JsonElement element = colorsArray.get(i);
			if (element == null || element.isJsonNull()){
				colors.put(i, null);
				continue;
			}
			String hex = colorsArray.get(i).getAsString();
			colors.put(i, Color.ofHex(hex));
		}

		GDChatColor.Mode mode = GDChatColor.Mode.valueOf(object.get("mode").getAsString());
		GDChatColor color = null;
		switch (mode){
			case NONE, SINGLE, GRADIENT -> {
				color = new GDChatColor(mode, colors);
			}
			case RAINBOW -> {
				color = new GDChatColor(mode, colors);
				int rainbowMode = object.get("rainbowStartingPosition").getAsInt();
				boolean reversedMode = object.get("rainbowReversed").getAsBoolean();
				color.setRainbowMode(rainbowMode);
				color.setRainbowReversed(reversedMode);
			}
		}
		boolean bold = object.get("bold").getAsBoolean();
		boolean italic = object.get("italic").getAsBoolean();
		boolean underlined = object.get("underlined").getAsBoolean();
		boolean strikethrough = object.get("strikethrough").getAsBoolean();

		color.setBold(bold);
		color.setItalic(italic);
		color.setUnderlined(underlined);
		color.setStrikethrough(strikethrough);

		return color;
	}

	@Override
	public JsonElement serialize(GDChatColor chatColor, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject object = new JsonObject();
		object.addProperty("mode", chatColor.mode().name());
		object.addProperty("bold", chatColor.bold());
		object.addProperty("italic", chatColor.italic());
		object.addProperty("underlined", chatColor.underlined());
		object.addProperty("strikethrough", chatColor.strikethrough());

		JsonArray colors = new JsonArray();
		for (int i = 0; i < 9; i++) {
			colors.add(chatColor.colors().get(i).asHex());
		}
		object.add("colors", colors);
		if (Objects.requireNonNull(chatColor.mode()) == GDChatColor.Mode.RAINBOW) {
			object.addProperty("rainbowStartingPosition", chatColor.rainbowMode());
			object.addProperty("rainbowReversed", chatColor.rainbowReversed());
		}

		return object;
	}
}
