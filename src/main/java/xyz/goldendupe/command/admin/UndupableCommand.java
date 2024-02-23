package xyz.goldendupe.command.admin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.internal.legacy.GDCommand;
import xyz.goldendupe.command.internal.legacy.GDCommandInfo;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


@GDCommandInfo.Command(
		name = "undupable",
		senderType = GDCommandInfo.SenderType.PLAYER,
		memberType = GDCommandInfo.MemberType.ADMINISTRATOR,
		aliases = {"makeundupable"})
public class UndupableCommand extends GDCommand {
	private static final PlainTextComponentSerializer PLAIN = PlainTextComponentSerializer.plainText();
	private static final Component LINE = MiniMessage.miniMessage().deserialize("<red>This item is undupable!");
	protected UndupableCommand(GoldenDupe goldenDupe, GDCommandInfo commandInfo) {
		super(goldenDupe, commandInfo);
	}

	@Override
	public void execute(@NotNull CommandSender sender, @NotNull String[] args, boolean hasArgs) { }

	@Override
	public void execute(@NotNull Player sender, @NotNull String[] args, boolean hasArgs) {
		ItemStack itemStack = sender.getInventory().getItemInMainHand();
		if (itemStack.isEmpty()){
			commandMessenger.message(sender, "undupable.message-air");
			return;
		}
		ItemMeta meta = itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(itemStack.getType());
		PersistentDataContainer container = meta.getPersistentDataContainer();

		if (container.has(goldenDupe.KEY_UNDUPABLE)){
			commandMessenger.message(sender, "undupable.message-already-undupable");
			return;
		}
		container.set(goldenDupe.KEY_UNDUPABLE, PersistentDataType.BOOLEAN, true);

		List<Component> lore = meta.lore();
		if (lore != null){
			lore = new LinkedList<>();
			for (Component component : Objects.requireNonNull(meta.lore())){
				String line = PLAIN.serialize(component);
				if (line.equalsIgnoreCase("This item is undupable!")){
					continue;
				}
				lore.add(component);
			}

			lore.add(LINE);
		}

		meta.lore(lore);

		itemStack.setItemMeta(meta);
		commandMessenger.message(sender, "undupable.message-success");
	}

	@Override
	public List<String> tab(@NotNull CommandSender sender, @NotNull String[] args, boolean hasArgs) { return null; }

	@Override
	public List<String> tab(@NotNull Player sender, @NotNull String[] args, boolean hasArgs) {
		return Collections.emptyList();
	}
}
