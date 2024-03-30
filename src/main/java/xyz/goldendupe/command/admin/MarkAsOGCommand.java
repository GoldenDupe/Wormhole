package xyz.goldendupe.command.admin;

import bet.astral.cloudplusplus.annotations.Cloud;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.MetaNode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.bukkit.parser.OfflinePlayerParser;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.EnumParser;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.utils.MemberType;
import xyz.goldendupe.utils.OriginalMemberType;

@Cloud
public class MarkAsOGCommand extends GDCloudCommand {
	public static final String OG_ROLE_KEY = "original-role";
	public MarkAsOGCommand(GoldenDupe plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		command(
				commandBuilder("mark-as-og")
						.commandDescription(Description.of(""))
						.permission(MemberType.OWNER.permissionOf("mark-as-og"))
						.required(
								OfflinePlayerParser
										.offlinePlayerComponent()
										.name("who")
						)
						.required(
								EnumParser.enumComponent(MemberType.class)
										.name("old-role")
						)
						.handler(context->{
							CommandSender sender = context.sender();
							OfflinePlayer who = context.get("who");
							OriginalMemberType originalMemberType = context.get(OG_ROLE_KEY);

							LuckPerms luckPerms = plugin.luckPerms();
							User user = luckPerms.getUserManager().getUser(who.getUniqueId());

							if (user == null){
								sender.sendMessage("Couldn't find luck perms user for "+ who.getName());
								return;
							}
							MetaNode roleNode = MetaNode.builder("original-role", originalMemberType.name()).build();
							user.data().add(roleNode);
							luckPerms.getUserManager().saveUser(user);

							sender.sendMessage("Marked "+ who.getName()+ " as original player. (OG ROLE: "+ originalMemberType.name()+")");
						})
		);
	}
}
