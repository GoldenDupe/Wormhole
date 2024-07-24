package xyz.goldendupe.command.admin;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.placeholder.Placeholder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitTask;
import org.incendo.cloud.Command;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.command.defaults.UptimeCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.utils.MemberType;
import xyz.goldendupe.utils.Timer;

@Cloud
public class RebootCommand extends GDCloudCommand {

    final long timeForRestart = 60000;
    int stage = 0;
    final Timer rebootTimer = new Timer();
    BukkitTask task = null;

    public RebootCommand(GoldenDupeCommandRegister register, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
        super(register, commandManager);
        Command.Builder<CommandSender> rebootCommand = commandManager
                .commandBuilder(
                        "reboot",
                        Description.of("Restarts the server."),
                        "rb")
                .permission(MemberType.ADMINISTRATOR.cloudOf("reboot"))
                .handler(context -> {

                    long timeUntilRestart = timeForRestart - rebootTimer.get();

                    if (task != null) {
                        messenger.message(context.sender(), Translations.COMMAND_REBOOT_ALREADY_RESTARTING,
                                Placeholder.of("until-restart", UptimeCommand.convertMillisToTimeString(timeUntilRestart)));
                        return;
                    }

                    messenger.message(context.sender(), Translations.COMMAND_REBOOT_RESTARTING,
                            Placeholder.of("until-restart", UptimeCommand.convertMillisToTimeString(timeUntilRestart)));

                    rebootTimer.reset();

                    //This shit is ugly I hope it works
                    task = Bukkit.getServer().getScheduler().runTaskTimer(goldenDupe(), () -> {

                        Component comp = Component.text("Server Restarting in " +
                                UptimeCommand.convertMillisToTimeString(timeForRestart - rebootTimer.get()) + "!");

                        if (stage <= 0) {
                            Bukkit.broadcast(Component.text("Server Restarting in " +
                                    UptimeCommand.convertMillisToTimeString((timeForRestart + 1000) - rebootTimer.get()) + "!", NamedTextColor.DARK_RED, TextDecoration.BOLD));
                            stage++;
                        } else if (stage == 1 && rebootTimer.hasReached((timeForRestart + 1000) / 4)) {
                            Bukkit.broadcast(Component.text("Server Restarting in " +
                                    UptimeCommand.convertMillisToTimeString((timeForRestart + 1000) - rebootTimer.get()) + "!", NamedTextColor.DARK_RED, TextDecoration.BOLD));
                            stage++;
                        } else if (stage == 2 && rebootTimer.hasReached((timeForRestart + 2000) / 2)) {
                            Bukkit.broadcast(Component.text("Server Restarting in " +
                                    UptimeCommand.convertMillisToTimeString((timeForRestart + 2000) - rebootTimer.get()) + "!", NamedTextColor.DARK_RED, TextDecoration.BOLD));
                            stage++;
                        } else if (stage == 3 && rebootTimer.hasReached((long) ((timeForRestart + 2000) / 1.1f))) {
                            Bukkit.broadcast(Component.text("Server Restarting in " +
                                    UptimeCommand.convertMillisToTimeString((timeForRestart + 2000) - rebootTimer.get()) + "!", NamedTextColor.DARK_RED, TextDecoration.BOLD));
                            stage++;
                        } else if (rebootTimer.hasReached(timeForRestart + 3000)) {
                            Bukkit.broadcast(Component.text("Server Restarting!"));
                            rebootTimer.reset();
                            goldenDupe().onRestart();
                            Bukkit.spigot().restart();
                        }

                    }, 0, 20);

                });

        commandManager.command(rebootCommand);

        commandManager.command(rebootCommand
                .literal("cancel")
                .handler(context -> {

                    if (task == null) {
                        messenger.message(context.sender(), Translations.COMMAND_REBOOT_NOT_RESTARTING);
                        return;
                    }

                    stage = 0;
                    rebootTimer.reset();
                    task.cancel();
                    task = null;
                    messenger.broadcast(Translations.COMMAND_REBOOT_ABORT);

                })
        );

    }

}
