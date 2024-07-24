package xyz.goldendupe.command.donator.og;

import bet.astral.guiman.ClickableBuilder;
import bet.astral.guiman.GUI;
import bet.astral.guiman.GUIBuilder;
import bet.astral.messenger.v2.component.ComponentType;
import bet.astral.messenger.v2.placeholder.Placeholder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.command.CommandSender;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.bootstrap.InitAfterBootstrap;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.GoldenMessenger;
import xyz.goldendupe.messenger.Translations;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CreativeGUICommand extends GDCloudCommand implements InitAfterBootstrap {
	private final Map<Integer, GUI> guis = new IdentityHashMap<>();

	public CreativeGUICommand(GoldenDupeCommandRegister register, PaperCommandManager<CommandSender> commandManager) {
		super(register, commandManager);
		register.bootstrap.initAfterBootstraps.add(this);
		commandPlayer(commandBuilderPlayer("blocks", Description.of("Allows player to take items from a menu with all blocks in the server. Illegal items will not be in the menu."), "blocksmenu", "blockmenu")
				.handler(context -> {
					guis.get(0).generateInventory(context.sender());
				})
		);
	}


	private GUI create(@NotNull List<Material> materials, int page, int maxPages, boolean isLastPage, int from, int to) {
		GUIBuilder builder = new GUIBuilder(6);
		builder.setBackground(new ClickableBuilder(Material.BLACK_STAINED_GLASS_PANE, meta -> meta.setHideTooltip(true)));
		List<Material> copy = materials.subList(from, to);
		int slot = 0;
		for (Material material : copy) {
			builder.addSlotClickable(slot, new ClickableBuilder(material, meta -> {
				meta.displayName(getComponent(Translations.GUI_CREATIVE_ITEM_NAME, Placeholder.of("block", Component.translatable(material.translationKey()))));
				meta.lore(List.of());
			}).setGeneralAction((clickable, item, player) -> {
				player.getInventory().addItem(ItemStack.of(item.getType(), 1));
			}));
			slot++;
		}
		builder.setBackground(new ClickableBuilder(Material.BLACK_STAINED_GLASS_PANE, meta -> meta.setHideTooltip(true)));
		if (page > 1) {
			builder.setSlotClickable(45, new ClickableBuilder(Material.ARROW, meta -> {
				meta.displayName(getComponent(Translations.GUI_CREATIVE_BACK));
			}).setGeneralAction((clickable, itemStack, player) -> guis.get(page - 1).generateInventory(player)));
		}
		if (!isLastPage) {
			builder.setSlotClickable(53, new ClickableBuilder(Material.ARROW, meta -> {
				meta.displayName(getComponent(Translations.GUI_CREATIVE_NEXT));
			}).setGeneralAction((clickable, itemStack, player) -> guis.get(page + 1).generateInventory(player)));
		}
		builder.setSlotClickable(49, new ClickableBuilder(Material.BARRIER, meta -> {
			meta.displayName(getComponent(Translations.GUI_CREATIVE_CLOSE));
		}).setGeneralAction((clickable, item, player) -> player.closeInventory(InventoryCloseEvent.Reason.PLAYER)));
		builder.name(getComponent(Translations.GUI_CREATIVE_TITLE, Placeholder.of("page", page), Placeholder.of("max_page", maxPages)));
		GUI gui = builder.build();
		gui.shared = false;
		return gui;
	}

	public Component getComponent(Translations.Translation translation, Placeholder... placeholders) {
		GoldenMessenger messenger = goldenDupe().messenger();
		return messenger.parseComponent(translation, Locale.US, ComponentType.CHAT, placeholders);
	}

	@Override
	public void init() {
		Registry<Material> registry = Registry.MATERIAL;
		List<Material> materials = registry.stream().filter(item -> item.isItem() && item.getBlockTranslationKey() != null).toList();
		int rows = materials.size() / 9;
		int pages = rows / 5;
		for (int i = 0; i < pages; i++) {
			GUI gui = create(materials, i, pages, i+1==pages, i*9, i*9*5);
			guis.put(i, gui);
		}
	}
}