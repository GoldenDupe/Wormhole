package xyz.goldendupe.command.defaults;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.placeholder.Placeholder;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.incendo.cloud.Command;
import org.incendo.cloud.bukkit.parser.PlayerParser;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.models.impl.GDSpawn;
import xyz.goldendupe.utils.MemberType;
import xyz.goldendupe.utils.TimedTeleport;

import java.util.HashMap;
import java.util.Map;

@Cloud
public class SpawnCommand extends GDCloudCommand {
	private static final Map<MemberType, Integer> cooldowns = new HashMap<>();

	public SpawnCommand(GoldenDupeCommandRegister register, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(register, commandManager);
		cooldowns.put(MemberType.DEFAULT, 120);
		cooldowns.put(MemberType.DONATOR, 80);
		cooldowns.put(MemberType.OG, 60);
		cooldowns.put(MemberType.MODERATOR, 60);
		cooldowns.put(MemberType.ADMINISTRATOR, 20);

		for (Spawn spawn : Spawn.values()) {
			for (String name : spawn.names) {
				abstractSpawn(spawn.name, name);
			}
		}
	}

	private void abstractSpawn(String spawn, String name) {
		Command.Builder<Player> commandBuilder = commandManager.commandBuilder(
						name,
						Description.of("Allows player to teleport to " + name)
				)
				.senderType(Player.class)
				.optional(
						PlayerParser.playerComponent().name("who-to-teleport"))
				.handler(context -> {
					Player sender = context.sender();
					sender.sendMessage("Spawn command needs to be rewritten");
					/*
					if (context.optional("who-to-teleport").isPresent() && sender.hasPermission(MemberType.ADMINISTRATOR.permissionOf("spawn.teleport-others"))) {
						Player whoToTeleport = (Player) context.optional("who-to-teleport").orElse(null);
						if (whoToTeleport == null){
							return;
						}
						GDSpawn newSpawn = goldenDupe().getSpawnDatabase().get(name);

						whoToTeleport.teleportAsync(newSpawn.asLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);
						commandMessenger.message(sender, name + ".message-admin-teleport", Placeholder.of("new", newSpawn.getName()), Placeholder.of("player", whoToTeleport.name()));
						commandMessenger.message(sender, name + ".message-teleported", Placeholder.of("new", newSpawn.getName()));

						return;
					}
					GDPlayer player = goldenDupe().playerDatabase().fromPlayer(sender);
					GDSpawn oldTeleport = player.teleportingSpawn();

					GDSpawn newSpawn = goldenDupe().getSpawnDatabase().get(spawn);
					if (newSpawn == null) {
						commandMessenger.message(sender, name + ".message-unusable");
						return;
					}
					//noinspection DataFlowIssue
					if (!sender.hasPermission(newSpawn.getPermission())) {
						commandMessenger.message(sender, name + ".message-no-teleport-permissions");
						return;
					}

					if (oldTeleport != null) {
						if (oldTeleport.equals(newSpawn)) {
							commandMessenger.message(sender, name + ".message-already-teleporting", new Placeholder("old", oldTeleport.getName()), new Placeholder("new", newSpawn.getName()));
						} else {
							commandMessenger.message(sender, name + ".message-cancel-teleport-rewrite", new Placeholder("old", oldTeleport.getName()), new Placeholder("new", newSpawn.getName()));
						}
						return;
					}

					commandMessenger.message(sender, name + ".message-teleporting", new Placeholder("new", newSpawn.getName()));
					Integer cooldown = cooldowns.get(MemberType.of(sender));
					if (cooldown == null) {
						cooldown = 0;
					}
					new TimedTeleport(commandMessenger, spawn,
							sender, newSpawn.asLocation(),
							false, cooldown)
							.setMoveConsumer(entity -> player.setTeleportingSpawn(null))
							.setTeleportConsumer(entity -> player.setTeleportingSpawn(null))
							.setTeleportCause(PlayerTeleportEvent.TeleportCause.COMMAND)
							.accept();

					 */
				});
		commandManager.command(commandBuilder);
	}

	@Getter
	public enum Spawn {
		OVERWORLD("overworld", "spawn", "overworld", "ow"),
		NETHER("nether", "nether", "hell"),
		END("end", "theend", "end");


		private final String name;
		private final String[] names;

		Spawn(String name, String... names1) {
			this.name = name;
			this.names = names1;
		}

	}
}