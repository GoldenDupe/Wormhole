package xyz.goldendupe.command.defaults;

import xyz.goldendupe.messenger.GoldenMessenger;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.menu.SGMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.internal.legacy.GDCommand;
import xyz.goldendupe.command.internal.legacy.GDCommandInfo;
import xyz.goldendupe.models.GDPlayer;

import java.util.Collections;
import java.util.List;

@GDCommandInfo.Command(name = "clear", senderType = GDCommandInfo.SenderType.PLAYER,
		aliases = {"clearinv", "clearinventory", "inventoryclear", "invclear"})
public class ClearInventoryCommand extends GDCommand {
	public static final SGMenu menu;
	static {
		GoldenDupe goldenDupe = GoldenDupe.getPlugin(GoldenDupe.class);
		GoldenMessenger commandMessenger = goldenDupe.commandMessenger();
		menu = goldenDupe.spiGUI().create("Clear Inventory Confirmation", 3);
		menu.setAutomaticPaginationEnabled(false);

		ItemStack itemStackConfirm = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
		ItemMeta metaConfirm = itemStackConfirm.getItemMeta();
		metaConfirm.displayName(Component.text("Confirm", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
		metaConfirm.lore(List.of(Component.text("Click to clear inventory.", NamedTextColor.RED).appendSpace().append(Component.text("(No going back!)", NamedTextColor.DARK_RED)).decoration(TextDecoration.ITALIC, false)));
		itemStackConfirm.setItemMeta(metaConfirm);

		ItemStack itemStackCancel = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemMeta metaCancel = itemStackConfirm.getItemMeta();
		metaCancel.displayName(Component.text("Cancel", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
		metaCancel.lore(List.of(Component.text("Click to cancel the clearing of inventory.", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)));
		itemStackCancel.setItemMeta(metaCancel);

		SGButton buttonConfirm = new SGButton(itemStackConfirm);
		SGButton buttonCancel = new SGButton(itemStackCancel);

		buttonConfirm.setListener(event -> {
			HumanEntity entity = event.getWhoClicked();
			if (entity instanceof Player player){
				player.getInventory().clear();
				player.getInventory().setHelmet(null);
				player.getInventory().setChestplate(null);
				player.getInventory().setLeggings(null);
				player.getInventory().setBoots(null);
				player.getInventory().setItemInOffHand(null);
			}
			entity.closeInventory(InventoryCloseEvent.Reason.CANT_USE);
			commandMessenger.message(entity, "spawn.message-cleared");
		});
		buttonCancel.setListener(event -> {
			HumanEntity entity = event.getWhoClicked();
			entity.closeInventory(InventoryCloseEvent.Reason.CANT_USE);
			commandMessenger.message(entity, "spawn.message-canceled");
		});

		menu.setButton(0, buttonConfirm);
		menu.setButton(1, buttonConfirm);
		menu.setButton(2, buttonConfirm);
		menu.setButton(3, buttonConfirm);
		menu.setButton(9, buttonConfirm);
		menu.setButton(10, buttonConfirm);
		menu.setButton(11, buttonConfirm);
		menu.setButton(12, buttonConfirm);
		menu.setButton(18, buttonConfirm);
		menu.setButton(19, buttonConfirm);
		menu.setButton(20, buttonConfirm);
		menu.setButton(21, buttonConfirm);

		menu.setButton(5, buttonCancel);
		menu.setButton(6, buttonCancel);
		menu.setButton(7, buttonCancel);
		menu.setButton(8, buttonCancel);
		menu.setButton(14, buttonCancel);
		menu.setButton(15, buttonCancel);
		menu.setButton(16, buttonCancel);
		menu.setButton(17, buttonCancel);
		menu.setButton(23, buttonCancel);
		menu.setButton(24, buttonCancel);
		menu.setButton(25, buttonCancel);
		menu.setButton(26, buttonCancel);

	}
	protected ClearInventoryCommand(GoldenDupe goldenDupe, GDCommandInfo commandInfo) {
		super(goldenDupe, commandInfo);
	}

	@Override
	public void execute(@NotNull CommandSender sender, @NotNull String[] args, boolean hasArgs) { }

	@Override
	public void execute(@NotNull Player sender, @NotNull String[] args, boolean hasArgs) {
		GDPlayer player = goldenDupe.playerDatabase().fromPlayer(sender);
		if (is(args, 0, "-change-auto-confirm")){
			if (player.autoConfirmClearInv()){
				commandMessenger.message(sender, "clear.message-auto-confirm-enable");
			} else {
				commandMessenger.message(sender, "clear.message-auto-confirm-disable");
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
				commandMessenger.message(sender, "clear.message-cleared");
				return;
			}
			sender.openInventory(menu.getInventory());
		}
	}

	@Override
	public List<String> tab(@NotNull CommandSender sender, @NotNull String[] args, boolean hasArgs) { return null; }

	@Override
	public List<String> tab(@NotNull Player sender, @NotNull String[] args, boolean hasArgs) {
		if (!hasArgs){
			return List.of("-change-auto-confirm");
		}
		return Collections.emptyList();
	}
}
