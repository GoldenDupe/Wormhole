package xyz.goldendupe.command.donator;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.guiman.Clickable;
import bet.astral.guiman.ClickableBuilder;
import bet.astral.guiman.GUI;
import bet.astral.guiman.GUIBuilder;
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
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.models.chatcolor.Color;
import xyz.goldendupe.models.chatcolor.GDChatColor;
import xyz.goldendupe.utils.MemberType;

import java.lang.reflect.Field;
import java.util.*;

import static xyz.goldendupe.models.chatcolor.Color.MINECOIN;

@Cloud
public class ChatColorCommand extends GDCloudCommand {
	private GUI mainMenu;
	private final Clickable background;
	private final Map<UUID, MenuProfile> profiles = new HashMap<>();
	private final List<ClickableBuilder> preGeneratedButtons = new ArrayList<>();
	private final Map<Color, ClickableBuilder> generatedButtonsByColor = new HashMap<>();

	// https://minecraft-heads.com/custom-heads/alphabet?page=36
	// Declaring them here means the server will lag during players are online
	private static final ItemStack ONE = skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDliMzAzMDNmOTRlN2M3ODVhMzFlNTcyN2E5MzgxNTM1ZGFmNDc1MzQ0OWVhNDFkYjc0NmUxMjM0ZTlkZDJiNSJ9fX0=");
	private static final ItemStack TWO = skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTNjYTdkN2MxNTM0ZGM2YjllZDE2NDdmOTAyNWRkZjI0NGUwMTA3ZGM4ZGQ0ZjRmMDg1MmM4MjA4MWQ2MzUwZSJ9fX0=");
	private static final ItemStack THREE = skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTk1ZTFlMmZiMmRlN2U2Mjk5YTBmNjFkZGY3ZDlhNmQxMDFmOGQ2NjRmMTk1OWQzYjY3ZGNlOGIwNDlhOGFlMSJ9fX0=");
	private static final ItemStack FOUR = skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzgzOWVhZjllZTA3NjcwNjA3ZDNkYTRkNmMxZDMwZmU1OWRiNTY0NThmYWQ1ZjU1YjU0MTJkNTZiM2RlYjU1OSJ9fX0=");
	private static final ItemStack FIVE = skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTgxNDBlYzQ4NDU2MzFhODlmZmU4MzQ0YWU5OGQxMDQ5YjgyYzIxNTkzOTMyOTBiZDM4YThhMDA4NDY1YjNkOSJ9fX0=");
	private static final ItemStack SIX = skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGViZjliNmE2YzRlYjBjYmZkMGEzZDM3YzM0YzQ1ODYwNjgxZjEzZjcyNzBmMTMzN2ZkMTM2YTcwMTE4OThmMCJ9fX0=");
	private static final ItemStack SEVEN = skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjRjMmQ2Y2JkZmYwMGI5N2FmN2Y4Y2ZlODc2N2ZkODdjZDY1NjM5YWJkZjgzZWMxMDM5YTQ2NDE1ZTY5ZTM5OCJ9fX0=");
	private static final ItemStack EIGHT = skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQyNzkxNzUyZjY3YTY0NTBiOTc1ZDM1NzQxMjM1NmIwYTk5ZTM1NTVlNjdlYmRhZDJkNDAzMjYxMjliZGRhNCJ9fX0=");
	private static final ItemStack NINE = skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjRjMmQ2Y2JkZmYwMGI5N2FmN2Y4Y2ZlODc2N2ZkODdjZDY1NjM5YWJkZjgzZWMxMDM5YTQ2NDE1ZTY5ZTM5OCJ9fX0=");
	private static final ItemStack LEFT = skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODFjOTZhNWMzZDEzYzMxOTkxODNlMWJjN2YwODZmNTRjYTJhNjUyNzEyNjMwM2FjOGUyNWQ2M2UxNmI2NGNjZiJ9fX0=");
	private static final ItemStack RIGHT = skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzMzYWU4ZGU3ZWQwNzllMzhkMmM4MmRkNDJiNzRjZmNiZDk0YjM0ODAzNDhkYmI1ZWNkOTNkYThiODEwMTVlMyJ9fX0=");


