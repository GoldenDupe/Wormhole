package bet.astral.cloudplusplus;

import org.bukkit.command.CommandSender;

public interface Cooldown {
	void setCooldown(CommandSender sender);
	void resetCooldown(CommandSender sender);
	long getCooldownLeft(CommandSender sender);
	boolean hasCooldown(CommandSender sender);
}
