package bet.astral.wormhole.command.teleport;

import bet.astral.cloudplusplus.CommandRegisterer;
import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.wormhole.command.PluginCommand;
import bet.astral.wormhole.command.arguments.RequestPlayerParser;
import bet.astral.wormhole.integration.Integration;
import bet.astral.wormhole.managers.RequestManager;
import bet.astral.wormhole.objects.Request;
import bet.astral.wormhole.plugin.Translations;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.key.CloudKey;

@Cloud
public class TPACommand extends PluginCommand {
    public TPACommand(CommandRegisterer<CommandSender> registerer, CommandManager<CommandSender> commandManager) {
        super(registerer, commandManager);

        command("tpa", Translations.D_TPA_CMD,
                b->
                        b.permission("wormhole.tpa")
                                .senderType(Player.class)
                                .required(RequestPlayerParser.playerComponent().name("player"))
                                .meta(CloudKey.of("teleport-type", Request.Type.class), Request.Type.TO_PLAYER)
                                .handler(context->{
                                    Player player = context.sender();
                                    Player other = context.get("player");

                                    PlaceholderList placeholders = new PlaceholderList();
                                    placeholders.add("player", player.getName());
                                    placeholders.add("other", other.getName());

                                    Integration integration = getWormhole().getMasterIntegration();
                                    if (!integration.canTeleportToPlayer(player)){
                                        messenger.message(player, Translations.M_TPA_CANNOT_TELEPORT, placeholders);
                                        return;
                                    }

                                    RequestManager requestManager = getWormhole().getRequestManager();
                                    requestManager.request(Request.Type.TO_PLAYER, player, other, null);
                                }),
                "tpask").register();
    }
}
