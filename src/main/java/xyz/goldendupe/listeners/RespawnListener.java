package xyz.goldendupe.listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRespawnEvent;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.database.SpawnDatabase;
import xyz.goldendupe.models.impl.GDSpawn;

import java.util.Set;

public class RespawnListener implements GDListener{
	private final GoldenDupe goldenDupe;

	public RespawnListener(GoldenDupe goldenDupe) {
		this.goldenDupe = goldenDupe;
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event){
		Set<PlayerRespawnEvent.RespawnFlag> flags = event.getRespawnFlags();
		if (flags.contains(PlayerRespawnEvent.RespawnFlag.ANCHOR_SPAWN)) {
			return;
		} else if (flags.contains(PlayerRespawnEvent.RespawnFlag.BED_SPAWN)){
			return;
		}

		SpawnDatabase spawnDatabase = goldenDupe.getSpawnDatabase();
		GDSpawn spawn = spawnDatabase.get("overworld");
		Location location = event.getPlayer().getLocation();
		World world = location.getWorld();
		World.Environment environment = world.getEnvironment();
		switch (environment) {
			case THE_END -> {
				spawn = spawnDatabase.get("end");
			}
			case NETHER -> {
				spawn = spawnDatabase.get("nether");
			}
		}
		event.setRespawnLocation(spawn.asLocation());
	}

	@Override
	public GoldenDupe goldenDupe() {
		return goldenDupe;
	}
}
