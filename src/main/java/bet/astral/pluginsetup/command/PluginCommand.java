package bet.astral.pluginsetup.command;

import bet.astral.cloudplusplus.CommandRegisterer;
import bet.astral.cloudplusplus.minecraft.paper.bootstrap.commands.CPPBootstrapCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.incendo.cloud.CommandManager;

public class PluginCommand extends CPPBootstrapCommand<CommandSourceStack> {
    public PluginCommand(CommandRegisterer<CommandSourceStack> registerer, CommandManager<CommandSourceStack> commandManager) {
        super(registerer, commandManager);
    }
}
