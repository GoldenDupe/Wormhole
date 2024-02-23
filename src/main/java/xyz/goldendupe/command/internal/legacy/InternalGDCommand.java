package xyz.goldendupe.command.internal.legacy;

import bet.astral.messenger.placeholder.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupe;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("removal")
@Deprecated()
public class InternalGDCommand extends Command {
	private final GoldenDupe goldenDupe;
	private final GDCommand command;
	private final GDCommandInfo commandInfo;

	public InternalGDCommand(@NotNull GDCommand command) {
		super(command.commandInfo.name());
		this.goldenDupe = command.goldenDupe;
		this.command = command;
		this.commandInfo = command.commandInfo;
		permissionMessage(commandInfo.permissionMessage());
		setUsage(commandInfo.usage());
		setAliases(commandInfo.aliases());
	}


	@Override
	public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
		if (!sender.hasPermission(commandInfo.permission())){
			sender.sendMessage(commandInfo.permissionMessage());
			return true;
		}
		switch (commandInfo.senderType()){
			case PLAYER -> {
				if (!(sender instanceof Player)){
					command.commandMessenger.message(sender, commandInfo.cannotUseMessageKey(), true, new Placeholder("type", commandInfo.senderType().what()));
					return true;
				}
			}
			case CONSOLE -> {
				if (!(sender instanceof ConsoleCommandSender)){
					command.commandMessenger.message(sender, commandInfo.cannotUseMessageKey(), true, new Placeholder("type", commandInfo.senderType().what()));
					return true;
				}
			}
		}
		if (commandInfo.cooldown()>0){
			if (commandInfo.hasCooldown(sender)){
				command.commandMessenger.message(sender, commandInfo.cooldownMessageKey(), true, command.commandMessenger.createCooldownPlaceholders(commandInfo.getCooldown(sender)));
				return true;
			}
		}
		if (args.length< commandInfo.minArguments()){
			sender.sendMessage(getUsage());
			return true;
		}
		if (commandInfo.senderType() == GDCommandInfo.SenderType.PLAYER){
			command.execute((Player) sender, args, args.length!=0);
		} else {
			command.execute(sender, args, args.length!=0);
		}
		if (commandInfo.cooldown()>0) {
			commandInfo.setCooldown(sender);
		}
		return true;
	}

	@Override
	public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
		if (!sender.hasPermission(commandInfo.permission())) {
			return Collections.emptyList();
		}
		if (commandInfo.senderType()== GDCommandInfo.SenderType.PLAYER && sender instanceof Player player){
			return command.tab(player, args, args.length>0);
		} else if (commandInfo.senderType()==GDCommandInfo.SenderType.CONSOLE && sender instanceof ConsoleCommandSender){
			return super.tabComplete(sender, alias, args);
		} else if (commandInfo.senderType() == GDCommandInfo.SenderType.ALL) {
			return super.tabComplete(sender, alias, args);
		}
		return Collections.emptyList();
	}
}
