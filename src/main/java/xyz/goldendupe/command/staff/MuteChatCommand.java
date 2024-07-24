package xyz.goldendupe.command.staff;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.permission.Permission;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.Command;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.models.GDSettings;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class MuteChatCommand extends GDCloudCommand {
	public MuteChatCommand(GoldenDupeCommandRegister register, PaperCommandManager<CommandSender> commandManager) {
		super(register, commandManager);
		Command.Builder<CommandSender> command =commandManager.commandBuilder("mutechat", Description.of(""), "mc")
				.permission(MemberType.MODERATOR.permissionOf("mutechat"))
				.handler(context->{
					GDSettings data = goldenDupe().getSettings();
					if (data.isGlobalChatMute()){
						data.setGlobalChatMute(false);
						data.setGlobalChatMuteAllowedUsers(GDSettings.AllowedUsers.ALL);
						messenger.broadcast(Translations.COMMAND_MUTECHAT_UNMUTED);
					} else {
						GDSettings.AllowedUsers users = GDSettings.AllowedUsers.STAFF;
						data.setGlobalChatMute(false);
						data.setGlobalChatMuteAllowedUsers(users);
						messenger.broadcast(Translations.COMMAND_MUTECHAT_MUTED);
						messenger.broadcast(Permission.of(users.getBypass()), Translations.COMMAND_MUTECHAT_BYPASS);
					}
				});
		command(command);
		command(abstractCommand(command, GDSettings.AllowedUsers.DONATOR));
		command(abstractCommand(command, GDSettings.AllowedUsers.STAFF));
		command(abstractCommand(command, GDSettings.AllowedUsers.ADMIN));
		command(abstractCommand(command, GDSettings.AllowedUsers.OWNER));
	}

	private Command.Builder<CommandSender> abstractCommand(Command.Builder<CommandSender> builder, @NotNull GDSettings.AllowedUsers users){
		return builder
				.literal(users.name().toLowerCase())
				.permission(users.getCommand())
				.handler(context->{
					GDSettings data = goldenDupe().getSettings();
					if (data.isGlobalChatMute()){
						data.setGlobalChatMute(false);
						data.setGlobalChatMuteAllowedUsers(GDSettings.AllowedUsers.ALL);
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
