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
import org.incendo.cloud.parser.standard.EnumParser;
import org.jetbrains.annotations.Nullable;

@Cloud
public class TPPlayerDataLocationCommand extends PluginCommand {
    public TPPlayerDataLocationCommand(PluginCommandManager registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
        super(registerer, commandManager);
        command("tpdata", Translations.SG_RENAME_HOME_LINE_4,
                b->b.permission("wormhole.teleport.data")
                        .senderType(Player.class)
                        .argument(EnumParser.enumComponent(LocationDataType.class).name("type"))
                        .argument(OfflinePlayerParser.offlinePlayerComponent().name("player"))
                        .handler(context->{
                            final Player player = context.sender();
                            final OfflinePlayer other = context.get("player");
                            final LocationDataType type = context.get("type");
                            final Location location = type.getLocation(other);

                            if (location == null) {
                                // Send message
                                return;
                            }
                            player.teleportAsync(location);
                        }), "teleportdata").register();
    }

    public enum LocationDataType {
        LOCATION,
        RESPAWN,
        RESPAWN_VALIDATE,
        LAST_DEATH,
        ;

        public Location getLocation(@Nullable OfflinePlayer player) {
            return switch (this) {
                case LOCATION->player.getLocation();
                case RESPAWN->player.getRespawnLocation(false);
                case RESPAWN_VALIDATE -> player.getRespawnLocation(true);
                case LAST_DEATH->player.getLastDeathLocation();
            };
        }
    }
}
