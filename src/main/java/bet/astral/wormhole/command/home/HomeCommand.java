package bet.astral.wormhole.command.home;

import bet.astral.cloudplusplus.CommandRegisterer;
import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.wormhole.command.PluginCommand;
import bet.astral.wormhole.command.arguments.PlayerHomeParser;
import bet.astral.wormhole.integration.Integration;
import bet.astral.wormhole.objects.data.PlayerData;
import bet.astral.wormhole.objects.data.PlayerHome;
import bet.astral.wormhole.plugin.Translations;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.key.CloudKey;
import org.jetbrains.annotations.NotNull;

@Cloud
public class HomeCommand extends PluginCommand {
    public HomeCommand(CommandRegisterer<CommandSender> registerer, CommandManager<CommandSender> commandManager) {
        super(registerer, commandManager);

        command("home", Translations.D_HOMES_CMD,
                b->
                        b.permission("wormhole.homes")
                                .senderType(Player.class)
                                .optional(PlayerHomeParser.PlayerHomeComponent().name("home"))
                                .meta(CloudKey.of("home-type", HomeType.class), HomeType.PLAYER_HOME)
                                .handler(this::handle)
                , "base").register();
        command("playerwarp", Translations.D_HOMES_CMD,
                b->
                        b.permission("wormhole.homes")
                                .senderType(Player.class)
                                .optional(PlayerHomeParser.PlayerHomeComponent().name("home"))
                                .meta(CloudKey.of("home-type", HomeType.class), HomeType.PLAYER_WARP)
                                .handler(this::handle)
                , "pw").register();
    }

    public <C> void handle(@NotNull CommandContext<C> context) {
        final Player player = (Player) context.sender();
        final PlayerData playerData = getWormhole().getPlayerCache().getCache(player);
        final PlayerHome homeDefault = playerData.getHomeOrWarp("home");
        final PlayerHome home = (PlayerHome) context.optional("home").orElse(homeDefault);
        final HomeType homeType = context.command().commandMeta().optional(CloudKey.of("home-type", HomeType.class)).orElse(HomeType.PLAYER_HOME);

        if (homeDefault == null) {

            if (homeType == HomeType.PLAYER_HOME){
                getWormhole().getHomeGUI().openHomes(player, 0);
                return;
            }
            messenger.message(player, switch (homeType) {
                case PLAYER_WARP -> Translations.M_PLAYER_WARP_NO_WARPS;
                default -> throw new IllegalStateException("Unexpected value: " + homeType);
            });
            return;
        }

        Integration integration = getWormhole().getMasterIntegration();
        PlaceholderList placeholders = new PlaceholderList();
        placeholders.add("home", home.getName());

        if (!integration.canTeleportToHomeOrWarp(player, home)) {
            messenger.message(player, switch (homeType){
                case PLAYER_HOME ->  Translations.M_HOME_CANNOT_TELEPORT;
                case PLAYER_WARP -> Translations.M_PLAYER_WARP_CANNOT_TELEPORT;
            }, placeholders);
            return;
        }

        home.warp(player);
    }
}
