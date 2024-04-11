package xyz.goldendupe.command.donator;

import bet.astral.cloudplusplus.ComponentSuggestion;
import bet.astral.cloudplusplus.ComponentTooltipSuggestion;
import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.Placeholder;
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
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.utils.MemberType;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Cloud
public class RenameCommand extends GDCloudCommand {
	public RenameCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
		super(goldenDupe, commandManager);
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
											if (context.sender() instanceof Player player) {
												ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
												if (meta.hasDisplayName()) {
													return List.of(
															new ComponentTooltipSuggestion(ComponentSuggestion.Mode.LEGACY,
																	meta.displayName(),
																	meta.displayName()));
												}
												return
														List.of(
																new ComponentTooltipSuggestion(ComponentSuggestion.Mode.LEGACY,
																		Component.translatable(player.getInventory().getItemInMainHand(), Component.translatable(player.getInventory().getItemInMainHand())),
																		Component.translatable(player.getInventory().getItemInMainHand(), Component.translatable(player.getInventory().getItemInMainHand()))
														));
											}
											return Collections.emptyList();
										});
									}
								})
						)
						.handler(context -> {
							Player player = context.sender();

							String argument = context.get("name");
							if (player.getInventory().getItemInMainHand().isEmpty()) {
								commandMessenger.message(player, "rename.message-cannot-rename-air");
								return;
							}
							Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(argument);
							component = component.hoverEvent(HoverEvent.showText(component));

							if (PlainTextComponentSerializer.plainText().serialize(component).length() > 20 && !player.hasPermission(MemberType.ADMINISTRATOR.permissionOf("rename"))) {
								commandMessenger.message(player, "rename.message-too-long");
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

							commandMessenger.message(player, "rename.message-renamed", new Placeholder("name", component), new Placeholder("old_name", oldName));
						}));
	}
}
