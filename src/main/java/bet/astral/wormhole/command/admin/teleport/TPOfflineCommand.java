package bet.astral.wormhole.command.admin.teleport;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.wormhole.command.PluginCommand;
import bet.astral.wormhole.command.PluginCommandManager;
import bet.astral.wormhole.plugin.translation.Translations;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.bukkit.parser.OfflinePlayerParser;
import org.incendo.cloud.paper.PaperCommandManager;

@Cloud
public class TPOfflineCommand extends PluginCommand {
    public TPOfflineCommand(PluginCommandManager registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
        super(registerer, commandManager);
        command("tpo", Translations.SG_RENAME_HOME_LINE_4,
                b->b
                        .permission("wormhole.teleport.offline")
                        .senderType(Player.class)
                        .argument(OfflinePlayerParser.offlinePlayerComponent().name("player"))
                        .handler(context->{
                            Player player = context.sender();
                            OfflinePlayer offlinePlayer = context.get("player");

                            Location location = offlinePlayer.getLocation();
                            if (location == null){
                                // Send message about not having played
                                return;
                            }
                            player.teleportAsync(offlinePlayer.getLocation());
                        }), "tpoffline", "teleportoffline").register();
    }
}