	public ChatColorCommand(GoldenDupeBootstrap bootstrap, PaperCommandManager<CommandSender> commandManager) {
		super(bootstrap, commandManager);

		ItemStack backgroundItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		backgroundItem.editMeta(meta -> {
			meta.displayName(Component.text(""));
			meta.addItemFlags(ItemFlag.values());
		});
		background = new ClickableBuilder(backgroundItem).setPriority(0).build();

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
							.setData("color", color)
							.setData("slot", chatColor.slot())
							.setData("name", displayname);

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

		Command.Builder<Player> chatColorBuilder = commandManager.commandBuilder("chatcolor", Description.of("Allows donators to change their chat color."), "chatcolour", "chatformat")
				.senderType(Player.class)
				.handler(context -> {
					profiles.putIfAbsent(context.sender().getUniqueId(), new MenuProfile());
					Bukkit.getScheduler().runTask(goldenDupe(), () -> createMainMenu(context.sender()));
				});
		commandManager.command(chatColorBuilder);
	}

	private static class MenuProfile {
		GUI gradientPositionMenu;
		GUI singleMenu;
		GUI rainbowMenu;
		GUI formatMenu;
		Map<Integer, GUI> gradientMenus = new HashMap<>();

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

			List<ClickType> clickTypes = List.of(ClickType.RIGHT, ClickType.LEFT, ClickType.SHIFT_RIGHT, ClickType.SHIFT_LEFT);
			Clickable gradientBuilder = new ClickableBuilder(gradient).setAction(clickTypes, (clickable, item, playerCon) -> createGradientMenu(playerCon)).setPermission(MemberType.DONATOR.permissionOf("chatcolor.gradient")).build();
			Clickable singleBuilder = new ClickableBuilder(single).setAction(clickTypes, (clickable, item, playerCon) -> createSingleMenu(playerCon)).setPermission(MemberType.DONATOR.permissionOf("chatcolor.color")).build();
			Clickable rainbowBuilder = new ClickableBuilder(rainbow).setAction(clickTypes, (clickable, item, playerCon) -> createRainbowMenu(playerCon)).setPermission(MemberType.DONATOR.permissionOf("chatcolor.rainbow")).build();
			Clickable formatBuilder = new ClickableBuilder(format).setAction(clickTypes, (clickable, item, playerCon) -> createFormatMenu(playerCon)).setPermission(MemberType.DONATOR.permissionOf("chatcolor.format")).build();

			mainMenu = new GUIBuilder(2)
					.name(Component.text("Chat Color"))
					.addSlotClickable(1, gradientBuilder)
					.addSlotClickable(3, singleBuilder)
					.addSlotClickable(5, rainbowBuilder)
					.addSlotClickable(7, formatBuilder)
					.addSlotClickable(13, new ClickableBuilder(Material.BARRIER, (meta) -> {
								meta.displayName(Component.text("Reset Everything", Color.DARK_RED).decoration(TextDecoration.ITALIC, false));
							})
									.setAction(clickTypes, (clickable, item, p) -> {
										GDPlayer gdPlayer = goldenDupe().playerDatabase().fromPlayer(p);
										GDChatColor chatColor = gdPlayer.color();
										chatColor.setColors(null);
										chatColor.reset();
										p.closeInventory();
									})
					)
					.setBackground(background)
					.build();

