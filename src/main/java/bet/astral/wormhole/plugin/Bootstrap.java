package bet.astral.wormhole.plugin;

import bet.astral.cloudplusplus.minecraft.paper.bootstrap.BootstrapHandler;
import bet.astral.cloudplusplus.minecraft.paper.mapper.CommandSourceStackToCommandSenderMapper;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.receiver.Receiver;
import bet.astral.messenger.v2.receiver.ReceiverConverter;
import bet.astral.messenger.v2.source.LanguageTable;
import bet.astral.messenger.v2.source.source.gson.GsonLanguageSource;
import bet.astral.messenger.v3.minecraft.paper.cloud.PaperCaptionMessenger;
import bet.astral.messenger.v3.minecraft.paper.cloud.locale.CommandSenderLocaleExtractor;
import bet.astral.wormhole.command.PluginCommand;
import bet.astral.wormhole.command.PluginCommandManager;
import bet.astral.wormhole.plugin.translation.TranslationFileWriter;
import bet.astral.wormhole.plugin.translation.Translations;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import lombok.Getter;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.translations.LocaleExtractor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class Bootstrap extends BootstrapHandler implements PluginBootstrap {
    private static String NAME = "PluginCommand";
    @Getter
    private PluginCommandManager commandManager;
    @Getter
    private Messenger messenger;
    @Override
    public void bootstrap(BootstrapContext bootstrapContext) {
        PaperCommandManager.Bootstrapped<CommandSender> commandManager = initializeCommandManager(bootstrapContext);
        messenger = initializeMessenger(bootstrapContext);
        this.commandManager = new PluginCommandManager(this, messenger, commandManager);
        registerCommands();
    }
    private void registerCommands() {
        commandManager.registerCommands(PluginCommand.class.getPackageName());
    }
    private Messenger initializeMessenger(BootstrapContext bootstrapContext) {
        PaperCaptionMessenger<?> messenger = new PaperCaptionMessenger<CommandSender>(ComponentLogger.logger(NAME)) {
            @Override
            public @NotNull LocaleExtractor<CommandSender> getLocaleExtractor() {
                return new CommandSenderLocaleExtractor();
            }
        };
        messenger.setSendTranslationKey(true);

        TranslationFileWriter.ensureTranslationsExist(messenger, Translations.class, loadLanguageFile(bootstrapContext, Locale.US));

        messenger.registerLanguageTable(Locale.US,
                LanguageTable.of(
                        new GsonLanguageSource(messenger,
                                Locale.US,
                                loadLanguageFile(bootstrapContext, Locale.US),
                                MiniMessage.miniMessage()
                                )
        ));
        messenger.setDefaultLocale(messenger.getLanguageTable(Locale.US).getLanguageSource());
        messenger.loadTranslations(Locale.US, Translations.class);
        messenger.setSendTranslationKey(true);

        // Make sure no message is sent to an offline player
        messenger.registerReceiverConverter((ReceiverConverter) o -> {
            if (o instanceof OfflinePlayer && !(o instanceof Player)) {
                return Receiver.empty();
            }
            return null;
        });
         return messenger;
    }
    private @Nullable File loadLanguageFile(@NotNull BootstrapContext bootstrapContext, @NotNull Locale locale) {
        File file = new File(bootstrapContext.getDataDirectory().toFile(), locale.toLanguageTag()+".json");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return file.exists() ? file : null;
    }

    @Contract("_ -> new")
    private PaperCommandManager.@NotNull Bootstrapped<CommandSender> initializeCommandManager(BootstrapContext bootstrapContext) {
        return PaperCommandManager.builder(new CommandSourceStackToCommandSenderMapper())
                .executionCoordinator(ExecutionCoordinator.simpleCoordinator())
                .buildBootstrapped(bootstrapContext);
    }

    @Override
    public JavaPlugin createPlugin(PluginProviderContext context) {
        return new WormholePlugin(this);
    }
}
