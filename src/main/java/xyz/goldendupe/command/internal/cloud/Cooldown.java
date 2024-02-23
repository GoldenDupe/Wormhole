package xyz.goldendupe.command.internal.cloud;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.goldendupe.command.internal.legacy.GDCommandInfo;

public interface Cooldown {
	void setCooldown(CommandSender sender);
	void resetCooldown(CommandSender sender);
	long getCooldownLeft(CommandSender sender);
	boolean hasCooldown(CommandSender sender);
}
