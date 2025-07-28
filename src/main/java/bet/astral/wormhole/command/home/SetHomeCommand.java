package bet.astral.wormhole.command.home;

import bet.astral.cloudplusplus.CommandRegisterer;
import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.wormhole.command.PluginCommand;
import bet.astral.wormhole.integration.Integration;
import bet.astral.wormhole.objects.PlayerData;
import bet.astral.wormhole.objects.PlayerHome;
import bet.astral.wormhole.plugin.Translations;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.parser.standard.StringParser;

@Cloud
public class SetHomeCommand extends PluginCommand {
    public SetHomeCommand(CommandRegisterer<CommandSender> registerer, CommandManager<CommandSender> commandManager) {
        super(registerer, commandManager);
        command("homes", Translations.D_HOMES_CMD,
                b->
                        b.permission("wormhole.sethome")
                                .senderType(Player.class)
                                .optional(StringParser.stringComponent(StringParser.StringMode.SINGLE).name("home"))
                                .handler(context->{
                                    Player player = context.sender();
                                    PlayerData playerData = getWormhole().getPlayerCache().getCache(player);
                                    String homeName = (String) context.optional("home").orElse("home");

                                    Integration integration = getWormhole().getMasterIntegration();
                                    PlaceholderList placeholders = new PlaceholderList();
                                    placeholders.add("home", homeName);

                                    if (!integration.canSetHome(player)) {
                                        messenger.message(player, Translations.M_SET_HOME_CANNOT_SET_CANCELLED, placeholders);
                                        return;
                                    }


                                    PlayerHome home = playerData.getHome(homeName);
                                    if (home != null){
                                        placeholders.add("%relocate%", "/relocate "+homeName);
                                        messenger.message(player, Translations.M_SET_HOME_RELOCATE, placeholders);
                                        return;
                                    }

                                    int homesLeft = playerData.getMaxHomes()-playerData.getHomes().size();
                                    placeholders.add("current_homes", playerData.getHomesAndWarps().size());
                                    placeholders.add("max_homes", playerData.getMaxHomes());

                                    if (homesLeft <= 0 && playerData.getMaxHomes()!=-1) {
                                        messenger.message(player, Translations.M_SET_HOME_CANNOT_SET_MAX_HOMES, placeholders);
                                        return;
                                    }
                                    placeholders.add("homes_left", homesLeft-1);

                                    home = new PlayerHome(player, homeName);
                                    playerData.getHomesAndWarps().add(home);

                                    getWormhole().getPlayerCache().save(playerData);

                                    messenger.message(player, Translations.M_SET_HOME_SUCCESS, placeholders);
                                })
                , "setbase").register();
    }
}
