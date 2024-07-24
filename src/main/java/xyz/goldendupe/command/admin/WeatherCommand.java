package xyz.goldendupe.command.admin;

import bet.astral.cloudplusplus.annotations.Cloud;
import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.permission.Permission;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class WeatherCommand extends GDCloudCommand {
	public WeatherCommand(GoldenDupeCommandRegister register, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(register, commandManager);
		abstractCommand(Weather.SUN, "sun", "sunny");
		abstractCommand(Weather.RAIN, "rain", "rainy");
		abstractCommand(Weather.THUNDER, "thunder", "storm");
	}

	public void abstractCommand(Weather weatherType, String name, String... aliases){
		command(
				commandManager.commandBuilder(name,
						Description.of("Allows changing the weather type to "+ name+"."))
						.permission(Permission.of(MemberType.ADMINISTRATOR.permissionOf("weather."+name)))
						.handler(context->{
							CommandSender commandSender = context.sender();
							messenger.message(commandSender, weatherType.translation);
							if (commandSender instanceof Player player){
								weatherType.set(player.getWorld());
							} else {
								Bukkit.getWorlds().forEach(weatherType::set);
							}
						})
		);
	}

	enum Weather {
		SUN(WeatherType.CLEAR, Translations.COMMAND_SUN_CHANGED) {
			@Override
			public void set(World world) {
				world.setClearWeatherDuration(24000);
			}
		},
		RAIN(WeatherType.DOWNFALL, Translations.COMMAND_RAIN_CHANGED) {
			@Override
			public void set(World world) {
				world.setClearWeatherDuration(0);
			}
		},
		THUNDER(WeatherType.CLEAR, Translations.COMMAND_THUNDER_CHANGED) {
			@Override
			public void set(World world) {
				world.setClearWeatherDuration(0);
				world.setThundering(true);
			}
		},
		;
		private final WeatherType type;
		private final Translations.Translation translation;
		Weather(WeatherType type, Translations.Translation translation) {
			this.type = type;
			this.translation = translation;
		}

		public void set(World world){
		}
	}
}
