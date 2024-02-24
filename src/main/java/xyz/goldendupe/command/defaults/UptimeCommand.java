package xyz.goldendupe.command.defaults;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.cloud.GDCloudCommand;

@Cloud
public class UptimeCommand extends GDCloudCommand {

    public UptimeCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
        super(goldenDupe, commandManager);

        commandManager.command(
                commandManager.commandBuilder(
                                "uptime",
                                Description.of("Tells the player how long the server has been running."),
                                "ut", "runtime"
                        )
                        .senderType(Player.class)
                        .handler(context -> {
                            long runtimeMillis = System.currentTimeMillis() - goldenDupe.getStartTimeMillis();

                            commandMessenger.message(context.sender(), "uptime.message-uptime",
                                    new Placeholder("time", convertMillisToTimeString(runtimeMillis)));
                        })
        );

    }

    @NotNull String convertMillisToTimeString(@Range(from = 0, to = Long.MAX_VALUE) long millis) {
        millis = Math.abs(millis);

        final long seconds = millis / 1000;
        final long minutes = seconds / 60;
        final long hours = minutes / 60;
        final long days = hours / 24;

        StringBuilder result = new StringBuilder();

        if (days > 0) {
            result.append(days).append(" day");
            if (days != 1) result.append("s");
            result.append(", ");
        }

        if (hours % 24 > 0) {
            result.append(hours % 24).append(" hour");
            if (hours % 24 != 1) result.append("s");
            result.append(", ");
        }

        if (minutes % 60 > 0) {
            result.append(minutes % 60).append(" minute");
            if (minutes % 60 != 1) result.append("s");
            result.append(" and ");
        }

        if (seconds % 60 > 0 || (millis < 1000 && millis > 0)) {
            result.append(seconds % 60).append(" second");
            if (seconds % 60 != 1) result.append("s");
            result.append(" ");
        }

        result.append("(").append(millis).append(" millis)");

        return result.toString();
    }

}
