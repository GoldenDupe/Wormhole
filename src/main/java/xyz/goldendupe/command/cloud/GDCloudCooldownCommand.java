package xyz.goldendupe.command.cloud;

import bet.astral.cloudplusplus.annotations.DoNotReflect;
import bet.astral.cloudplusplus.command.CloudPPCooldownCommand;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.messenger.GoldenMessenger;

@DoNotReflect
public class GDCloudCooldownCommand extends CloudPPCooldownCommand<GoldenDupe> {
	protected final GoldenDupe goldenDupe;
	protected GoldenMessenger commandMessenger;
	protected GoldenMessenger debugMessenger;
	public GDCloudCooldownCommand(GoldenDupe plugin, PaperCommandManager<CommandSender> commandManager, long cooldown) {
		super(plugin, commandManager, cooldown);
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
