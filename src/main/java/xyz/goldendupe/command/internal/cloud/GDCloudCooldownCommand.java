package xyz.goldendupe.command.internal.cloud;

import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.internal.legacy.GDCommandInfo;

import java.util.HashMap;
import java.util.Map;

@GDCommandInfo.DoNotReflect
public class GDCloudCooldownCommand extends GDCloudCommand implements Cooldown, Listener {
	private final Map<CommandSender, Long> cooldowns = new HashMap<>();
	private final long cooldown;

	public GDCloudCooldownCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager, long cooldown) {
		super(goldenDupe, commandManager);
		this.cooldown = cooldown;
		goldenDupe.getServer().getPluginManager().registerEvents(this, goldenDupe);
	}

	@Override
	public void setCooldown(CommandSender sender) {
		cooldowns.put(sender, System.currentTimeMillis()+cooldown);
	}

	@Override
	public void resetCooldown(CommandSender sender) {
		cooldowns.remove(sender);
	}

	@Override
	public long getCooldownLeft(CommandSender sender) {
		long left = (cooldowns.get(sender) != null ? cooldowns.get(sender) : 0) - System.currentTimeMillis();
		if (left<0){
			left = 0;
		}
		if (left==0){
			resetCooldown(sender);
		}
		return left;
	}

	@Override
	public boolean hasCooldown(CommandSender sender) {
		return getCooldownLeft(sender)>0;
	}

	@EventHandler
	private void onPlayerLeave(PlayerQuitEvent event){
		cooldowns.remove(event.getPlayer());
	}
}
