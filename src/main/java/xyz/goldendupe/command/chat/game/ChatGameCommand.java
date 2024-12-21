package xyz.goldendupe.command.chat.game;

import org.bukkit.command.CommandSender;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.admin.GoldenDupeCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.utils.MemberType;

public class ChatGameCommand extends GoldenDupeCommand {
    protected static RegistrableCommand<? extends CommandSender> root;

    public ChatGameCommand(GoldenDupeCommandRegister register, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
        super(register, commandManager);
        setup(this);
    }

    private static void setup(GoldenDupeCommand command){
        if (root == null) {
            root = command.command("chatgame", Translations.CHATGAME_DESCRIPTION, b->
                    b.permission(MemberType.DEFAULT.permissionOf("chatgame")), "chatevent");
        }
    }
}
