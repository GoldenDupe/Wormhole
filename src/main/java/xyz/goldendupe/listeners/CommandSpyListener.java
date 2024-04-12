package xyz.goldendupe.listeners;

import bet.astral.messenger.placeholder.Placeholder;
import io.papermc.paper.event.player.PlayerSignCommandPreprocessEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.incendo.cloud.permission.PredicatePermission;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.models.astronauts.CSPYUser;

import java.util.Set;

public class CommandSpyListener implements GDListener {
	private final GoldenDupe goldenDupe;

	public CommandSpyListener(GoldenDupe goldenDupe) {
		this.goldenDupe = goldenDupe;
	}

	@EventHandler
	public void onCommandPreprocess(PlayerCommandPreprocessEvent event){
		Player whoExecuted = event.getPlayer();
		String command = event.getMessage();
		goldenDupe.commandMessenger()
				.broadcast(
						PredicatePermission.of(sender -> {
							if (sender instanceof Player player) {
								CSPYUser user = goldenDupe.commandSpyDatabase().fromPlayer(player);
								if (user.isCommandSpyToggled()) {
									if (user.blockedUsers().contains(whoExecuted.getUniqueId())) {
										return false;
									}
									Set<String> commands = user.blockedCommands();
									for (String cmd : commands) {
										if (command.startsWith(cmd)) {
											return false;
										}
									}
									return true;
								}
							}
							return false;
						}),
						"commandspy.message-executed", new Placeholder("player", whoExecuted.getName()), new Placeholder("command", command),
						new Placeholder("command_copy", Component.text(command).clickEvent(ClickEvent.copyToClipboard(command))),
						new Placeholder("command_suggest", Component.text(command).clickEvent(ClickEvent.suggestCommand(command)))
				);
	}
	@EventHandler
	public void onCommandSignPreprocess(PlayerSignCommandPreprocessEvent event){
		Player whoExecuted = event.getPlayer();
		String command = event.getMessage();
		goldenDupe.commandMessenger()
				.broadcast(
						PredicatePermission.of(sender -> {
							if (sender instanceof Player player) {
								CSPYUser user = goldenDupe.commandSpyDatabase().fromPlayer(player);
								if (user.isCommandSpyToggled()) {
									if (user.blockedUsers().contains(whoExecuted.getUniqueId())) {
										return false;
									}
									Set<String> commands = user.blockedCommands();
									for (String cmd : commands) {
										if (command.startsWith(cmd)) {
											return true;
										}
									}
								}
							}
							return false;
						}),
						"commandspy.message-executed-sign", new Placeholder("player", whoExecuted.getName()), new Placeholder("command", command),
						new Placeholder("command_copy", Component.text(command).clickEvent(ClickEvent.copyToClipboard(command))),
						new Placeholder("command_suggest", Component.text(command).clickEvent(ClickEvent.suggestCommand(command)))
				);
	}

	@Override
	public GoldenDupe goldenDupe() {
		return goldenDupe;
	}
}
