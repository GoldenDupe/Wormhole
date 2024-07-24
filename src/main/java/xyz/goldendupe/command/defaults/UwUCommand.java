package xyz.goldendupe.command.defaults;

import bet.astral.cloudplusplus.annotations.Cloud;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.incendo.cloud.minecraft.extras.RichDescription;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.listeners.ChatFormatListener;
import xyz.goldendupe.listeners.ChatUwUListener;
import xyz.goldendupe.models.GDChat;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.models.chatcolor.Color;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class UwUCommand extends GDCloudCommand {
	public UwUCommand(GoldenDupeCommandRegister register, PaperCommandManager<CommandSender> commandManager) {
		super(register, commandManager);
		commandManager.command(commandManager.commandBuilder("uwu", RichDescription.of(Component.text("GoldenDupe? But UwU?", Color.YELLOW, TextDecoration.OBFUSCATED)))
						.permission(MemberType.DEFAULT.permissionOf("uwu"))
				.handler(context->{
					CommandSender sender = context.sender();
					Component uwu = ChatUwUListener.uwuString();
					if (sender instanceof Player player){
						GDPlayer gdPlayer = goldenDupe().playerDatabase().fromPlayer(player);
						GDChat chat = gdPlayer.chat();
						for (Player audience : Bukkit.getOnlinePlayers()){
							audience.sendMessage(ChatFormatListener.format(player, audience, chat, uwu));
						}
						goldenDupe().getServer().getConsoleSender().sendMessage(
								ChatFormatListener.format(player, goldenDupe().getServer().getConsoleSender(), chat, uwu));
					} else {
						BukkitCommand.broadcastCommandMessage(sender, uwu);
					}
				})
		);
	}
}
