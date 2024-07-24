package xyz.goldendupe.command.defaults;

import bet.astral.cloudplusplus.annotations.Cloud;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.bukkit.parser.PlayerParser;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.listeners.ChatFormatListener;
import xyz.goldendupe.models.GDChat;
import xyz.goldendupe.models.chatcolor.Color;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class WelcomeCommand extends GDCloudCommand {
	public WelcomeCommand(GoldenDupeCommandRegister bootstrap, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(bootstrap, commandManager);
		commandPlayer(commandManager.commandBuilder("welcome")
				.argument(PlayerParser.playerComponent().name("who"))
				.senderType(Player.class)
				.permission(MemberType.DEFAULT.permissionOf("welcome"))
				.handler(context -> {
					Bukkit.broadcast(
							ChatFormatListener.format(context.sender(), context.get("who"), GDChat.GLOBAL, ((Player) context.get("who")).name().append(Component.text(" welcome to ").append(Component.text("Golden", Color.GOLD, TextDecoration.BOLD)).append(Component.text("Dupe", Color.WHITE, TextDecoration.BOLD)).append(Component.text("!")))));
				})
		);
	}
}