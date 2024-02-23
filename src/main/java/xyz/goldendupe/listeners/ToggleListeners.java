package xyz.goldendupe.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.models.GDPlayer;

public class ToggleListeners implements GDListener {
	private final GoldenDupe goldenDupe;

	public ToggleListeners(GoldenDupe goldenDupe) {
		this.goldenDupe = goldenDupe;
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent event){
		GDPlayer player = goldenDupe.playerDatabase().fromPlayer(event.getPlayer());
		if (player.isToggleDropItem()){
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPickup(PlayerAttemptPickupItemEvent event){
		GDPlayer player = goldenDupe.playerDatabase().fromPlayer(event.getPlayer());
		if (player.isTogglePickupItem()){
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDrink(PlayerItemConsumeEvent event){
		ItemStack itemStack = event.getReplacement();
		if (itemStack != null && itemStack.getType() == Material.GLASS_BOTTLE){
			GDPlayer player = goldenDupe.playerDatabase().fromPlayer(event.getPlayer());
			if (player.isTogglePotionBottles()){
				event.setCancelled(true);
			}
		}
	}

	@Override
	public GoldenDupe goldenDupe() {
		return goldenDupe;
	}
}
