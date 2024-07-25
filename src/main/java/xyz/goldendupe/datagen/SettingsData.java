package xyz.goldendupe.datagen;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.models.GDSettings;

import java.util.*;

public class SettingsData extends GDSettings {
	public SettingsData() {
		super(null, null, null, null, null);
	}

	@Override
	public GoldenDupe getGoldenDupe() {
		fetchGoldenDupe();
		return super.getGoldenDupe();
	}

	@Override
	public Set<Material> getIllegalDupeCombat() {
		Set<Material> materials = new HashSet<>();
		materials.add(Material.ENDER_PEARL);
		materials.add(Material.TOTEM_OF_UNDYING);
		materials.add(Material.POTION);
		materials.add(Material.SPLASH_POTION);
		materials.add(Material.LINGERING_POTION);
		materials.add(Material.CHORUS_FRUIT);
		return materials;
	}

	@Override
	public Set<Material> getIllegalDupe() {
		Set<Material> materials = new HashSet<>();
		materials.add(Material.BEDROCK);
		materials.add(Material.BARRIER);
		materials.add(Material.STRUCTURE_VOID);
		materials.add(Material.STRUCTURE_BLOCK);
		materials.add(Material.REINFORCED_DEEPSLATE);
		materials.add(Material.COMMAND_BLOCK);
		materials.add(Material.CHAIN_COMMAND_BLOCK);
		materials.add(Material.REPEATING_COMMAND_BLOCK);
		materials.add(Material.END_PORTAL_FRAME);
		materials.add(Material.LIGHT);
		materials.add(Material.DRAGON_EGG);
		materials.add(Material.SPAWNER);
		materials.add(Material.TRIAL_SPAWNER);
		materials.add(Material.VAULT);
		materials.add(Material.COMMAND_BLOCK_MINECART);

		materials.add(Material.PLAYER_HEAD);
		materials.add(Material.ZOMBIE_HEAD);
		materials.add(Material.CREEPER_HEAD);
		materials.add(Material.PIGLIN_HEAD);
		materials.add(Material.DRAGON_HEAD);
		materials.add(Material.WITHER_SKELETON_SKULL);
		materials.add(Material.SKELETON_SKULL);
		materials.add(Material.ALLAY_SPAWN_EGG);
		materials.add(Material.AXOLOTL_SPAWN_EGG);
		materials.add(Material.BAT_SPAWN_EGG);
		materials.add(Material.BEE_SPAWN_EGG);
		materials.add(Material.BLAZE_SPAWN_EGG);
		materials.add(Material.BREEZE_SPAWN_EGG);
		materials.add(Material.BOGGED_SPAWN_EGG);
		materials.add(Material.CAT_SPAWN_EGG);
		materials.add(Material.CAMEL_SPAWN_EGG);
		materials.add(Material.CAVE_SPIDER_SPAWN_EGG);
		materials.add(Material.CHICKEN_SPAWN_EGG);
		materials.add(Material.COD_SPAWN_EGG);
		materials.add(Material.COW_SPAWN_EGG);
		materials.add(Material.CREEPER_SPAWN_EGG);
		materials.add(Material.DOLPHIN_SPAWN_EGG);
		materials.add(Material.DROWNED_SPAWN_EGG);
		materials.add(Material.ELDER_GUARDIAN_SPAWN_EGG);
		materials.add(Material.ENDER_DRAGON_SPAWN_EGG);
		materials.add(Material.ENDERMAN_SPAWN_EGG);
		materials.add(Material.ENDERMITE_SPAWN_EGG);
		materials.add(Material.EVOKER_SPAWN_EGG);
		materials.add(Material.FOX_SPAWN_EGG);
		materials.add(Material.FROG_SPAWN_EGG);
		materials.add(Material.GHAST_SPAWN_EGG);
		materials.add(Material.GLOW_SQUID_SPAWN_EGG);
		materials.add(Material.GOAT_SPAWN_EGG);
		materials.add(Material.GUARDIAN_SPAWN_EGG);
		materials.add(Material.HOGLIN_SPAWN_EGG);
		materials.add(Material.HORSE_SPAWN_EGG);
		materials.add(Material.HUSK_SPAWN_EGG);
		materials.add(Material.IRON_GOLEM_SPAWN_EGG);
		materials.add(Material.LLAMA_SPAWN_EGG);
		materials.add(Material.MAGMA_CUBE_SPAWN_EGG);
		materials.add(Material.MOOSHROOM_SPAWN_EGG);
		materials.add(Material.MULE_SPAWN_EGG);
		materials.add(Material.OCELOT_SPAWN_EGG);
		materials.add(Material.PANDA_SPAWN_EGG);
		materials.add(Material.PARROT_SPAWN_EGG);
		materials.add(Material.PHANTOM_SPAWN_EGG);
		materials.add(Material.PIG_SPAWN_EGG);
		materials.add(Material.PIGLIN_SPAWN_EGG);
		materials.add(Material.PIGLIN_BRUTE_SPAWN_EGG);
		materials.add(Material.PILLAGER_SPAWN_EGG);
		materials.add(Material.POLAR_BEAR_SPAWN_EGG);
		materials.add(Material.PUFFERFISH_SPAWN_EGG);
		materials.add(Material.RABBIT_SPAWN_EGG);
		materials.add(Material.RAVAGER_SPAWN_EGG);
		materials.add(Material.SALMON_SPAWN_EGG);
		materials.add(Material.SHEEP_SPAWN_EGG);
		materials.add(Material.SHULKER_SPAWN_EGG);
		materials.add(Material.SILVERFISH_SPAWN_EGG);
		materials.add(Material.SKELETON_SPAWN_EGG);
		materials.add(Material.SKELETON_HORSE_SPAWN_EGG);
		materials.add(Material.SLIME_SPAWN_EGG);
		materials.add(Material.SNIFFER_SPAWN_EGG);
		materials.add(Material.SNOW_GOLEM_SPAWN_EGG);
		materials.add(Material.SPIDER_SPAWN_EGG);
		materials.add(Material.SQUID_SPAWN_EGG);
		materials.add(Material.STRAY_SPAWN_EGG);
		materials.add(Material.STRIDER_SPAWN_EGG);
		materials.add(Material.TADPOLE_SPAWN_EGG);
		materials.add(Material.TRADER_LLAMA_SPAWN_EGG);
		materials.add(Material.TROPICAL_FISH_SPAWN_EGG);
		materials.add(Material.TURTLE_SPAWN_EGG);
		materials.add(Material.VEX_SPAWN_EGG);
		materials.add(Material.VILLAGER_SPAWN_EGG);
		materials.add(Material.VINDICATOR_SPAWN_EGG);
		materials.add(Material.WANDERING_TRADER_SPAWN_EGG);
		materials.add(Material.WARDEN_SPAWN_EGG);
		materials.add(Material.WITCH_SPAWN_EGG);
		materials.add(Material.WITHER_SPAWN_EGG);
		materials.add(Material.WITHER_SKELETON_SPAWN_EGG);
		materials.add(Material.WOLF_SPAWN_EGG);
		materials.add(Material.ZOGLIN_SPAWN_EGG);
		materials.add(Material.ZOMBIE_SPAWN_EGG);
		materials.add(Material.ZOMBIE_HORSE_SPAWN_EGG);
		materials.add(Material.ZOMBIE_VILLAGER_SPAWN_EGG);
		materials.add(Material.ZOMBIFIED_PIGLIN_SPAWN_EGG);
		return materials;
	}

