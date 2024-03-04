package xyz.goldendupe.models;

import lombok.Getter;
import org.bukkit.*;
import org.bukkit.block.DecoratedPot;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.MusicInstrumentMeta;
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
		for (Material material : Material.values()){
			if (randomIllegals.contains(material.name())){
				continue;
			}
			randomItems.add(new ItemStack(material));
		}
		if (illegalConfig.getBoolean("random.all-goat-horns", false)){
			for (MusicInstrument instrument : Registry.INSTRUMENT){
				ItemStack itemStack = new ItemStack(Material.GOAT_HORN);
				MusicInstrumentMeta meta = (MusicInstrumentMeta) (itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(Material.GOAT_HORN));
				meta.setInstrument(instrument);
				itemStack.setItemMeta(meta);
				randomItems.add(itemStack);
			}
		}
		if (illegalConfig.getBoolean("random.full-decorated-potteries", false)){
			for (Material material : Tag.ITEMS_BREAKS_DECORATED_POTS.getValues()){
				ItemStack itemStack = new ItemStack(Material.DECORATED_POT);
				BlockStateMeta meta = (BlockStateMeta) (itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(Material.DECORATED_POT));
				DecoratedPot decoratedPot = (DecoratedPot) meta.getBlockState();
				decoratedPot.setSherd(DecoratedPot.Side.BACK, material);
				decoratedPot.setSherd(DecoratedPot.Side.FRONT, material);
				decoratedPot.setSherd(DecoratedPot.Side.LEFT, material);
				decoratedPot.setSherd(DecoratedPot.Side.RIGHT, material);
				meta.setBlockState(decoratedPot);
				itemStack.setItemMeta(meta);
				randomItems.add(itemStack);
			}
		}
		if (illegalConfig.getBoolean("random.all-enchanted-books", true)){
			for (Enchantment enchantment : Registry.ENCHANTMENT){
				if (enchantment == Enchantment.MENDING){
					// FUCK MENDING
					continue;
				}
				for (int i = 0; i < enchantment.getMaxLevel(); i++){
					ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);
					EnchantmentStorageMeta meta = (EnchantmentStorageMeta) (itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(Material.ENCHANTED_BOOK));
					meta.addStoredEnchant(enchantment, i, false);
					itemStack.setItemMeta(meta);
					randomItems.add(itemStack);
				}
			}
		}
		for (int i = 0; i < illegalConfig.getInt("random.random-firework-boost", 0); i++){
			ItemStack itemStack = new ItemStack(Material.FIREWORK_ROCKET);
			FireworkMeta meta = (FireworkMeta) (itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(Material.FIREWORK_ROCKET));
			meta.setPower(i);
			itemStack.setItemMeta(meta);
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
