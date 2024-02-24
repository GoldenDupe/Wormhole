package xyz.goldendupe.listeners;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import xyz.goldendupe.GoldenDupe;

public class WaterListener implements GDListener {
	private final GoldenDupe goldenDupe;
	protected WaterListener(GoldenDupe goldenDupe){
		this.goldenDupe = goldenDupe;
	}
	@EventHandler
	private void onWaterPlace(BlockPlaceEvent event){
		if (event.getBlock().getType() == Material.WATER){
			event.setCancelled(false);
			event.setBuild(false);
			if (event.getBlock().getWorld().getEnvironment()== World.Environment.NETHER){
				event.getBlock().setType(Material.WATER);
			}
		}
	}

	@Override
	public GoldenDupe goldenDupe() {
		return goldenDupe;
	}
}
