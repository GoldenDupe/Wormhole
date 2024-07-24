package xyz.goldendupe;

import bet.astral.messenger.v2.locale.LanguageTable;
import bet.astral.messenger.v2.locale.source.FileLanguageSource;
import bet.astral.messenger.v2.locale.source.LanguageSource;
import com.google.gson.Gson;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.command.bootstrap.InitAfterBootstrap;
import xyz.goldendupe.datagen.GenerateMessages;
import xyz.goldendupe.messenger.GoldenMessenger;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.models.chatcolor.Color;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class GoldenDupeBootstrap implements PluginBootstrap {
	private ComponentLogger logger;
	@Getter
	private GoldenDupeCommandRegister commandRegister;
	private boolean devServer = false;
	final GoldenMessenger messenger = new GoldenMessenger();
	public List<InitAfterBootstrap> initAfterBootstraps = new LinkedList<>();
	@Override
	public void bootstrap(@NotNull BootstrapContext bootstrapContext) {
		logger = bootstrapContext.getLogger();


		File dataFolder = new File(bootstrapContext.getPluginSource().getParent().toFile(), "GoldenDupe");

		GenerateMessages generator = new GenerateMessages();
		try {
			generator.generate(dataFolder);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		LanguageSource englishSource;
		try {
			englishSource = FileLanguageSource.gson(messenger, Locale.US, new File(dataFolder, "messages.json"), MiniMessage.miniMessage());
			LanguageTable englishTable = LanguageTable.of(englishSource);
			messenger.setLocale(Locale.US);
			messenger.setDefaultLocale(englishSource);
			messenger.registerLanguageTable(Locale.US, englishTable);
			messenger.setUseReceiverLocale(false);
			messenger.setSendASync(true);
			messenger.setPrefix(MiniMessage.miniMessage().deserialize("<gold><bold>G<white><bold>D<reset> "));
			messenger.enablePrefix();
			messenger.loadTranslations(List.copyOf(Translations.translations()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		commandRegister = new GoldenDupeCommandRegister(messenger, bootstrapContext, this);
	}


	public <T> T getJson(@NotNull Gson gson, @NotNull File file, Class<T> type) throws IOException {
		FileReader reader = new FileReader(file);
		T t = gson.fromJson(reader, type);
		reader.close();
		return t;
	}

	@Override
	public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
		devServer = true;
		logger.info("Server Debug Mode: "+ devServer);

		GoldenDupe goldenDupe = new GoldenDupe(this);
		ComponentLogger logger = goldenDupe.getComponentLogger();
		if (devServer) {
			logger.error("");
			logger.error(" | Enabling GoldenDupe");
			logger.error(" | Enabling using Development status. ");
			logger.error(" | Disable development mode if this plugin is in the public server!");
			logger.error("");
		}
		return new GoldenDupe(this);
	}

	public boolean isDev() {
		return devServer;
	}
}
