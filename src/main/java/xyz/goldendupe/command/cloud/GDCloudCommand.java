package xyz.goldendupe.command.cloud;

import bet.astral.cloudplusplus.command.CPPCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.messenger.GoldenMessenger;

public class GDCloudCommand extends CPPCommand<CommandSender> {
	private GoldenDupe goldenDupe;
	protected GoldenMessenger messenger;
	@Deprecated(forRemoval = true)
	protected GoldenMessenger commandMessenger;
	public GDCloudCommand(GoldenDupeBootstrap registerer, PaperCommandManager<CommandSender> commandManager) {
		super(registerer, commandManager);
		messenger = commandMessenger = (GoldenMessenger) registerer.getMessenger();
	}

	public GoldenDupe goldenDupe(){
		if (goldenDupe == null){
			goldenDupe = GoldenDupe.instance();
		}
		return goldenDupe;
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
