package xyz.goldendupe.anti.crash;

import bet.astral.tuples.Pair;
import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import lombok.Getter;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import xyz.goldendupe.GoldenDupe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class CrashNotifier implements Listener {
	private final GoldenDupe goldenDupe;
	private final Notifier notifier;
	private int notificationId = 0;
	private final Map<Integer, Notification> notificationMap = new HashMap<>();
	private final Map<Pair<Integer, Integer>, Integer> redstoneAmount = new HashMap<>();

	public CrashNotifier(GoldenDupe goldenDupe,
	                     Notifier notifier) {
		this.goldenDupe = goldenDupe;
		this.notifier = notifier;
	}

	public void notify(Notification notification){
		notifier.notifyStaff(notification);
		notificationMap.put(notification.getId(), notification);
	}

	/**
	 * Checks if projectile is arrow or trident as stacking arrows above player's head can crash the server,
	 * if there are too many of them
	 */
	@EventHandler
	private void onArrowLand(ProjectileHitEvent event) {
		if (event.getHitEntity() == null) {
			return;
		}
		if (event.getEntity() instanceof Trident || event.getEntity() instanceof Arrow) {
			Projectile projectile = event.getEntity();
			List<MetadataValue> metadataValues = projectile.getMetadata("projectile-hits");
			metadataValues.removeIf(value -> value.getOwningPlugin() != goldenDupe);
			if (metadataValues.isEmpty()) {
				projectile.setMetadata("projectile-hits", new FixedMetadataValue(goldenDupe, 1));
			}
			MetadataValue metadataValue = metadataValues.get(0);
			Object value = metadataValue.value();
			if (value instanceof Integer times) {
				projectile.setMetadata("projectile-hits", new FixedMetadataValue(goldenDupe, times + 1));

				if (times > 20) {
					notify(new Notification(
							System.currentTimeMillis(),
							getIdAndAdd(),
							projectile.getLocation(),
							event.getHitEntity() instanceof OfflinePlayer ? (OfflinePlayer) event.getHitEntity() : null,
							Notification.Type.ARROW_LANDED_TOO_MANY_TIMES
					));
					projectile.remove();
				}
			}
		}
	}

	/**
	 * Clears the redstone used in events within one tick.
	 */
	@EventHandler
	private void onBeginTick(ServerTickEndEvent event){
		redstoneAmount.clear();
	}

	/**
	 * Limits the amount of redstone in a chunk
	 */
	@EventHandler
	private void onRedstoneTick(BlockRedstoneEvent event) {
		Chunk chunk = event.getBlock().getChunk();
		Pair<Integer, Integer> pair = Pair.immutable(chunk.getX(), chunk.getZ());
		redstoneAmount.putIfAbsent(pair, 0);
		redstoneAmount.put(pair, redstoneAmount.get(pair) + 1);
		if (redstoneAmount.get(pair) == 200) {
			notify(new Notification(
					System.currentTimeMillis(),
					getIdAndAdd(),
					event.getBlock().getLocation(),
					null,
					Notification.Type.REDSTONE_200
			));
		}
		if (redstoneAmount.get(pair) == 400) {
			notify(new Notification(
					System.currentTimeMillis(),
					getIdAndAdd(),
					event.getBlock().getLocation(),
					null,
					Notification.Type.REDSTONE_400
			));
		}
		if (redstoneAmount.get(pair) == 600) {
			notify(new Notification(
					System.currentTimeMillis(),
					getIdAndAdd(),
					event.getBlock().getLocation(),
					null,
					Notification.Type.REDSTONE_600
			));
		}
		if (redstoneAmount.get(pair) == 800) {
			notify(new Notification(
					System.currentTimeMillis(),
					getIdAndAdd(),
					event.getBlock().getLocation(),
					null,
					Notification.Type.REDSTONE_800
			));
		}
	}

	private int getIdAndAdd(){
		return notificationId;
	}
}
