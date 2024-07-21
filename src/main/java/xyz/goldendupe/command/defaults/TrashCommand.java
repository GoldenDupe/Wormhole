package xyz.goldendupe.command.defaults;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupe;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.models.chatcolor.Color;
import xyz.goldendupe.utils.MemberType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Cloud
public class TrashCommand extends GDCloudCommand {
	public static final long CLEAR_TIME = 300000;
	private static List<String> trashNames = new ArrayList<>();
	public static List<TrashInventory> trashCans = new ArrayList<>();
	private static List<ItemStack> bar = new ArrayList<>();
	static {
		trashNames.addAll(List.of(
				"FlammableFlowMC", "Josh", "Antritus", "_Devourer", "BaguetteWithInternetAccess",
				"Androdir", "Sowrd", "Bl1tzy", "2397", "Dream", "MrEnderBroFTW", "BluJay77",
				"SolarMedial", "Zoytax", "ToadAndYoshi", "PromanForever", "TotemPopper69", "NotSirGeorge",
				"SlushFiend", "ToadAndYoshi", "CyberedCake", "AgentPenguin", "SourceLeak", "PalePenguin",
				"JeffersonMBA", "Lynxdeer", "Prodeathmaster", "lilcorgi_", "ycu", "nc_optical", "ItsIsolation",
				"t6b", "crewly", "zeroarmy27", "sxlace_", "z_conquest", "catalystcx", "ImNotLying", "Kitkat2116",
				"Fate65", "bltck", "dev_ghosty - Scammed $100", "Kazaretski",  "pl0ks", "frogiswoman", "brodaaa",
				"kaylinthedragon", "arielazi", "1crusty1", "unluckyl", "craftingtoty1000", "kgroom123", "alphaarmor",
				"circular_man", "keith__", "jonahed", "notpetya", "cryptokian", "numba2"
		));

		NamespacedKey key = new NamespacedKey(GoldenDupe.getPlugin(GoldenDupe.class), "random");
		ItemStack before = new ItemStack(Material.ARROW);
		ItemStack after = new ItemStack(Material.ARROW);
		ItemStack clear = new ItemStack(Material.BARRIER);
		ItemStack voidItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		before.editMeta(meta->{
			meta.displayName(Component.text("Last Page", Color.MINECOIN).decoration(TextDecoration.ITALIC, false));
			meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "z");
		});
		after.editMeta(meta-> {
			meta.displayName(Component.text("Next Page", Color.MINECOIN).decoration(TextDecoration.ITALIC, false));
			meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "z");
		});
		voidItem.editMeta(meta->{
			meta.displayName(Component.text(""));
			meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "z");
		});
		clear.editMeta(meta->{
			meta.displayName(Component.text("Clear", Color.RED).decoration(TextDecoration.ITALIC, false));
			meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "z");
		});


		bar.add(before);
		bar.add(voidItem);
		bar.add(voidItem);
		bar.add(voidItem);
		bar.add(clear);
		bar.add(voidItem);
		bar.add(voidItem);
		bar.add(voidItem);
		bar.add(after);

		Random random = new Random(System.nanoTime()*System.currentTimeMillis());
		for (int i = 0; i < 10; i++)
			Collections.shuffle(trashNames, random);

		int forward = 1;
		int backward = -1;
		for (int i = 0; i < trashNames.size()-1; i++){
			TrashInventory inventory = new TrashInventory(forward, backward);
			trashCans.add(inventory);

			if (i==trashNames.size()-1){
				forward = trashNames.size()-1;
			}
			forward++;
			backward++;
		}
	}

	public TrashCommand(GoldenDupeBootstrap bootstrap, PaperCommandManager<CommandSender> commandManager) {
		super(bootstrap, commandManager);
		commandManager.command(commandManager.commandBuilder("trash",
						Description.of("Allows players to delete"),
						"garbage", "josh", "disposal", "rubbish", "tinder")
				.permission(MemberType.DEFAULT.cloudOf("trash"))
				.senderType(Player.class)
				.handler(context -> {
					goldenDupe().getServer().getScheduler().runTask(goldenDupe(), () -> {
						context.sender().openInventory(trashCans.get(0).inventory);
					});
				})
		);
	}

	public static class TrashInventory implements InventoryHolder {
		private final Inventory inventory;
		public final int forward;
		public final int backward;

		public TrashInventory(int forward, int backward) {
			this.forward = forward;
			this.backward = backward;
			this.inventory = Bukkit.createInventory(this, 54, Component.text(trashNames.get(backward+1)));
			int j = 0;
			for (int i = 45; i < 54; i++){
				inventory.setItem(i, bar.get(j));
				j++;
			}
		}


		@Override
		public @NotNull Inventory getInventory() {
			return inventory;
		}
	}
}
