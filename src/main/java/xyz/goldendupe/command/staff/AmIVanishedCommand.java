package xyz.goldendupe.command.staff;

import bet.astral.cloudplusplus.annotations.Cloud;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class AmIVanishedCommand extends GDCloudCommand {
	public AmIVanishedCommand(GoldenDupe plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		commandPlayer(
				commandBuilder("amivanished",
						Description.of("Allows player to see if they are vanished."))
						.permission(MemberType.MODERATOR.cloudOf("am-i-vanished"))
						.senderType(Player.class)
						.handler(context->{
							Player sender = context.sender();
							if (goldenDupe.playerDatabase().fromPlayer(sender).vanished()){
								commandMessenger.message(sender, "amivanished.message-vanished");
							} else {
								commandMessenger.message(sender, "amivanished.message-not-svanished");
							}
						})
		);
	}
}
