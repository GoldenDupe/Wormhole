package xyz.goldendupe.command.staff;


import bet.astral.cloudplusplus.annotations.Cloud;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.incendo.cloud.Command;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.GoldenMessenger;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class VanishCommand extends GDCloudCommand {
	public static final NamespacedKey KEY_VANISHED = new NamespacedKey("goldendupe", "vanished");

	public VanishCommand(GoldenDupe plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		Command.Builder<Player> builder = commandBuilder("vanish",
				Description.of("Allows player to vanish from players who don't have permissions"),
				"v", "sv")
				.permission(MemberType.MODERATOR.cloudOf("vanish").or(MemberType.ADMINISTRATOR.cloudOf("vanish")))
				.senderType(Player.class)
				.handler(context -> {
					Player sender = context.sender();
					GDPlayer gdPlayer = plugin.playerDatabase().fromPlayer(sender);
					gdPlayer.setVanished(!gdPlayer.vanished());

					if (gdPlayer.vanished()) {
						sender.getPersistentDataContainer().set(KEY_VANISHED, PersistentDataType.BOOLEAN, true);
						Bukkit.getOnlinePlayers().stream().filter(player -> !player.equals(sender))
								.filter(player->player.hasPermission(MemberType.MODERATOR.permissionOf("vanish.see")))
								.forEach(player -> {
									player.hidePlayer(goldenDupe, sender);
								});
						commandMessenger.message(sender,
								"vanish.message-vanished");
						commandMessenger.broadcast(GoldenMessenger.MessageChannel.STAFF,
								"vanish.broadcast-staff-vanished", commandMessenger.createPlaceholders(sender));
					} else {
						sender.getPersistentDataContainer().remove(KEY_VANISHED);
						Bukkit.getOnlinePlayers().stream().filter(player -> !player.equals(sender))
								.forEach(player -> {
									player.showPlayer(goldenDupe, sender);
								});
						commandMessenger.message(sender,
								"vanish.message-unvanished");
						commandMessenger.broadcast(GoldenMessenger.MessageChannel.STAFF,
								"vanish.broadcast-staff-unvanished", commandMessenger.createPlaceholders(sender));
					}
				});
		command(builder);
	}
}