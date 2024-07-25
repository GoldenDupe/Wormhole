package xyz.goldendupe.command.donator.og;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.placeholder.Placeholder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.Command;
import org.incendo.cloud.bukkit.parser.PlayerParser;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.minecraft.extras.suggestion.ComponentTooltipSuggestion;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.models.chatcolor.Color;
import xyz.goldendupe.utils.MemberType;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Cloud
public class FlightCommand extends GDCloudCommand {
	public FlightCommand(GoldenDupeCommandRegister register, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(register, commandManager);
		Command.Builder<Player> builder = commandBuilderPlayer("fly",
				Description.of("Allows player to toggle the flight mode.")
				, "flight")
				.permission(MemberType.OG.cloudOf("fly").or(MemberType.ADMINISTRATOR.cloudOf("fly")).or(MemberType.MODERATOR.cloudOf("fly")))
				.senderType(Player.class)
				.handler(context->{
					Player sender = context.sender();
					sender.setAllowFlight(!sender.getAllowFlight());
					sender.setFlying(sender.getAllowFlight());
					if (sender.isFlying()){
						messenger.message(sender, Translations.COMMAND_FLY_TRUE);
					} else {
						messenger.message(sender, Translations.COMMAND_FLY_FALSE);
					}
				});
		commandPlayer(builder);
		commandPlayer(builder
				.permission(MemberType.ADMINISTRATOR.cloudOf("fly.others"))
				.argument(PlayerParser.playerComponent().name("who-to-switch")
						.suggestionProvider(
								new SuggestionProvider<>() {
									@Override
									public @NonNull CompletableFuture<? extends @NonNull Iterable<? extends @NonNull Suggestion>> suggestionsFuture(@NonNull CommandContext<Object> context, @NonNull CommandInput input) {
										return CompletableFuture.supplyAsync(() -> Bukkit.getOnlinePlayers().stream().map(player ->
												ComponentTooltipSuggestion.suggestion(player.getName(),
																player.name().appendSpace().append(Component.text("|", Color.DARK_GRAY))
																		.appendSpace()
																		.append(Component.text("Flight allowed", Color.YELLOW))
																		.append(Component.text(":", Color.GRAY))
																		.appendSpace()
																		.append(player.getAllowFlight() ? Component.text("Enabled", Color.GREEN) : Component.text("Disabled", Color.RED))
																		.appendNewline()
																		.appendSpace()
																		.append(Component.text("Flying", Color.YELLOW))
																		.append(Component.text(":", Color.GRAY))
																		.appendSpace()
																		.append(player.isFlying() ? Component.text("Enabled", Color.GREEN) : Component.text("Disabled", Color.RED))))
												.collect(Collectors.toSet()));
									}
								}
						))
				.handler(context->{
					Player sender = context.sender();
					Player other = context.get("who-to-switch");

					other.setAllowFlight(!sender.getAllowFlight());
					other.setFlying(other.getAllowFlight());
					if (!other.equals(sender)) {
						if (other.getAllowFlight()) {
							messenger.message(sender, Translations.COMMAND_FLY_ADMIN_TRUE, Placeholder.of("player", other.name()));
							messenger.message(other, Translations.COMMAND_FLY_TRUE);
						} else {
							messenger.message(sender, Translations.COMMAND_FLY_ADMIN_FALSE, Placeholder.of("player", other.name()));
							messenger.message(other, Translations.COMMAND_FLY_FALSE);
						}
						return;
					}
					if (other.getAllowFlight()) {
						messenger.message(other, Translations.COMMAND_FLY_TRUE);
					} else {
						messenger.message(other, Translations.COMMAND_FLY_FALSE);
					}
				})
		);
	}
}
