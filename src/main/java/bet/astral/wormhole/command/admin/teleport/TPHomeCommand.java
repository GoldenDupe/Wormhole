package bet.astral.wormhole.command.admin.teleport;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.wormhole.command.PluginCommand;
import bet.astral.wormhole.command.PluginCommandManager;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.paper.PaperCommandManager;

@Cloud
public class TPHomeCommand extends PluginCommand {
    public TPHomeCommand(PluginCommandManager registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
        super(registerer, commandManager);
    }

}
