package xyz.goldendupe.command.defaults;

import bet.astral.fluffy.manager.CombatManager;
import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.placeholder.Placeholder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.incendo.cloud.Command;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.IntegerParser;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.models.GDSavedData;
import xyz.goldendupe.models.GDSettings;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.utils.ContainerUtils;
import xyz.goldendupe.utils.MemberType;

import java.util.*;

@Cloud
public class DupeCommand extends GDCloudCommand {
	private static final int DUPE_GUI_TICKS = 7;
	private final Map<UUID, DonatorDupeMenu> dupeInventories = new HashMap<>();
	public DupeCommand(GoldenDupeBootstrap bootstrap, PaperCommandManager<CommandSender> commandManager) {
		super(bootstrap, commandManager);
		Command.Builder<Player> dupe = commandManager.commandBuilder(
				"dupe",
				Description.of("Allows player to dupe their current held item."),
				"d"
		)
				.senderType(Player.class)
				.permission(MemberType.DEFAULT.permissionOf("dupe"))
				.handler(context->{
					Player sender = context.sender();
					ItemStack itemStack = sender.getInventory().getItemInMainHand();
					if (!canDupe(itemStack)){
						messenger.message(sender, Translations.COMMAND_DUPE_UNDUPABLE);
						return;
					} else if (anyIllegals(itemStack)){
						if (ContainerUtils.isShulkerBox(itemStack)){
							messenger.message(sender, Translations.COMMAND_DUPE_SHULKER);
						} else if (ContainerUtils.isBundle(itemStack)){
							messenger.message(sender, Translations.COMMAND_DUPE_BUNDLE);
						} else {
							messenger.message(sender, Translations.COMMAND_DUPE_UNDUPABLE);
						}
						return;
					} else if (!canDupeCombat(itemStack, sender)){
						messenger.message(sender, Translations.COMMAND_DUPE_COMBAT);
						return;
					}
					sender.getInventory().addItem(itemStack);
					GDPlayer gdPlayer = goldenDupe().playerDatabase().fromPlayer(sender);
					GDSavedData globalData = goldenDupe().getSavedData();

					gdPlayer.setTimesDuped(gdPlayer.getTimesDuped()+1);
					gdPlayer.setItemsDuped(gdPlayer.getItemsDuped()+itemStack.getAmount());
					globalData.setTimesDuped(globalData.getTimesDuped()+1);
					globalData.setItemsDuped(globalData.getItemsDuped()+itemStack.getAmount());
				});

		commandManager.command(dupe);
		commandManager.command(dupe
				.argument(
						IntegerParser.integerComponent()
								.name("amount")
								.parser(IntegerParser.integerParser(2, 8)))
				.handler(context->{
					Player sender = context.sender();
					int times = context.get("amount");
					ItemStack itemStack = sender.getInventory().getItemInMainHand();

					if (!canDupe(itemStack)){
						messenger.message(sender, Translations.COMMAND_DUPE_UNDUPABLE);
						return;
					} else if (anyIllegals(itemStack)){
						if (ContainerUtils.isShulkerBox(itemStack)){
							messenger.message(sender, Translations.COMMAND_DUPE_SHULKER);
						} else if (ContainerUtils.isBundle(itemStack)){
							messenger.message(sender, Translations.COMMAND_DUPE_BUNDLE);
						} else {
							messenger.message(sender, Translations.COMMAND_DUPE_UNDUPABLE);
						}
						return;
					} else if (!canDupeCombat(itemStack, sender)){
						messenger.message(sender, Translations.COMMAND_DUPE_COMBAT);
						return;
					}
					GDPlayer gdPlayer = goldenDupe().playerDatabase().fromPlayer(sender);
					GDSavedData globalData = goldenDupe().getSavedData();
					int debug = 0;
					for (int i = 0; i < times-1; i++){
						itemStack = sender.getInventory().getItemInMainHand();
						gdPlayer.setTimesDuped(gdPlayer.getTimesDuped()+1);
						gdPlayer.setItemsDuped(gdPlayer.getItemsDuped()+itemStack.getAmount());
						globalData.setTimesDuped(globalData.getTimesDuped()+1);
						globalData.setItemsDuped(globalData.getItemsDuped()+itemStack.getAmount());

						debug++;
						if (sender.getInventory().addItem(itemStack).isEmpty()){
							continue;
						}
						messenger.message(sender, Translations.COMMAND_DUPE_SUPER_DUPER, Placeholder.of("super-duper", debug + 1));
						return;
					}
					if (debug>1){
						messenger.message(sender, Translations.COMMAND_DUPE_SUPER_DUPER, Placeholder.of("super-duper", debug+1));
					}
				})
		);
		commandManager.command(dupe.literal("menu")
				.permission(MemberType.DONATOR.cloudOf("dupe-menu"))
				.handler(context->{
					goldenDupe().getServer().getScheduler().runTask(goldenDupe(), ()->{
						Player sender = context.sender();
						dupeInventories.putIfAbsent(sender.getUniqueId(), new DonatorDupeMenu(this, sender));
						sender.openInventory(dupeInventories.get(sender.getUniqueId()).getInventory());
					});
				})
		);

		goldenDupe().getServer().getScheduler().runTaskTimer(goldenDupe(), (timer)->{
			if (dupeInventories.isEmpty()){
				return;
			}
			for (DonatorDupeMenu menu : dupeInventories.values()){
				menu.tick();
				for (HumanEntity entity : menu.inventory.getViewers()){
					if (entity instanceof Player player){
						player.updateInventory();
					}
				}
			}
		}, 20, DUPE_GUI_TICKS);
	}

