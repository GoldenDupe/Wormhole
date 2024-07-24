package xyz.goldendupe.command.defaults;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class ToggleItemsCommand extends GDCloudCommand {
	public static final int RANDOM_ITEM_TICKS = 20*15; // 20 ticks * 15 seconds
	public ToggleItemsCommand(GoldenDupeCommandRegister register, PaperCommandManager<CommandSender> commandManager) {
		super(register, commandManager);
		commandManager.command(
				commandManager.commandBuilder(
								"toggle",
								Description.of("Allows a player to toggle dropping items to the ground"),
								"toggleitems", "random", "randomitems", "togglerandomitems")
						.permission(MemberType.DEFAULT.cloudOf("toggle-drop"))
						.senderType(Player.class)
						.handler(context->{
							Player sender = context.sender();
							GDPlayer player = goldenDupe().playerDatabase().fromPlayer(sender);
							boolean toggle = player.isToggled();
							player.setToggled(!toggle);

							if (!toggle){
								messenger.message(sender, Translations.COMMAND_TOGGLE_ITEMS_TRUE);
							} else {
								messenger.message(sender, Translations.COMMAND_TOGGLE_ITEMS_FALSE);
							}
						})
		);
	}
}
