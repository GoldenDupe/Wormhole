package xyz.goldendupe.command.defaults;

import bet.astral.messenger.placeholder.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.incendo.cloud.Command;
import org.incendo.cloud.bukkit.parser.PlayerParser;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.models.GDSpawn;
import xyz.goldendupe.utils.MemberType;

import java.util.HashMap;
import java.util.Map;

@Cloud
public class SpawnCommand extends GDCloudCommand {
	private static Map<MemberType, Long> cooldowns = new HashMap<>();

	public SpawnCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
		super(goldenDupe, commandManager);
		cooldowns.put(MemberType.DEFAULT, 2500L);
		cooldowns.put(MemberType.DONATOR, 1750L);
		cooldowns.put(MemberType.MODERATOR, 1750L);
		cooldowns.put(MemberType.ADMINISTRATOR, 1000L);

		abstractSpawn("overworld", "spawn");
		abstractSpawn("nether", "nether");
		abstractSpawn("end", "end");
	}

	private void abstractSpawn(String spawn, String name) {
		Command.Builder<Player> commandBuilder = commandManager.commandBuilder(
						name,
						Description.of("Allows player to teleport to " + name)
				)
				.senderType(Player.class)
				.handler(context -> {
					Player sender = context.sender();
					GDPlayer player = goldenDupe.playerDatabase().fromPlayer(sender);
					GDSpawn oldTeleport = player.teleportingSpawn();

					GDSpawn newSpawn = goldenDupe.getSpawn(spawn);
					if (!sender.hasPermission(newSpawn.permission())) {
						commandMessenger.message(sender, name + ".message-no-teleport-permissions");
						return;
					}

					if (oldTeleport != null) {
						if (oldTeleport.equals(newSpawn)) {
							commandMessenger.message(sender, name + ".message-already-teleporting", new Placeholder("old", oldTeleport.name()), new Placeholder("new", newSpawn.name()));
						} else {
							commandMessenger.message(sender, name + ".message-cancel-teleport-rewrite", new Placeholder("old", oldTeleport.name()), new Placeholder("new", newSpawn.name()));
						}
						return;
					}

					commandMessenger.message(sender, name + ".message-teleporting");
					player.setTeleportingSpawn(newSpawn);
					Long cooldown = cooldowns.get(MemberType.of(sender));
					if (cooldown == null) {
						cooldown = 0L;
					}
					player.setTeleportSpawnCooldown(System.currentTimeMillis() + cooldown);
					Location location = neutralize(player.player().getLocation());
					goldenDupe.getServer().getScheduler().runTaskTimer(goldenDupe, (task) -> {
						if (neutralize(player.player().getLocation()).distanceSquared(location) > 0.3) {
							commandMessenger.message(sender, name + ".message-moved", new Placeholder("old", newSpawn.name()), new Placeholder("new", newSpawn.name()));
							player.setTeleportSpawnCooldown(0);
							player.setTeleportingSpawn(null);
							return;
						}
						GDSpawn spawnTo = player.teleportingSpawn();
						if (!spawnTo.equals(newSpawn)) {
							task.cancel();
						}
						long left = System.currentTimeMillis() - player.teleportSpawnCooldown();
						if (left > 0) {
							return;
						}
						Location spawnLoc = new Location(Bukkit.getWorld(newSpawn.world()), newSpawn.x(), newSpawn.y(), newSpawn.z(), newSpawn.yaw(), newSpawn.pitch());
						sender.teleportAsync(spawnLoc, PlayerTeleportEvent.TeleportCause.COMMAND);
						player.setTeleportSpawnCooldown(0);
						player.setTeleportingSpawn(null);
						commandMessenger.message(sender, name + ".message-teleported", new Placeholder("old", newSpawn.name()), new Placeholder("new", newSpawn.name()));
						task.cancel();
					}, 0, 5);

				});
		commandManager.command(commandBuilder);

		commandManager.command(
				commandBuilder
						.permission(MemberType.ADMINISTRATOR.cloudOf("spawn.teleport-others"))
						.argument(
								PlayerParser.playerComponent().name("who-to-teleport"))
						.handler(context -> {
							Player sender = context.sender();
							Player whoToTeleport = context.sender();
							GDSpawn newSpawn = goldenDupe.getSpawn(name);

							whoToTeleport.teleportAsync(newSpawn.asLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);
							commandMessenger.message(sender, name + ".message-admin-teleport", new Placeholder("new", newSpawn.name()), new Placeholder("player", whoToTeleport.name()));
							commandMessenger.message(sender, name + ".message-teleported", new Placeholder("new", newSpawn.name()));
						})
		);
	}

	private Location neutralize(Location location) {
		Location loc = location.clone();
		loc.setYaw(0);
		loc.setPitch(0);
		return loc;
	}
}