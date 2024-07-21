package xyz.goldendupe.command.admin;

import bet.astral.cloudplusplus.annotations.Cloud;
import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.permission.Permission;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class WeatherCommand extends GDCloudCommand {
	public WeatherCommand(GoldenDupe plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);

		command(
				comma
		);
	}

	public void abstractCommand(WeatherType weatherType, String name, String... aliases){
		command(
				commandBuilder(name,
						"Allows changing the weather type to "+ name+".")
						.permission(Permission.of(MemberType.ADMINISTRATOR.permissionOf("weather."+name)))
						.handler(context->{
							CommandSender commandSender = context.sender();
							commandMessenger.message(commandSender, "weather.message-changed-weather-"+name);
							if (commandSender instanceof Player player){
								player.getWorld().weather
							}
						})
		);
	}
}
