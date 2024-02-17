package xyz.goldendupe.listeners;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class WaterListener implements Listener {
	@EventHandler
	public void onWaterPlace(BlockPlaceEvent event){
		if (event.getBlock().getType() == Material.WATER){
			event.setCancelled(false);
			event.setBuild(false);
			if (event.getBlock().getWorld().getEnvironment()== World.Environment.NETHER){
				event.getBlock().setType(Material.WATER);
			}
		}
	}
}
