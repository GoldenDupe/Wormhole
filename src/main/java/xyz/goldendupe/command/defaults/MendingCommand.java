package xyz.goldendupe.command.defaults;

import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.paper.PaperCommandManager;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class MendingCommand extends GDCloudCommand {
	public MendingCommand(GoldenDupeBootstrap bootstrap, PaperCommandManager<CommandSender> commandManager) {
		super(bootstrap, commandManager);

		Command.Builder<Player> builder =
				commandManager.commandBuilder("mending",
						"parsing")
						.permission(MemberType.DEFAULT.cloudOf("mending"))
						.senderType(Player.class)
						.handler(context->{
							Player sender = context.sender();
							if (sender.getInventory().getItemInMainHand().isEmpty()){
								commandMessenger.message(sender, Translations.COMMAND_MENDING_AIR);
								return;
							}
							if (!Enchantment.MENDING.canEnchantItem(sender.getInventory().getItemInMainHand())){
								commandMessenger.message(sender, Translations.COMMAND_MENDING_CANNOT_ENCHANT);
								return;
							}
							sender.getInventory().getItemInMainHand().addEnchantment(Enchantment.MENDING, 1);
							commandMessenger.message(sender, Translations.COMMAND_MENDING_SUCCESS);
						})
				;
		commandManager.command(builder);
	}
}
