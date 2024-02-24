package xyz.goldendupe.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.defaults.TrashCommand;

public class TrashListener implements GDListener{
	private final GoldenDupe goldenDupe;

	public TrashListener(GoldenDupe goldenDupe) {
		this.goldenDupe = goldenDupe;
	}

	@EventHandler
	private void onTrashInventory(InventoryClickEvent event){
		if (event.getInventory().getHolder() instanceof TrashCommand.TrashInventory inventory){
			if (event.getSlot()>44){
				event.setCancelled(true);
				if (event.getSlot()==45){
					event.getWhoClicked().openInventory(TrashCommand.trashCans.get(Math.max(inventory.backward, 0)).getInventory());
				} else if (event.getSlot()==53){
					event.getWhoClicked().openInventory(TrashCommand.trashCans.get(Math.min(inventory.forward, TrashCommand.trashCans.size()-1)).getInventory());
				} else if (event.getSlot()==49){
					for (int i = 0; i < 45; i++){
						event.getInventory().setItem(i, null);
					}
				}
			}
		}
	}
	@EventHandler
	private void onTrashInventory(InventoryDragEvent event){
		if (event.getInventory().getHolder() instanceof TrashCommand.TrashInventory inventory){
			event.setCancelled(true);
		}
	}

	@Override
	public GoldenDupe goldenDupe() {
		return goldenDupe;
	}
}
