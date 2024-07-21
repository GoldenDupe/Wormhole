package xyz.goldendupe.command.admin;

import bet.astral.cloudplusplus.annotations.Cloud;
import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.EnumParser;
import org.incendo.cloud.permission.Permission;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class WeatherCommand extends GDCloudCommand {
	public WeatherCommand(GoldenDupeBootstrap bootstrap, PaperCommandManager<CommandSender> commandManager) {
		super(bootstrap, commandManager);

	}

	public void abstractCommand(Weather weatherType, String name, String... aliases){
		command(
				commandManager.commandBuilder(name,
						"Allows changing the weather type to "+ name+".")
						.permission(Permission.of(MemberType.ADMINISTRATOR.permissionOf("weather."+name)))
						.required(EnumParser.enumComponent(Weather.class).name("weather"))
						.handler(context->{
							CommandSender commandSender = context.sender();
							Weather weather = context.get("weather");
							messenger.message(commandSender, weather.translation);
							if (commandSender instanceof Player player){
								weather.set(player.getWorld());
							} else {
								Bukkit.getWorlds().forEach(weather::set);
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
