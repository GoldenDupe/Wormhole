package xyz.goldendupe.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
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
		if (player==null){
			return;
		}
		if (player.isToggleDropItem()){
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPickup(PlayerAttemptPickupItemEvent event){
		GDPlayer player = goldenDupe.playerDatabase().fromPlayer(event.getPlayer());
		if (player==null){
			return;
		}
		if (player.isTogglePickupItem()){
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDrink(PlayerItemConsumeEvent event) {
		ItemStack itemStack = event.getItem();
		if (itemStack.getType() == Material.POTION) {
			GDPlayer player = goldenDupe.playerDatabase().fromPlayer(event.getPlayer());
			if (player.isTogglePotionBottles()) {
				Player ePlayer = event.getPlayer();
				EquipmentSlot hand = event.getHand();
				if (hand==EquipmentSlot.OFF_HAND){
					if (itemStack.getAmount()>1){
						event.getPlayer().getScheduler()
								.runDelayed(goldenDupe, _ -> event.getPlayer().getInventory().removeItem(ItemStack.of(Material.GLASS_BOTTLE)), null, 1);
					} else {
						event.getPlayer().getScheduler()
								.runDelayed(goldenDupe, _ -> event.getPlayer().getInventory().setItem(hand, ItemStack.empty()), null, 1);
					}
				} else {
					int slot = ePlayer.getInventory().getHeldItemSlot();
					if (itemStack.getAmount()>1){
						event.getPlayer().getScheduler()
								.runDelayed(goldenDupe, _ -> event.getPlayer().getInventory().removeItem(ItemStack.of(Material.GLASS_BOTTLE)), null, 1);
					} else {
						event.getPlayer().getScheduler()
								.runDelayed(goldenDupe, _ -> event.getPlayer().getInventory().setItem(slot, ItemStack.empty()), null, 1);
					}
				}
			}
		}
	}

	@Override
	public GoldenDupe goldenDupe() {
		return goldenDupe;
	}
}
