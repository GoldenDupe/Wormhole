package bet.astral.wormhole.command;

import bet.astral.cloudplusplus.minecraft.paper.bootstrap.commands.CPPBootstrapCommand;
import bet.astral.wormhole.plugin.WormholePlugin;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.paper.PaperCommandManager;

public class PluginCommand extends CPPBootstrapCommand<CommandSender> {
    private static WormholePlugin wormholePlugin;
    public PluginCommand(PluginCommandManager registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
        super(registerer, commandManager);
    }

    public WormholePlugin getWormhole() {
        if (wormholePlugin == null) {
            wormholePlugin = WormholePlugin.getPlugin(WormholePlugin.class);
        }
        return wormholePlugin;
    }
}
