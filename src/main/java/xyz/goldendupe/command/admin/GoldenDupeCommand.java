package xyz.goldendupe.command.admin;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.unity.libs.bet.astral.messenger.message.message.IMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.Command;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.utils.MemberType;

import java.lang.reflect.Field;
import java.util.Map;

@Cloud
public class GoldenDupeCommand extends GDCloudCommand {
	public GoldenDupeCommand(GoldenDupeBootstrap bootstrap, PaperCommandManager<CommandSender> commandManager) {
		super(bootstrap, commandManager);
		Command.Builder<CommandSender> builder = commandManager.commandBuilder("goldendupe",
				Description.of("GoldenDupe is known for it's shitty community."))
				.handler(context->{
					context.sender().sendMessage("Hello goldenwupe!");
				});
		command(builder);
		command(builder.literal("reload")
				.permission(MemberType.OWNER.permissionOf("goldendupe.reload"))
				.handler(context->{
					for (int i = 0; i < 3; i++){
						Bukkit.broadcast(MiniMessage.miniMessage().deserialize(
								"<gold><bold>Golden<white>Dupe<reset> <red>GoldenDupe will be reloading! Please wait as it might lag for a bit!"
						));
					}
					goldenDupe().getServer().getScheduler().runTaskLaterAsynchronously(goldenDupe(),
							()->{
								try {

									goldenDupe().reloadConfig();
									goldenDupe().reloadMessengers();
									goldenDupe().getGlobalData().reload();
								} catch (Exception e) {
									Bukkit.broadcast(MiniMessage.miniMessage().deserialize(
											"<gold><bold>Golden<white>Dupe<reset> <red>GoldenDupe failed to reload the server! Check console for the error! A server restart is required!"
									));
									e.printStackTrace();
									return;
								}

								Bukkit.broadcast(MiniMessage.miniMessage().deserialize(
										"<gold><bold>Golden<white>Dupe<reset> <green>GoldenDupe has reloaded successfully!"
								));
							}, 20);
				})
		);
	}
}
