package xyz.goldendupe.command.admin;

import bet.astral.messenger.v2.placeholder.Placeholder;
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
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.models.astronauts.CSPYUser;
import xyz.goldendupe.utils.MemberType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Cloud
public class CommandSpyCommand extends GDCloudCommand {
	public CommandSpyCommand(GoldenDupeCommandRegister register, PaperCommandManager<CommandSender> commandManager) {
		super(register, commandManager);
		Command.Builder<Player> spyCommand = commandManager
				.commandBuilder("commandspy",
						Description.of("Allows administrators to see the commands players execute"),
								"cspy"
				)
				.permission(MemberType.ADMINISTRATOR.cloudOf("commandspy"))
				.senderType(Player.class)
				.handler(context->{
					messenger.message(context.sender(), Translations.COMMAND_SPY_HELP);
				});
		commandManager.command(spyCommand);

		commandManager.command(spyCommand
				.literal("toggle")
				.handler(context->{
					Player player = context.sender();
					CSPYUser user = goldenDupe().commandSpyDatabase().fromPlayer(player);
					boolean isToggled = !user.isCommandSpyToggled();
					if (isToggled){
						messenger.message(player, Translations.COMMAND_SPY_TOGGLE_TRUE);
					} else {
						messenger.message(player, Translations.COMMAND_SPY_TOGGLE_FALSE);
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
													CSPYUser user = goldenDupe().commandSpyDatabase().fromPlayer(player);
													List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
													players.removeIf(p -> user.blockedUsers().contains(p.getUniqueId()));

													return players.stream().map(Player::getName).map(Suggestion::suggestion).collect(Collectors.toSet());
												});
											}
										}
								)
				)
				.handler(
						context->{
							Player player = context.sender();
							Player who = context.get("player-to-block");

							CSPYUser user = goldenDupe().commandSpyDatabase().fromPlayer(player);
							if (user.blockedUsers().contains(who.getUniqueId())){
								messenger.message(player, Translations.COMMAND_SPY_PLAYER_ALREADY_BLOCKED, Placeholder.of("player", who.name()));
								return;
							}
							messenger.message(player, Translations.COMMAND_SPY_PLAYER_BLOCKED, Placeholder.of("player", who.name()));
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
													CSPYUser user = goldenDupe().commandSpyDatabase().fromPlayer(player);
													List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
													players.removeIf(p -> !user.blockedUsers().contains(p.getUniqueId()));

													return players.stream().map(Player::getName).map(Suggestion::suggestion).collect(Collectors.toSet());
												});
											}
										}
								)
				)
				.handler(
						context->{
							Player player = context.sender();
							Player who = context.get("player-to-unblock");

							CSPYUser user = goldenDupe().commandSpyDatabase().fromPlayer(player);
							if (!user.blockedUsers().contains(who.getUniqueId())){
								messenger.message(player, Translations.COMMAND_SPY_PLAYER_ALREADY_UNBLOCKED, Placeholder.of("player", who.name()));
								return;
							}
							messenger.message(player, Translations.COMMAND_SPY_PLAYER_UNBLOCKED, Placeholder.of("player", who.name()));
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
													CSPYUser user = goldenDupe().commandSpyDatabase().fromPlayer(player);
													List<String> commands = new ArrayList<>();
													for (String command : Bukkit.getServer().getCommandMap().getKnownCommands().keySet()){
														if (user.blockedCommands().contains("/"+command)){
															continue;
														}
														commands.add("/"+command);
													}

													return commands.stream().map(Suggestion::suggestion).collect(Collectors.toSet());
												});
											}
										}
								)
				)
				.handler(
						context->{
							Player player = context.sender();
							String command = context.get("command-to-block");

							CSPYUser user = goldenDupe().commandSpyDatabase().fromPlayer(player);
							if (user.blockedCommands().contains(command)){
								messenger.message(player, Translations.COMMAND_SPY_COMMAND_ALREADY_BLOCKED, Placeholder.of("command", command));
								return;
							}
							messenger.message(player, Translations.COMMAND_SPY_COMMAND_BLOCKED, Placeholder.of("command", command));
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
													CSPYUser user = goldenDupe().commandSpyDatabase().fromPlayer(player);
													List<String> commands = new ArrayList<>();
													for (String command : Bukkit.getServer().getCommandMap().getKnownCommands().keySet()){
														if (!user.blockedCommands().contains("/"+command)){
															continue;
														}
														commands.add("/"+command);
													}

													return commands.stream().map(Suggestion::suggestion).collect(Collectors.toSet());
												});
											}
										}
								)
				)
				.handler(
						context->{
							Player player = context.sender();
							String command = context.get("command-to-unblock");

							CSPYUser user = goldenDupe().commandSpyDatabase().fromPlayer(player);
							if (!user.blockedCommands().contains(command)){
								messenger.message(player, Translations.COMMAND_SPY_COMMAND_ALREADY_UNBLOCKED, Placeholder.of("command", command));
								return;
							}
							messenger.message(player, Translations.COMMAND_SPY_COMMAND_UNBLOCKED, Placeholder.of("command", command));
							user.blockedCommands().remove(command);
						}));

	}
}
