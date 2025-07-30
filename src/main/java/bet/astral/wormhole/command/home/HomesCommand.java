package bet.astral.wormhole.command.home;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.component.ComponentType;
import bet.astral.messenger.v2.info.MessageInfoBuilder;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.messenger.v2.translation.TranslationKey;
import bet.astral.wormhole.command.PluginCommand;
import bet.astral.wormhole.command.PluginCommandManager;
import bet.astral.wormhole.command.arguments.PlayerHomeParser;
import bet.astral.wormhole.managers.PlayerCacheManager;
import bet.astral.wormhole.objects.data.PlayerData;
import bet.astral.wormhole.objects.data.PlayerWarp;
import bet.astral.wormhole.objects.data.Warp;
import bet.astral.wormhole.plugin.translation.Translations;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.key.CloudKey;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Cloud
public class HomesCommand extends PluginCommand {
    public static Component warpsToList(@NotNull Messenger messenger, Player receiver, List<? extends Warp> warps, TranslationKey commaKey, TranslationKey homeKey, TranslationKey warpKey) {
        // Parse comma or other formatting at end of each value
        Component comma = messenger.parseComponent(new MessageInfoBuilder(commaKey).withReceiver(receiver).build(), ComponentType.CHAT);
        if (comma == null){
            comma = Component.text("");
        }

        // Format the homes
        boolean empty = true;
        Component builtHomesComponent = Component.empty();
        for (Warp home : warps) {
            PlaceholderList homePlaceholders = (PlaceholderList) home.toPlaceholders();
            Component info;
            // Homes and warps
            if (home instanceof PlayerWarp) {
                info = messenger.parseComponent(new MessageInfoBuilder(warpKey).withReceiver(receiver).withPlaceholders(homePlaceholders).build(), ComponentType.CHAT);
            } else {
                info = messenger.parseComponent(new MessageInfoBuilder(homeKey).withReceiver(receiver).withPlaceholders(homePlaceholders).build(), ComponentType.CHAT);
            }

            assert info != null;
            builtHomesComponent = builtHomesComponent.append(info);

            // Add comma to the end
            if (!empty){
                builtHomesComponent = builtHomesComponent.append(comma);
            }
            empty = false;
        }
        return builtHomesComponent;
    }


    public HomesCommand(PluginCommandManager registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
        super(registerer, commandManager);

        command("homes", Translations.D_HOME_CMD,
                b->
                        b.permission("wormhole.home")
                                .senderType(Player.class)
                                .optional(PlayerHomeParser.PlayerHomeComponent().name("home"))
                                .meta(CloudKey.of("home-type", HomeType.class), HomeType.PLAYER_HOME)
                                .handler(this::handle)
                , "bases").register();
        command("playerwarps", Translations.D_HOME_CMD,
                b->
                        b.permission("wormhole.home")
                                .senderType(Player.class)
                                .optional(PlayerHomeParser.PlayerHomeComponent().name("home"))
                                .meta(CloudKey.of("home-type", HomeType.class), HomeType.PLAYER_WARP)
                                .handler(this::handle)
                , "pws").register();
    }

    public <C> void handle(CommandContext<C> context){
        Player player = (Player) context.sender();
        PlayerCacheManager playerCacheManager = getWormhole().getPlayerCache();
        PlayerData playerData = playerCacheManager.getCache(player);

        HomeType type = context.command().commandMeta().optional(CloudKey.of("home-type", HomeType.class)).orElse(HomeType.PLAYER_HOME);
        if (type == HomeType.PLAYER_HOME){
            getWormhole().getHomeGUI().openHomes(player, 0);
            return;
        }

        PlaceholderList placeholders = new PlaceholderList();

        // TODO THIS

        Component warpsInList = warpsToList(messenger, player, playerData.getHomesAndWarps(), Translations.M_HOMES_LIST_COMMA, Translations.M_HOMES_LIST_HOME, Translations.M_HOMES_LIST_WARP);
        placeholders.add("homes", warpsInList);

        messenger.message(player, Translations.M_HOMES, placeholders);
    }
}
