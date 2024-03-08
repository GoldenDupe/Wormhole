package xyz.goldendupe.command.admin;

import bet.astral.cloudplusplus.annotations.Cloud;
import io.papermc.paper.adventure.AdventureComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.Command;
import org.incendo.cloud.brigadier.suggestion.TooltipSuggestion;
import org.incendo.cloud.bukkit.parser.PlayerParser;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.EnumParser;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.models.chatcolor.Color;
import xyz.goldendupe.utils.MemberType;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Cloud
public class GamemodeCommand extends GDCloudCommand {
	public GamemodeCommand(GoldenDupe plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		abstractCommand(GameMode.SURVIVAL, "gms");
		abstractCommand(GameMode.CREATIVE, "gmc");
		abstractCommand(GameMode.SPECTATOR, "gmsp");
		abstractCommand(GameMode.ADVENTURE, "gma");
		commandPlayer(
				commandBuilder("gamemode", Description.of("Allows admin to switch their gamemode to other gamemodes."),
						"gm")
						.permission(MemberType.ADMINISTRATOR.cloudOf("gamemode"))
						.senderType(Player.class)
						.required(EnumParser.enumComponent(GameMode.class).name("gamemode"))
						.optional(PlayerParser.playerComponent().name("who-to-switch")
								.suggestionProvider(
										new SuggestionProvider<>() {
											@Override
											public @NonNull CompletableFuture<? extends @NonNull Iterable<? extends @NonNull Suggestion>> suggestionsFuture(@NonNull CommandContext<Object> context, @NonNull CommandInput input) {
												return CompletableFuture.supplyAsync(() -> Bukkit.getOnlinePlayers().stream().map(player ->
														TooltipSuggestion.tooltipSuggestion(player.getName(),
																new AdventureComponent(
																		player.name().appendSpace().append(Component.text("|", Color.DARK_GRAY))
																				.appendSpace()
																				.append(Component.text("Current Gamemode", Color.YELLOW))
																				.append(Component.text(":", Color.GRAY))
																				.appendSpace()
																				.append(Objects.requireNonNull(gamemodeSwitch(player.getGameMode())))))).collect(Collectors.toSet()));
											}
										}
								))
						.handler(context -> {
							GameMode gameMode = context.get("gamemode");
							Player sender = context.sender();
							Player other = (Player) context.optional("who-to-switch").orElse(sender);

							goldenDupe.getServer().getScheduler().runTask(goldenDupe, () -> {

								if (other.getGameMode() == gameMode) {
									if (other.equals(sender)) {
										commandMessenger.message(sender,
												gameMode.name().toLowerCase()
														+ ".message-already-same");
									} else {
										commandMessenger.message(sender,
												gameMode.name().toLowerCase()
														+ ".message-admin-already-same", commandMessenger.createPlaceholders(other));
									}
								} else if (!other.equals(sender)) {
									other.setGameMode(gameMode);
									commandMessenger.message(other,
											gameMode.name().toLowerCase()
													+ ".message-enabled");
									commandMessenger.message(sender,
											gameMode.name().toLowerCase()
													+ ".message-admin-enabled", commandMessenger.createPlaceholders(other));
								} else {
									other.setGameMode(gameMode);
									commandMessenger.message(sender,
											gameMode.name().toLowerCase()
													+ ".message-enabled");
								}
							});
						})

		);
	}

	private void abstractCommand(GameMode gameMode, String... alias) {
		Command.Builder<Player> builder = commandBuilder(gameMode.name().toLowerCase(),
						Description.of("Allows admins to switch their gamemode to "+ gameMode.name().toLowerCase()),
						alias)
						.permission(MemberType.ADMINISTRATOR.cloudOf(gameMode.name().toLowerCase()))
						.senderType(Player.class)
						.optional(PlayerParser.playerComponent().name("who-to-switch")
								.suggestionProvider(
										new SuggestionProvider<>() {
											@Override
											public @NonNull CompletableFuture<? extends @NonNull Iterable<? extends @NonNull Suggestion>> suggestionsFuture(@NonNull CommandContext<Object> context, @NonNull CommandInput input) {
												return CompletableFuture.supplyAsync(() -> Bukkit.getOnlinePlayers().stream().map(player ->
														TooltipSuggestion.tooltipSuggestion(player.getName(),
																new AdventureComponent(
																		player.name().appendSpace().append(Component.text("|", Color.DARK_GRAY))
																				.appendSpace()
																				.append(Component.text("Current Gamemode", Color.YELLOW))
																				.append(Component.text(":", Color.GRAY))
																				.appendSpace()
																				.append(Objects.requireNonNull(gamemodeSwitch(player.getGameMode())))))).collect(Collectors.toSet()));

											}
										}
								)
						)
						.handler(context->{
							Player sender = context.sender();
							Player other = (Player) context.optional("who-to-switch").orElse(sender);

							goldenDupe.getServer().getScheduler().runTask(goldenDupe, () -> {

								if (other.getGameMode() == gameMode) {
									if (other.equals(sender)) {
										commandMessenger.message(sender,
												gameMode.name().toLowerCase()
														+ ".message-already-same");
									} else {
										commandMessenger.message(sender,
												gameMode.name().toLowerCase()
														+ ".message-admin-already-same", commandMessenger.createPlaceholders(other));
									}
								} else if (!other.equals(sender)) {
									other.setGameMode(gameMode);
									commandMessenger.message(other,
											gameMode.name().toLowerCase()
													+ ".message-enabled");
									commandMessenger.message(sender,
											gameMode.name().toLowerCase()
													+ ".message-admin-enabled", commandMessenger.createPlaceholders(other));
								} else {
									other.setGameMode(gameMode);
									commandMessenger.message(sender,
											gameMode.name().toLowerCase()
													+ ".message-enabled");
								}
							});
						});
		commandPlayer(builder);
	}

	private Component gamemodeSwitch(GameMode gameMode){
		switch (gameMode){
			case CREATIVE -> {
				return Component.text("Creative", Color.YELLOW);
			}
			case SURVIVAL -> {
				return Component.text("Survival", Color.GREEN);
			}
			case ADVENTURE -> {
				return Component.text("Adventure", Color.BLUE);
			}
			case SPECTATOR -> {
				return Component.text("Spectator", Color.EMERALD);
			}
		}
		return null;
	}
}
