package xyz.goldendupe.command.og;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.Placeholder;
import io.papermc.paper.adventure.AdventureComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
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
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.models.chatcolor.Color;
import xyz.goldendupe.utils.MemberType;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Cloud
public class FlightCommand extends GDCloudCommand {
	public FlightCommand(GoldenDupe plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		Command.Builder<Player> builder = commandBuilder("fly",
				Description.of("Allows player to toggle the flight mode.")
				, "flight")
				.permission(MemberType.OG.cloudOf("fly").or(MemberType.ADMINISTRATOR.cloudOf("fly")).or(MemberType.MODERATOR.cloudOf("fly")))
				.senderType(Player.class)
				.handler(context->{
					Player sender = context.sender();
					sender.setAllowFlight(!sender.getAllowFlight());
					sender.setFlying(sender.getAllowFlight());
					commandMessenger.message(sender, "fly." + (sender.isFlying() ? "message-enabled" : "message-disabled"));
				});
		command(builder);
		command(builder
				.permission(MemberType.ADMINISTRATOR.cloudOf("fly.others"))
				.argument(PlayerParser.playerComponent().name("who-to-switch")
						.suggestionProvider(
								new SuggestionProvider<>() {
									@Override
									public @NonNull CompletableFuture<? extends @NonNull Iterable<? extends @NonNull Suggestion>> suggestionsFuture(@NonNull CommandContext<Object> context, @NonNull CommandInput input) {
										return CompletableFuture.supplyAsync(() -> Bukkit.getOnlinePlayers().stream().map(player ->
												TooltipSuggestion.tooltipSuggestion(player.getName(),
														new AdventureComponent(
																player.name().appendSpace().append(Component.text("|", Color.DARK_GRAY))
																		.appendSpace()
																		.append(Component.text("Flight", Color.YELLOW))
																		.append(Component.text(":", Color.GRAY))
																		.appendSpace()
																		.append(player.isFlying() ? Component.text("Enabled", Color.GREEN) : Component.text("Disabled", Color.RED))))).collect(Collectors.toSet()));
									}
								}
						))
				.handler(context->{
					Player sender = context.sender();
					Player other = context.get("who-to-switch");

					other.setAllowFlight(!sender.getAllowFlight());
					other.setFlying(other.getAllowFlight());
					if (!other.equals(sender)) {
						commandMessenger.message(sender, "fly." + (other.isFlying() ? "message-admin-enabled" : "message-admin-disabled"),
								new Placeholder("player", commandMessenger.createPlaceholders(other)));
						commandMessenger.message(other, "fly." + (other.isFlying() ? "message-enabled" : "message-disabled"));
						return;
					}
					commandMessenger.message(other, "fly." + (other.isFlying() ? "message-enabled" : "message-disabled"));
				})
		);
	}
}
