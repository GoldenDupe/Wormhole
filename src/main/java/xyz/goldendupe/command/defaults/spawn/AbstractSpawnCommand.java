package xyz.goldendupe.command.defaults.spawn;

import bet.astral.messenger.placeholder.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.internal.legacy.GDCommand;
import xyz.goldendupe.command.internal.legacy.GDCommandInfo;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.models.GDSpawn;

import java.util.*;

@GDCommandInfo.DoNotReflect
public class AbstractSpawnCommand extends GDCommand {
	public static List<String> spawns = new LinkedList<>();
	private Map<GDCommandInfo.MemberType, Long> cooldowns = new HashMap<>();
	private final String spawn;
	protected AbstractSpawnCommand(GoldenDupe goldenDupe, GDCommandInfo commandInfo, String spawn) {
		super(goldenDupe, commandInfo);
		spawns.add(spawn);
		cooldowns.put(GDCommandInfo.MemberType.DEFAULT, commandInfo.configSection.getLong("teleport-time.default"));
		cooldowns.put(GDCommandInfo.MemberType.DONATOR, commandInfo.configSection.getLong("teleport-time.donator", 0));
		cooldowns.put(GDCommandInfo.MemberType.MODERATOR, commandInfo.configSection.getLong("teleport-time.staff", 0));
		cooldowns.put(GDCommandInfo.MemberType.ADMINISTRATOR, commandInfo.configSection.getLong("teleport-time.admin", 0));
		cooldowns.put(GDCommandInfo.MemberType.OWNER, commandInfo.configSection.getLong("teleport-time.owner", 0));

		this.spawn = spawn;
	}

	@Override
	public void execute(@NotNull CommandSender sender, @NotNull String[] args, boolean hasArgs) { }

	@Override
	public void execute(@NotNull Player sender, @NotNull String[] args, boolean hasArgs) {
		GDPlayer player = goldenDupe.playerDatabase().fromPlayer(sender);
		GDSpawn oldTeleport = player.teleportingSpawn();

		GDSpawn spawn = goldenDupe.spawns().get(this.spawn.toLowerCase());
		if (!sender.hasPermission(spawn.permission())){
			commandMessenger.message(sender, commandInfo.name()+".message-no-teleport-permissions");
			return;
		}
		if (oldTeleport != null) {
			if (oldTeleport.equals(spawn)) {
				commandMessenger.message(sender, commandInfo.name()+".message-already-teleporting", new Placeholder("old", oldTeleport.name()), new Placeholder("new", spawn.name()));
			} else {
				commandMessenger.message(sender, commandInfo.name()+".message-cancel-teleport-rewrite", new Placeholder("old", oldTeleport.name()), new Placeholder("new", spawn.name()));
			}
			return;
		}
		commandMessenger.message(sender, commandInfo.name()+".message-teleporting");
		player.setTeleportingSpawn(spawn);
		Long cooldown = cooldowns.get(GDCommandInfo.MemberType.of(sender));
		if (cooldown == null){
			cooldown = 0L;
		}
		player.setTeleportSpawnCooldown(System.currentTimeMillis()+cooldown);
		Location location = neutralize(player.player().getLocation());
		goldenDupe.getServer().getScheduler().runTaskTimer(goldenDupe, (task)->{
			if (neutralize(player.player().getLocation()).distanceSquared(location)>0.3){
				commandMessenger.message(sender, commandInfo.name()+".message-moved", new Placeholder("old", spawn.name()), new Placeholder("new", spawn.name()));
				player.setTeleportSpawnCooldown(0);
				player.setTeleportingSpawn(null);
				return;
			}
			GDSpawn spawnTo = player.teleportingSpawn();
			if (!spawnTo.equals(spawn)){
				task.cancel();
			}
			long left = System.currentTimeMillis()- player.teleportSpawnCooldown();
			if (left>0){
				return;
			}
			Location spawnLoc = new Location(Bukkit.getWorld(spawn.world()), spawn.x(), spawn.y(), spawn.z(), spawn.yaw(), spawn.pitch());
			sender.teleportAsync(spawnLoc, PlayerTeleportEvent.TeleportCause.COMMAND);
			player.setTeleportSpawnCooldown(0);
			player.setTeleportingSpawn(null);
			task.cancel();
			}, 0, 5);
	}

	private Location neutralize(Location location){
		Location loc = location.clone();
		loc.setYaw(0);
		loc.setPitch(0);
		return loc;
	}

	@Override
	public List<String> tab(@NotNull CommandSender sender, @NotNull String[] args, boolean hasArgs) { return null; }

	@Override
	public List<String> tab(@NotNull Player sender, @NotNull String[] args, boolean hasArgs) {
		return Collections.emptyList();
	}
}
