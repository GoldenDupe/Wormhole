package xyz.goldendupe.command.defaults;

import bet.astral.fluffy.manager.CombatManager;
import bet.astral.messenger.placeholder.Placeholder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.incendo.cloud.Command;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.IntegerParser;
import org.jetbrains.annotations.NotNull;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.utils.ContainerUtils;
import xyz.goldendupe.utils.MemberType;

import java.util.*;

@Cloud
public class DupeCommand extends GDCloudCommand {
	private static final int DUPE_GUI_TICKS = 15;
	private final Map<UUID, DonatorDupeMenu> dupeInventories = new HashMap<>();
	public DupeCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
		super(goldenDupe, commandManager);
		Command.Builder<Player> dupe = commandManager.commandBuilder(
				"dupe",
				Description.of("Allows player to dupe their current held item."),
				"d"
		)
				.senderType(Player.class)
				.handler(context->{
					Player sender = context.sender();
					ItemStack itemStack = sender.getInventory().getItemInMainHand();
					if (!canDupe(itemStack)){
						commandMessenger.message(sender, "dupe.message-undupable");
						return;
					} else if (anyIllegals(itemStack)){
						if (ContainerUtils.isShulkerBox(itemStack)){
							commandMessenger.message(sender, "dupe.message-undupable-shulker");
						} else if (ContainerUtils.isBundle(itemStack)){
							commandMessenger.message(sender, "dupe.message-undupable-bundle");
						} else {
							commandMessenger.message(sender, "dupe.message-undupable");
						}
						return;
					} else if (!canDupeCombat(itemStack, sender)){
						commandMessenger.message(sender, "dupe.message-undupable-combat");
						return;
					}
					sender.getInventory().addItem(itemStack);
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
						commandMessenger.message(sender, "dupe.message-undupable");
						return;
					} else if (anyIllegals(itemStack)){
						if (ContainerUtils.isShulkerBox(itemStack)){
							commandMessenger.message(sender, "dupe.message-undupable-shulker");
						} else if (ContainerUtils.isBundle(itemStack)){
							commandMessenger.message(sender, "dupe.message-undupable-bundle");
						} else {
							commandMessenger.message(sender, "dupe.message-undupable");
						}
						return;
					} else if (!canDupeCombat(itemStack, sender)){
						commandMessenger.message(sender, "dupe.message-undupable-combat");
						return;
					}

					int debug = 0;
					for (int i = 0; i < times-1; i++){
						itemStack = sender.getInventory().getItemInMainHand();

						debug++;
						if (sender.getInventory().addItem(itemStack).isEmpty()){
							continue;
						}
						commandMessenger.message(sender, "dupe.message-super-duper", new Placeholder("super-duper", debug));
						return;
					}
					if (debug>1){
						commandMessenger.message(sender, "dupe.message-super-duper", new Placeholder("super-duper", debug));
					}
				})
		);
		commandManager.command(dupe.literal("menu")
				.permission(MemberType.DONATOR.cloudOf("dupe-menu"))
				.handler(context->{
					Player sender = context.sender();
					dupeInventories.putIfAbsent(sender.getUniqueId(), new DonatorDupeMenu(this, sender));
					sender.openInventory(dupeInventories.get(sender.getUniqueId()).getInventory());
				})
		);

		goldenDupe.getServer().getScheduler().runTaskTimer(goldenDupe, (timer)->{
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

	public boolean canDupe(ItemStack itemStack){
		if (itemStack.hasItemMeta()){
			ItemMeta meta = itemStack.getItemMeta();
			PersistentDataContainer container = meta.getPersistentDataContainer();
			if (container.has(goldenDupe.KEY_UNDUPABLE) && Boolean.TRUE.equals(container.get(goldenDupe.KEY_UNDUPABLE, PersistentDataType.BOOLEAN))){
				return false;
			}
		}
		return !(goldenDupe.getIllegalDupe().contains(itemStack.getType()));
	}
	public boolean canDupeCombat(ItemStack itemStack, @NotNull OfflinePlayer player){
		CombatManager combatManager = goldenDupe.getFluffy().getCombatManager();
		if (combatManager.hasTags(player)){
			return !(goldenDupe.getIllegalDupeCombat().contains(itemStack.getType()));
		}
		return true;
	}



	public boolean anyIllegals(ItemStack itemStack){
		if (ContainerUtils.isCarryableContainer(itemStack)){
			for (ItemStack item : ContainerUtils.getCarryableContainer(itemStack)){
				if (anyIllegals(item)){
					return false;
				}
			}
		} else {
			return canDupe(itemStack);
		}
		return false;
	}

	private static class DonatorDupeMenu implements InventoryHolder {
		private final UUID owner;
		private final Inventory inventory;
		private final DupeCommand dupeCommand;
		public DonatorDupeMenu(DupeCommand dupeCommand, Player owner){
			inventory = Bukkit.createInventory(this, 2, Component.text("Donator Auto Duper"));
			inventory.clear();
			this.dupeCommand = dupeCommand;
			this.owner = owner.getUniqueId();
		}

		private void tick(){
			if (inventory.isEmpty()){
				return;
			}
			for (ItemStack itemStack : inventory.getContents()){
				if (itemStack==null || itemStack.isEmpty()){
					continue;
				}
				if (!dupeCommand.canDupe(itemStack)){
					return;
				}
				if (dupeCommand.anyIllegals(itemStack)) {
					continue;
				}
				if (!dupeCommand.canDupeCombat(itemStack, Bukkit.getOfflinePlayer(owner))){
					continue;
				}
				itemStack.setAmount(itemStack.getAmount());
				if (itemStack.getType().getMaxStackSize()>itemStack.getAmount()){
					itemStack.setAmount(itemStack.getType().getMaxStackSize());
				}
			}
		}

		@Override
		public @NotNull Inventory getInventory() {
			return inventory;
		}
	}
}
