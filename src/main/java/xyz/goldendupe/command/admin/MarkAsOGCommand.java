package xyz.goldendupe.command.admin;

import bet.astral.cloudplusplus.annotations.Cloud;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.bukkit.parser.OfflinePlayerParser;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.EnumParser;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.bootstrap.LuckPermsAfterBootstrap;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.utils.MemberType;
import xyz.goldendupe.utils.OriginalMemberType;

@Cloud
public class MarkAsOGCommand extends GDCloudCommand {
	public static final String OG_ROLE_KEY = "original-role";
	public MarkAsOGCommand(GoldenDupeCommandRegister register, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(register, commandManager);
		command(
				commandManager.commandBuilder("mark-as-og")
						.commandDescription(Description.of(""))
						.permission(MemberType.OWNER.permissionOf("mark-as-og"))
						.required(
								OfflinePlayerParser
										.offlinePlayerComponent()
										.name("who")
						)
						.required(
								EnumParser.enumComponent(OriginalMemberType.class)
										.name(OG_ROLE_KEY)
						)
						.handler(context->{
							CommandSender sender = context.sender();
							OfflinePlayer who = context.get("who");
							OriginalMemberType originalMemberType = context.get(OG_ROLE_KEY);

							LuckPermsAfterBootstrap.setMetadata(who.getUniqueId(), OG_ROLE_KEY, originalMemberType.name());
							sender.sendMessage("Marked "+ who.getName()+ " as original player. (OG ROLE: "+ originalMemberType.name()+")");
						})
		);
	}
}
