package xyz.goldendupe.utils;

import bet.astral.messenger.v2.Messenger;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitTask;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.messenger.Translations;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class TimedTeleport {
	private final String messagePrefix;
	private final Messenger messenger;
	private final Location to;
	private final boolean allowMoving;
	private final Map<Entity, Location> locationMap = new HashMap<>();
	private final Set<Entity> entitiesToTeleport;
	private int ticksLeft;
	private AtomicReference<BukkitTask> task;
	private Consumer<Entity> startConsumer = null;
	private Consumer<Entity> moveConsumer = null;
	private Consumer<Entity> teleportConsumer = null;
	@Getter
	private PlayerTeleportEvent.TeleportCause teleportCause = PlayerTeleportEvent.TeleportCause.PLUGIN;

	public TimedTeleport(Messenger messenger, String messagePrefix, List<Entity> entities, Location to,  boolean allowMoving, int ticksToTeleport){
		this.messagePrefix = messagePrefix;
		this.messenger = messenger;
		this.to = to;
		this.allowMoving = allowMoving;
		this.entitiesToTeleport = new HashSet<>(entities);
		this.ticksLeft = ticksToTeleport;
	}
	public TimedTeleport(Messenger messenger, String messagePrefix, Entity entity, Location to, boolean allowMoving, int ticksToTeleport){
		this.messagePrefix = messagePrefix;
		this.messenger = messenger;
		this.to = to;
		this.allowMoving = allowMoving;
		this.entitiesToTeleport = new HashSet<>();
		this.entitiesToTeleport.add(entity);
		this.ticksLeft = ticksToTeleport;
	}

	public TimedTeleport accept(){
		Bukkit.getScheduler().runTaskTimer(GoldenDupe.instance(), task->{
			if (TimedTeleport.this.task == null){
				TimedTeleport.this.task = new AtomicReference<>(task);
				for (Entity entity : entitiesToTeleport){
					locationMap.put(entity, neutralize(entity.getLocation()));
					if (startConsumer != null)
						startConsumer.accept(entity);
				}
			}
			if (!allowMoving) {
				for (Entity entity : new ArrayList<>(entitiesToTeleport)) {
					Location newLoc = neutralize(entity.getLocation());
					if (newLoc.distance(locationMap.get(entity)) > 0.25) {
						entitiesToTeleport.remove(entity);
						if (entity instanceof Player player) {
							messenger.message(player, Translations.get(messagePrefix + ".teleport-canceled-moved"));
						}
						if (moveConsumer != null)
							moveConsumer.accept(entity);
					}
				}
				if (entitiesToTeleport.isEmpty()) {
					task.cancel();
					return;
				}
			}

			if (ticksLeft <= 0){
				for (Entity entity : entitiesToTeleport) {
					entity.teleportAsync(to, teleportCause);
					if (entity instanceof Player player) {
						messenger.message(player, Translations.get(messagePrefix + ".teleported"));
					}
					if (teleportConsumer != null)
						teleportConsumer.accept(entity);
				}

				task.cancel();
				return;
			}

			ticksLeft--;
		}, 1, 1);
		return this;
	}

	public TimedTeleport cancel(){
		if (task != null && task.get() != null && !task.get().isCancelled()){
			task.get().cancel();
		}
		return this;
	}

	public TimedTeleport removeEntity(Entity entity){
		entitiesToTeleport.remove(entity);
		if (entitiesToTeleport.isEmpty()){
			cancel();
		}
		return this;
	}

	private Location neutralize(Location location) {
		Location loc = location.clone();
		loc.setYaw(0);
		loc.setPitch(0);
		return loc;
	}

	public TimedTeleport setStartConsumer(Consumer<Entity> startConsumer) {
		this.startConsumer = startConsumer;
		return this;
	}

	public TimedTeleport setMoveConsumer(Consumer<Entity> moveConsumer) {
		this.moveConsumer = moveConsumer;
		return this;
	}

	public TimedTeleport setTeleportConsumer(Consumer<Entity> teleportConsumer) {
		this.teleportConsumer = teleportConsumer;
		return this;
	}

	public TimedTeleport setTeleportCause(PlayerTeleportEvent.TeleportCause teleportCause) {
		this.teleportCause = teleportCause;
		return this;
	}
}
