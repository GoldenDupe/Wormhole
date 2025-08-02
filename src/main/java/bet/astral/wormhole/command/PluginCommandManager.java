package bet.astral.wormhole.command;

import bet.astral.cloudplusplus.minecraft.paper.bootstrap.BootstrapCommandRegisterer;
import bet.astral.cloudplusplus.minecraft.paper.bootstrap.BootstrapHandler;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.receiver.Receiver;
import bet.astral.messenger.v3.minecraft.paper.PaperMessenger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PluginCommandManager implements BootstrapCommandRegisterer<CommandSender> {
    private PaperCommandManager.Bootstrapped<CommandSender> commandManager;
    private BootstrapHandler bootstrapHandler;
    private Messenger messenger;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public PluginCommandManager(BootstrapHandler bootstrapHandler, Messenger messenger, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
        this.messenger = messenger;
        this.bootstrapHandler = bootstrapHandler;
        this.commandManager = commandManager;
    }

    @Override
    public PaperCommandManager.@NotNull Bootstrapped<CommandSender> getCommandManager() {
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
    public Receiver convertToReceiver(@NotNull CommandSender sender) {
        if (sender instanceof Player player) {
            return PaperMessenger.playerManager.players.get(player.getUniqueId());
        }
        return messenger.convertReceiver(sender);
    }

    @Override
    public boolean isDebug() {
        return false;
    }

    @Override
    public @NotNull BootstrapHandler getHandler() {
        return bootstrapHandler;
    }
}
