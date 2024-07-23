package xyz.goldendupe.command.defaults;

import bet.astral.guiman.Clickable;
import bet.astral.guiman.ClickableBuilder;
import bet.astral.guiman.GUI;
import bet.astral.guiman.GUIBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.flag.CommandFlag;
import xyz.goldendupe.GoldenDupe;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.utils.MemberType;

import java.util.List;

@Cloud
public class ClearInventoryCommand extends GDCloudCommand {

	public static final GUI clearMenu;

	static {

		List<ClickType> clickTypes = List.of(ClickType.RIGHT, ClickType.LEFT, ClickType.SHIFT_RIGHT, ClickType.SHIFT_LEFT);

		ItemStack itemStackConf = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
		itemStackConf.editMeta(meta -> {
			meta.displayName(Component.text("Confirm", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
			meta.lore(List.of(Component.text("Click to clear inventory.", NamedTextColor.RED)
					.appendSpace().append(Component.text("(No going back!)", NamedTextColor.DARK_RED))
					.decoration(TextDecoration.ITALIC, false)));
		});

		ItemStack itemStackDeny = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		itemStackDeny.editMeta(meta -> {
			meta.displayName(Component.text("Cancel", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
			meta.lore(List.of(Component.text("Click to cancel the clearing of inventory.", NamedTextColor.RED)
					.decoration(TextDecoration.ITALIC, false)));
		});

		Clickable conf = new ClickableBuilder(itemStackConf).setAction(clickTypes, (clickable, i, player) -> {
					player.getInventory().clear();
					player.getInventory().setHelmet(null);
					player.getInventory().setChestplate(null);
					player.getInventory().setLeggings(null);
					player.getInventory().setBoots(null);
					player.getInventory().setItemInOffHand(null);
					player.closeInventory(InventoryCloseEvent.Reason.CANT_USE);
					GoldenDupe.instance().messenger().message(player, Translations.COMMAND_SET_HOME_SUCCESS);
				}).build();

		Clickable deny = new ClickableBuilder(itemStackDeny).setAction(clickTypes, (clickable, i, player) -> {
			player.closeInventory(InventoryCloseEvent.Reason.CANT_USE);
			GoldenDupe.getPlugin(GoldenDupe.class).messenger().message(player, Translations.COMMAND_CLEAR_CANCEL);
		}).build();

		GUIBuilder builder = new GUIBuilder(3).name(Component.text("Clear Inventory Confirmation"));
		List<Integer> slotsConf = List.of(0, 1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21);
		List<Integer> slotsDeny = List.of(5, 6, 7, 8, 14, 15, 16, 17, 23, 24, 25, 26);

		for (Integer i : slotsConf) {
			builder.addSlotClickable(i, conf);
		}

		for (Integer i : slotsDeny) {
			builder.addSlotClickable(i, deny);
		}

		clearMenu = builder.build();
		clearMenu.shared = true;

	}

	public ClearInventoryCommand(GoldenDupeBootstrap bootstrap, PaperCommandManager<CommandSender> commandManager) {
		super(bootstrap, commandManager);
		commandManager.command(
				commandManager.commandBuilder(
								"clear",
								Description.of("Clears the player's inventory."),
								"clearinv", "clearinventory", "inventoryclear", "invclear"
						)
						.permission(MemberType.DEFAULT.permissionOf("trash"))
						.senderType(Player.class)
						.flag(CommandFlag.builder("change-auto-confirm"))
						.handler(context -> {
							Player sender = context.sender();
							GDPlayer player = goldenDupe().playerDatabase().fromPlayer(sender);
							if (context.flags().isPresent("change-auto-confirm")) {
								if (player.autoConfirmClearInv()) {
									messenger.message(sender, Translations.COMMAND_CLEAR_TOGGLE_TRUE);
								} else {
									messenger.message(sender, Translations.COMMAND_CLEAR_TOGGLE_FALSE);
								}
								player.setAutoConfirmClearInv(!player.autoConfirmClearInv());
							} else {
								if (player.autoConfirmClearInv()) {
									sender.getInventory().clear();
									sender.getInventory().setHelmet(null);
									sender.getInventory().setChestplate(null);
									sender.getInventory().setLeggings(null);
									sender.getInventory().setBoots(null);
									sender.getInventory().setItemInOffHand(null);
									messenger.message(sender, Translations.COMMAND_CLEAR_CLEARED);
									return;
								}

								goldenDupe().getServer().getScheduler().runTask(goldenDupe(), ()->{
									clearMenu.generateInventory(sender);
								});
							}
						})
		);
	}
}
