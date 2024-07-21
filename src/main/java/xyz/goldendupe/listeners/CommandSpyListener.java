package xyz.goldendupe.listeners;

import bet.astral.messenger.v2.paper.receiver.PlayerReceiver;
import bet.astral.messenger.v2.permission.PredicatePermission;
import bet.astral.messenger.v2.placeholder.Placeholder;
import io.papermc.paper.event.player.PlayerSignCommandPreprocessEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.messenger.Translations;
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
		goldenDupe.messenger()
				.broadcast(
						PredicatePermission.of(sender -> {
							if (sender instanceof PlayerReceiver player) {
								CSPYUser user = goldenDupe.commandSpyDatabase().fromPlayer(player.getPlayer());
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
						Translations.LISTENER_COMMAND_SPY_EXECUTED, Placeholder.of("player", whoExecuted.getName()), Placeholder.of("command", command),
						Placeholder.of("command_copy", Component.text(command).clickEvent(ClickEvent.copyToClipboard(command)).hoverEvent(HoverEvent.showText(Component.text("Click to copy this command to your clipboard.")))),
						Placeholder.of("command_suggest", Component.text(command).clickEvent(ClickEvent.suggestCommand(command)).hoverEvent(HoverEvent.showText(Component.text("Click to suggest this command to your chat box."))))
				);
	}
	@EventHandler
	public void onCommandSignPreprocess(PlayerSignCommandPreprocessEvent event){
		Player whoExecuted = event.getPlayer();
		String command = event.getMessage();
		goldenDupe.messenger()
				.broadcast(
						PredicatePermission.of(sender -> {
							if (sender instanceof PlayerReceiver player) {
								CSPYUser user = goldenDupe.commandSpyDatabase().fromPlayer(player.getPlayer());
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
						Translations.LISTENER_COMMAND_SPY_EXECUTED_SIGN,
						Placeholder.of("player", whoExecuted.getName()), Placeholder.of("command", command),
						Placeholder.of("command_copy", Component.text(command).clickEvent(ClickEvent.copyToClipboard(command)).hoverEvent(HoverEvent.showText(Component.text("Click to copy this command to your clipboard.")))),
						Placeholder.of("command_suggest", Component.text(command).clickEvent(ClickEvent.suggestCommand(command)).hoverEvent(HoverEvent.showText(Component.text("Click to suggest this command to your chat box.")))),
						Placeholder.of("x", (int) event.getSign().getLocation().getX()),
						Placeholder.of("y", (int) event.getSign().getLocation().getY()),
						Placeholder.of("z", (int) event.getSign().getLocation().getZ()),
						Placeholder.of("world", event.getSign().getLocation().getWorld().getName())
				);
	}

	@Override
	public GoldenDupe goldenDupe() {
		return goldenDupe;
	}
}