	public boolean canDupe(ItemStack itemStack) {
		if (itemStack.hasItemMeta()) {
			ItemMeta meta = itemStack.getItemMeta();
			PersistentDataContainer container = meta.getPersistentDataContainer();
			if (container.has(goldenDupe().KEY_UNDUPABLE) && Boolean.TRUE.equals(container.get(goldenDupe().KEY_UNDUPABLE, PersistentDataType.BOOLEAN))) {
				return false;
			}
		}
		for (Material material : goldenDupe().getSettings().getIllegalDupe()) {
			if (material == itemStack.getType()) {
				return false;
			}
		}
		return true;
	}
	public boolean canDupeCombat(ItemStack itemStack, @NotNull OfflinePlayer player){
		CombatManager combatManager = goldenDupe().getFluffy().getCombatManager();
		if (combatManager.hasTags(player)){
			return !(goldenDupe().getSettings().getIllegalDupeCombat().contains(itemStack.getType()));
		}
		return true;
	}



	public boolean anyIllegals(ItemStack itemStack){
		if (ContainerUtils.isCarryableContainer(itemStack)){
			for (ItemStack item : ContainerUtils.getCarryableContainer(itemStack)){
				if (item == null){
					continue;
				}
				if (anyIllegals(item)){
					return false;
				}
			}
		} else {
			return !canDupe(itemStack);
		}
		return false;
	}

	private static class DonatorDupeMenu implements InventoryHolder {
		private static final NamespacedKey BACKGROUND_KEY = new NamespacedKey("goldendupe", "gui_duper");
		private static final ItemStack BACKGROUND = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		static {
			BACKGROUND.editMeta(meta->{
				meta.addItemFlags(ItemFlag.values());
				meta.displayName(Component.text(""));
				meta.getPersistentDataContainer().set(BACKGROUND_KEY, PersistentDataType.BOOLEAN, true);
			});
		}
		private final UUID owner;
		private final Inventory inventory;
		private final DupeCommand dupeCommand;
		public DonatorDupeMenu(DupeCommand dupeCommand, Player owner){
//			inventory = Bukkit.createInventory(this, InventoryType.DROPPER, Component.text("Donator Auto Duper"));
			inventory = Bukkit.createInventory(this, 9*3, Component.text("Donator Auto Duper"));
			inventory.clear();
			inventory.setItem(0, BACKGROUND);
			inventory.setItem(1, BACKGROUND);
			inventory.setItem(7, BACKGROUND);
			inventory.setItem(8, BACKGROUND);

			inventory.setItem(9, BACKGROUND);
			inventory.setItem(10, BACKGROUND);
			inventory.setItem(16, BACKGROUND);
			inventory.setItem(17, BACKGROUND);

			inventory.setItem(18, BACKGROUND);
			inventory.setItem(19, BACKGROUND);
			inventory.setItem(25, BACKGROUND);
			inventory.setItem(26, BACKGROUND);
			this.dupeCommand = dupeCommand;
			this.owner = owner.getUniqueId();
		}

		private void tick() {
			if (inventory.isEmpty()) {
				return;
			}
			for (ItemStack itemStack : inventory.getContents()) {
				if (itemStack == null || itemStack.isEmpty()) {
					continue;
				}
				if (!dupeCommand.canDupe(itemStack)) {
					continue;
				}
				if (dupeCommand.anyIllegals(itemStack)) {
					continue;
				}
				if (!dupeCommand.canDupeCombat(itemStack, Bukkit.getOfflinePlayer(owner))) {
					continue;
				}
				if (itemStack.getItemMeta().getPersistentDataContainer().getOrDefault(BACKGROUND_KEY, PersistentDataType.BOOLEAN, false)) {
					continue;
				}
				itemStack.setAmount(Math.min(itemStack.getAmount() * 2, itemStack.getType().getMaxStackSize()));
			}
		}

		@Override
		public @NotNull Inventory getInventory() {
			return inventory;
		}
	}
}
