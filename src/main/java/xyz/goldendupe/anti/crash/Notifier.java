package xyz.goldendupe.anti.crash;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import xyz.goldendupe.models.chatcolor.Color;
import xyz.goldendupe.utils.MemberType;

import java.util.Objects;

public class Notifier {
	public void notifyStaff(Notification notification) {
		Bukkit.broadcast(
				Component.text("AntiCrash", Color.GOLD, TextDecoration.BOLD)
						.appendSpace()
						.append(Component.text("Stopped potential crash from according. ", Color.GRAY))
						.append(Component.text("Type: ", Color.WHITE)).append(Component.text(notification.getType().name()))
						.append(Component.text("At: ", Color.WHITE))
						.append(Component.text("x: ", Color.GRAY).append(Component.text(notification.getLocation().getX(), Color.GREEN)).append(Component.text(", ", Color.GRAY))
								.append(Component.text("y: ", Color.GRAY).append(Component.text(notification.getLocation().getY(), Color.GREEN)).append(Component.text(", ", Color.GRAY))
										.append(Component.text("z: ", Color.GRAY).append(Component.text(notification.getLocation().getZ(), Color.GREEN)).append(Component.text(", ", Color.GRAY)))
										.append(Component.text("world: ", Color.GRAY)).append(Component.text(notification.getLocation().getWorld().getName(), Color.YELLOW)).append(Component.text(", ", Color.GRAY))))
						.append(Component.text("Player: ", Color.GRAY)).append(Component.text(notification.getPlayer() != null ? Objects.requireNonNull(notification.getPlayer().getName()) : "NONE", Color.RED))
						.append(Component.text("Nearby Players: ", Color.GRAY)
								.append(
										Component.textOfChildren(
												notification.getLocation().getWorld()
														.getNearbyPlayers(notification.getLocation(), 30)
														.stream()
														.map(CommandSender::name)
														.map(Component::appendSpace)
														.toArray(ComponentLike[]::new)
										)))
						.append(Component.text()).append(Component.text("(CLICK TO TELEPORT)"))
						.clickEvent(ClickEvent.runCommand("/anticrash teleport " + notification.getId()))
						.hoverEvent(HoverEvent.showText(Component.text("Click here to teleport to this location.", Color.GRAY).decoration(TextDecoration.ITALIC, false)))
						.appendSpace()
						.append(Component.text("DEALT WITH", Color.DARK_GREEN, TextDecoration.BOLD)
								.clickEvent(ClickEvent.runCommand("/anticrash dealtwith " + notification.getId()))
								.hoverEvent(HoverEvent.showText(Component.text("Click here to mark this anti crash notification as dealt with."))))
				,
				MemberType.MODERATOR.permissionOf("anti-crash")
		);
	}
}