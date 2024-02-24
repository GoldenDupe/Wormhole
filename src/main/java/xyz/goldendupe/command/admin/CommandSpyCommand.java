package xyz.goldendupe.command.admin;

import bet.astral.astronauts.goldendupe.Astronauts;
import bet.astral.messenger.placeholder.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.Command;
import org.incendo.cloud.bukkit.parser.PlayerParser;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.internal.cloud.Cloud;
import xyz.goldendupe.command.internal.cloud.GDCloudCommand;
import xyz.goldendupe.command.internal.legacy.GDCommandInfo;
import xyz.goldendupe.models.astronauts.CSPYUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Astronauts
@Cloud
public class CommandSpyCommand extends GDCloudCommand {
	public CommandSpyCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
		super(goldenDupe, commandManager);
		Command.Builder<Player> spyCommand = commandManager
				.commandBuilder("commandspy",
						Description.of("Allows administrators to see the commands players execute"),
								"cspy"
				)
				.permission(GDCommandInfo.MemberType.ADMINISTRATOR.cloudOf("commandspy"))
				.senderType(Player.class)
				.handler(context->{
					commandMessenger.message(context.sender(), "commandspy.message-help");
				});
		commandManager.command(spyCommand);

		commandManager.command(spyCommand
				.literal("toggle")
				.handler(context->{
					Player player = context.sender();
					CSPYUser user = goldenDupe.commandSpyDatabase().fromPlayer(player);
					boolean isToggled = !user.isCommandSpyToggled();
					if (isToggled){
						commandMessenger.message(player, "commandspy.message-toggle-true");
					} else {
						commandMessenger.message(player, "commandspy.message-toggle-false");
					}
					user.setCommandSpyToggled(isToggled);
				})
		);
		commandManager.command(spyCommand
				.literal("block-player")
				.argument(
						PlayerParser.playerComponent()
								.name("player-to-block")
								.suggestionProvider(
										new SuggestionProvider<>() {
											@Override
											public @NonNull CompletableFuture<@NonNull Iterable<@NonNull Suggestion>> suggestionsFuture(@NonNull CommandContext<Object> context, @NonNull CommandInput input) {
												return CompletableFuture.supplyAsync(() -> {
													Player player = (Player) context.sender();
													CSPYUser user = goldenDupe.commandSpyDatabase().fromPlayer(player);
													List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
													players.removeIf(p -> user.blockedUsers().contains(p.getUniqueId()));

													return players.stream().map(Player::getName).map(Suggestion::simple).collect(Collectors.toSet());
												});
											}
										}
								)
				)
				.handler(
						context->{
							Player player = context.sender();
							Player who = context.get("player-to-block");

							CSPYUser user = goldenDupe.commandSpyDatabase().fromPlayer(player);
							if (user.blockedUsers().contains(who.getUniqueId())){
								commandMessenger.message(player, "commandspy.message-player-already-blocked");
								return;
							}
							commandMessenger.message(player, "commandspy.message-player-blocked", new Placeholder("player", who.getName()));
							user.blockedUsers().add(who.getUniqueId());
						}));
		commandManager.command(spyCommand
				.literal("unblock-player")
				.argument(
						PlayerParser.playerComponent()
								.name("player-to-unblock")
								.suggestionProvider(
										new SuggestionProvider<>() {
											@Override
											public @NonNull CompletableFuture<@NonNull Iterable<@NonNull Suggestion>> suggestionsFuture(@NonNull CommandContext<Object> context, @NonNull CommandInput input) {
												return CompletableFuture.supplyAsync(() -> {
													Player player = (Player) context.sender();
													CSPYUser user = goldenDupe.commandSpyDatabase().fromPlayer(player);
													List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
													players.removeIf(p -> !user.blockedUsers().contains(p.getUniqueId()));

													return players.stream().map(Player::getName).map(Suggestion::simple).collect(Collectors.toSet());
												});
											}
										}
								)
				)
				.handler(
						context->{
							Player player = context.sender();
							Player who = context.get("player-to-unblock");

							CSPYUser user = goldenDupe.commandSpyDatabase().fromPlayer(player);
							if (!user.blockedUsers().contains(who.getUniqueId())){
								commandMessenger.message(player, "commandspy.message-player-already-unblocked");
								return;
							}
							commandMessenger.message(player, "commandspy.message-player-unblocked", new Placeholder("player", who.getName()));
							user.blockedUsers().add(who.getUniqueId());
						}));



		commandManager.command(spyCommand
				.literal("block-command")
				.argument(
						StringParser.stringComponent(StringParser.StringMode.GREEDY)
								.name("command-to-block")
								.suggestionProvider(
										new SuggestionProvider<>() {
											@Override
											public @NonNull CompletableFuture<@NonNull Iterable<@NonNull Suggestion>> suggestionsFuture(@NonNull CommandContext<Object> context, @NonNull CommandInput input) {
												return CompletableFuture.supplyAsync(() -> {
													Player player = (Player) context.sender();
													CSPYUser user = goldenDupe.commandSpyDatabase().fromPlayer(player);
													List<String> commands = new ArrayList<>();
													for (String command : Bukkit.getServer().getCommandMap().getKnownCommands().keySet()){
														if (user.blockedCommands().contains("/"+command)){
															continue;
														}
														commands.add("/"+command);
													}

													return commands.stream().map(Suggestion::simple).collect(Collectors.toSet());
												});
											}
										}
								)
				)
				.handler(
						context->{
							Player player = context.sender();
							String command = context.get("command-to-block");

							CSPYUser user = goldenDupe.commandSpyDatabase().fromPlayer(player);
							if (user.blockedCommands().contains(command)){
								commandMessenger.message(player, "commandspy.message-command-already-blocked");
								return;
							}
							commandMessenger.message(player, "commandspy.message-command-blocked", new Placeholder("command", command));
							user.blockedCommands().remove(command);
						}));

		commandManager.command(spyCommand
				.literal("unblock-command")
				.argument(
						StringParser.stringComponent(StringParser.StringMode.GREEDY)
								.name("command-to-unblock")
								.suggestionProvider(
										new SuggestionProvider<>() {
											@Override
											public @NonNull CompletableFuture<@NonNull Iterable<@NonNull Suggestion>> suggestionsFuture(@NonNull CommandContext<Object> context, @NonNull CommandInput input) {
												return CompletableFuture.supplyAsync(() -> {
													Player player = (Player) context.sender();
													CSPYUser user = goldenDupe.commandSpyDatabase().fromPlayer(player);
													List<String> commands = new ArrayList<>();
													for (String command : Bukkit.getServer().getCommandMap().getKnownCommands().keySet()){
														if (!user.blockedCommands().contains("/"+command)){
															continue;
														}
														commands.add("/"+command);
													}

													return commands.stream().map(Suggestion::simple).collect(Collectors.toSet());
												});
											}
										}
								)
				)
				.handler(
						context->{
							Player player = context.sender();
							String command = context.get("command-to-unblock");

							CSPYUser user = goldenDupe.commandSpyDatabase().fromPlayer(player);
							if (!user.blockedCommands().contains(command)){
								commandMessenger.message(player, "commandspy.message-command-already-unblocked");
								return;
							}
							commandMessenger.message(player, "commandspy.message-command-unblocked", new Placeholder("command", command));
							user.blockedCommands().remove(command);
						}));

	}
}
