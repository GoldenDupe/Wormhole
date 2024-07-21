package xyz.goldendupe.listeners;

import bet.astral.messenger.v2.placeholder.Placeholder;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.utils.MemberType;
import xyz.goldendupe.utils.StringUtils;

public class IllegalListener implements GDListener {
	private final GoldenDupe goldenDupe;
	protected IllegalListener(GoldenDupe goldenDupe) {
		this.goldenDupe = goldenDupe;
	}


	@EventHandler(priority = EventPriority.HIGH)
	private void onBlockPlace(BlockPlaceEvent event){
		Block block = event.getBlock();
		if (goldenDupe.getGlobalData().getIllegalPlacement().contains(block.getType())){
			if (event.getPlayer().hasPermission(MemberType.ADMINISTRATOR.permissionOf("bypass-illegal-build"))){
				return;
			}
			event.setCancelled(true);
			event.setBuild(false);
			goldenDupe.messenger().message(event.getPlayer(), Translations.LISTENER_ILLEGAL, Placeholder.of("block", StringUtils.properCase(block.getType().name())));
		}
	}

	@Override
	public GoldenDupe goldenDupe() {
		return goldenDupe;
	}
}
