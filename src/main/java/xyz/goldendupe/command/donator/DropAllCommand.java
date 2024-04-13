package xyz.goldendupe.command.donator;

import bet.astral.cloudplusplus.annotations.Cloud;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class DropAllCommand extends GDCloudCommand {
	public DropAllCommand(GoldenDupe plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		commandPlayer(
				commandBuilderPlayer("dropall",
						Description.of("Allows player to drop all their items at time."))
						.permission(MemberType.DONATOR.cloudOf("dropall"))
						.handler(context -> {
							Bukkit.getScheduler().runTask(plugin, () -> {
								Player player = context.sender();
								Vector vector = player.getEyeLocation().getDirection().multiply(0.34);
								Location location = player.getEyeLocation().add(0, -0.15, 0);
								for (ItemStack itemStack : player.getInventory().getContents()) {
									drop(player, location, vector, itemStack);
								}
								player.getInventory().clear();
							});
						})
		);
	}

	private void drop(Player player, Location location, Vector vector, ItemStack itemStack){
		if (itemStack == null){
			return;
		}
		location.getWorld().dropItem(location, itemStack, (item)->{
			item.setOwner(player.getUniqueId());
			item.setThrower(player.getUniqueId());
			item.setVelocity(vector);
		});
	}
}
