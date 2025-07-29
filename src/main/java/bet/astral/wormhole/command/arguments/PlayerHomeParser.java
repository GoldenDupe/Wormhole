package bet.astral.wormhole.command.arguments;

import bet.astral.wormhole.command.home.HomeType;
import bet.astral.wormhole.managers.PlayerCacheManager;
import bet.astral.wormhole.objects.data.PlayerData;
import bet.astral.wormhole.objects.data.PlayerHome;
import bet.astral.wormhole.objects.data.PlayerWarp;
import bet.astral.wormhole.plugin.Translations;
import bet.astral.wormhole.plugin.WormholePlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apiguardian.api.API;
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
import org.incendo.cloud.minecraft.extras.suggestion.ComponentTooltipSuggestion;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;
import org.incendo.cloud.suggestion.Suggestion;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerHomeParser<C> implements ArgumentParser<C, PlayerHome>, BlockingSuggestionProvider<C> {
    /**
     * Creates a new PlayerHome parser.
     *
     * @param <C> command sender type
     * @return the created parser
     * @since 2.0.0
     */
    @API(status = API.Status.STABLE, since = "2.0.0")
    public static <C> @NonNull ParserDescriptor<C, PlayerHome> playerHomeParser() {
        return ParserDescriptor.of(new PlayerHomeParser<>(), PlayerHome.class);
    }

    /**
     * Returns a {@link CommandComponent.Builder} using {@link #playerHomeParser()} as the parser.
     *
     * @param <C> the command sender type
     * @return the component builder
     * @since 2.0.0
     */
    @API(status = API.Status.STABLE, since = "2.0.0")
    public static <C> CommandComponent.@NonNull Builder<C, PlayerHome> PlayerHomeComponent() {
        return CommandComponent.<C, PlayerHome>builder().parser(playerHomeParser());
    }

    @Override
    public @NonNull ArgumentParseResult<PlayerHome> parse(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput commandInput
    ) {
        final String input = commandInput.readString();

        HomeType homeType = commandContext.command().commandMeta().optional(CloudKey.of("home-type", HomeType.class)).orElse(HomeType.PLAYER_HOME);

        WormholePlugin wormholePlugin = WormholePlugin.getPlugin(WormholePlugin.class);
        PlayerCacheManager playerCacheManager = wormholePlugin.getPlayerCache();
        PlayerData playerData = playerCacheManager.getCache((Player) commandContext.get(BukkitCommandContextKeys.BUKKIT_COMMAND_SENDER));
        PlayerHome PlayerHome = switch (homeType) {
            case PLAYER_HOME -> playerData.getHome(input);
            case PLAYER_WARP -> playerData.getWarp(input);
        };

        if (PlayerHome == null) {
            return ArgumentParseResult.failure(new PlayerHomeParser.PlayerHomeParseException(input, commandContext, homeType));
        }

        return ArgumentParseResult.success(PlayerHome);
    }

    @Override
    public @NonNull Iterable<@NonNull Suggestion> suggestions(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput input
    ) {
        final CommandSender bukkit = commandContext.get(BukkitCommandContextKeys.BUKKIT_COMMAND_SENDER);
        final Player player = (Player) bukkit;
        WormholePlugin wormholePlugin = WormholePlugin.getPlugin(WormholePlugin.class);
        PlayerCacheManager playerCacheManager = wormholePlugin.getPlayerCache();
        PlayerData data = playerCacheManager.getCache(player);
        HomeType homeType = commandContext.command().commandMeta().optional(CloudKey.of("home-type", HomeType.class)).orElse(HomeType.PLAYER_HOME);

        List<? extends PlayerHome> homes = switch (homeType){
            case PLAYER_HOME -> data.getHomes();
            case PLAYER_WARP -> data.getWarps();
        };

        return homes.stream()
                .map(val ->
                        ComponentTooltipSuggestion.suggestion(
                                val.getName(),
                                Component.text(val.getName(), val instanceof PlayerWarp ? NamedTextColor.GREEN : NamedTextColor.YELLOW)))
                .collect(Collectors.toList());
    }


    /**
     * PlayerHome parse exception
     */
    public static final class PlayerHomeParseException extends ParserException {

        private final String input;

        /**
         * Construct a new PlayerHome parse exception
         *
         * @param input   String input
         * @param context Command context
         */
        public PlayerHomeParseException(
                final @NonNull String input,
                final @NonNull CommandContext<?> context,
                final @NotNull HomeType homeType
        ) {
            super(
                    PlayerHomeParser.class,
                    context,
                    switch (homeType) {
                        case PLAYER_HOME -> Translations.C_PLAYER_HOME_PARSE_EXCEPTION;
                        case PLAYER_WARP -> Translations.C_PLAYER_WARP_PARSE_EXCEPTION;
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
}
