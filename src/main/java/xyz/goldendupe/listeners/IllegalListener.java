package xyz.goldendupe.listeners;

import bet.astral.messagemanager.placeholder.Placeholder;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.Season;
import xyz.goldendupe.utils.StringUtils;

@Season(added = 1, unlock = 1, alwaysUnlocked = true)
public class IllegalListener extends GDListener {
	public IllegalListener(GoldenDupe goldenDupe) {
		super(goldenDupe);
	}


	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockPlace(BlockPlaceEvent event){
		Block block = event.getBlock();
		if (!goldenDupe.canBePlaced(block)){
			event.setCancelled(true);
			event.setBuild(false);
			goldenDupe.messenger().message(event.getPlayer(), "cannot-place-illegal", new Placeholder("block", StringUtils.properCase(block.getType().name())));
		}
	}
}