	@Override
	public Set<Material> getIllegalPlacements() {
		Set<Material> materials = new HashSet<>();
		materials.add(Material.BEDROCK);
		materials.add(Material.BARRIER);
		materials.add(Material.STRUCTURE_VOID);
		materials.add(Material.STRUCTURE_BLOCK);
		materials.add(Material.REINFORCED_DEEPSLATE);
		materials.add(Material.COMMAND_BLOCK);
		materials.add(Material.CHAIN_COMMAND_BLOCK);
		materials.add(Material.REPEATING_COMMAND_BLOCK);
		materials.add(Material.END_PORTAL_FRAME);
		materials.add(Material.LIGHT);
		materials.add(Material.DRAGON_EGG);
		materials.add(Material.SPAWNER);
		materials.add(Material.TRIAL_SPAWNER);
		materials.add(Material.VAULT);
		return materials;
	}

	@Override
	public RandomItemsData getRandomItemData() {
		Set<Material> illegals = new HashSet<>(getIllegalDupe());

		return new RandomItemsData(illegals,
				Set.of(Enchantment.MENDING.getKey(), Enchantment.SHARPNESS.getKey(), Enchantment.PROTECTION.getKey(), Enchantment.POWER.getKey()),
				true,
				true,
				true,
				true,
				3,
				true,
				true,
				true,
				true,
				5
		);
	}

	@Override
	public boolean isGlobalChatMute() {
		return false;
	}

	@Override
	public AllowedUsers getGlobalChatMuteAllowedUsers() {
		return AllowedUsers.ALL;
	}

	@Override
	public List<String> getUwuString() {
		List<String> messages = new LinkedList<>();
		messages.add("I weally weally love GowdenDupe~");
		messages.add("I weally weally wuv gowdendewp~");
		messages.add("I'm gay");
		return messages;
	}
}
