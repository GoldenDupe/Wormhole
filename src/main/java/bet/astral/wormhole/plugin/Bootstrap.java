package bet.astral.wormhole.plugin;

import bet.astral.cloudplusplus.minecraft.paper.bootstrap.BootstrapHandler;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.source.LanguageTable;
import bet.astral.messenger.v2.source.source.gson.GsonLanguageSource;
import bet.astral.messenger.v3.minecraft.paper.PaperMessenger;
import bet.astral.wormhole.command.PluginCommandManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import lombok.Getter;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;

import java.io.File;
import java.util.Locale;

public class Bootstrap extends BootstrapHandler implements PluginBootstrap {
    private static String NAME = "PluginCommand";
    @Getter
    private PluginCommandManager commandManager;
    @Getter
    private Messenger messenger;
    @Override
    public void bootstrap(BootstrapContext bootstrapContext) {
        PaperCommandManager.Bootstrapped<CommandSourceStack> commandManager = initializeCommandManager(bootstrapContext);
        messenger = initializeMessenger(bootstrapContext);
        this.commandManager = new PluginCommandManager(this, messenger, commandManager);
        registerCommands();
    }
    private void registerCommands() {
        commandManager.registerCommands(ExampleCommand.class.getPackageName());
    }
    private Messenger initializeMessenger(BootstrapContext bootstrapContext) {
        Messenger messenger = new PaperMessenger(ComponentLogger.logger(NAME));
        messenger.setSendTranslationKey(true);
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
         return messenger;
    }
    private File loadLanguageFile(BootstrapContext bootstrapContext, Locale locale) {
        File file = new File(bootstrapContext.getDataDirectory().toFile(), locale.toLanguageTag());
        return file.exists() ? file : null;
    }

    private PaperCommandManager.Bootstrapped<CommandSourceStack> initializeCommandManager(BootstrapContext bootstrapContext) {
        return PaperCommandManager.builder()
                .executionCoordinator(ExecutionCoordinator.simpleCoordinator())
                .buildBootstrapped(bootstrapContext);
    }
}
