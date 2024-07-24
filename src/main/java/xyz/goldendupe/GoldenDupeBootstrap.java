package xyz.goldendupe;

import bet.astral.cloudplusplus.CommandRegisterer;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.receiver.Receiver;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import lombok.Getter;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import xyz.goldendupe.command.bootstrap.InitAfterBootstrap;
import xyz.goldendupe.command.cloud.SenderMapper;
import xyz.goldendupe.messenger.GoldenMessenger;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class GoldenDupeBootstrap implements PluginBootstrap, CommandRegisterer<CommandSender> {
	private ComponentLogger logger;
	@Getter
	private boolean devServer = false;
	private final Messenger messenger = new GoldenMessenger();
	private String devServerName;
	private String devServerSuperVisor;
	private PaperCommandManager<CommandSender> commandManager;
	public List<InitAfterBootstrap> initAfterBootstraps = new LinkedList<>();
	@Override
	public void bootstrap(@NotNull BootstrapContext bootstrapContext) {
		logger = bootstrapContext.getLogger();
		File file = new File(bootstrapContext.getDataDirectory().toFile(), bootstrapContext.getConfiguration().getName()+"/"+".dev");
		devServer = file.exists();
		if (devServer){
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				FileConfiguration configuration = YamlConfiguration.loadConfiguration(reader);
				reader.close();
				devServer = configuration.getBoolean("is", true);
				devServerName = configuration.getString("name", "Unknown");
				devServerSuperVisor = configuration.getString("supervisor", "Unknown");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		loadCommands(bootstrapContext);
	}

	@Override
	public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
		GoldenDupe goldenDupe = new GoldenDupe(this);
		ComponentLogger logger = goldenDupe.getComponentLogger();
		if (devServer) {
			logger.error("");
			logger.error(" | Enabling GoldenDupe");
			logger.error(" | Enabling using Development status. ");
			logger.error(" | Disable development mode if this plugin is in the public server!");
			logger.error("");
			logger.error(" | Name: "+devServerName);
			logger.error(" | Super Visor: "+devServerSuperVisor);
			logger.error("");
		}
		return new GoldenDupe(this);
	}

	private void loadCommands(BootstrapContext context) {
		commandManager = PaperCommandManager.builder(new SenderMapper())
				.executionCoordinator(ExecutionCoordinator.asyncCoordinator())
				.buildBootstrapped(context);

		registerCommands(
				"xyz.goldendupe.command.admin",
				"xyz.goldendupe.command.defaults",
				"xyz.goldendupe.command.donator",
				"xyz.goldendupe.command.staff",
				"xyz.goldendupe.command.og"
		);
	}

	@Override
	public CommandManager<CommandSender> getCommandManager() {
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
