package bet.astral.wormhole.command.teleport;

import bet.astral.cloudplusplus.CommandRegisterer;
import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.wormhole.command.PluginCommand;
import bet.astral.wormhole.command.arguments.RequestParser;
import bet.astral.wormhole.managers.RequestManager;
import bet.astral.wormhole.objects.Request;
import bet.astral.wormhole.plugin.Translations;
import bet.astral.wormhole.plugin.WormholePlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.key.CloudKey;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@Cloud
public class TPDenyCommand extends PluginCommand {
    public TPDenyCommand(CommandRegisterer<CommandSender> registerer, CommandManager<CommandSender> commandManager) {
        super(registerer, commandManager);

        command("tpdeny", Translations.D_TPA_CMD,
                b ->
                        b.permission("wormhole.tpdeny")
                                .senderType(Player.class)
                                .meta(CloudKey.of("teleport-type", Request.Type.class), Request.Type.TO_PLAYER)
                                .meta(CloudKey.of("request-type", RequestParser.Type.class), RequestParser.Type.RECEIVED)
                                .optional(RequestParser.requestComponent().name("request"))
                                .handler(this::handle)).register();

        command("tpheredeny", Translations.D_TPA_CMD,
                b ->
                        b.permission("wormhole.tpheredeny")
                                .senderType(Player.class)
                                .meta(CloudKey.of("teleport-type", Request.Type.class), Request.Type.PLAYER_HERE)
                                .meta(CloudKey.of("request-type", RequestParser.Type.class), RequestParser.Type.RECEIVED)
                                .optional(RequestParser.requestComponent().name("request"))
                                .handler(this::handle)).register();
        command("tphomedeny", Translations.D_TPA_CMD,
                b ->
                        b.permission("wormhole.tphomedeny")
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
            messenger.message(player, Translations.M_TPDENY_NO_TELEPORT_REQUESTS, placeholders);
            return;
        }
        requestManager.deny(request);
    }
}