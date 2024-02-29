package xyz.goldendupe.command.donator;

import bet.astral.cloudplusplus.annotations.Cloud;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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
                                "nick",
                                Description.of("Allows a player to use a nickname.")
                        )
                        .permission(MemberType.DONATOR.cloudOf("nick"))
                        .argument(StringParser.stringComponent(StringParser.StringMode.SINGLE).name("nickname"))
                        .senderType(Player.class)
                        .handler(context -> {
                            Player sender = context.sender();
//                            String str = if they dont have perm -> LegacyComponentSerializer.legacyAmpersand().deserialize();
//                            sender.displayName(str);
                            //TODO make this tmr i went to sleep (or u make it)

                        })
        );
    }
}
