package xyz.goldendupe.command.defaults;

import bet.astral.messenger.placeholder.Placeholder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.incendo.cloud.Command;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.IntegerParser;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.command.internal.cloud.Cloud;
import xyz.goldendupe.command.internal.cloud.GDCloudCommand;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.internal.legacy.GDCommandInfo;
import xyz.goldendupe.utils.ContainerUtils;

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
					if (!goldenDupe.canDupe(itemStack)){
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
					if (!goldenDupe.canDupe(itemStack)){
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
				.permission(GDCommandInfo.MemberType.DONATOR.cloudOf("dupe-menu"))
				.handler(context->{
					Player sender = context.sender();
					dupeInventories.putIfAbsent(sender.getUniqueId(), new DonatorDupeMenu());
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

	public static boolean anyIllegals(ItemStack itemStack){
		if (ContainerUtils.isCarryableContainer(itemStack)){
			for (ItemStack item : ContainerUtils.getCarryableContainer(itemStack)){
				if (anyIllegals(item)){
					return false;
				}
			}
		} else {
			return !GoldenDupe.getPlugin(GoldenDupe.class).canDupe(itemStack);
		}
		return false;
	}

	private static class DonatorDupeMenu implements InventoryHolder {
		private final Inventory inventory;
		public DonatorDupeMenu(){
			inventory = Bukkit.createInventory(this, 2, Component.text("Donator Auto Duper"));
			inventory.clear();
		}

		private void tick(){
			if (inventory.isEmpty()){
				return;
			}
			for (ItemStack itemStack : inventory.getContents()){
				if (itemStack==null || itemStack.isEmpty()){
					continue;
				}
				if (anyIllegals(itemStack)) {
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
