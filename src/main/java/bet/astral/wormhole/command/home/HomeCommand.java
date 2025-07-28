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

@Cloud
public class HomeCommand extends PluginCommand {
    public HomeCommand(CommandRegisterer<CommandSender> registerer, CommandManager<CommandSender> commandManager) {
        super(registerer, commandManager);

        command("home", Translations.D_HOMES_CMD,
                b->
                        b.permission("wormhole.homes")
                                .senderType(Player.class)
                                .optional(PlayerHomeParser.PlayerHomeComponent().name("home"))
                                .handler(context->{
                                    final Player player = context.sender();
                                    final PlayerData playerData = getWormhole().getPlayerCache().getCache(player);
                                    final PlayerHome homeDefault = playerData.getHome("home");
                                    final PlayerHome home = (PlayerHome) context.optional("home").orElse(homeDefault);

                                    if (homeDefault == null) {
                                        messenger.message(player, Translations.M_HOME_NO_HOMES);
                                        return;
                                    }

                                    Integration integration = getWormhole().getMasterIntegration();
                                    PlaceholderList placeholders = new PlaceholderList();
                                    placeholders.add("home", home.getName());

                                    if (!integration.canTeleportToHome(player)) {
                                        messenger.message(player, Translations.M_HOME_CANNOT_TELEPORT, placeholders);
                                        return;
                                    }

                                    home.warp(player);
                                })
                , "base").register();
    }
}
