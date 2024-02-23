package xyz.goldendupe.models.chatcolor;

import com.samjakob.spigui.buttons.SGButton;
import org.bukkit.inventory.ItemStack;

public class CCButton extends SGButton implements Cloneable {
	public final Color color;
	public final String name;
	public final int slot;
	/**
	 * Creates an SGButton with the specified {@link ItemStack} as it's 'icon' in the inventory.
	 *
	 * @param icon The desired 'icon' for the SGButton.
	 */
	public CCButton(ItemStack icon, String name, int slot, Color chatColor) {
		super(icon);
		this.color = chatColor;
		this.name = name;
		this.slot = slot;
	}

	public CCButton clone(){
		return new CCButton(this.getIcon(), name, slot, this.color);
	}
}
