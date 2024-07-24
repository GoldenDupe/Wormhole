package xyz.goldendupe.command.defaults;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.placeholder.Placeholder;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class UptimeCommand extends GDCloudCommand {

    public UptimeCommand(GoldenDupeCommandRegister register, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
        super(register, commandManager);

        commandManager.command(
                commandManager.commandBuilder(
                                "uptime",
                                Description.of("Tells the player how long the server has been running."),
                                "ut", "runtime"
                        )
                        .permission(MemberType.DEFAULT.permissionOf("uptime"))
                        .handler(context -> {
                            long runtimeMillis = goldenDupe().getStartTimer().get();

                            messenger.message(context.sender(), Translations.COMMAND_UPTIME,
                                    Placeholder.of("time", convertMillisToTimeString(runtimeMillis)));
                        })
        );

    }

    public static @NotNull String convertMillisToTimeString(@Range(from = 0, to = Long.MAX_VALUE) long millis) {
        millis = Math.abs(millis);

        final long seconds = millis / 1000;
        final long minutes = seconds / 60;
        final long hours = minutes / 60;
        final long days = hours / 24;

        StringBuilder result = new StringBuilder();

        if (days > 0) {
            result.append(days).append(" day");
            if (days != 1) result.append("s");
            if (hours % 24 > 0) result.append(", ");
            else if (minutes % 60 > 0) result.append(", ");
        }

        if (hours % 24 > 0) {
            result.append(hours % 24).append(" hour");
            if (hours % 24 != 1) result.append("s");
            if (minutes % 60 > 0) result.append(", ");
        }

        if (minutes % 60 > 0) {
            result.append(minutes % 60).append(" minute");
            if (minutes % 60 != 1) result.append("s");
            if (seconds % 60 > 0) result.append(" and ");
        }

        if (seconds % 60 > 0 || (millis < 1000 && millis > 0)) {
            result.append(seconds % 60).append(" second");
            if (seconds % 60 != 1) result.append("s");
        }

        return result.toString();
    }

}
