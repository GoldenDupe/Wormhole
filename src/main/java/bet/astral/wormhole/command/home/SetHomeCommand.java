package bet.astral.wormhole.command.home;

import bet.astral.cloudplusplus.CommandRegisterer;
import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.wormhole.command.PluginCommand;
import bet.astral.wormhole.integration.Integration;
import bet.astral.wormhole.objects.data.PlayerData;
import bet.astral.wormhole.objects.data.PlayerHome;
import bet.astral.wormhole.objects.data.PlayerWarp;
import bet.astral.wormhole.plugin.Translations;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.key.CloudKey;
import org.incendo.cloud.parser.standard.StringParser;

import java.util.List;

@Cloud
public class SetHomeCommand extends PluginCommand {
    public SetHomeCommand(CommandRegisterer<CommandSender> registerer, CommandManager<CommandSender> commandManager) {
        super(registerer, commandManager);
        command("sethome", Translations.D_SET_HOME_CMD,
                b ->
                        b.permission("wormhole.home.set")
                                .senderType(Player.class)
                                .required(StringParser.stringComponent(StringParser.StringMode.SINGLE).name("home"))
                                .meta(CloudKey.of("home-type", HomeType.class), HomeType.PLAYER_HOME)
                                .handler(this::handle)
                , "setbase").register();
        command("setplayerwarp", Translations.D_SET_PLAYER_WARP_CMD,
                b ->
                        b.permission("wormhole.playerwarp.set")
                                .senderType(Player.class)
                                .required(StringParser.stringComponent(StringParser.StringMode.SINGLE).name("home"))
                                .meta(CloudKey.of("home-type", HomeType.class), HomeType.PLAYER_WARP)
                                .handler(this::handle)
                , "spw").register();
    }

    public <C> void handle(CommandContext<C> context) {
        final Player player = (Player) context.sender();
        final PlayerData playerData = getWormhole().getPlayerCache().getCache(player);
        final String homeName = (String) context.optional("home").orElse("home");
        final HomeType homeType = (HomeType) context.optional("homeType").orElse(HomeType.PLAYER_HOME);

        Integration integration = getWormhole().getMasterIntegration();
        PlaceholderList placeholders = new PlaceholderList();
        placeholders.add("home", homeName);

        switch (homeType) {
            case PLAYER_HOME -> {
                if (!integration.canCreatePlayerHome(player, homeName, player.getLocation())) {
                    return;
                }
            }
            case PLAYER_WARP -> {
                if (!integration.canCreatePlayerWarp(player, homeName, player.getLocation())) {
                    return;
                }
            }
        }


        PlayerHome home = playerData.getHomeOrWarp(homeName);
        if (home != null) {
            placeholders.add("%relocate%", switch (homeType) {
                case PLAYER_HOME -> "/relocatehome " + homeName;
                case PLAYER_WARP -> "/relocateplayerwarp " + homeName;
            });
            messenger.message(player, switch (homeType) {
                case PLAYER_WARP -> Translations.M_SET_HOME_RELOCATE;
                case PLAYER_HOME -> Translations.M_SET_PLAYER_WARP_RELOCATE;
            }, placeholders);
            return;
        }
        int maxHomes = switch (homeType) {
            case PLAYER_HOME -> playerData.getMaxHomes();
            case PLAYER_WARP -> playerData.getMaxWarps();
        };
        List<? extends PlayerHome> homes = switch (homeType) {
            case PLAYER_HOME -> playerData.getHomes();
            case PLAYER_WARP -> playerData.getWarps();
        };

        int homesLeft = maxHomes - homes.size();
        placeholders.add("current_homes", homes.size());
        placeholders.add("max_homes", maxHomes);

        if (homesLeft <= 0 && playerData.getMaxHomes() != -1) {
            messenger.message(player, switch (homeType) {
                case PLAYER_HOME -> Translations.M_SET_HOME_CANNOT_SET_MAX_HOMES;
                case PLAYER_WARP -> Translations.M_SET_PLAYER_WARP_CANNOT_SET_MAX_WARPS;
            }, placeholders);
            return;
        }
        placeholders.add("homes_left", homesLeft - 1);


        switch (homeType) {
            case PLAYER_HOME -> {
                home = new PlayerHome(player, homeName);
                playerData.getHomesAndWarps().add(home);
                getWormhole().getPlayerCache().save(playerData);
                messenger.message(player, Translations.M_SET_HOME_SUCCESS, placeholders);
            }
            case PLAYER_WARP -> {
                PlayerWarp playerWarp = new PlayerWarp(player.getUniqueId(), homeName, player.getLocation());
                playerData.getHomesAndWarps().add(playerWarp);
                getWormhole().getPlayerCache().save(playerData);
                messenger.message(player, Translations.M_SET_PLAYER_WARP_SUCCESS, placeholders);
            }
        }
    }
}
