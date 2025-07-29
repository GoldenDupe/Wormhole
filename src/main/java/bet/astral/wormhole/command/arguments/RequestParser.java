package bet.astral.wormhole.command.arguments;

import bet.astral.wormhole.managers.RequestManager;
import bet.astral.wormhole.objects.Request;
import bet.astral.wormhole.plugin.Translations;
import bet.astral.wormhole.plugin.WormholePlugin;
import org.apiguardian.api.API;
import org.bukkit.Bukkit;
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
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class RequestParser<C> implements ArgumentParser<C, Request>, BlockingSuggestionProvider<C> {

    /**
     * Creates a new Request parser.
     *
     * @param <C> command sender type
     * @return the created parser
     * @since 2.0.0
     */
    @API(status = API.Status.STABLE, since = "2.0.0")
    public static <C> @NonNull ParserDescriptor<C, Request> requestParser() {
        return ParserDescriptor.of(new RequestParser<>(), Request.class);
    }

    /**
     * Returns a {@link CommandComponent.Builder} using {@link #requestParser()} as the parser.
     *
     * @param <C> the command sender type
     * @return the component builder
     * @since 2.0.0
     */
    @API(status = API.Status.STABLE, since = "2.0.0")
    public static <C> CommandComponent.@NonNull Builder<C, Request> requestComponent() {
        return CommandComponent.<C, Request>builder().parser(requestParser());
    }

    @Override
    public @NonNull ArgumentParseResult<Request> parse(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput commandInput
    ) {
        final String input = commandInput.readString();
        final Request.Type requestType = commandContext.command().commandMeta().optional(CloudKey.of("teleport-type", Request.Type.class)).orElse(Request.Type.TO_PLAYER);
        final Type type = commandContext.command().commandMeta().optional(CloudKey.of("request-type", Type.class)).orElse(Type.RECEIVED);
        WormholePlugin wormholePlugin = WormholePlugin.getPlugin(WormholePlugin.class);

        Player player = Bukkit.getPlayer(input);

        if (player == null) {
            return ArgumentParseResult.failure(new org.incendo.cloud.bukkit.parser.PlayerParser.PlayerParseException(input, commandContext));
        }

        if (commandContext.sender() instanceof Player self && self.getUniqueId().equals(player.getUniqueId())) {
            return ArgumentParseResult.failure(new RequestPlayerParser.PlayerParseSelfException(input, commandContext));
        }

        final Player bukkit = (Player) commandContext.get(BukkitCommandContextKeys.BUKKIT_COMMAND_SENDER);
        RequestManager requestManager = wormholePlugin.getRequestManager();

        List<Request> requests = switch (type) {
            case RECEIVED -> requestManager.getReceivedRequests(requestType).get(bukkit.getUniqueId());
            case SENT -> requestManager.getSentRequests(requestType).get(bukkit.getUniqueId());
        };
        Request request = switch (type) {
            case RECEIVED ->
                    requests.stream().filter(r -> r.getPlayer().getUniqueId().equals(player.getUniqueId())).findFirst().orElse(null);
            case SENT ->
                    requests.stream().filter(r -> r.getRequested().getUniqueId().equals(player.getUniqueId())).findFirst().orElse(null);
        };

        if (request == null) {
            return ArgumentParseResult.failure(new RequestParseException(input, commandContext, type));
        }
        return ArgumentParseResult.success(request);
    }

    @Override
    public @NonNull Iterable<@NonNull Suggestion> suggestions(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput input
    ) {
        final Request.Type requestType = commandContext.command().commandMeta().optional(CloudKey.of("teleport-type", Request.Type.class)).orElse(Request.Type.TO_PLAYER);
        final Type type = commandContext.command().commandMeta().optional(CloudKey.of("request-type", Type.class)).orElse(Type.RECEIVED);
        WormholePlugin wormholePlugin = WormholePlugin.getPlugin(WormholePlugin.class);

        final Player bukkit = (Player) commandContext.get(BukkitCommandContextKeys.BUKKIT_COMMAND_SENDER);
        RequestManager requestManager = wormholePlugin.getRequestManager();

        List<Request> requests = switch (type) {
            case RECEIVED -> requestManager.getReceivedRequests(requestType).get(bukkit.getUniqueId());
            case SENT -> requestManager.getSentRequests(requestType).get(bukkit.getUniqueId());
        };

        return requests.stream().map(r -> switch (type) {
            case RECEIVED -> r.getPlayer().getName();
            case SENT -> r.getRequested().getName();
        }).filter(Objects::nonNull).map(Suggestion::suggestion).toList();
    }

    /**
     * Request parse exception
     */
    public static final class RequestParseException extends ParserException {

        private final String input;

        /**
         * Construct a new Request parse exception
         *
         * @param input   String input
         * @param context Command context
         */
        public RequestParseException(
                final @NonNull String input,
                final @NonNull CommandContext<?> context,
                final @NotNull Type type
        ) {
            super(
                    RequestParser.class,
                    context,
                    switch (type) {
                        case RECEIVED -> Translations.C_PLAYER_HAS_NOT_SENT_REQUEST_EXCEPTION;
                        case SENT -> Translations.C_PLAYER_HAS_NOT_RECEIVED_REQUEST_EXCEPTION;
                    },
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

    public enum Type {
        RECEIVED,
        SENT
    }
}
