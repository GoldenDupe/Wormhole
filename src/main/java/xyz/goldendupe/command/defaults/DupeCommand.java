package xyz.goldendupe.command.defaults;

import bet.astral.messenger.placeholder.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.Season;
import xyz.goldendupe.command.internal.legacy.GDCommand;
import xyz.goldendupe.command.internal.legacy.GDCommandInfo;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.utils.ContainerUtils;

import java.util.Collections;
import java.util.List;

@Season(added = 1, unlock = 1, alwaysUnlocked = true) // Always unlocked just helps,
// so we don't need to update this file each time we update the server.
@GDCommandInfo.Command(name = "dupe", aliases = "d",
		senderType = GDCommandInfo.SenderType.PLAYER, memberType = GDCommandInfo.MemberType.DEFAULT)
public class DupeCommand extends GDCommand {
	protected DupeCommand(GoldenDupe goldenDupe, GDCommandInfo commandInfo) {
		super(goldenDupe, commandInfo);
	}

	@Override
	public void execute(@NotNull CommandSender sender, @NotNull String[] args, boolean hasArgs) {
	}

	public boolean anyIllegals(ItemStack itemStack){
		if (ContainerUtils.isCarryableContainer(itemStack)){
			for (ItemStack item : ContainerUtils.getCarryableContainer(itemStack)){
				if (anyIllegals(item)){
					return false;
				}
			}
		} else {
			return !goldenDupe.canDupe(itemStack);
		}
		return false;
	}

	@Override
	public void execute(@NotNull Player sender, @NotNull String[] args, boolean hasArgs) {
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

		// TODO - Combat stuff


		int dupeTimes = 1;
		if (isInt(args, 0)){
			dupeTimes = asInt(args, 0);
			sender.sendMessage(""+dupeTimes);
			if (dupeTimes>8){
				commandMessenger.message(sender, "dupe.message-too-large");
				return;
			} else if (dupeTimes <1){
				commandMessenger.message(sender, "dupe.message-too-small");
				return;
			}
		}
		if (dupeTimes == 1){
			sender.getInventory().addItem(sender.getInventory().getItemInMainHand());
			return;
		}
		int duped = 0;
		for (int i = 0; i < dupeTimes; i++){
			duped++;
			if (!sender.getInventory().addItem(sender.getInventory().getItemInMainHand()).isEmpty()){
				commandMessenger.message(sender, "dupe.message-super-duper", new Placeholder("super-duper", duped));
				return;
			}
		}
		commandMessenger.message(sender, "dupe.message-super-duper", new Placeholder("super-duper", dupeTimes));
	}

	@Override
	public List<String> tab(@NotNull CommandSender sender, @NotNull String[] args, boolean hasArgs) {
		return null;
	}

	@Override
	public List<String> tab(@NotNull Player sender, @NotNull String[] args, boolean hasArgs) {
		if (!hasArgs){
			return List.of("1", "2", "3", "4", "5", "6", "7", "8");
		}
		return Collections.emptyList();
	}
}
