package xyz.goldendupe.command;

import bet.astral.goldenmessenger.GoldenMessenger;
import bet.astral.messagemanager.placeholder.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupe;

import java.util.Collections;
import java.util.List;

@GDCommandInfo.DoNotReflect
public abstract class GDCommand extends Command {
	public boolean isDisabled = false;
	protected final GoldenDupe goldenDupe;
	protected GoldenMessenger commandMessenger;
	protected GoldenMessenger debugMessenger;
	protected final GDCommandInfo commandInfo;
	protected GDCommand(GoldenDupe goldenDupe, GDCommandInfo commandInfo){
		super(commandInfo.name());
		this.goldenDupe = goldenDupe;
		this.commandInfo = commandInfo;
		permissionMessage(commandInfo.permissionMessage());
		setUsage(commandInfo.usage());
		setAliases(commandInfo.aliases());
	}

	public void reloadMessengers() {
		commandMessenger = goldenDupe.commandMessenger();
		debugMessenger = goldenDupe.debugMessenger();
	}


	public abstract void execute(@NotNull CommandSender sender, @NotNull String[] args, boolean hasArgs);
	public abstract void execute(@NotNull Player sender, @NotNull String[] args, boolean hasArgs);

	public abstract List<String> tab(@NotNull CommandSender sender, @NotNull String[] args, boolean hasArgs);
	public abstract List<String> tab(@NotNull Player sender, @NotNull String[] args, boolean hasArgs);


	@Override
	public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
		if (!sender.hasPermission(commandInfo.permission())){
			sender.sendMessage(commandInfo.permissionMessage());
			return true;
		}
		switch (commandInfo.senderType()){
			case PLAYER -> {
				if (!(sender instanceof Player)){
					commandMessenger.message(sender, commandInfo.cannotUseMessageKey(), true, new Placeholder("type", commandInfo.senderType().what()));
					return true;
				}
			}
			case CONSOLE -> {
				if (!(sender instanceof ConsoleCommandSender)){
					commandMessenger.message(sender, commandInfo.cannotUseMessageKey(), true, new Placeholder("type", commandInfo.senderType().what()));
					return true;
				}
			}
		}
		if (commandInfo.cooldown()>0){
			if (commandInfo.hasCooldown(sender)){
				commandMessenger.message(sender, commandInfo.cooldownMessageKey(), true, commandMessenger.createCooldownPlaceholders(commandInfo.getCooldown(sender)));
				return true;
			}
		}
		if (args.length< commandInfo.minArguments()){
			sender.sendMessage(getUsage());
			return true;
		}
		if (commandInfo.senderType() == GDCommandInfo.SenderType.PLAYER){
			execute((Player) sender, args, args.length!=0);
		} else {
			execute(sender, args, args.length!=0);
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
		return super.tabComplete(sender, alias, args);
	}

	public String asString(String[] args, int from){
		StringBuilder builder = new StringBuilder();
		for (int i = from; i < args.length; i++){
			try {
				if (!builder.isEmpty()) {
					builder.append(" ");
				}
				builder.append(args[i]);
			} catch (IndexOutOfBoundsException ignore){
				return builder.toString();
			}
		}
		return builder.toString();
	}

	public boolean is(String[] args, int i, String... anyOf){
		//
		// args[0] = length 1
		// args[1] = length 2
		// args[2] = length 3
		//
		try {
			if (args.length<i+1){
				return true;
			}
			for (String arg : anyOf) {
				if (arg == null){
					continue;
				}
				if (args[i].equalsIgnoreCase(arg)){
					return true;
				}
			}
		} catch (IndexOutOfBoundsException ignore) {
			return false;
		}
		return false;
	}


	public boolean isInt(String[] args, int i){
		try {
			if (args.length<i+1){
				return true;
			}
			try {
				Integer.parseInt(args[i]);
				return true;
			} catch (NumberFormatException e){
				return false;
			}
		} catch (IndexOutOfBoundsException ignore) {
			return false;
		}
	}

	public boolean isDouble(String[] args, int i){
		try {
			if (args.length<i+1){
				return true;
			}
			try {
				Double.parseDouble(args[i]);
				return true;
			} catch (NumberFormatException e){
				return false;
			}
		} catch (IndexOutOfBoundsException ignore) {
			return false;
		}
	}

	public int asInt(String[] args, int i){
		try {
			if (args.length<i+1){
				return 0;
			}
			try {
				Integer.parseInt(args[i]);
				return i;
			} catch (NumberFormatException e){
				return 0;
			}
		} catch (IndexOutOfBoundsException ignore) {
			return 0;
 		}
	}
	public int asDouble(String[] args, int i){
		try {
			if (args.length<i+1){
				return 0;
			}
			try {
				Integer.parseInt(args[i]);
				return i;
			} catch (NumberFormatException e){
				return 0;
			}
		} catch (IndexOutOfBoundsException ignore) {
			return 0;
		}
	}

}