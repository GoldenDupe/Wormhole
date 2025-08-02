package bet.astral.wormhole.command.admin;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.translation.TranslationKey;
import bet.astral.wormhole.command.PluginCommand;
import bet.astral.wormhole.command.PluginCommandManager;
import bet.astral.wormhole.plugin.translation.Translations;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.paper.PaperCommandManager;

import java.util.List;

@Cloud
public class WormholeCommand extends PluginCommand {
    public WormholeCommand(PluginCommandManager registerer, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
        super(registerer, commandManager);
        RegistrableCommand<? extends CommandSender> command = command("wormhole", Translations.SG_RENAME_HOME_LINE_4, b->b.permission("wormhole.admin"));
        command.register();
        command(command, "reload-messages", Translations.SG_RENAME_HOME_LINE_4, b->b.permission("wormhole.admin.reload-messages").handler(context->{
            List<TranslationKey> translationKeyList = messenger.fetchTranslations(Translations.class);
            messenger.loadTranslations(translationKeyList);
        })).register();
    }
}