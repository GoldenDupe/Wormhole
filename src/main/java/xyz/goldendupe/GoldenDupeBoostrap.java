package xyz.goldendupe;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import lombok.Getter;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class GoldenDupeBoostrap implements PluginBootstrap {
	@Getter
	private boolean devServer = false;
	private String devServerName;
	private String devServerSuperVisor;
	@Override
	public void bootstrap(@NotNull BootstrapContext bootstrapContext) {
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
}
