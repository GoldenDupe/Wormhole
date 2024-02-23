package xyz.goldendupe.command.internal.legacy;

import xyz.goldendupe.command.CommandFinder;
import xyz.goldendupe.messenger.GoldenMessenger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupe;

import java.util.List;

@SuppressWarnings("removal")
@Deprecated(forRemoval = true)
@GDCommandInfo.DoNotReflect
public abstract class GDCommand implements CommandFinder, MessageReload {
	public boolean isDisabled = false;
	protected final GoldenDupe goldenDupe;
	protected GoldenMessenger commandMessenger;
	protected GoldenMessenger debugMessenger;
	protected final GDCommandInfo commandInfo;
	protected GDCommand(GoldenDupe goldenDupe, GDCommandInfo commandInfo){
		this.goldenDupe = goldenDupe;
		this.commandInfo = commandInfo;
		reloadMessengers();
	}

	@Override
	public void reloadMessengers() {
		commandMessenger = goldenDupe.commandMessenger();
		debugMessenger = goldenDupe.debugMessenger();
	}


	public abstract void execute(@NotNull CommandSender sender, @NotNull String[] args, boolean hasArgs);
	public abstract void execute(@NotNull Player sender, @NotNull String[] args, boolean hasArgs);

	public abstract List<String> tab(@NotNull CommandSender sender, @NotNull String[] args, boolean hasArgs);
	public abstract List<String> tab(@NotNull Player sender, @NotNull String[] args, boolean hasArgs);


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
		return asInt(args, i) != null;
	}

	public boolean isDouble(String[] args, int i){
		return asDouble(args, i) != null;
	}

	public Integer asInt(String[] args, int i) {
		try {
			return Integer.parseInt(args[i]);
		} catch (NumberFormatException | IndexOutOfBoundsException e) {
			return null;
		}
	}

	public Double asDouble(String[] args, int i) {
		try {
			return Double.parseDouble(args[i]); // Parse and return the double value directly
		} catch (NumberFormatException | IndexOutOfBoundsException e) {
			return null;
		}
	}
}