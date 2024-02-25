package xyz.goldendupe.command.staff;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.LegacyPlaceholder;
import bet.astral.messenger.placeholder.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.GoldenMessenger;
import xyz.goldendupe.models.GDChat;
import xyz.goldendupe.models.GDPlayer;

import java.util.LinkedList;
import java.util.List;

@Cloud
public class CustomChatCommand extends GDCloudCommand {

    public CustomChatCommand(GoldenDupe plugin, PaperCommandManager<CommandSender> commandManager) {
        super(plugin, commandManager);

        for (GDChat chat : GDChat.values()) {
            if (chat == GDChat.GLOBAL || chat == GDChat.CLAN) continue;
            abstractChatCommand(chat);
        }

    }

    void abstractChatCommand(GDChat type) {

        final String st = type.name().toLowerCase();
        String name = (type == GDChat.STAFF ? st : st + "s");

        commandManager.command(
                commandManager.commandBuilder(
                                st + "chat",
                                Description.of("Lets " + name + " send messages only other " + name + " can see."),
                                st.charAt(0) + "chat"
                        )
                        .optional(StringParser.stringComponent(StringParser.StringMode.GREEDY).name("chat-text"))
                        .permission("goldendupe." + st + "." + st + "chat")
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
                                    GDPlayer gdPlayer = goldenDupe.playerDatabase().fromPlayer(player);
                                    GDChat chat = gdPlayer.chat();
                                    if (chat == type) {
                                        commandMessenger.message(sender, st + ".message-toggle-off");
                                        gdPlayer.setChat(GDChat.GLOBAL);
                                    } else {
                                        commandMessenger.message(sender, st + ".message-toggle-on");
                                        gdPlayer.setChat(type);
                                    }
                                } else {
                                    List<Placeholder> placeholders = new LinkedList<>(commandMessenger.createPlaceholders(player));
                                    placeholders.add(new LegacyPlaceholder("message", message));
                                    commandMessenger
                                            .broadcast(getMessageChannelFromGDChat(type),
                                                    st + "chat.message-chat", placeholders);
                                }
                            } else if (hasArgs && sender instanceof ConsoleCommandSender) {
                                commandMessenger
                                        .broadcast(getMessageChannelFromGDChat(type),
                                                st + "chat.message-chat-console",
                                                new LegacyPlaceholder("message", message));
                            }

                        })
        );

    }

    GoldenMessenger.MessageChannel getMessageChannelFromGDChat(GDChat chat) {
        return switch (chat) {
            case ADMIN -> GoldenMessenger.MessageChannel.ADMIN;
            case STAFF -> GoldenMessenger.MessageChannel.STAFF;
            case DONATOR -> GoldenMessenger.MessageChannel.DONATOR;
            default -> null;
        };
    }

}
