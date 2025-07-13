package bet.astral.pluginsetup.command;

import bet.astral.cloudplusplus.CommandRegisterer;
import bet.astral.cloudplusplus.annotations.Cloud;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.incendo.cloud.CommandManager;

@Cloud
public class ExampleCommand extends PluginCommand{
    public ExampleCommand(CommandRegisterer<CommandSourceStack> registerer, CommandManager<CommandSourceStack> commandManager) {
        super(registerer, commandManager);
    }
}
