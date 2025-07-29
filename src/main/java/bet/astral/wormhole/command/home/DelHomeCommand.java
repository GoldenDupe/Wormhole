package bet.astral.wormhole.command.home;

import bet.astral.cloudplusplus.CommandRegisterer;
import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.wormhole.command.PluginCommand;
import bet.astral.wormhole.command.arguments.PlayerHomeParser;
import bet.astral.wormhole.objects.data.PlayerData;
import bet.astral.wormhole.objects.data.PlayerHome;
import bet.astral.wormhole.plugin.Translations;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.key.CloudKey;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static bet.astral.wormhole.command.home.HomesCommand.warpsToList;

@Cloud
public class DelHomeCommand extends PluginCommand {
    public DelHomeCommand(CommandRegisterer<CommandSender> registerer, CommandManager<CommandSender> commandManager) {
        super(registerer, commandManager);
        command("delhome", Translations.D_DEL_HOME_CMD,
                b ->
                        b.permission("wormhole.delhome")
                                .senderType(Player.class)
                                .optional(PlayerHomeParser.PlayerHomeComponent().name("home"))
                                .meta(CloudKey.of("home-type", HomeType.class), HomeType.PLAYER_HOME)
                                .handler(this::handle)
                , "delbase", "deletehome", "deletebase").register();
        command("delplayerwarp", Translations.D_DEL_WARP_CMD,
                b ->
                        b.permission("wormhole.delhome")
                                .senderType(Player.class)
                                .optional(PlayerHomeParser.PlayerHomeComponent().name("home"))
                                .meta(CloudKey.of("home-type", HomeType.class), HomeType.PLAYER_WARP)
                                .handler(this::handle)
                , "deleteplayerwarp").register();
    }

    public <C> void handle(@NotNull CommandContext<C> context) {
        final Player player = (Player) context.sender();
        final PlayerData playerData = getWormhole().getPlayerCache().getCache(player);
        final PlayerHome homeDefault = playerData.getHomeOrWarp("home");
        PlayerHome home = (PlayerHome) context.optional("home").orElse(null);
        final HomeType homeType = context.command().commandMeta().get(CloudKey.of("home-type", HomeType.class));

        PlaceholderList placeholders = new PlaceholderList();
        List<? extends PlayerHome> homes = switch (homeType) {
            case PLAYER_HOME -> playerData.getHomes();
            case PLAYER_WARP -> playerData.getWarps();
        };

        if (homes.size() > 1 && home == null) {
            Component warpsInList = warpsToList(messenger, player, homes,
                    switch (homeType) {
                        case PLAYER_HOME -> Translations.M_DEL_HOME_LIST_COMMA;
                        case PLAYER_WARP -> Translations.M_DEL_WARP_LIST_COMMA;
                    },
                    switch (homeType) {
                        case PLAYER_HOME -> Translations.M_DEL_HOME_LIST_HOME;
                        case PLAYER_WARP -> Translations.M_DEL_WARP_LIST_HOME;
                    },
                    switch (homeType) {
                        case PLAYER_HOME -> Translations.M_DEL_HOME_LIST_WARP;
                        case PLAYER_WARP -> Translations.M_DEL_WARP_LIST_WARP;
                    });
            placeholders.add("homes", warpsInList);
            messenger.message(player, switch (homeType) {
                case PLAYER_HOME -> Translations.M_DEL_HOME_SELECT_HOME_TO_DELETE;
                case PLAYER_WARP -> Translations.M_DEL_WARP_SELECT_HOME_TO_DELETE;
            }, placeholders);
        } else {
            if (home == null) {
                home = homeDefault;
            }

            placeholders.add("home", home.getName());
            placeholders.addAll(home.toPlaceholders());
            messenger.message(player, switch (homeType) {
                case PLAYER_HOME -> Translations.M_DEL_HOME_SUCCESS;
                case PLAYER_WARP -> Translations.M_DEL_WARP_SUCCESS;
            }, placeholders);
            playerData.getHomesAndWarps().remove(home);
            home.setExists(false);
            getWormhole().getPlayerCache().save(playerData);
        }
    }
}