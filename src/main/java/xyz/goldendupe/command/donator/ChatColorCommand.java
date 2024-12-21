package xyz.goldendupe.command.donator;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.guiman.background.Background;
import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.clickable.ClickableBuilder;
import bet.astral.guiman.clickable.ClickableProvider;
import bet.astral.guiman.gui.InventoryGUI;
import bet.astral.guiman.gui.builders.InventoryGUIBuilder;
import bet.astral.guiman.permission.Permission;
import bet.astral.guiman.utils.ChestRows;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.incendo.cloud.Command;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.bootstrap.InitAfterBootstrap;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.models.chatcolor.Color;
import xyz.goldendupe.models.chatcolor.GDChatColor;
import xyz.goldendupe.utils.MemberType;
import xyz.goldendupe.utils.RunSync;

import java.lang.reflect.Field;
import java.util.*;

import static xyz.goldendupe.models.chatcolor.Color.YELLOW;

@Cloud
public class ChatColorCommand extends GDCloudCommand implements InitAfterBootstrap {
	private InventoryGUI mainMenu;
	private Background background;
	private final Map<UUID, MenuProfile> profiles = new HashMap<>();
	private final List<ClickableBuilder> preGeneratedButtons = new ArrayList<>();
	private final Map<Color, ClickableBuilder> generatedButtonsByColor = new HashMap<>();

	// https://minecraft-heads.com/custom-heads/alphabet?page=36
	// Declaring them here means the server will lag __not__ during players are online
	private static ItemStack ONE;
	private static ItemStack TWO;
	private static ItemStack THREE;
	private static ItemStack FOUR;
	private static ItemStack FIVE;
	private static ItemStack SIX;
	private static ItemStack SEVEN;
	private static ItemStack EIGHT;
	private static ItemStack NINE;
	private static ItemStack LEFT;
	private static ItemStack RIGHT;

