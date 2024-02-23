package xyz.goldendupe.listeners;

import io.papermc.paper.event.player.AsyncChatDecorateEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.models.chatcolor.Color;


public class ChatItemListener implements GDListener {
	private final GoldenDupe goldenDupe;

	public ChatItemListener(GoldenDupe goldenDupe) {
		this.goldenDupe = goldenDupe;
	}

	@EventHandler
	private void chatItem(AsyncChatDecorateEvent event){
		Player player = event.player();
		assert player != null;

		String plain = PlainTextComponentSerializer.plainText().serialize(event.result());
		if (plain.toLowerCase().contains("[item]") || plain.toLowerCase().contains("[i]")){
			ItemStack itemStack = player.getInventory().getItemInMainHand();
			Component itemComponent = Component.text("");
			if (itemStack.isEmpty()){
				itemComponent = Component.text("1x", Color.GREEN).appendSpace().append(Component.text(player.getName(), Color.MINECOIN)).append(Component.text("'s hand", Color.MINECOIN)).hoverEvent(HoverEvent.showText(Component.text("Nothing to see here, it's just a hand.", Color.WHITE)));
			} else {
				ItemMeta meta = itemStack.getItemMeta();
				itemComponent = Component.text(itemStack.getAmount()+"x", NamedTextColor.GREEN)
						.appendSpace().append(meta.hasDisplayName() ? meta.displayName() : Component.translatable(itemStack.translationKey()))
						.hoverEvent(itemStack)
				;
			}
			Component finalItemComponent = itemComponent;
			event.result(event.result().replaceText(builder->
							builder.match("(?i)[item]").replacement(finalItemComponent))
					.replaceText(builder -> builder.match("(?i)[i]").replacement(finalItemComponent))
			);
		}
	}

	                      @Override
	public GoldenDupe goldenDupe() {
		return goldenDupe;
	}
}
