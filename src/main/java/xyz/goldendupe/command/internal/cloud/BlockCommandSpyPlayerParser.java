package xyz.goldendupe.command.internal.cloud;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.bukkit.BukkitCaptionKeys;
import org.incendo.cloud.bukkit.BukkitCommandContextKeys;
import org.incendo.cloud.bukkit.parser.PlayerParser;
import org.incendo.cloud.caption.CaptionVariable;
import org.incendo.cloud.component.CommandComponent;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.exception.parsing.ParserException;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;
import org.incendo.cloud.suggestion.Suggestion;
import xyz.goldendupe.GoldenDupe;

import java.util.stream.Collectors;

public class BlockCommandSpyPlayerParser<C> implements ArgumentParser<C, Player>, BlockingSuggestionProvider<C> {
	public BlockCommandSpyPlayerParser() {
	}


	public static <C> @NonNull ParserDescriptor<C, Player> playerParser() {
		return ParserDescriptor.of(new BlockCommandSpyPlayerParser(), Player.class);
	}

	public static <C> CommandComponent.@NonNull Builder<C, Player> playerComponent(boolean blocked) {
		return CommandComponent.builder().parser(playerParser());
	}

	public @NonNull ArgumentParseResult<Player> parse(final @NonNull CommandContext<C> commandContext, final @NonNull CommandInput commandInput) {
		String input = commandInput.readString();
		Player player = Bukkit.getPlayer(input);
		return player == null ? ArgumentParseResult.failure(new PlayerParser.PlayerParseException(input, commandContext)) : ArgumentParseResult.success(player);
	}

	public @NonNull Iterable<@NonNull Suggestion> suggestions(final @NonNull CommandContext<C> commandContext, final @NonNull CommandInput input) {
		GoldenDupe goldenDupe = GoldenDupe.getPlugin(GoldenDupe.class);
		CommandSender bukkit = commandContext.get(BukkitCommandContextKeys.BUKKIT_COMMAND_SENDER);

		return Bukkit.getOnlinePlayers().stream().filter((player) -> !(bukkit instanceof Player)
				|| ((Player)bukkit).canSee(player)).map(OfflinePlayer::getName).map(Suggestion::simple).collect(Collectors.toList());
	}

	public static final class PlayerParseException extends ParserException {
		private final String input;

		public PlayerParseException(final @NonNull String input, final @NonNull CommandContext<?> context) {
			super(PlayerParser.class, context, BukkitCaptionKeys.ARGUMENT_PARSE_FAILURE_PLAYER, new CaptionVariable[]{CaptionVariable.of("input", input)});
			this.input = input;
		}

		public @NonNull String input() {
			return this.input;
		}
	}
}
