package xyz.goldendupe;

import bet.astral.cloudplusplus.minecraft.paper.PaperCommandRegisterer;
import bet.astral.cloudplusplus.minecraft.paper.mapper.CommandSourceStackToCommandSenderMapper;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.receiver.Receiver;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoldenDupeCommandRegister implements PaperCommandRegisterer<CommandSender> {
	private final PaperCommandManager.Bootstrapped<CommandSender> commandManager;
	private final Logger logger = LoggerFactory.getLogger("GoldenDupeCommands");
	private final Messenger messenger;
	public final GoldenDupeBootstrap bootstrap;

	public GoldenDupeCommandRegister(Messenger messenger, BootstrapContext context, GoldenDupeBootstrap bootstrap) {
		commandManager = PaperCommandManager.builder(new CommandSourceStackToCommandSenderMapper())
				.executionCoordinator(ExecutionCoordinator.asyncCoordinator())
				.buildBootstrapped(context);
		this.messenger = messenger;
		this.bootstrap = bootstrap;

		registerCommands(
				"xyz.goldendupe.command.admin",
				"xyz.goldendupe.command.defaults",
				"xyz.goldendupe.command.donator",
				"xyz.goldendupe.command.staff",
				"xyz.goldendupe.command.og"
		);
	}

	@Override
	public PaperCommandManager.Bootstrapped<CommandSender> getCommandManager() {
		return commandManager;
	}

	@Override
	public Logger getSlf4jLogger() {
		return logger;
	}

	@Override
	public Messenger getMessenger() {
		return messenger;
	}

	@Override
	public Receiver convertToReceiver(@NotNull CommandSender commandSender) {
		return messenger.convertReceiver(commandSender);
	}

	@Override
	public boolean isDebug() {
		return false;
	}
}
