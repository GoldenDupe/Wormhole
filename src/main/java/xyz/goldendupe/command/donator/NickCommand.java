package xyz.goldendupe.command.donator;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.messenger.placeholder.PlaceholderList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class NickCommand extends GDCloudCommand {
    public NickCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
        super(goldenDupe, commandManager);
        commandManager.command(
                commandManager.commandBuilder(
                                "nickname",
                                Description.of("Allows a player to use a nickname."),
                        "nick", "displayname"
                        )
                        .permission(MemberType.DONATOR.cloudOf("nick"))
                        .argument(StringParser.stringComponent(StringParser.StringMode.SINGLE).name("nickname"))
                        .senderType(Player.class)
                        .handler(context -> {
                            Player sender = context.sender();
                            String nickname = context.get("nick");
                            String colorless = ChatColor.stripColor(nickname);
                            if (colorless.length()>16){
                                commandMessenger.message(sender, "nickname.message-too-long", new Placeholder("nickname", colorless), new Placeholder("length", colorless.length()));
                                return;
                            }
                            if (!colorless.matches("[a-zA-Z0-9_-]")){
                                commandMessenger.message(sender, "nickname.message-illegal", new Placeholder("nickname", colorless), new Placeholder("length", colorless.length()));
                                return;
                            }
                            Component component;
                            PlaceholderList placeholders = new PlaceholderList();
                            if (sender.hasPermission(MemberType.DONATOR.permissionOf("nick.color"))) {
                                component = LegacyComponentSerializer.legacyAmpersand().deserialize(nickname);
                            } else {
                                component = Component.text(colorless);
                            }
                            placeholders.add("nickname", component);

                            commandMessenger.message(sender, "nickname.message-changed", placeholders);

                            sender.displayName(component);
                        })
        );
    }
}
