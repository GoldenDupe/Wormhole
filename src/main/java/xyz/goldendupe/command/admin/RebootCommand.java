package xyz.goldendupe.command.admin;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.Placeholder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitTask;
import org.incendo.cloud.Command;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.command.defaults.UptimeCommand;
import xyz.goldendupe.utils.MemberType;
import xyz.goldendupe.utils.Timer;

@Cloud
public class RebootCommand extends GDCloudCommand {

    final long timeForRestart = 60000;
    int stage = 0;
    final Timer rebootTimer = new Timer();
    BukkitTask task = null;

    public RebootCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
        super(goldenDupe, commandManager);

        Command.Builder<CommandSender> rebootCommand = commandManager
                .commandBuilder(
                        "reboot",
                        Description.of("Restarts the server."),
                        "rb")
                .permission(MemberType.ADMINISTRATOR.cloudOf("reboot"))
                .handler(context -> {

                    long timeUntilRestart = timeForRestart - rebootTimer.get();

                    if (task != null) {
                        commandMessenger.message(context.sender(), "reboot.server-already-restarting",
                                new Placeholder("until-restart", UptimeCommand.convertMillisToTimeString(timeUntilRestart)));
                        return;
                    }

                    commandMessenger.message(context.sender(), "reboot.server-restarting",
                            new Placeholder("until-restart", UptimeCommand.convertMillisToTimeString(timeUntilRestart)));

                    rebootTimer.reset();

                    //This shit is ugly I hope it works
                    task = Bukkit.getServer().getScheduler().runTaskTimer(plugin, () -> {

                        Component comp = Component.text("Server Restarting in " +
                                UptimeCommand.convertMillisToTimeString(timeForRestart - rebootTimer.get()) + "!");

                        if (stage <= 0) {
                            Bukkit.broadcast(Component.text("Server Restarting in " +
                                    UptimeCommand.convertMillisToTimeString((timeForRestart + 1000) - rebootTimer.get()) + "!"));
                            stage++;
                        } else if (stage == 1 && rebootTimer.hasReached((timeForRestart + 1000) / 4)) {
                            Bukkit.broadcast(Component.text("Server Restarting in " +
                                    UptimeCommand.convertMillisToTimeString((timeForRestart + 1000) - rebootTimer.get()) + "!"));
                            stage++;
                        } else if (stage == 2 && rebootTimer.hasReached((timeForRestart + 2000) / 2)) {
                            Bukkit.broadcast(Component.text("Server Restarting in " +
                                    UptimeCommand.convertMillisToTimeString((timeForRestart + 2000) - rebootTimer.get()) + "!"));
                            stage++;
                        } else if (stage == 3 && rebootTimer.hasReached((long) ((timeForRestart + 2000) / 1.1f))) {
                            Bukkit.broadcast(Component.text("Server Restarting in " +
                                    UptimeCommand.convertMillisToTimeString((timeForRestart + 2000) - rebootTimer.get()) + "!"));
                            stage++;
                        } else if (rebootTimer.hasReached(timeForRestart + 3000)) {
                            Bukkit.broadcast(Component.text("Server Restarting!"));
                            rebootTimer.reset();
                            goldenDupe.onRestart();
                            Bukkit.spigot().restart();
                        }

                    }, 0, 20);

                });

        commandManager.command(rebootCommand);

        commandManager.command(rebootCommand
                .literal("cancel")
                .handler(context -> {

                    if (task == null) {
                        commandMessenger.message(context.sender(), "reboot.server-not-restarting");
                        return;
                    }

                    stage = 0;
                    rebootTimer.reset();
                    task.cancel();
                    task = null;
                    commandMessenger.message(context.sender(), "reboot.restart-aborted");

                })
        );

    }

}
