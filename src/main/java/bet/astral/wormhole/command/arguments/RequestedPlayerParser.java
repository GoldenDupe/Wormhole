package bet.astral.wormhole.command.arguments;

import bet.astral.wormhole.managers.RequestManager;
import bet.astral.wormhole.objects.Request;
import bet.astral.wormhole.plugin.translation.Translations;
import bet.astral.wormhole.plugin.WormholePlugin;
import org.apiguardian.api.API;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.bukkit.BukkitCommandContextKeys;
import org.incendo.cloud.caption.CaptionVariable;
import org.incendo.cloud.component.CommandComponent;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.exception.parsing.ParserException;
import org.incendo.cloud.key.CloudKey;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;
import org.incendo.cloud.suggestion.Suggestion;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class RequestedPlayerParser<C> implements ArgumentParser<C, Player>, BlockingSuggestionProvider<C> {

    public WormholePlugin getWormholePlugin() {
        return (WormholePlugin) WormholePlugin.getProvidingPlugin(WormholePlugin.class);
    }

    /**
     * Creates a new player parser.
     *
     * @param <C> command sender type
     * @return the created parser
     * @since 2.0.0
     */
    @API(status = API.Status.STABLE, since = "2.0.0")
    public static <C> @NonNull ParserDescriptor<C, Player> playerParser() {
        return ParserDescriptor.of(new RequestedPlayerParser<>(), Player.class);
    }

    /**
     * Returns a {@link CommandComponent.Builder} using {@link #playerParser()} as the parser.
     *
     * @param <C> the command sender type
     * @return the component builder
     * @since 2.0.0
     */
    @API(status = API.Status.STABLE, since = "2.0.0")
    public static <C> CommandComponent.@NonNull Builder<C, Player> playerComponent() {
        return CommandComponent.<C, Player>builder().parser(playerParser());
    }

    @Override
    public @NonNull ArgumentParseResult<Player> parse(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput commandInput
    ) {
        final String input = commandInput.readString();

        Player player = Bukkit.getPlayer(input);

        if (player == null) {
            return ArgumentParseResult.failure(new org.incendo.cloud.bukkit.parser.PlayerParser.PlayerParseException(input, commandContext));
        }

        if (commandContext.sender() instanceof Player self && self.getUniqueId().equals(player.getUniqueId())) {
            return ArgumentParseResult.failure(new RequestPlayerParser.PlayerParseSelfException(input, commandContext));
        }

        final Player bukkit = (Player) commandContext.get(BukkitCommandContextKeys.BUKKIT_COMMAND_SENDER);
        final Request.Type type = commandContext.command().commandMeta().optional(CloudKey.of("teleport-type", Request.Type.class)).orElse(Request.Type.TO_PLAYER);
        WormholePlugin plugin = getWormholePlugin();
        RequestManager requestManager = plugin.getRequestManager();
        Map<UUID, List<Request>> requestMap = requestManager.getSentRequests(type);
        if (requestMap.containsKey(bukkit.getUniqueId())) {
            List<Request> requests = requestMap.get(bukkit.getUniqueId());
            OfflinePlayer firstMatch = requests.stream().map(Request::getRequested).filter(requested -> requested.getUniqueId().equals(player.getUniqueId())).findAny().orElse(null);
            if (firstMatch != null && firstMatch.isOnline()) {
                return ArgumentParseResult.success(player);
            }
        }

        return ArgumentParseResult.failure(new PlayerParseNotRequestedException(input, commandContext));
    }

    @Override
    public @NonNull Iterable<@NonNull Suggestion> suggestions(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput input
    ) {
        final CommandSender bukkit = commandContext.get(BukkitCommandContextKeys.BUKKIT_COMMAND_SENDER);

        final Player bukkitPlayer = (Player) bukkit;
        final Request.Type type = commandContext.command().commandMeta().optional(CloudKey.of("teleport-type", Request.Type.class)).orElse(Request.Type.TO_PLAYER);
        WormholePlugin plugin = getWormholePlugin();
        RequestManager requestManager = plugin.getRequestManager();
        Map<UUID, List<Request>> requestMap = requestManager.getSentRequests(type);
        if (requestMap.containsKey(bukkitPlayer.getUniqueId())) {
            List<Request> requests = requestMap.get(bukkitPlayer.getUniqueId());
            return requests.stream().filter(player->player.getRequested().isOnline()).map(player->player.getRequested().getName()).map(Suggestion::suggestion).collect(Collectors.toList());
        }
        return List.of();
    }

    /**
     * Player parse exception
     */
    public static final class PlayerParseNotRequestedException extends ParserException {

        private final String input;

        /**
         * Construct a new Player parse exception
         *
         * @param input   String input
         * @param context Command context
         */
        public PlayerParseNotRequestedException(
                final @NonNull String input,
                final @NonNull CommandContext<?> context
        ) {
            super(
                    org.incendo.cloud.bukkit.parser.PlayerParser.class,
                    context,
                    Translations.C_PLAYER_ALREADY_REQUESTED_PARSE_EXCEPTION,
                    CaptionVariable.of("input", input)
            );
            this.input = input;
        }

        /**
         * Get the supplied input
         *
         * @return String value
         */
        public @NonNull String input() {
            return this.input;
        }
    }
}
