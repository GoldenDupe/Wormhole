package xyz.goldendupe.utils;

import bet.astral.messenger.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import xyz.goldendupe.GoldenDupe;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class TimedTeleport {
	private final String messagePrefix;
	private final Messenger<?> messenger;
	private final Location to;
	private final boolean allowMoving;
	private final Map<Entity, Location> locationMap = new HashMap<>();
	private final Set<Entity> entitiesToTeleport;
	private int ticksLeft;
	private AtomicReference<BukkitTask> task;
	public TimedTeleport(Messenger<?> messenger, String messagePrefix, List<Entity> entities, Location to,  boolean allowMoving, int ticksToTeleport){
		this.messagePrefix = messagePrefix;
		this.messenger = messenger;
		this.to = to;
		this.allowMoving = allowMoving;
		this.entitiesToTeleport = new HashSet<>(entities);
		this.ticksLeft = ticksToTeleport;
	}
	public TimedTeleport(Messenger<?> messenger, String messagePrefix, Entity entity, Location to, boolean allowMoving, int ticksToTeleport){
		this.messagePrefix = messagePrefix;
		this.messenger = messenger;
		this.to = to;
		this.allowMoving = allowMoving;
		this.entitiesToTeleport = new HashSet<>();
		this.entitiesToTeleport.add(entity);
		this.ticksLeft = ticksToTeleport;
	}

	public void accept(){
		Bukkit.getScheduler().runTaskTimer(GoldenDupe.instance(), task->{
			if (TimedTeleport.this.task == null){
				TimedTeleport.this.task = new AtomicReference<>(task);
				for (Entity entity : entitiesToTeleport){
					locationMap.put(entity, neutralize(entity.getLocation()));
				}
			}
			if (!allowMoving) {
				for (Entity entity : new ArrayList<>(entitiesToTeleport)) {
					Location newLoc = neutralize(entity.getLocation());
					if (newLoc.distance(locationMap.get(entity)) > 0.25) {
						entitiesToTeleport.remove(entity);
						if (entity instanceof Player player) {
							messenger.message(player, messagePrefix + ".message-teleport-canceled-moved");
						}
					}
				}
				if (entitiesToTeleport.isEmpty()) {
					task.cancel();
					return;
				}
			}

			if (ticksLeft <= 0){
				for (Entity entity : entitiesToTeleport) {
					entity.teleportAsync(to);
					if (entity instanceof Player player) {
						messenger.message(player, messagePrefix + ".message-teleport-canceled-moved");
					}
				}

				task.cancel();
				return;
			}

			ticksLeft--;
		}, 1, 1);
	}

	public void cancel(){
		if (task != null && task.get() != null && !task.get().isCancelled()){
			task.get().cancel();
		}
	}

	public void removeEntity(Entity entity){
		entitiesToTeleport.remove(entity);
		if (entitiesToTeleport.isEmpty()){
			cancel();
		}
	}

	private Location neutralize(Location location) {
		Location loc = location.clone();
		loc.setYaw(0);
		loc.setPitch(0);
		return loc;
	}
}
