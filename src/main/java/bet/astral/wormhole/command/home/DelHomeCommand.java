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

import static bet.astral.wormhole.command.home.HomesCommand.warpsToList;

@Cloud
public class DelHomeCommand extends PluginCommand {
    public DelHomeCommand(CommandRegisterer<CommandSender> registerer, CommandManager<CommandSender> commandManager) {
        super(registerer, commandManager);
        command("delhome", Translations.D_DEL_HOME_CMD,
                b->
                        b.permission("wormhole.delhome")
                                .senderType(Player.class)
                                .optional(PlayerHomeParser.PlayerHomeComponent().name("home"))
                                .handler(context->{
                                    final Player player = context.sender();
                                    final PlayerData playerData = getWormhole().getPlayerCache().getCache(player);
                                    final PlayerHome homeDefault = playerData.getHome("home");
                                    PlayerHome home = (PlayerHome) context.optional("home").orElse(null);

                                    PlaceholderList placeholders = new PlaceholderList();
                                    if (playerData.getHomesAndWarps().size()>1 && home == null){
                                        Component warpsInList = warpsToList(messenger, player, playerData.getHomesAndWarps(), Translations.M_DEL_HOME_LIST_COMMA, Translations.M_DEL_HOME_LIST_HOME, Translations.M_DEL_HOME_LIST_WARP);
                                        placeholders.add("homes", warpsInList);
                                        messenger.message(player, Translations.M_DEL_HOME_SELECT_HOME_TO_DELETE, placeholders);
                                    } else {
                                        if (home == null) {
                                            home = homeDefault;
                                        }

                                        placeholders.add("home", home.getName());
                                        placeholders.addAll(home.toPlaceholders());
                                        messenger.message(player, Translations.M_DEL_HOME_SUCCESS, placeholders);
                                        playerData.getHomesAndWarps().remove(home);
                                        getWormhole().getPlayerCache().save(playerData);
                                    }
                                })
                , "delbase").register();
    }
}