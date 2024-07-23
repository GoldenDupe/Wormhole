package xyz.goldendupe.command.staff;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.permission.Permission;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.Command;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.models.GDGlobalData;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class MuteChatCommand extends GDCloudCommand {
	public MuteChatCommand(GoldenDupeBootstrap registerer, PaperCommandManager<CommandSender> commandManager) {
		super(registerer, commandManager);
		Command.Builder<CommandSender> command =commandManager.commandBuilder("mutechat", Description.of(""), "mc")
				.permission(MemberType.MODERATOR.permissionOf("mutechat"))
				.handler(context->{
					GDGlobalData data = goldenDupe().getGlobalData();
					if (data.isGlobalChatMute()){
						data.setGlobalChatMute(false);
						data.setGlobalChatMuteAllowedUsers(GDGlobalData.AllowedUsers.ALL);
						messenger.broadcast(Translations.COMMAND_MUTECHAT_UNMUTED);
					} else {
						GDGlobalData.AllowedUsers users = GDGlobalData.AllowedUsers.STAFF;
						data.setGlobalChatMute(false);
						data.setGlobalChatMuteAllowedUsers(users);
						messenger.broadcast(Translations.COMMAND_MUTECHAT_MUTED);
						messenger.broadcast(Permission.of(users.getBypass()), Translations.COMMAND_MUTECHAT_BYPASS);
					}
				});
		command(command);
		command(abstractCommand(command, GDGlobalData.AllowedUsers.DONATOR));
		command(abstractCommand(command, GDGlobalData.AllowedUsers.STAFF));
		command(abstractCommand(command, GDGlobalData.AllowedUsers.ADMIN));
		command(abstractCommand(command, GDGlobalData.AllowedUsers.OWNER));
	}

	private Command.Builder<CommandSender> abstractCommand(Command.Builder<CommandSender> builder, @NotNull GDGlobalData.AllowedUsers users){
		return builder
				.literal(users.name().toLowerCase())
				.permission(users.getCommand())
				.handler(context->{
					GDGlobalData data = goldenDupe().getGlobalData();
					if (data.isGlobalChatMute()){
						data.setGlobalChatMute(false);
						data.setGlobalChatMuteAllowedUsers(GDGlobalData.AllowedUsers.ALL);
						messenger.broadcast(Translations.COMMAND_MUTECHAT_UNMUTED);
					} else {
						data.setGlobalChatMute(false);
						data.setGlobalChatMuteAllowedUsers(users);
						messenger.broadcast(Translations.COMMAND_MUTECHAT_MUTED);
						messenger.broadcast(Permission.of(users.getBypass()), Translations.COMMAND_MUTECHAT_BYPASS);
					}
				});
	}
}