	public void init() {
		ONE = skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDliMzAzMDNmOTRlN2M3ODVhMzFlNTcyN2E5MzgxNTM1ZGFmNDc1MzQ0OWVhNDFkYjc0NmUxMjM0ZTlkZDJiNSJ9fX0=");
		TWO = skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTNjYTdkN2MxNTM0ZGM2YjllZDE2NDdmOTAyNWRkZjI0NGUwMTA3ZGM4ZGQ0ZjRmMDg1MmM4MjA4MWQ2MzUwZSJ9fX0=");
		THREE = skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTk1ZTFlMmZiMmRlN2U2Mjk5YTBmNjFkZGY3ZDlhNmQxMDFmOGQ2NjRmMTk1OWQzYjY3ZGNlOGIwNDlhOGFlMSJ9fX0=");
		FOUR = skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzgzOWVhZjllZTA3NjcwNjA3ZDNkYTRkNmMxZDMwZmU1OWRiNTY0NThmYWQ1ZjU1YjU0MTJkNTZiM2RlYjU1OSJ9fX0=");
		FIVE = skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTgxNDBlYzQ4NDU2MzFhODlmZmU4MzQ0YWU5OGQxMDQ5YjgyYzIxNTkzOTMyOTBiZDM4YThhMDA4NDY1YjNkOSJ9fX0=");
		SIX = skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGViZjliNmE2YzRlYjBjYmZkMGEzZDM3YzM0YzQ1ODYwNjgxZjEzZjcyNzBmMTMzN2ZkMTM2YTcwMTE4OThmMCJ9fX0=");
		SEVEN = skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjRjMmQ2Y2JkZmYwMGI5N2FmN2Y4Y2ZlODc2N2ZkODdjZDY1NjM5YWJkZjgzZWMxMDM5YTQ2NDE1ZTY5ZTM5OCJ9fX0=");
		EIGHT = skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQyNzkxNzUyZjY3YTY0NTBiOTc1ZDM1NzQxMjM1NmIwYTk5ZTM1NTVlNjdlYmRhZDJkNDAzMjYxMjliZGRhNCJ9fX0=");
		NINE = skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjRjMmQ2Y2JkZmYwMGI5N2FmN2Y4Y2ZlODc2N2ZkODdjZDY1NjM5YWJkZjgzZWMxMDM5YTQ2NDE1ZTY5ZTM5OCJ9fX0=");
		LEFT = skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODFjOTZhNWMzZDEzYzMxOTkxODNlMWJjN2YwODZmNTRjYTJhNjUyNzEyNjMwM2FjOGUyNWQ2M2UxNmI2NGNjZiJ9fX0=");
		RIGHT = skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzMzYWU4ZGU3ZWQwNzllMzhkMmM4MmRkNDJiNzRjZmNiZDk0YjM0ODAzNDhkYmI1ZWNkOTNkYThiODEwMTVlMyJ9fX0=");

		ItemStack backgroundItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		backgroundItem.editMeta(meta -> {
			meta.displayName(Component.text(""));
			meta.addItemFlags(ItemFlag.values());
		});
		background = Background.noTooltip(backgroundItem);

		Class<?> colorClazz = Color.class;
		for (Field field : colorClazz.getFields()) {
			if (field.isAnnotationPresent(Color.ChatColor.class)) {
				try {
					Color.ChatColor chatColor = field.getAnnotation(Color.ChatColor.class);
					Color color = (Color) field.get(null);
					ItemStack itemStack = new ItemStack(chatColor.material());
					ItemMeta meta = itemStack.getItemMeta();
					meta.addItemFlags(ItemFlag.values());
					Component displayname = MiniMessage.miniMessage().deserialize(chatColor.formattedName()).decoration(TextDecoration.ITALIC, false);
					meta.displayName(displayname);

					String name = field.getName().toLowerCase();

					itemStack.setItemMeta(meta);

					ClickableBuilder clickable = new ClickableBuilder(itemStack)
							.data("material", chatColor.material())
							.data("color", color)
							.data("slot", chatColor.slot())
							.data("name", displayname)
							.data("permission", name)
							;

					preGeneratedButtons.add(clickable);
					generatedButtonsByColor.put(color, clickable);

					goldenDupe().registerPermission(MemberType.DONATOR.permissionOf("chatcolor.single." + name));
					goldenDupe().registerPermission(MemberType.DONATOR.permissionOf("chatcolor.gradient." + name));
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		}

		goldenDupe().registerPermission(MemberType.DONATOR.permissionOf("chatcolor.gradient"));

		goldenDupe().registerPermission(MemberType.DONATOR.permissionOf("chatcolor.rainbow"));

		goldenDupe().registerPermission(MemberType.DONATOR.permissionOf("chatcolor.format"));
		goldenDupe().registerPermission(MemberType.DONATOR.permissionOf("chatcolor.format.underlined"));
		goldenDupe().registerPermission(MemberType.DONATOR.permissionOf("chatcolor.format.strikethrough"));
		goldenDupe().registerPermission(MemberType.DONATOR.permissionOf("chatcolor.format.italic"));
		goldenDupe().registerPermission(MemberType.DONATOR.permissionOf("chatcolor.format.bold"));

		goldenDupe().registerPermission(MemberType.DONATOR.permissionOf("chatcolor.single"));


	}


	public ChatColorCommand(GoldenDupeCommandRegister register, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(register, commandManager);
		Command.Builder<Player> chatColorBuilder = commandManager.commandBuilder("chatcolor", Description.of("Allows donators to change their chat color."), "chatcolour", "chatformat")
				.senderType(Player.class)
				.handler(context -> {
					profiles.putIfAbsent(context.sender().getUniqueId(), new MenuProfile());
					Bukkit.getScheduler().runTask(goldenDupe(), () -> createMainMenu(context.sender()));
				});
		commandManager.command(chatColorBuilder);
	}

	private static class MenuProfile {
		InventoryGUI singleMenu;
		InventoryGUI rainbowMenu;
		InventoryGUI formatMenu;

		public MenuProfile() {

		}
	}

	private void createMainMenu(Player player) {
		if (mainMenu == null) {
			MiniMessage mm = MiniMessage.miniMessage();
			ItemStack gradient = new ItemStack(Material.MAGMA_CREAM);
			gradient.editMeta(meta -> meta.displayName(mm.deserialize("<!italic><gradient:#FF5555:#FF9D33:#00AA00:#FF5555:#A0000>Chat Color Gradients").compact()));
			ItemStack single = new ItemStack(Material.SLIME_BALL);
			single.editMeta(meta -> meta.displayName(mm.deserialize("<!italic><yellow>Chat Colors").compact()));
			ItemStack rainbow = new ItemStack(Material.ENDER_EYE);
			rainbow.editMeta(meta -> meta.displayName(mm.deserialize("<!italic><rainbow>Chat Rainbow \"Gradients\"").compact()));
			ItemStack format = new ItemStack(Material.CREEPER_BANNER_PATTERN);
			format.editMeta(meta -> {
				meta.displayName(mm.deserialize("<!italic><yellow><underlined><strikethrough><bold>Chat Formatting").compact());
				meta.addItemFlags(ItemFlag.values());
			});

			Clickable gradientBuilder = Clickable.builder(gradient).actionGeneral((action) -> createGradientMenu(action.getWho())).permission(MemberType.DONATOR.permissionOf("chatcolor.gradient")).build();
			Clickable singleBuilder = Clickable.builder(single).actionGeneral((action) -> createSingleMenu(action.getWho())).permission(MemberType.DONATOR.permissionOf("chatcolor.color")).build();
			Clickable rainbowBuilder = Clickable.builder(rainbow).actionGeneral((action) -> createRainbowMenu(action.getWho())).permission(MemberType.DONATOR.permissionOf("chatcolor.rainbow")).build();
			Clickable formatBuilder = Clickable.builder(format).actionGeneral((action) -> createFormatMenu(action.getWho())).permission(MemberType.DONATOR.permissionOf("chatcolor.format")).build();

			mainMenu = InventoryGUI.builder(ChestRows.TWO)
					.title(Component.text("Chat Color"))
					.addClickable(1, gradientBuilder)
					.addClickable(3, singleBuilder)
					.addClickable(5, rainbowBuilder)
					.addClickable(7, formatBuilder)
					.addClickable(13, Clickable.builder(Material.BARRIER, (meta) -> {
										meta.displayName(Component.text("Reset Everything", Color.DARK_RED).decoration(TextDecoration.ITALIC, false));
									})
									.actionGeneral((action) -> {
										GDPlayer gdPlayer = goldenDupe().playerDatabase().fromPlayer(action.getWho());
										GDChatColor chatColor = gdPlayer.color();
										chatColor.setColors(null);
										chatColor.reset();
										RunSync.runSync(()->action.getWho().closeInventory());
									})
					)
					.background(background)
					.messenger(messenger)
					.replaceItemsEachOpen()
					.build();
		}
		mainMenu.open(player);
	}

	private void createGradientMenu(Player player) {
		MenuProfile profile = profiles.get(player.getUniqueId());
		GDPlayer gdPlayer = goldenDupe().playerDatabase().fromPlayer(player);
		GDChatColor chatColor = gdPlayer.getColor();

		InventoryGUIBuilder builder = InventoryGUI.builder(ChestRows.TWO)
				.title(Component.text("Chat Color > Gradient"));
		for (int i = 0; i < 8; i++) {
			int finalI = i;
			ItemStack item = new ItemStack(Material.PAPER);
			item.editMeta(meta -> meta.displayName(Component.text("Set gradient position " + (finalI+1) + " color", YELLOW).decoration(TextDecoration.ITALIC, false).compact()));


			Clickable clickable =
					new ClickableBuilder(item).actionGeneral((action) -> createGradientSelect(action.getWho(), finalI))
							.permission(MemberType.DONATOR.permissionOf("chatcolor.gradient.position." + (i + 1))).build();
			builder.clickable(i, clickable);

			Color color = chatColor.colors().get(i);
			if (color != null){
				ClickableBuilder bldr = generatedButtonsByColor.get(color);
				Component name = (Component) bldr.getData("name");
				Material material = (Material) bldr.getData("material");
                assert material != null;
                builder.clickable(i+9, Clickable.builder(material, meta->{
					meta.displayName(Component.text("Position "+(finalI+1)+" color: ").decoration(TextDecoration.ITALIC, false).append(name));
				}).permission(MemberType.DONATOR.permissionOf("chatcolor.gradient.position."+(i+1))));
			}
		}
		builder
				.addClickable(17, Clickable.builder(Material.BARRIER, (meta) -> {
									meta.displayName(Component.text("Reset Positions", Color.RED).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
								})
								.actionGeneral((action) -> {
									gdPlayer.color().setColors(null);
									gdPlayer.color().setMode(GDChatColor.Mode.SINGLE);
									action.getWho().sendMessage(Component.text("Reset your gradient color selections!", Color.RED));

									createGradientMenu(player); // Open new inventory to ensure the gradient selections are saved.
								})
				)
				.background(background)
				.messenger(messenger)
				.replaceItemsEachOpen()
				.build().open(player);
	}

	private void createSingleMenu(Player player) {
		MenuProfile profile = profiles.get(player.getUniqueId());
		if (profile.singleMenu != null) {
			profile.singleMenu.open(player);
			return;
		}

		InventoryGUIBuilder guiBuilder = InventoryGUI.builder(ChestRows.FOUR)
				.title(Component.text("Chat Color > Single").decoration(TextDecoration.ITALIC, false).compact());
		for (ClickableBuilder builder : this.preGeneratedButtons) {
			ClickableBuilder slotBuilder = builder.clone();
			slotBuilder.permission(MemberType.DONATOR.permissionOf("chatcolor.single."+builder.getData("permission")));
			slotBuilder.actionGeneral((action) -> {
				GDPlayer gdPlayer = goldenDupe().playerDatabase().fromPlayer(action.getWho());
				Clickable clickable = action.getClickable();

				Color color = (Color) clickable.getData("color");

				Component name = (Component) clickable.getData("name");
				action.getWho().sendMessage(Component.text("Set your chat color to ", YELLOW).append(name).decoration(TextDecoration.ITALIC, false).compact());

				GDChatColor chatColor = gdPlayer.color();
				if (chatColor.equals(GDChatColor.DEFAULT)) {
					chatColor = new GDChatColor(GDChatColor.Mode.SINGLE, color);
				}
				chatColor.colors().put(0, color);
				chatColor.setMode(GDChatColor.Mode.SINGLE);
				gdPlayer.setColor(chatColor);

				RunSync.runSync(player::closeInventory);
			});
			@SuppressWarnings("DataFlowIssue") int slot = (int) slotBuilder.getData("slot");
			guiBuilder.clickable(slot, slotBuilder.build());
		}
		guiBuilder.background(background);

		profile.singleMenu = guiBuilder.messenger(messenger).replaceItemsEachOpen().build();
		profile.singleMenu.open(player);
	}

	private void createRainbowMenu(Player player) {
		MenuProfile profile = profiles.get(player.getUniqueId());
		if (profile.rainbowMenu != null) {
			profile.rainbowMenu.open(player);
			return;
		}

		profile.rainbowMenu = InventoryGUI.builder(ChestRows.TWO).title(Component.text("Chat Color > Rainbow"))
				.addClickable(0, rainbowButton(0, ONE))
				.addClickable(1, rainbowButton(1, TWO))
				.addClickable(2, rainbowButton(2, THREE))
				.addClickable(3, rainbowButton(3, FOUR))
				.addClickable(4, rainbowButton(4, FIVE))
				.addClickable(5, rainbowButton(5, SIX))
				.addClickable(6, rainbowButton(6, SEVEN))
				.addClickable(7, rainbowButton(7, EIGHT))
				.addClickable(8, rainbowButton(8, NINE))
				.addClickable(11, rainbowReverse(true, LEFT))
				.addClickable(15, rainbowReverse(false, RIGHT))
				.addClickable(13, Clickable.builder(Material.BARRIER, (meta) -> meta.displayName(Component.text("Reset Rainbow Settings")))
						.actionGeneral((action) -> {
							GDPlayer gdPlayer = goldenDupe().playerDatabase().fromPlayer(action.getWho());
							gdPlayer.color().setRainbowMode(0).setRainbowReversed(false);
							action.getWho().sendMessage(Component.text("Reset your settings for your rainbow chat color.", Color.GREEN));
						})
				)
				.background(background)
				.messenger(messenger).replaceItemsEachOpen().build();
		profile.rainbowMenu.open(player);
	}

	private Clickable rainbowButton(int slot, ItemStack itemStack) {
		List<ClickType> clickTypes = List.of(ClickType.RIGHT, ClickType.LEFT, ClickType.SHIFT_RIGHT, ClickType.SHIFT_LEFT);
		MiniMessage mm = MiniMessage.miniMessage();
		ItemStack item = itemStack.clone();
		item.editMeta(meta -> {
			meta.displayName(Component.text("Rainbow start position " + (slot + 1), YELLOW).decoration(TextDecoration.ITALIC, false));
			List<Component> list = new ArrayList<>();
			list.add(mm.deserialize("<!italic><gray>Forward: <rainbow:" + slot + ">|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||").compact());
			list.add(mm.deserialize("<!italic><gray>Reversed: <rainbow:!" + slot + ">|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||").compact());
			meta.lore(list);
		});
		return new ClickableBuilder(item).actionGeneral((action) -> {
					Player player = action.getWho();
					GDPlayer gdPlayer = goldenDupe().playerDatabase().fromPlayer(player);
					GDChatColor chatColor = gdPlayer.color();
					if (chatColor.equals(GDChatColor.DEFAULT)) {
						chatColor = new GDChatColor(GDChatColor.Mode.RAINBOW, GDChatColor.DEFAULT.colors().get(0));
					}
					if (chatColor.mode() != GDChatColor.Mode.RAINBOW) {
						player.sendMessage(Component.text("Enabled rainbow chat color", YELLOW));
					}
					chatColor.setMode(GDChatColor.Mode.RAINBOW);
					chatColor.setRainbowMode(slot);
					gdPlayer.setColor(chatColor);
					player.sendMessage(Component.text("Set rainbow start position to " + slot, YELLOW));
				}).displayIfNoPermissions()
				.permission(MemberType.DONATOR.permissionOf("chatcolor.rainbow.position." + slot))
				.build();
	}

	private Clickable rainbowReverse(boolean reverse, ItemStack itemStack) {
		MiniMessage mm = MiniMessage.miniMessage();
		ItemStack item = itemStack.clone();
		item.editMeta(meta -> {
			meta.displayName(Component.text("Set the rainbow to " + (reverse ? "reversed" : "forward"), YELLOW).decoration(TextDecoration.ITALIC, false).compact());
			List<Component> list = new ArrayList<>();
			list.add(mm.deserialize((reverse ? "<gray>" : "<green>") + "<!italic>Forward: <rainbow:>|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||"));
			list.add(mm.deserialize((!reverse ? "<gray>" : "<green>") + "<!italic>Reversed: <rainbow:!>|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||"));
			meta.lore(list);
		});
		return new ClickableBuilder(item).actionGeneral((action) -> {
					Player player = action.getWho();
					GDPlayer gdPlayer = goldenDupe().playerDatabase().fromPlayer(player);
					GDChatColor chatColor = gdPlayer.color();
					if (chatColor.equals(GDChatColor.DEFAULT)) {
						chatColor = new GDChatColor(GDChatColor.Mode.RAINBOW, GDChatColor.DEFAULT.colors().get(0));
					}
					if (chatColor.mode() != GDChatColor.Mode.RAINBOW) {
						player.sendMessage(Component.text("Enabled rainbow chat color", Color.YELLOW));
					}
					chatColor.setMode(GDChatColor.Mode.RAINBOW);
					chatColor.setRainbowReversed(reverse);
					gdPlayer.setColor(chatColor);
					player.sendMessage(Component.text("Set rainbow type to " + (reverse ? "reserved" : "forward"), YELLOW));
				})
				.displayIfNoPermissions()
				.permission(MemberType.DONATOR.permissionOf("chatcolor.rainbow." + (reverse ? "reverse" : "forward")))
				.build();
	}

	private void createGradientSelect(Player player, int data) {
		InventoryGUIBuilder guiBuilder = InventoryGUI.builder(ChestRows.FOUR).title(Component.text("... > Gradient > " + (data + 1) + " Color").decoration(TextDecoration.ITALIC, false).compact());
		for (ClickableBuilder builder : this.preGeneratedButtons) {
			ClickableBuilder slotBuilder = builder.clone();
			slotBuilder.permission(MemberType.DONATOR.permissionOf("chatcolor.single."+builder.getData("permission")));
			slotBuilder.actionGeneral((action) -> {
				Player p = action.getWho();
				Clickable clickable = action.getClickable();

				GDPlayer gdPlayer = goldenDupe().playerDatabase().fromPlayer(player);

				Color color = (Color) clickable.getData("color");

				Component name = (Component) clickable.getData("name");
				p.sendMessage(Component.text("Set your gradient position " + (data + 1) + " color to ", YELLOW).append(name).decoration(TextDecoration.ITALIC, false).compact());

				GDChatColor chatColor = gdPlayer.color();
				if (chatColor.equals(GDChatColor.DEFAULT)) {
					chatColor = new GDChatColor(GDChatColor.Mode.SINGLE, color);
				}
				if (chatColor.mode() != GDChatColor.Mode.GRADIENT) {
					p.sendMessage(Component.text("Enabled gradient chat color", YELLOW));
				}
				chatColor.colors().put(data, color);
				chatColor.setMode(GDChatColor.Mode.GRADIENT);
				gdPlayer.setColor(chatColor);
				createGradientMenu(player);
			});
			@SuppressWarnings("DataFlowIssue") int slot = (int) slotBuilder.getData("slot");
			guiBuilder.clickable(slot, slotBuilder.build());
		}

		ItemStack reset = new ItemStack(Material.PAPER);
		reset.editMeta(meta -> {
			meta.displayName(Component.text("RESET", NamedTextColor.RED).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
		});
		guiBuilder.addClickable(35, new ClickableBuilder(reset).actionGeneral((action) -> {
			Player p = action.getWho();
			GDPlayer gdPlayer = goldenDupe().playerDatabase().fromPlayer(p);
			gdPlayer.color().colors().put(data, null);
			player.sendMessage(Component.text("Removed your gradient position " + (data + 1) + ".", YELLOW));
			createGradientMenu(player);
		}).build());
		guiBuilder.background(background);

		InventoryGUI gui = guiBuilder.messenger(messenger).replaceItemsEachOpen().build();
		gui.open(player);
	}

	private void createFormatMenu(Player player) {
		MenuProfile profile = profiles.get(player.getUniqueId());
		if (profile.formatMenu != null) {
			profile.formatMenu.open(player);
			return;
		}
		GDPlayer gdPlayer = goldenDupe().playerDatabase().fromPlayer(player);
		ClickableProvider underlined = (p) -> Clickable.builder(Material.LIGHT_WEIGHTED_PRESSURE_PLATE, meta -> {
					GDPlayer playerProfile = goldenDupe().playerDatabase().fromPlayer(p);
					meta.displayName(Component.text("Underlined", YELLOW, TextDecoration.UNDERLINED).decoration(TextDecoration.ITALIC, false));
					meta.lore(List.of(Component.text("Click to " + ((playerProfile.color().underlined()) ? "disable" : "enable") + " underlined messages", YELLOW).decoration(TextDecoration.ITALIC, false)));
				}).actionGeneral(action -> {
					GDPlayer playerProfile = goldenDupe().playerDatabase().fromPlayer(action.getWho());
					GDChatColor chatColor = playerProfile.color();
					if (chatColor.equals(GDChatColor.DEFAULT)) {
						chatColor = new GDChatColor(GDChatColor.Mode.SINGLE, chatColor.colors().get(0));
					}
					boolean enabled = chatColor.underlined();
					chatColor.setUnderlined(!enabled);

					action.getWho().sendMessage(Component.text((enabled ? "Disabled" : "Enabled") + " underlined formatting in chat messages.", YELLOW));
					createFormatMenu(action.getWho());
				}).permission(MemberType.DONATOR.permissionOf("chatcolor.format.strikethrough"))
				.build();

		ClickableProvider strikethrough = (p) -> Clickable.builder(Material.CHAIN, meta -> {
					GDPlayer playerProfile = goldenDupe().playerDatabase().fromPlayer(p);
					meta.displayName(Component.text("Strikethrough", YELLOW, TextDecoration.STRIKETHROUGH).decoration(TextDecoration.ITALIC, false));
					meta.lore(List.of(Component.text("Click to " + ((playerProfile.color().underlined()) ? "disable" : "enable") + " strikethrough messages", YELLOW).decoration(TextDecoration.ITALIC, false)));
				}).actionGeneral(action -> {
					GDPlayer playerProfile = goldenDupe().playerDatabase().fromPlayer(action.getWho());
					GDChatColor chatColor = playerProfile.color();
					if (chatColor.equals(GDChatColor.DEFAULT)) {
						chatColor = new GDChatColor(GDChatColor.Mode.SINGLE, chatColor.colors().get(0));
					}
					boolean enabled = chatColor.underlined();
					chatColor.setStrikethrough(!enabled);

					action.getWho().sendMessage(Component.text((enabled ? "Disabled" : "Enabled") + " strikethrough formatting in chat messages.", YELLOW));
					createFormatMenu(action.getWho());
				}).permission(MemberType.DONATOR.permissionOf("chatcolor.format.strikethrough"))
				.build();

		ClickableProvider reset = (p) -> Clickable.builder(Material.BARRIER, meta -> {
					meta.displayName(Component.text("Reset", YELLOW, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
					meta.lore(List.of(Component.text("Click to reset select formatting styles", YELLOW).decoration(TextDecoration.ITALIC, false)));
				}).actionGeneral(action -> {
					GDPlayer playerProfile = goldenDupe().playerDatabase().fromPlayer(action.getWho());
					GDChatColor chatColor = playerProfile.color();
					if (chatColor.equals(GDChatColor.DEFAULT)) {
						chatColor = new GDChatColor(GDChatColor.Mode.SINGLE, chatColor.colors().get(0));
					}
					chatColor.setBold(false);
					chatColor.setItalic(false);
					chatColor.setUnderlined(false);
					chatColor.setStrikethrough(false);

					playerProfile.setColor(chatColor);

					action.getWho().sendMessage(Component.text("Reset all chat formatting choices.", YELLOW));
					createFormatMenu(action.getWho());
				}).permission(Permission.of(MemberType.DONATOR.permissionOf("chatcolor.format.strikethrough"))
						.or(Permission.of(MemberType.DONATOR.permissionOf("chatcolor.format.underlined")))
						.or(Permission.of(MemberType.DONATOR.permissionOf("chatcolor.format.bold")))
						.or(Permission.of(MemberType.DONATOR.permissionOf("chatcolor.format.italic"))))
				.build();

		ClickableProvider italic = (p) -> Clickable.builder(Material.STICK, meta -> {
					GDPlayer playerProfile = goldenDupe().playerDatabase().fromPlayer(p);
					meta.displayName(Component.text("Italic", YELLOW, TextDecoration.ITALIC));
					meta.lore(List.of(Component.text("Click to " + ((playerProfile.color().underlined()) ? "disable" : "enable") + " italic formatting", YELLOW).decoration(TextDecoration.ITALIC, false)));
				}).actionGeneral(action -> {
					GDPlayer playerProfile = goldenDupe().playerDatabase().fromPlayer(action.getWho());
					GDChatColor chatColor = playerProfile.color();
					if (chatColor.equals(GDChatColor.DEFAULT)) {
						chatColor = new GDChatColor(GDChatColor.Mode.SINGLE, chatColor.colors().get(0));
					}
					boolean enabled = chatColor.underlined();
					chatColor.setItalic(!enabled);

					action.getWho().sendMessage(Component.text((enabled ? "Disabled" : "Enabled") + " italic formatting in your chat messages.", YELLOW));
					createFormatMenu(action.getWho());
				}).permission(MemberType.DONATOR.permissionOf("chatcolor.format.italic"))
				.build();
		ClickableProvider bold = (p) -> Clickable.builder(Material.MAP, meta -> {
					GDPlayer playerProfile = goldenDupe().playerDatabase().fromPlayer(p);
					meta.displayName(Component.text("Bold", YELLOW, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
					meta.lore(List.of(Component.text("Click to " + ((playerProfile.color().underlined()) ? "disable" : "enable") + " bold formatting", YELLOW).decoration(TextDecoration.ITALIC, false)));
				}).actionGeneral(action -> {
					GDPlayer playerProfile = goldenDupe().playerDatabase().fromPlayer(action.getWho());
					GDChatColor chatColor = playerProfile.color();
					if (chatColor.equals(GDChatColor.DEFAULT)) {
						chatColor = new GDChatColor(GDChatColor.Mode.SINGLE, chatColor.colors().get(0));
					}
					boolean enabled = chatColor.underlined();
					chatColor.setBold(!enabled);

					action.getWho().sendMessage(Component.text((enabled ? "Disabled" : "Enabled") + " bold formatting in your chat messages.", YELLOW));
					createFormatMenu(action.getWho());
				}).permission(MemberType.DONATOR.permissionOf("chatcolor.format.bold"))
				.build();

		profile.formatMenu = InventoryGUI.builder(ChestRows.ONE)
				.title(Component.text("Chat Color > Format"))
				.addClickable(0, underlined)
				.addClickable(2, strikethrough)
				.addClickable(4, reset)
				.addClickable(6, italic)
				.addClickable(8, bold)
				.replaceItemsEachOpen()
				.background(background)
				.replaceItemsEachOpen()
				.messenger(messenger).build();

	}

	private static ItemStack skull(String value) {
		ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
		PlayerProfile playerProfile = Bukkit.createProfile(UUID.randomUUID());
		playerProfile.setProperty(new ProfileProperty("textures", value));
		meta.setPlayerProfile(playerProfile);
		itemStack.setItemMeta(meta);
		return itemStack;
	}
}