package xyz.goldendupe.command.cloud;

import bet.astral.cloudplusplus.annotations.DoNotReflect;
import bet.astral.cloudplusplus.command.CloudPPCommand;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.messenger.GoldenMessenger;

@DoNotReflect
public class GDCloudCommand extends CloudPPCommand<GoldenDupe> {
	protected final GoldenDupe goldenDupe;
	protected GoldenMessenger commandMessenger;
	protected GoldenMessenger debugMessenger;
	public GDCloudCommand(GoldenDupe plugin, PaperCommandManager<CommandSender> commandManager) {
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
}
