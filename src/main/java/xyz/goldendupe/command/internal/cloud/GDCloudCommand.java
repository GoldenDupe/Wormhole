package xyz.goldendupe.command.internal.cloud;


import org.bukkit.command.CommandSender;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.CommandFinder;
import xyz.goldendupe.command.internal.legacy.GDCommandInfo;
import xyz.goldendupe.command.internal.legacy.MessageReload;
import xyz.goldendupe.messenger.GoldenMessenger;

@SuppressWarnings("removal")
@GDCommandInfo.DoNotReflect
public class GDCloudCommand implements CommandFinder, MessageReload {
	protected final PaperCommandManager<CommandSender> commandManager;
	protected final GoldenDupe goldenDupe;
	protected GoldenMessenger commandMessenger;
	protected GoldenMessenger debugMessenger;

	public GDCloudCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
		this.goldenDupe = goldenDupe;
		this.commandManager = commandManager;
		reloadMessengers();
	}

	@Override
	public void reloadMessengers() {
		this.commandMessenger = goldenDupe.commandMessenger();
		this.debugMessenger = goldenDupe.debugMessenger();
	}
}
