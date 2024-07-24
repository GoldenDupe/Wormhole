package xyz.goldendupe.command.donator;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.placeholder.Placeholder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.minecraft.extras.suggestion.ComponentTooltipSuggestion;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.utils.MemberType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Cloud
public class RenameCommand extends GDCloudCommand {
	public RenameCommand(GoldenDupeCommandRegister register, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(register, commandManager);
		commandManager.command(
				commandManager.commandBuilder("rename", Description.of("Allows player to rename their item to specified string."))
						.permission(MemberType.DONATOR.cloudOf("rename"))
						.senderType(Player.class)
						.argument(StringParser.stringComponent(StringParser.StringMode.GREEDY)
								.name("name")
								.suggestionProvider(new SuggestionProvider<>() {
									@Override
									public @NonNull CompletableFuture<@NonNull Iterable<@NonNull Suggestion>> suggestionsFuture(@NonNull CommandContext<Object> context, @NonNull CommandInput input) {
										return CompletableFuture.supplyAsync(() -> {
											List<Suggestion> suggestions = new ArrayList<>();
											if (context.sender() instanceof Player player) {
												ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
												if (meta.hasDisplayName()) {
													suggestions.add(
															ComponentTooltipSuggestion.suggestion(LegacyComponentSerializer.legacyAmpersand().serialize(meta.displayName()),
																	meta.displayName()));
												}
											}
											return suggestions;
										});
									}
								})
						)
						.handler(context -> {
							Player player = context.sender();

							String argument = context.get("name");
							if (player.getInventory().getItemInMainHand().isEmpty()) {
								messenger.message(player, Translations.COMMAND_RENAME_AIR);
								return;
							}
							Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(argument);
							component = component.hoverEvent(HoverEvent.showText(component));

							if (PlainTextComponentSerializer.plainText().serialize(component).length() > 50 && !player.hasPermission(MemberType.ADMINISTRATOR.permissionOf("rename"))) {
								messenger.message(player, Translations.COMMAND_RENAME_TOO_LONG);
								return;
							}

							Component oldName = player.getInventory().getItemInMainHand().getItemMeta().displayName();
							if (oldName == null) {
								oldName = Component.translatable(player.getInventory().getItemInMainHand()).hoverEvent(HoverEvent.showText(Component.translatable(player.getInventory().getItemInMainHand())));
							} else {
								oldName = oldName.hoverEvent(HoverEvent.showText(oldName));
							}

							final Component finalComponent = component;
							player.getInventory().getItemInMainHand().editMeta(meta -> meta.displayName(finalComponent));

							messenger.message(player, Translations.COMMAND_RENAME_SUCCESS, Placeholder.of("name", component), Placeholder.of("old_name", oldName));
						}));
	}
}
