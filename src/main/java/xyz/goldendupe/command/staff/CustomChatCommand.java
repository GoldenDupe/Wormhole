package xyz.goldendupe.command.staff;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.placeholder.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.models.GDChat;
import xyz.goldendupe.models.GDPlayer;

import java.util.LinkedList;
import java.util.List;

@Cloud
public class CustomChatCommand extends GDCloudCommand {

    public CustomChatCommand(GoldenDupeCommandRegister register, PaperCommandManager<CommandSender> commandManager) {
        super(register, commandManager);
        for (GDChat chat : GDChat.values()) {
            if (chat.asMessageChannel() == null || chat.asMemberType() == null) return;
            abstractChatCommand(chat);
        }
    }

    void abstractChatCommand(GDChat type) {

        final String name = type.name().toLowerCase();
        String group = (type == GDChat.STAFF ? name : name + "s");

        commandManager.command(
                commandManager.commandBuilder(
                                name + "chat",
                                Description.of("Lets " + group + " send messages only other " + group + " can see."),
                                name.charAt(0) + "chat",
                                name.charAt(0)+"c"
                        )
                        .optional(StringParser.stringComponent(StringParser.StringMode.GREEDY).name("chat-text"))
                        .permission(type.asMemberType().cloudOf("rank-chat"))
                        .handler(context -> {

                            CommandSender sender = context.sender();
                            String message = null;
                            if (context.optional("chat-text").isPresent())
                                message = (String) context.optional("chat-text").get();

                            boolean hasArgs = true;
                            if (message == null) {
                                hasArgs = false;
                                message = "";
                            }

                            if (sender instanceof Player player) {
                                if (!hasArgs) {
                                    GDPlayer gdPlayer = goldenDupe().playerDatabase().fromPlayer(player);
                                    GDChat chat = gdPlayer.chat();
                                    if (chat == type) {
                                        messenger.message(sender, Translations.get(name + ".enabled"));
                                        gdPlayer.setChat(GDChat.GLOBAL);
                                    } else {
                                        messenger.message(sender, Translations.get(name + ".disabled"));
                                        gdPlayer.setChat(type);
                                    }
                                } else {
                                    List<Placeholder> placeholders = new LinkedList<>();
                                    placeholders.add(Placeholder.legacy("message", message));
                                    messenger
                                            .broadcast(type.asMessageChannel(),
                                                    Translations.get(name + ".chat"), placeholders);
                                }
                            } else if (hasArgs && sender instanceof ConsoleCommandSender) {
                                messenger
                                        .broadcast(type.asMessageChannel(),
                                                Translations.get(name + ".chat-console"),
                                                Placeholder.legacy("message", message));
                            }

                        })
        );

    }

}
