package xyz.goldendupe.command.admin;

import bet.astral.astronauts.goldendupe.Astronauts;
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
				})
				;
		commandManager.command(spyCommand);
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
													players.removeIf(p -> !player.canSee(p));

													List<Suggestion> suggestions = new ArrayList<>();
													for (Player p : players) {
														suggestions.add(Suggestion.simple(p.getName()));
													}
													return suggestions;
												});
											}
										}
								)
				)
				.handler(
						context->{
							Player player = context.sender();
							Player who = context.get("player-to-block");


						}
				)
		);
	}
}
