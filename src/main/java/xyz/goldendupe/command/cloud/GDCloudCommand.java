package xyz.goldendupe.command.cloud;

import bet.astral.cloudplusplus.CommandRegisterer;
import bet.astral.cloudplusplus.annotations.DoNotReflect;
import bet.astral.cloudplusplus.command.CloudPPCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.messenger.GoldenMessenger;

@DoNotReflect
public class GDCloudCommand extends CloudPPCommand<GoldenDupe, CommandSender> {
	protected final GoldenDupe goldenDupe;
	protected GoldenMessenger commandMessenger;
	protected GoldenMessenger debugMessenger;
	public GDCloudCommand(CommandRegisterer<>, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		this.goldenDupe = plugin;
		reloadMessengers();
	}

	@Override
	public void reloadMessengers() {
		super.reloadMessengers();
		this.commandMessenger = (GoldenMessenger) super.commandMessenger;
		this.debugMessenger = (GoldenMessenger) super.debugMessenger;
	}

	public void commandPlayer(Command<Player> command) {
		commandManager.command(command);
	}

	public void commandPlayer(Command.Builder<Player> command) {
		commandManager.command(command);
	}

	public Command.Builder<Player> commandBuilderPlayer(String name, Description description, String... aliases) {
		return commandManager.commandBuilder(name, description, aliases).senderType(Player.class);
	}
}
