package xyz.goldendupe.models;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.block.DecoratedPot;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.utils.impl.SpawnPosition;

import java.io.File;
import java.util.*;

@Getter
public class GDGlobalData {
	private final GoldenDupe goldenDupe;
	private final Map<UUID, GDMessageGroup> messageGroups = new HashMap<>();
	private final Map<String, SpawnPosition> spawns = new HashMap<>();
	private Set<Material> illegalDupeCombat;
	private Set<Material> illegalDupe;
	private List<ItemStack> randomItems;
	private Set<Material> illegalPlacement;
	@Setter
	private long timesDuped;
	@Setter
	private long itemsDuped;
	@Setter
	private long randomItemsGenerated;

	public GDGlobalData(GoldenDupe goldenDupe) {
		this.goldenDupe = goldenDupe;
	}

	public void reload(){
		reloadIllegals();

	}

	/*
	 * ILLEGALS
	 */

	public void reloadIllegals(){
		File file = new File(goldenDupe.getDataFolder(), "illegals.yml");
		YamlConfiguration illegalConfig = YamlConfiguration.loadConfiguration(file);

		illegalDupe = new HashSet<>();
		illegalDupeCombat = new HashSet<>();
		randomItems = new LinkedList<>();
		illegalPlacement = new HashSet<>();


		addMaterials(illegalDupe, illegalConfig.getStringList("dupe.illegals"));
		illegalDupe.add(Material.AIR);
		addMaterials(illegalDupeCombat, illegalConfig.getStringList("dupe.combat"));
		addMaterials(illegalPlacement, illegalConfig.getStringList("placement"));
		List<String> randomIllegals = illegalConfig.getStringList("random.illegals");
		randomItems = new LinkedList<>();
		for (Material material : Registry.MATERIAL){
			if (randomIllegals.contains(material.name())){
				continue;
			}
			World world = Bukkit.getWorlds().get(0);
			try {
				if (material.isEnabledByFeature(world)) {
					randomItems.add(new ItemStack(material));
				}
			} catch (NullPointerException e){
				getGoldenDupe().getLogger().severe("Couldn't check if material "+ material.key().namespace()+":"+ material.key().value()+ " is a data pack only item!");
			}
		}
	}

	private void addMaterials(Collection<Material> materials, List<String> mats){
		for (String mat : mats){
			try {
				Material material = Material.valueOf(mat);
				if (!materials.contains(material)) {
					materials.add(material);
				}
			} catch (IllegalStateException ignore){
			}
		}
	}

	public ItemStack patchRandomItem(ItemStack itemStack, Random random) {
		File file = new File(goldenDupe.getDataFolder(), "illegals.yml");
		YamlConfiguration illegalConfig = YamlConfiguration.loadConfiguration(file);
		ItemMeta meta = itemStack.getItemMeta();

		if (illegalConfig.getBoolean("random.all-goat-horns", false) && meta instanceof MusicInstrumentMeta instrumentMeta){
			List<MusicInstrument> instruments = Registry.INSTRUMENT.stream().toList();
			if (random.nextDouble()>0.1){
				instrumentMeta.setInstrument(instruments.get(random.nextInt(0, instruments.size()-1)));
			}
		}
		if (illegalConfig.getBoolean("random.full-decorated-potteries", false) && meta instanceof BlockStateMeta blockStateMeta){
			DecoratedPot decoratedPot = (DecoratedPot) blockStateMeta.getBlockState();
			List<Material> materials = Tag.ITEMS_BREAKS_DECORATED_POTS.getValues().stream().toList();
			for (DecoratedPot.Side side : DecoratedPot.Side.values()) {
				if (random.nextDouble() > 0.45) {
					Material material = materials.get(random.nextInt(0, materials.size()));
					decoratedPot.setSherd(side, material);
				}
			}
			blockStateMeta.setBlockState(decoratedPot);
		}
		if (illegalConfig.getBoolean("random.all-enchanted-books", true) && meta instanceof EnchantmentStorageMeta enchantmentStorageMeta){
			if (random.nextDouble()>0.20){
				List<Enchantment> enchantments = Registry.ENCHANTMENT.stream().toList();
				Enchantment enchantment = enchantments.get(random.nextInt(0, enchantments.size()-1));
				if (enchantment.getMaxLevel() == 1){
					enchantmentStorageMeta.addStoredEnchant(enchantment, 1, false);
				} else {
					enchantmentStorageMeta.addStoredEnchant(enchantment, random.nextInt(1, enchantment.getMaxLevel()), false);
				}
			}
		}
		if (illegalConfig.getInt("random.random-firework-boost", 0) > 0 && meta instanceof FireworkMeta fireworkMeta){
			int power = random.nextInt(0, illegalConfig.getInt("random.random-firework-boost", 0));
			if (power != 0){
				fireworkMeta.setPower(power);
			}
		}

		itemStack.setItemMeta(meta);
		return itemStack;
	}


	/*
	 * SPAWNS
	 */


	public void requestSpawnSaves(){
	}
	public void requestSpawnRemove(SpawnPosition spawn){

	}
	public void addSpawn(SpawnPosition spawn) {
		this.spawns.put(spawn.getName().toLowerCase(), spawn);
		this.requestSpawnSaves();
	}

	public void removeSpawn(String spawnName) {
		this.requestSpawnRemove(spawns.get(spawnName));
		this.spawns.remove(spawnName.toLowerCase());
	}

	/*
	 * MESSAGE GROUPS
	 */

	public void requestMessageGroupSave(){
	}
}
