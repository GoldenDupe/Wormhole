package bet.astral.wormhole.command;

import bet.astral.cloudplusplus.CommandRegisterer;
import bet.astral.cloudplusplus.minecraft.paper.bootstrap.commands.CPPBootstrapCommand;
import bet.astral.wormhole.plugin.WormholePlugin;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.CommandManager;

public class PluginCommand extends CPPBootstrapCommand<CommandSender> {
    private static WormholePlugin wormholePlugin;
    public PluginCommand(CommandRegisterer<CommandSender> registerer, CommandManager<CommandSender> commandManager) {
        super(registerer, commandManager);
    }

    public WormholePlugin getWormhole() {
        if (wormholePlugin == null) {
            wormholePlugin = WormholePlugin.getPlugin(WormholePlugin.class);
        }
        return wormholePlugin;
    }
}
