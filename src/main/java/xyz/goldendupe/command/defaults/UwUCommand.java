package xyz.goldendupe.command.defaults;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.incendo.cloud.minecraft.extras.RichDescription;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.listeners.ChatFormatListener;
import xyz.goldendupe.listeners.ChatUwUListener;
import xyz.goldendupe.models.chatcolor.Color;

@Cloud
public class UwUCommand extends GDCloudCommand {
	public UwUCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
		super(goldenDupe, commandManager);
		commandManager.command(commandManager.commandBuilder("uwu", RichDescription.of(Component.text("GoldenDupe? But UWU?", Color.MINECOIN, TextDecoration.OBFUSCATED)))
				.handler(context->{
					CommandSender sender = context.sender();
					Component uwu = ChatUwUListener.uwuString();
					if (sender instanceof Player player){
						for (Player audience : Bukkit.getOnlinePlayers()){
							audience.sendMessage(ChatFormatListener.format(player, audience, uwu));
						}
						goldenDupe.getServer().getConsoleSender().sendMessage(
								ChatFormatListener.format(player, goldenDupe.getServer().getConsoleSender(), uwu));
					} else {
						BukkitCommand.broadcastCommandMessage(sender, uwu);
					}
				})
		);
	}
}
