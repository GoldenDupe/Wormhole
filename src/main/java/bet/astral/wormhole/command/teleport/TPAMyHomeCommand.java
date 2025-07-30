package bet.astral.wormhole.command.teleport;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.wormhole.command.PluginCommand;
import bet.astral.wormhole.command.PluginCommandManager;
import bet.astral.wormhole.command.arguments.PlayerHomeParser;
import bet.astral.wormhole.command.arguments.RequestPlayerParser;
import bet.astral.wormhole.integration.Integration;
import bet.astral.wormhole.managers.RequestManager;
import bet.astral.wormhole.objects.Request;
import bet.astral.wormhole.objects.data.PlayerHome;
import bet.astral.wormhole.plugin.translation.Translations;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.key.CloudKey;
import org.incendo.cloud.paper.PaperCommandManager;

@Cloud
public class TPAMyHomeCommand extends PluginCommand {
    public TPAMyHomeCommand(PluginCommandManager registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
        super(registerer, commandManager);

        command("tpamyhome", Translations.D_TPAMYHOME_CMD,
                b ->
                        b.permission("wormhole.tpamyhome")
                                .senderType(Player.class)
                                .required(RequestPlayerParser.playerComponent().name("player"))
                                .required(PlayerHomeParser.PlayerHomeComponent().name("home"))
                                .meta(CloudKey.of("teleport-type", Request.Type.class), Request.Type.TO_OWN_HOME)
                                .handler(context -> {
                                    Player player = context.sender();
                                    Player other = context.get("player");
                                    PlayerHome home = context.get("home");

                                    Integration integration = getWormhole().getMasterIntegration();
                                    if (!integration.canTeleportPlayerToHome(player, other)) {
                                        return;
                                    }

                                    RequestManager requestManager = getWormhole().getRequestManager();
                                    requestManager.request(Request.Type.TO_OWN_HOME, player, other, home);
                                }),
                "tpaskmyhome").register();
    }
}