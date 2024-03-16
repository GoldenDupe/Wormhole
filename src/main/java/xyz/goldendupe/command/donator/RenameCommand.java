package xyz.goldendupe.command.donator;

import bet.astral.messenger.placeholder.Placeholder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;
import xyz.goldendupe.GoldenDupe;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.utils.MemberType;

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
										return CompletableFuture.completedFuture(List.of(Suggestion.suggestion(context.sender() instanceof Player player ? LegacyComponentSerializer.legacyAmpersand().serialize(player.getInventory().getItemInMainHand().displayName()) : "")));
									}
								})
						)
						.handler(context->{
							Player player = context.sender();
							String argument = context.get("name");
							if (player.getInventory().getItemInMainHand().isEmpty()){
								commandMessenger.message(player, "rename.message-cannot-rename-air");
								return;
							}
							Component oldName = player.getInventory().getItemInMainHand().displayName();
							Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(argument);
							player.getInventory().getItemInMainHand().editMeta(meta->meta.displayName(component));
							commandMessenger.message(player, "rename.message-renamed", new Placeholder("name", component), new Placeholder("old_name", oldName));
						})
		);
	}
}
