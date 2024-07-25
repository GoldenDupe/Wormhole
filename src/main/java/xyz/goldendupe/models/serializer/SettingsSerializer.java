package xyz.goldendupe.models.serializer;

import com.google.gson.*;
import io.papermc.paper.ServerBuildInfo;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.models.GDSettings;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class SettingsSerializer implements JsonSerializer<GDSettings>, JsonDeserializer<GDSettings> {
	private final Gson gson = new Gson();
	@Override
	public GDSettings deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject object = json.getAsJsonObject();
		JsonObject dupe = object.getAsJsonObject("dupe");
		JsonObject itemData = object.getAsJsonObject("item-data");
		JsonObject enchant = itemData.getAsJsonObject("enchant");
		JsonObject firework = itemData.getAsJsonObject("fireworks");
		JsonObject ominous = itemData.getAsJsonObject("ominous-bottles");
		return new GDSettings(
				loadMaterials(dupe.getAsJsonArray("combat")),
				loadMaterials(dupe.getAsJsonArray("global")),
				loadMaterials(object.getAsJsonArray("placement_illegals")),
				new GDSettings.RandomItemsData(
						loadMaterials(itemData.getAsJsonArray("illegals")),
						loadNamespacedKeys(enchant.getAsJsonArray("illegal")),
						itemData.get("modify-decorated-pots").getAsBoolean(),
						enchant.get("modify-enchanted-books").getAsBoolean(),
						enchant.get("allow-only-vanilla-enchants").getAsBoolean(),
						firework.get("modify-fireworks").getAsBoolean(),
						firework.get("max-firework-boost").getAsInt(),
						itemData.get("modify-goat-horns").getAsBoolean(),
						itemData.get("modify-tipped-arrows").getAsBoolean(),
						itemData.get("modify-potions").getAsBoolean(),
						ominous.get("modify-ominous-bottles").getAsBoolean(),
						ominous.get("max-ominous-tier").getAsInt()
				),
				loadStringList(object.get("uwu-messages").getAsJsonArray())
		);
	}

	@Override
	public JsonElement serialize(GDSettings src, Type typeOfSrc, JsonSerializationContext context) {
		GoldenDupe goldenDupe = src.getGoldenDupe();
		Set<Material> illegalDupeCombat = src.getIllegalDupeCombat();
		Set<Material> illegalDupe = src.getIllegalDupe();
		GDSettings.RandomItemsData randomItemData = src.getRandomItemData();
		Set<Material> illegalPlacement = src.getIllegalPlacements();
		JsonObject object = new JsonObject();

		if (goldenDupe.isDevelopmentServer()){
			JsonObject dev = new JsonObject();
			dev.addProperty("development", true);
			dev.addProperty("debug", goldenDupe.isDebug());
			dev.addProperty("plugin_version", goldenDupe.getPluginMeta().getVersion());
			dev.addProperty("minecraft_version", ServerBuildInfo.buildInfo().minecraftVersionId());
			dev.addProperty("server_commit_version", ServerBuildInfo.buildInfo().gitCommit().orElse("UNKNOWN"));
			object.add("development_server", dev);
		} else {
			object.addProperty("development_server", false);
		}
		object.add("placement_illegals", saveMaterials(illegalPlacement));
		JsonObject dupeIllegals = new JsonObject();
		dupeIllegals.add("combat", saveMaterials(illegalDupeCombat));
		dupeIllegals.add("global", saveMaterials(illegalDupe));
		object.add("dupe", dupeIllegals);

		JsonObject random = new JsonObject();
		random.add("illegals", saveMaterials(randomItemData.getIllegalsItems()));
		random.addProperty("modify-decorated-pots", randomItemData.isAllowUpdatedDecoratedPots());
		JsonObject enchants = new JsonObject();
		enchants.addProperty("modify-enchanted-books", randomItemData.isAllowUpdatedEnchantmentBooks());
		enchants.addProperty("allow-only-vanilla-enchants", randomItemData.isAllowUpdatedDecoratedPots());
		enchants.add("illegal", saveNamespacedKeys(randomItemData.getIllegalEnchants()));
		random.add("enchant", enchants);
		JsonObject fireworks = new JsonObject();
		fireworks.addProperty("modify-fireworks", randomItemData.isAllowUpdatedFireworks());
		fireworks.addProperty("max-firework-boost", randomItemData.getMaxFireworkBoost());
		random.add("fireworks", fireworks);
		random.addProperty("modify-goat-horns", randomItemData.isAllowUpdatedGoatHorns());
		random.addProperty("modify-tipped-arrows", randomItemData.isAllowUpdatedArrows());
		random.addProperty("modify-potions", randomItemData.isAllowUpdatedPotions());
		JsonObject ominous = new JsonObject();
		ominous.addProperty("modify-ominous-bottles", randomItemData.isAllowUpdateOminousBottles());
		ominous.addProperty("max-ominous-tier", randomItemData.getMaxOminousTier());
		random.add("ominous-bottles", ominous);
		object.add("item-data", random);
		object.add("uwu-messages", saveStringList(src.getUwuString()));
		return object;
	}


	private Set<Material> loadMaterials(@NotNull JsonArray array){
		Set<NamespacedKey> keys = loadNamespacedKeys(array);
		Registry<Material> registry = Registry.MATERIAL;
		Set<Material> materials = new HashSet<>();
		keys.forEach(val->{
			Material material = registry.get(val);
			if (material!=null){
				materials.add(material);
			}
		});
		return materials;
	}
	private Set<NamespacedKey> loadNamespacedKeys(@NotNull JsonArray array){
		Set<NamespacedKey> keys = new HashSet<>();
		for (JsonElement element1 : array){
			keys.add(NamespacedKey.fromString(element1.getAsString()));
		}
		return keys;
	}

	public List<String> loadStringList(@NotNull JsonArray array){
		List<String> list = new LinkedList<>();
		for (JsonElement jsonElement : array) {
			list.add(jsonElement.getAsString());
		}
		return list;
	}
	private JsonArray saveMaterials(@NotNull Set<Material> materials){
		return saveNamespacedKeys(materials.stream().map(Material::getKey).collect(Collectors.toSet()));
	}
	private JsonArray saveNamespacedKeys(@NotNull Set<NamespacedKey> materials){
		JsonArray array = new JsonArray();
		materials.stream().map(val->gson.toJsonTree(val.toString())).forEach(array::add);
		return array;
	}
	private JsonArray saveStringList(@NotNull List<String> list){
		JsonArray array = new JsonArray();
		list.forEach(array::add);
		return array;
	}
}
