package xyz.goldendupe;

import bet.astral.chatgamecore.messenger.GameTranslations;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.component.ComponentType;
import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.messenger.v2.source.LanguageTable;
import bet.astral.messenger.v2.source.source.FileLanguageSource;
import bet.astral.messenger.v2.source.source.LanguageSource;
import bet.astral.messenger.v2.translation.Translation;
import com.google.gson.Gson;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.minecraft.extras.AudienceProvider;
import org.incendo.cloud.minecraft.extras.MinecraftExceptionHandler;
import org.incendo.cloud.suggestion.FilteringSuggestionProcessor;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.chat.games.CopyFastestChatGame;
import xyz.goldendupe.chat.games.TrueFalseChatGame;
import xyz.goldendupe.chat.games.UnscrambleChatGame;
import xyz.goldendupe.command.bootstrap.InitAfterBootstrap;
import xyz.goldendupe.datagen.GenerateChatGames;
import xyz.goldendupe.datagen.GenerateMessages;
import xyz.goldendupe.messenger.GoldenMessenger;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.messenger.chat.game.CopyFastestTranslations;
import xyz.goldendupe.messenger.chat.game.TrueOrFalseTranslations;
import xyz.goldendupe.messenger.chat.game.UnscrambleTranslations;

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
		File chatGameFolder = new File(dataFolder, "chatgame");

		GenerateMessages generator = new GenerateMessages();
		GenerateChatGames generateChatGames = new GenerateChatGames();

		try {
			generator.generate(dataFolder);
			generateChatGames.generate(chatGameFolder);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Component prefix = MiniMessage.miniMessage().deserialize("<gold><bold>G<white><bold>D<reset> ");
		try {
			LanguageSource englishSource = FileLanguageSource.gson(messenger, Locale.US, new File(dataFolder, "messages.json"), MiniMessage.miniMessage());
			LanguageTable englishTable = LanguageTable.of(englishSource);

			// Load additional language sources for chat games
			englishTable.addAdditionalLanguageSource(source(messenger, new File(chatGameFolder, "true-or-false-translations.json")));
			englishTable.addAdditionalLanguageSource(source(messenger, new File(chatGameFolder, "unscramble-translations.json")));
			englishTable.addAdditionalLanguageSource(source(messenger, new File(chatGameFolder, "type-fastest-translations.json")));
			englishTable.addAdditionalLanguageSource(source(messenger, new File(chatGameFolder, "root.json")));

			messenger.setLocale(Locale.US);
			messenger.setDefaultLocale(englishSource);
			messenger.registerLanguageTable(Locale.US, englishTable);
			messenger.setUseReceiverLocale(false);
			messenger.setSendASync(true);
			messenger.setPrefix(prefix);
			messenger.enablePrefix();

			messenger.loadTranslations(Translations.class); // Load using built in reflection methods
			// Load translations for chat games
			messenger.loadTranslations(GameTranslations.class);
			messenger.loadTranslations(TrueOrFalseTranslations.class);
			messenger.loadTranslations(UnscrambleTranslations.class);
			messenger.loadTranslations(CopyFastestTranslations.class);

//			messenger.message(messenger.console(), Translations.MESSENGER_TEST);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		commandRegister = new GoldenDupeCommandRegister(messenger, bootstrapContext, this);
		commandRegister.getCommandManager().suggestionProcessor(new FilteringSuggestionProcessor<>());
		AudienceProvider<CommandSender> senderAudienceProvider = (sender)->sender;
		MinecraftExceptionHandler.create(senderAudienceProvider).decorator(prefix::append).registerTo(commandRegister.getCommandManager());
		/*
		MinecraftExceptionHandler.create(senderAudienceProvider).decorator(t->getComponent(Translations.COMMAND_MANAGER_INVALID_SYNTAX, Placeholder.of("value", t))).defaultInvalidSyntaxHandler().registerTo(commandRegister.getCommandManager());
		MinecraftExceptionHandler.create(senderAudienceProvider).decorator(t->getComponent(Translations.COMMAND_MANAGER_INVALID_SENDER, Placeholder.of("value", t))).defaultInvalidSenderHandler().registerTo(commandRegister.getCommandManager());
		MinecraftExceptionHandler.create(senderAudienceProvider).decorator(t->getComponent(Translations.COMMAND_MANAGER_INVALID_PERMISSION, Placeholder.of("value", t))).defaultNoPermissionHandler().registerTo(commandRegister.getCommandManager());
		MinecraftExceptionHandler.create(senderAudienceProvider).decorator(t->getComponent(Translations.COMMAND_MANAGER_INVALID_ARGUMENT, Placeholder.of("value", t))).defaultArgumentParsingHandler().registerTo(commandRegister.getCommandManager());
		MinecraftExceptionHandler.create(senderAudienceProvider).decorator(t->getComponent(Translations.COMMAND_MANAGER_INTERNAL_EXCEPTION, Placeholder.of("value", t))).defaultCommandExecutionHandler().registerTo(commandRegister.getCommandManager());
		 */

        TrueFalseChatGame.parseStatements(new File(chatGameFolder, "true-or-false-values.json"));
        UnscrambleChatGame.parseStrings(new File(chatGameFolder, "unscramble-values.json"));
        CopyFastestChatGame.parseStrings(new File(chatGameFolder, "type-fastest-values.json"));
    }
	public LanguageSource source(Messenger messenger, File file) throws IOException {
		return FileLanguageSource.gson(messenger, Locale.US, file, MiniMessage.miniMessage());
	}

	public Component getComponent(@NotNull Translation translation, Placeholder... placeholders){
		return messenger.parseComponent(translation, Locale.US, ComponentType.CHAT, placeholders);
	}


	public <T> T getJson(@NotNull Gson gson, @NotNull File file, Class<T> type) throws IOException {
		FileReader reader = new FileReader(file);
		T t = gson.fromJson(reader, type);
		reader.close();
		return t;
	}

	@Override
	public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
//		devServer = true;
		logger.info("Server Debug Mode: {}", devServer);

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
