package xyz.goldendupe.command.donator.basic;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.messenger.v2.placeholder.PlaceholderList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class NickCommand extends GDCloudCommand {
    public NickCommand(GoldenDupeCommandRegister register, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
        super(register, commandManager);
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
                            String colorless = PlainTextComponentSerializer.plainText().serialize(LegacyComponentSerializer.legacyAmpersand().deserialize(nickname));
                            if (colorless.length()>16){
                                messenger.message(sender, Translations.COMMAND_NICKNAME_TOO_LONG, Placeholder.of("nickname", colorless), Placeholder.of("length", colorless.length()));
                                return;
                            }
                            if (!colorless.matches("[a-zA-Z0-9_-]")){
                                messenger.message(sender, Translations.COMMAND_NICKNAME_ILLEGAL, Placeholder.of("nickname", colorless), Placeholder.of("length", colorless.length()));
                                return;
                            }
                            Component component;
                            PlaceholderList placeholders = new PlaceholderList();
                            if (sender.hasPermission(MemberType.DONATOR.permissionOf("nick.color"))) {
                                component = LegacyComponentSerializer.legacyAmpersand().deserialize(nickname);
                            } else {
                                component = Component.text(colorless);
                            }
                            // Remove obfuscation, so people cannot have names others cannot read
                            component = component.decoration(TextDecoration.OBFUSCATED, false);
                            placeholders.add("nickname", component);

                            messenger.message(sender, Translations.COMMAND_NICKNAME_SUCCESS, placeholders);

                            sender.displayName(component);
                        })
        );
    }

}
