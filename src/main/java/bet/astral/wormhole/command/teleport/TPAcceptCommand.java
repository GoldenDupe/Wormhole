package bet.astral.wormhole.command.teleport;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.wormhole.command.PluginCommand;
import bet.astral.wormhole.command.PluginCommandManager;
import bet.astral.wormhole.command.arguments.RequestParser;
import bet.astral.wormhole.integration.Integration;
import bet.astral.wormhole.managers.RequestManager;
import bet.astral.wormhole.objects.Request;
import bet.astral.wormhole.plugin.translation.Translations;
import bet.astral.wormhole.plugin.WormholePlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.key.CloudKey;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@Cloud
public class TPAcceptCommand extends PluginCommand {
    public TPAcceptCommand(PluginCommandManager registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
        super(registerer, commandManager);

        command("tpaccept", Translations.D_TPACCEPT_CMD,
                b ->
                        b.permission("wormhole.tpa")
                                .senderType(Player.class)
                                .meta(CloudKey.of("teleport-type", Request.Type.class), Request.Type.TO_PLAYER)
                                .meta(CloudKey.of("request-type", RequestParser.Type.class), RequestParser.Type.RECEIVED)
                                .optional(RequestParser.requestComponent().name("request"))
                                .handler(this::handle)).register();

        command("tphereaccept", Translations.D_TPHEREACCEPT_CMD,
                b ->
                        b.permission("wormhole.tphereaccept")
                                .senderType(Player.class)
                                .meta(CloudKey.of("teleport-type", Request.Type.class), Request.Type.PLAYER_HERE)
                                .meta(CloudKey.of("request-type", RequestParser.Type.class), RequestParser.Type.RECEIVED)
                                .optional(RequestParser.requestComponent().name("request"))
                                .handler(this::handle)).register();
        command("tphomeaccept", Translations.D_TPHOMEACCEPT_CMD,
                b ->
                        b.permission("wormhole.tphomeaccept")
                                .senderType(Player.class)
                                .meta(CloudKey.of("teleport-type", Request.Type.class), Request.Type.TO_OWN_HOME)
                                .meta(CloudKey.of("request-type", RequestParser.Type.class), RequestParser.Type.RECEIVED)
                                .optional(RequestParser.requestComponent().name("request"))
                                .handler(this::handle)).register();
    }

    @SuppressWarnings("DuplicatedCode")
    public <C> void handle(@NotNull CommandContext<C> context) {
        WormholePlugin wormholePlugin = getWormhole();
        RequestManager requestManager = wormholePlugin.getRequestManager();
        Player player = (Player) context.sender();
        Request request = (Request) context.optional("request").orElseGet((Supplier<Request>) () -> {
            requestManager.sortLatestRequest(player);
            return requestManager.getLatestReceivedRequest().get(player.getUniqueId());
        });

        PlaceholderList placeholders = new PlaceholderList();

        if (request == null) {
            messenger.message(player, Translations.M_TPACCEPT_NO_TELEPORT_REQUESTS, placeholders);
            return;
        }

        placeholders.addAll(request.toPlaceholders());

        Integration integration = getWormhole().getMasterIntegration();
        if (!integration.canAcceptTeleportRequest(player, request.getType(), request)) {
            return;
        }
        requestManager.accept(request);
    }
}