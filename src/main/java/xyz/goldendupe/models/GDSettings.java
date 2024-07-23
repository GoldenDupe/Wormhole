package xyz.goldendupe.models;

import com.google.common.collect.ImmutableList;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.block.DecoratedPot;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.utils.MemberType;

import java.io.File;
import java.util.*;

@Getter
public class GDSettings {
	private GoldenDupe goldenDupe;
	private final Map<UUID, GDMessageGroup> messageGroups = new HashMap<>();
	@Setter
	private Set<Material> illegalDupeCombat;
	@Setter
	private Set<Material> illegalDupe;
	@Setter
	private Set<Material> illegalPlacements;
	@Setter
	private RandomItemsData randomItemData;
	@Setter
	@Deprecated(forRemoval = true)
	private long timesDuped;
	@Setter
	@Deprecated(forRemoval = true)
	private long itemsDuped;
	@Setter
	@Deprecated(forRemoval = true)
	private long randomItemsGenerated;
	@Setter
	@Deprecated(forRemoval = true)
	private boolean globalChatMute = false;
	@Setter
	@Deprecated(forRemoval = true)
	private AllowedUsers globalChatMuteAllowedUsers = AllowedUsers.ALL;

	public GDSettings(Set<Material> illegalDupeCombat, Set<Material> illegalDupe, Set<Material> illegalPlacements, RandomItemsData randomItemData) {
		this.illegalDupeCombat = illegalDupeCombat;
		this.illegalDupe = illegalDupe;
		this.illegalPlacements = illegalPlacements;
		this.randomItemData = randomItemData;
	}

	public void fetchGoldenDupe(){
		if (goldenDupe==null){
			goldenDupe = GoldenDupe.instance();
		}
	}


	public void reload(){
		fetchGoldenDupe();
		reloadIllegals();

	}

	/*
	 * ILLEGALS
	 */

	@Deprecated(forRemoval = true)
	public void reloadIllegals(){
		fetchGoldenDupe();
		File file = new File(goldenDupe.getDataFolder(), "illegals.yml");
		YamlConfiguration illegalConfig = YamlConfiguration.loadConfiguration(file);

		illegalDupe = new HashSet<>();
		illegalDupeCombat = new HashSet<>();


		addMaterials(illegalDupe, illegalConfig.getStringList("dupe.illegals"));
		illegalDupe.add(Material.AIR);
		addMaterials(illegalDupeCombat, illegalConfig.getStringList("dupe.combat"));
		addMaterials(illegalPlacements, illegalConfig.getStringList("placement"));
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
		fetchGoldenDupe();
		ItemMeta meta = itemStack.getItemMeta();

		if (randomItemData.allowUpdatedGoatHorns && meta instanceof MusicInstrumentMeta instrumentMeta){
			List<MusicInstrument> instruments = Registry.INSTRUMENT.stream().toList();
			if (random.nextDouble()>0.1){
				instrumentMeta.setInstrument(instruments.get(random.nextInt(0, instruments.size()-1)));
			}
		}
		if (randomItemData.allowUpdatedDecoratedPots && meta instanceof BlockStateMeta blockStateMeta){
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
		if (randomItemData.allowUpdatedEnchantmentBooks && meta instanceof EnchantmentStorageMeta enchantmentStorageMeta){
			if (random.nextDouble()>0.20){
				List<Enchantment> enchantments = new ArrayList<>(RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT)
						.stream().filter(enchantment -> !randomItemData.illegalEnchants.contains(enchantment.getKey())).toList());
				if (randomItemData.allowOnlyVanillaEnchantedBooks){
					enchantments.removeIf(enchant->!enchant.getKey().getNamespace().equalsIgnoreCase("minecraft"));
				}
				Enchantment enchantment = enchantments.get(random.nextInt(0, enchantments.size()-1));
				if (enchantment.getMaxLevel() == 1){
					enchantmentStorageMeta.addStoredEnchant(enchantment, 1, false);
				} else {
					enchantmentStorageMeta.addStoredEnchant(enchantment, random.nextInt(1, enchantment.getMaxLevel()), false);
				}
			}
		}
		if (randomItemData.allowUpdatedFireworks && meta instanceof FireworkMeta fireworkMeta){
			int power = random.nextInt(0, randomItemData.maxFireworkBoost);
			if (power > 0){
				fireworkMeta.setPower(power);
			}
		}

		itemStack.setItemMeta(meta);
		return itemStack;
	}

	@Getter
	public enum AllowedUsers {
		ALL("", MemberType.MODERATOR.permissionOf("mute-chat")),
		DONATOR(MemberType.DONATOR.permissionOf("mute-chat-bypass"), MemberType.ADMINISTRATOR.permissionOf("mute-chat.donator")),
		STAFF(MemberType.MODERATOR.permissionOf("mute-chat-bypass"), MemberType.MODERATOR.permissionOf("mute-chat.staff")),
		ADMIN(MemberType.ADMINISTRATOR.permissionOf("mute-chat-bypass"), MemberType.ADMINISTRATOR.permissionOf("mute-chat")),
		OWNER(MemberType.OWNER.permissionOf("mute-chat-bypass"), MemberType.OWNER.permissionOf("mute-chat")),
		;

		private final String bypass;
		private final String command;
		AllowedUsers(String bypass, String command) {
			this.bypass = bypass;
			this.command = command;
		}
	}

	@Getter
	public static class RandomItemsData {
		private final Random random;
		private final List<ItemStack> allowedItems;
		private final Set<Material> illegalsItems;
		private final Set<NamespacedKey> illegalEnchants;
		private final boolean allowUpdatedDecoratedPots;
		private final boolean allowUpdatedEnchantmentBooks;
		private final boolean allowOnlyVanillaEnchantedBooks;
		private final boolean allowUpdatedFireworks;
		private final int maxFireworkBoost;
		private final boolean allowUpdatedGoatHorns;

		public RandomItemsData(Random random, Set<Material> illegals, Set<NamespacedKey> illegalEnchants, boolean allowUpdatedSherds, boolean allowUpdatedBooks, boolean allowOnlyVanillaEnchants, boolean allowUpdatedFireworks, int maxFireworkBoost, boolean allowUpdatedGoatHorns) {
			this.random = random;
			this.illegalsItems = illegals;
			this.illegalEnchants = illegalEnchants;
			this.allowUpdatedDecoratedPots = allowUpdatedSherds;
			this.allowUpdatedEnchantmentBooks = allowUpdatedBooks;
			this.allowOnlyVanillaEnchantedBooks = allowOnlyVanillaEnchants;
			this.allowUpdatedFireworks = allowUpdatedFireworks;
			this.maxFireworkBoost = maxFireworkBoost;
			this.allowUpdatedGoatHorns = allowUpdatedGoatHorns;


			Registry<Material> materials = Registry.MATERIAL;
			World world = Bukkit.getWorlds().get(0);
			List<ItemStack> items = materials.stream().filter(material-> !illegalsItems.contains(material)).filter(material->material.isEnabledByFeature(world)).map(ItemStack::new).toList();
			allowedItems = ImmutableList.copyOf(items);
		}

		public List<ItemStack> getAllowedItems() {
			return ImmutableList.copyOf(allowedItems);
		}
	}
}
