package xyz.goldendupe.command.donator.og;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.guiman.background.Background;
import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.gui.InventoryGUI;
import bet.astral.guiman.gui.builders.InventoryGUIBuilder;
import bet.astral.guiman.utils.ChestRows;
import bet.astral.messenger.v2.component.ComponentType;
import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderCollection;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.command.CommandSender;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.bootstrap.InitAfterBootstrap;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.GoldenMessenger;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.utils.MemberType;

import java.util.*;

@Cloud
public class CreativeGUICommand extends GDCloudCommand implements InitAfterBootstrap {
	private final Map<Integer, InventoryGUI> guis = new IdentityHashMap<>(200);

	public CreativeGUICommand(GoldenDupeCommandRegister register, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(register, commandManager);
		commandPlayer(commandBuilderPlayer("blocks", Description.of("Allows player to take items from a menu with all blocks in the server. Illegal items will not be in the menu."), "blocksmenu", "blockmenu")
				.permission(MemberType.DONATOR.permissionOf("blocks-menu"))
				.handler(context -> {
					guis.get(0).open(context.sender());
				})
		);
	}


	private InventoryGUI create(@NotNull List<Material> materials, int page, int maxPages, boolean isLastPage, int from, int to) {
		InventoryGUIBuilder builder = InventoryGUI.builder(ChestRows.SIX);
        builder.background(Background.noTooltip(Material.BLACK_STAINED_GLASS_PANE));
		List<Material> copy = materials.subList(from, Math.min(to, materials.size()));
		int slot = 0;
		for (Material material : copy) {
			builder.addClickable(slot, Clickable.builder(material, meta -> {
				meta.displayName(getComponent(Translations.GUI_CREATIVE_ITEM_NAME, Placeholder.of("block", Component.translatable(material.translationKey()))));
				meta.lore(List.of());
			}).action(List.of(ClickType.LEFT, ClickType.RIGHT), (action) -> {
				action.getWho().getInventory().addItem(ItemStack.of(action.getItemStack().getType(), 1));
			}).action(List.of(ClickType.SHIFT_LEFT, ClickType.SHIFT_RIGHT), (action)-> {
				action.getWho().getInventory().addItem(ItemStack.of(action.getItemStack().getType(),
						action.getItemStack().getType().getMaxStackSize()));
			})
			);
			slot++;
		}
		if (page > 0) {
			builder.addClickable(45, Clickable.builder(Material.ARROW, meta -> {
				meta.displayName(getComponent(Translations.GUI_CREATIVE_BACK));
			}).actionGeneral((action) -> guis.get(page - 1).open(action.getWho())));
		}
		if (!isLastPage) {
			builder.addClickable(53, Clickable.builder(Material.ARROW, meta -> {
				meta.displayName(getComponent(Translations.GUI_CREATIVE_NEXT));
			}).actionGeneral((action) -> guis.get(page + 1).open(action.getWho())));
		}
		builder.addClickable(49, Clickable.builder(Material.BARRIER, meta -> {
			meta.displayName(getComponent(Translations.GUI_CREATIVE_CLOSE));
		}).actionGeneral((action) ->
				action.getWho().closeInventory(InventoryCloseEvent.Reason.PLAYER)));
		builder.title(Translations.GUI_CREATIVE_TITLE);
		builder.placeholderGenerator(_ -> PlaceholderCollection.list(Placeholder.of("page", page+1), Placeholder.of("max_page", maxPages)));
        return builder.messenger(goldenDupe().messenger()).build();
	}

	public Component getComponent(Translations.Translation translation, Placeholder... placeholders) {
		GoldenMessenger messenger = goldenDupe().messenger();
		return messenger.disablePrefixForNextParse().parseComponent(translation, Locale.US, ComponentType.CHAT, placeholders);
	}

	@Override
	public void init() {
		Registry<Material> registry = Registry.MATERIAL;
		Set<Material> illegals = GoldenDupe.instance().getSettings().getIllegalBlocksMenu();
		List<Material> materials = registry.stream().filter(item -> item.isItem() && item.getBlockTranslationKey() != null).filter(item->!item.isAir()).filter(material->!illegals.contains(material)).toList();
		final int rows = materials.size() / 9;
		final int pages = (rows / 5)+1;
		final int add = (9*5);
		int from = 0;
		int to = add;
		for (int i = 0; i < pages; i++) {
			InventoryGUI gui = create(materials, i, pages, i+1==pages, from, to);
			guis.put(i, gui);
			from+=add;
			to+=add;
		}
	}
}