			mainMenu.shared = false;
		}
		mainMenu.generateInventory(player);
	}

	private void createGradientMenu(Player player) {
		MenuProfile profile = profiles.get(player.getUniqueId());
		if (profile.gradientPositionMenu != null) {
			profile.gradientPositionMenu.generateInventory(player);
			return;
		}


		List<ClickType> clickTypes = List.of(ClickType.RIGHT, ClickType.LEFT, ClickType.SHIFT_RIGHT, ClickType.SHIFT_LEFT);
		GUIBuilder builder = new GUIBuilder(3)
				.name(Component.text("Chat Color > Gradient"));
		for (int i = 0; i < 9; i++) {
			int finalI = i;
			ItemStack item = new ItemStack(Material.PAPER);
			item.editMeta(meta -> meta.displayName(Component.text("Set gradient position " + finalI + " color", MINECOIN).decoration(TextDecoration.ITALIC, false).compact()));

			Clickable clickable = new ClickableBuilder(item).setAction(clickTypes, (cl, itemStack, p) -> createGradientSelect(player, finalI)).setPermission(MemberType.DONATOR.permissionOf("chatcolor.gradient.position." + (i + 1))).build();
			builder.setSlotClickable(i, clickable);


		}
		profile.gradientPositionMenu = builder
				.addSlotClickable(13, new ClickableBuilder(Material.BARRIER, (meta) -> {
							meta.displayName(Component.text("Reset Positions", Color.RED).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
						})
								.setAction(clickTypes, (clickable, item, p) -> {
									GDPlayer gdPlayer = goldenDupe().playerDatabase().fromPlayer(p);
									gdPlayer.color().setColors(null);
									gdPlayer.color().setMode(GDChatColor.Mode.SINGLE);
									p.sendMessage(Component.text("Reset your gradient color selections!", Color.RED));
								})
				)

				.setBackground(background)
				.build();

		profile.gradientPositionMenu.shared = false;
		profile.gradientPositionMenu.generateInventory(player);
	}

	private void createSingleMenu(Player player) {
		MenuProfile profile = profiles.get(player.getUniqueId());
		if (profile.singleMenu != null) {
			profile.singleMenu.generateInventory(player);
			return;
		}

		List<ClickType> clickTypes = List.of(ClickType.RIGHT, ClickType.LEFT, ClickType.SHIFT_RIGHT, ClickType.SHIFT_LEFT);

		GUIBuilder guiBuilder = new GUIBuilder(4).name(Component.text("Chat Color > Single").decoration(TextDecoration.ITALIC, false).compact());
		for (ClickableBuilder builder : this.preGeneratedButtons) {
			ClickableBuilder slotBuilder = builder.clone();
			slotBuilder.setAction(clickTypes, (clickable, item, p) -> {
				GDPlayer gdPlayer = goldenDupe().playerDatabase().fromPlayer(player);

				Color color = (Color) clickable.getData("color");

				Component name = (Component) clickable.getData("name");
				p.sendMessage(Component.text("Set your chat color to ", MINECOIN).append(name).decoration(TextDecoration.ITALIC, false).compact());

				GDChatColor chatColor = gdPlayer.color();
				if (chatColor.equals(GDChatColor.DEFAULT)) {
					chatColor = new GDChatColor(GDChatColor.Mode.SINGLE, color);
				}
				chatColor.colors().put(0, color);
				chatColor.setMode(GDChatColor.Mode.SINGLE);
				gdPlayer.setColor(chatColor);
				player.closeInventory();
			});
			@SuppressWarnings("DataFlowIssue") int slot = (int) slotBuilder.getData("slot");
			guiBuilder.setSlotClickable(slot, slotBuilder.build());
		}
		guiBuilder.setBackground(background);

		profile.singleMenu = guiBuilder.createGUI();
		profile.singleMenu.shared = false;
		profile.singleMenu.generateInventory(player);
	}

	private void createRainbowMenu(Player player) {
		MenuProfile profile = profiles.get(player.getUniqueId());
		if (profile.rainbowMenu != null) {
			profile.rainbowMenu.generateInventory(player);
			return;
		}

		List<ClickType> clickTypes = List.of(ClickType.RIGHT, ClickType.LEFT, ClickType.SHIFT_RIGHT, ClickType.SHIFT_LEFT);
		profile.rainbowMenu = new GUIBuilder(2).name(Component.text("Chat Color > Rainbow"))
				.addSlotClickable(0, rainbowButton(0, ONE))
				.addSlotClickable(1, rainbowButton(1, TWO))
				.addSlotClickable(2, rainbowButton(2, THREE))
				.addSlotClickable(3, rainbowButton(3, FOUR))
				.addSlotClickable(4, rainbowButton(4, FIVE))
				.addSlotClickable(5, rainbowButton(5, SIX))
				.addSlotClickable(6, rainbowButton(6, SEVEN))
				.addSlotClickable(7, rainbowButton(7, EIGHT))
				.addSlotClickable(8, rainbowButton(8, NINE))
				.addSlotClickable(11, rainbowReverse(true, LEFT))
				.addSlotClickable(15, rainbowReverse(false, RIGHT))
				.addSlotClickable(13, new ClickableBuilder(Material.BARRIER, (meta)->meta.displayName(Component.text("Reset Rainbow Settings")))
						.setAction(clickTypes, (clickable, item, p)->{
							GDPlayer gdPlayer = goldenDupe().playerDatabase().fromPlayer(p);
							gdPlayer.color().setRainbowMode(0).setRainbowReversed(false);
							p.sendMessage(Component.text("Reset your settings for your rainbow chat color.", Color.GREEN));
						})
				)
				.setBackground(background)
				.build();
		profile.rainbowMenu.shared = false;
		profile.rainbowMenu.generateInventory(player);
	}

	private Clickable rainbowButton(int slot, ItemStack itemStack) {
		List<ClickType> clickTypes = List.of(ClickType.RIGHT, ClickType.LEFT, ClickType.SHIFT_RIGHT, ClickType.SHIFT_LEFT);
		MiniMessage mm = MiniMessage.miniMessage();
		ItemStack item = itemStack.clone();
		item.editMeta(meta -> {
			meta.displayName(Component.text("Rainbow start position " + (slot + 1), MINECOIN).decoration(TextDecoration.ITALIC, false));
			List<Component> list = new ArrayList<>();
			list.add(mm.deserialize("<!italic>Forward: <rainbow:" + slot + ">|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||").compact());
			list.add(mm.deserialize("<!italic>Reversed: <rainbow:!" + slot + ">|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||").compact());
			meta.lore(list);
		});
		return new ClickableBuilder(item).setAction(clickTypes, (clickable, i, player) -> {
					GDPlayer gdPlayer = goldenDupe().playerDatabase().fromPlayer(player);
					GDChatColor chatColor = gdPlayer.color();
					if (chatColor.equals(GDChatColor.DEFAULT)) {
						chatColor = new GDChatColor(GDChatColor.Mode.RAINBOW, GDChatColor.DEFAULT.colors().get(0));
					}
					if (chatColor.mode() != GDChatColor.Mode.RAINBOW) {
						player.sendMessage(Component.text("Enabled rainbow chat color", MINECOIN));
					}
					chatColor.setMode(GDChatColor.Mode.RAINBOW);
					chatColor.setRainbowMode(slot);
					gdPlayer.setColor(chatColor);
					player.sendMessage(Component.text("Set rainbow start position to " + slot, MINECOIN));
				}).setDisplayIfNoPermissions(true)
				.setPermission(MemberType.DONATOR.permissionOf("chatcolor.rainbow.position." + slot))
				.build();
	}

	private Clickable rainbowReverse(boolean reverse, ItemStack itemStack) {
		List<ClickType> clickTypes = List.of(ClickType.RIGHT, ClickType.LEFT, ClickType.SHIFT_RIGHT, ClickType.SHIFT_LEFT);
		MiniMessage mm = MiniMessage.miniMessage();
		ItemStack item = itemStack.clone();
		item.editMeta(meta -> {
			meta.displayName(Component.text("Set the rainbow to " + (reverse ? "reversed" : "forward"), MINECOIN).decoration(TextDecoration.ITALIC, false).compact());
			List<Component> list = new ArrayList<>();
			list.add(mm.deserialize((reverse ? "<gray>" : "<green>") + "<!italic>Forward: <rainbow:>|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||"));
			list.add(mm.deserialize((!reverse ? "<gray>" : "<green>") + "<!italic>Reversed: <rainbow:!>|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||"));
			meta.lore(list);
		});
		return new ClickableBuilder(item).setAction(clickTypes, (clickable, i, player) -> {
					GDPlayer gdPlayer = goldenDupe().playerDatabase().fromPlayer(player);
					GDChatColor chatColor = gdPlayer.color();
					if (chatColor.equals(GDChatColor.DEFAULT)) {
						chatColor = new GDChatColor(GDChatColor.Mode.RAINBOW, GDChatColor.DEFAULT.colors().get(0));
					}
					if (chatColor.mode() != GDChatColor.Mode.RAINBOW) {
						player.sendMessage(Component.text("Enabled rainbow chat color", Color.MINECOIN));
					}
					chatColor.setMode(GDChatColor.Mode.RAINBOW);
					chatColor.setRainbowReversed(reverse);
					gdPlayer.setColor(chatColor);
					player.sendMessage(Component.text("Set rainbow type to " + (reverse ? "reserved" : "forward"), MINECOIN));
				})
				.setDisplayIfNoPermissions(true)
				.setPermission(MemberType.DONATOR.permissionOf("chatcolor.rainbow." + (reverse ? "reverse" : "forward")))
				.build();
	}

	private void createGradientSelect(Player player, int data) {
		MenuProfile profile = profiles.get(player.getUniqueId());
		if (profile.gradientMenus.get(data) != null) {
			profile.gradientMenus.get(data).generateInventory(player);
			return;
		}

		List<ClickType> clickTypes = List.of(ClickType.RIGHT, ClickType.LEFT, ClickType.SHIFT_RIGHT, ClickType.SHIFT_LEFT);

		GUIBuilder guiBuilder = new GUIBuilder(4).name(Component.text("... > Gradient > " + (data + 1) + " Color").decoration(TextDecoration.ITALIC, false).compact());
		for (ClickableBuilder builder : this.preGeneratedButtons) {
			ClickableBuilder slotBuilder = builder.clone();
			slotBuilder.setAction(clickTypes, (clickable, item, p) -> {
				GDPlayer gdPlayer = goldenDupe().playerDatabase().fromPlayer(player);

				Color color = (Color) clickable.getData("color");

				Component name = (Component) clickable.getData("name");
				p.sendMessage(Component.text("Set your gradient position " + (data + 1) + " color to ", MINECOIN).append(name).decoration(TextDecoration.ITALIC, false).compact());

				GDChatColor chatColor = gdPlayer.color();
				if (chatColor.equals(GDChatColor.DEFAULT)) {
					chatColor = new GDChatColor(GDChatColor.Mode.SINGLE, color);
				}
				if (chatColor.mode() != GDChatColor.Mode.GRADIENT) {
					p.sendMessage(Component.text("Enabled gradient chat color", MINECOIN));
				}
				chatColor.colors().put(data, color);
				chatColor.setMode(GDChatColor.Mode.GRADIENT);
				gdPlayer.setColor(chatColor);

				profile.gradientPositionMenu.generateInventory(player);
			});
			@SuppressWarnings("DataFlowIssue") int slot = (int) slotBuilder.getData("slot");
			guiBuilder.setSlotClickable(slot, slotBuilder.build());
		}

		ItemStack reset = new ItemStack(Material.PAPER);
		reset.editMeta(meta -> {
			meta.displayName(Component.text("RESET", NamedTextColor.RED).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.ITALIC, false));
		});
		guiBuilder.addSlotClickable(35, new ClickableBuilder(reset).setAction(clickTypes, (clickable, item, p) -> {
			GDPlayer gdPlayer = goldenDupe().playerDatabase().fromPlayer(p);
			gdPlayer.color().colors().put(data, null);
			player.sendMessage(Component.text("Removed your gradient position " + (data + 1) + ".", MINECOIN));
			profile.gradientPositionMenu.generateInventory(player);
		}).build());
		guiBuilder.setBackground(background);

		GUI gui = guiBuilder.createGUI();
		gui.shared = false;
		gui.generateInventory(player);
		profile.gradientMenus.put(data, gui);
	}

	private void createFormatMenu(Player player) {
		MenuProfile profile = profiles.get(player.getUniqueId());
		if (profile.formatMenu != null) {
			profile.formatMenu.generateInventory(player);
			return;
		}
		GDPlayer gdPlayer = goldenDupe().playerDatabase().fromPlayer(player);
		List<ClickType> clickTypes = List.of(ClickType.SHIFT_LEFT, ClickType.SHIFT_RIGHT, ClickType.RIGHT, ClickType.LEFT);

		ItemStack underlined = new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
		underlined.editMeta(meta -> {
			meta.displayName(Component.text("Underlined", MINECOIN, TextDecoration.UNDERLINED).decoration(TextDecoration.ITALIC, false));
			meta.lore(List.of(Component.text("Click to " + ((gdPlayer.color().underlined()) ? "disable" : "enable") + " underlined messages", MINECOIN).decoration(TextDecoration.ITALIC, false)));
		});
		ItemStack strike = new ItemStack(Material.CHAIN);
		strike.editMeta(meta -> {
			meta.displayName(Component.text("Strikethrough", MINECOIN, TextDecoration.STRIKETHROUGH).decoration(TextDecoration.ITALIC, false));
			meta.lore(List.of(Component.text("Click to " + ((gdPlayer.color().underlined()) ? "disable" : "enable") + " strikethrough messages", MINECOIN).decoration(TextDecoration.ITALIC, false)));
		});
		ItemStack italic = new ItemStack(Material.STICK);
		italic.editMeta(meta -> {
			meta.displayName(Component.text("Italic", MINECOIN, TextDecoration.ITALIC));
			meta.lore(List.of(Component.text("Click to " + ((gdPlayer.color().underlined()) ? "disable" : "enable") + " italic messages", MINECOIN).decoration(TextDecoration.ITALIC, false)));
		});
		ItemStack bold = new ItemStack(Material.MAP);
		bold.editMeta(meta -> {
			meta.displayName(Component.text("Bold", MINECOIN, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
			meta.lore(List.of(Component.text("Click to " + ((gdPlayer.color().underlined()) ? "disable" : "enable") + " bold messages", MINECOIN).decoration(TextDecoration.ITALIC, false)));
		});

		ItemStack reset = new ItemStack(Material.BARRIER);
		reset.editMeta(meta -> {
			meta.displayName(Component.text("Reset", MINECOIN, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
			meta.lore(List.of(Component.text("Click to reset select formatting styles", MINECOIN).decoration(TextDecoration.ITALIC, false)));
		});


		profile.formatMenu = new GUIBuilder(1)
				.name(Component.text("Chat Color > Format"))
				.addSlotClickable(0, new ClickableBuilder(underlined).setAction(clickTypes, (clickable, item, p) -> {
							GDChatColor chatColor = gdPlayer.color();
							if (chatColor.equals(GDChatColor.DEFAULT)) {
								chatColor = new GDChatColor(GDChatColor.Mode.SINGLE, chatColor.colors().get(0));
							}
							boolean enabled = chatColor.underlined();
							chatColor.setUnderlined(!enabled);

							clickable.getItemStack().editMeta(meta -> {
								meta.lore(List.of(Component.text("Click to " + (!enabled ? "disable" : "enable") + " underlined messages", MINECOIN).decoration(TextDecoration.ITALIC, false)));
							});
							player.sendMessage(Component.text((enabled ? "Disabled" : "Enabled") + " underlined messages in chat messages.", MINECOIN));
							createFormatMenu(player);
						})
						.setPermission(MemberType.DONATOR.permissionOf("chatcolor.format.underlined"))
						.build())
				.addSlotClickable(2, new ClickableBuilder(strike).setAction(clickTypes, (clickable, item, p) -> {
							GDChatColor chatColor = gdPlayer.color();
							if (chatColor.equals(GDChatColor.DEFAULT)) {
								chatColor = new GDChatColor(GDChatColor.Mode.SINGLE, chatColor.colors().get(0));
							}
							boolean enabled = chatColor.strikethrough();
							chatColor.setStrikethrough(!enabled);
							gdPlayer.setColor(chatColor);

							clickable.getItemStack().editMeta(meta -> {
								meta.lore(List.of(Component.text("Click to " + (!enabled ? "disable" : "enable") + " strikethrough messages", MINECOIN).decoration(TextDecoration.ITALIC, false)));
							});
							player.sendMessage(Component.text((enabled ? "Disabled" : "Enabled") + " strikethrough messages in chat messages.", MINECOIN));
							createFormatMenu(player);
						})
						.setPermission(MemberType.DONATOR.permissionOf("chatcolor.format.strikethrough"))
						.build())
				.addSlotClickable(6, new ClickableBuilder(italic).setAction(clickTypes, (clickable, item, p) -> {
							GDChatColor chatColor = gdPlayer.color();
							if (chatColor.equals(GDChatColor.DEFAULT)) {
								chatColor = new GDChatColor(GDChatColor.Mode.SINGLE, chatColor.colors().get(0));
							}
							boolean enabled = chatColor.italic();
							chatColor.setItalic(!enabled);
							gdPlayer.setColor(chatColor);

							clickable.getItemStack().editMeta(meta -> {
								meta.lore(List.of(Component.text("Click to " + (!enabled ? "disable" : "enable") + " italic messages", MINECOIN).decoration(TextDecoration.ITALIC, false)));
							});
							player.sendMessage(Component.text(((enabled) ? "Disabled" : "Enabled") + " italic messages in chat messages.", MINECOIN));
							createFormatMenu(player);
						})
						.setPermission(MemberType.DONATOR.permissionOf("chatcolor.format.italic"))
						.build())
				.addSlotClickable(8, new ClickableBuilder(bold).setAction(clickTypes, (clickable, item, p) -> {
							GDChatColor chatColor = gdPlayer.color();
							if (chatColor.equals(GDChatColor.DEFAULT)) {
								chatColor = new GDChatColor(GDChatColor.Mode.SINGLE, chatColor.colors().get(0));
							}
							boolean enabled = chatColor.bold();
							chatColor.setBold(!enabled);
							gdPlayer.setColor(chatColor);

							clickable.getItemStack().editMeta(meta -> {
								meta.lore(List.of(Component.text("Click to " + ((!enabled) ? "disable" : "enable") + " bold messages", MINECOIN).decoration(TextDecoration.ITALIC, false)));
							});
							player.sendMessage(Component.text(((enabled) ? "Disabled" : "Enabled") + " bold messages in chat messages.", MINECOIN));
							createFormatMenu(player);
						}).setPermission(MemberType.DONATOR.permissionOf("chatcolor.format.bold"))

						.build())
				.addSlotClickable(4, new ClickableBuilder(reset)
						.setAction(clickTypes, (clickable, item, p) -> {
							GDChatColor chatColor = gdPlayer.color();
							if (chatColor.equals(GDChatColor.DEFAULT)) {
								chatColor = new GDChatColor(GDChatColor.Mode.SINGLE, chatColor.colors().get(0));
							}
							chatColor.setBold(false);
							chatColor.setItalic(false);
							chatColor.setUnderlined(false);
							chatColor.setStrikethrough(false);

							gdPlayer.setColor(chatColor);

							player.sendMessage(Component.text("Reset all chat formatting choices.", MINECOIN));
							createFormatMenu(player);
						})
						.setPermission(MemberType.DONATOR.permissionOf("chatcolor.format.reset"))
						.build()
				)

				.replaceItemsEachOpen(true)
				.setBackground(background)
				.build();

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