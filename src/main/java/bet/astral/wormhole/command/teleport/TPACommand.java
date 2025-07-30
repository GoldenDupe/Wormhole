package bet.astral.wormhole.command.teleport;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.wormhole.command.PluginCommand;
import bet.astral.wormhole.command.PluginCommandManager;
import bet.astral.wormhole.command.arguments.RequestPlayerParser;
import bet.astral.wormhole.integration.Integration;
import bet.astral.wormhole.managers.RequestManager;
import bet.astral.wormhole.objects.Request;
import bet.astral.wormhole.plugin.translation.Translations;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.key.CloudKey;
import org.incendo.cloud.paper.PaperCommandManager;

@Cloud
public class TPACommand extends PluginCommand {
    public TPACommand(PluginCommandManager registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
        super(registerer, commandManager);

        command("tpa", Translations.D_TPA_CMD,
                b->
                        b.permission("wormhole.tpa")
                                .senderType(Player.class)
                                .required(RequestPlayerParser.playerComponent().name("player"))
                                .meta(CloudKey.of("teleport-type", Request.Type.class), Request.Type.TO_PLAYER)
                                .handler(this::handle),
                "tpask").register();

        command("tpahere", Translations.D_TPAHERE_CMD,
                b ->
                        b.permission("wormhole.tpahere")
                                .senderType(Player.class)
                                .required(RequestPlayerParser.playerComponent().name("player"))
                                .meta(CloudKey.of("teleport-type", Request.Type.class), Request.Type.PLAYER_HERE)
                                .handler(this::handle),
                "tpaskhere").register();

    }

    public <C> void handle(CommandContext<C> context) {
        Player player = (Player) context.sender();
        Player other = context.get("player");
        Request.Type type = context.command().commandMeta().get(CloudKey.of("teleport-type", Request.Type.class));

        Integration integration = getWormhole().getMasterIntegration();
        if (!integration.canTeleportToPlayer(player, other)){
            return;
        }

        RequestManager requestManager = getWormhole().getRequestManager();
        requestManager.request(type, player, other, null);
    }
}
