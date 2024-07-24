package xyz.goldendupe.command.admin.spawn;

import bet.astral.messenger.v2.placeholder.Placeholder;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.minecraft.extras.suggestion.ComponentTooltipSuggestion;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.flag.CommandFlag;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.command.defaults.SpawnCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.models.impl.GDSpawn;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static xyz.goldendupe.messenger.GoldenMessenger.format;

@Cloud
public class SetSpawnCommand extends GDCloudCommand {

	//Not sure if I broke this or not but I changed some of the structure
	public SetSpawnCommand(GoldenDupeCommandRegister register, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(register, commandManager);
		commandManager.command(
				commandManager.commandBuilder(
								"setspawn",
								Description.of("Adds a new spawn to the spawn database.")
						)
						.senderType(Player.class)
						.permission("goldendupe.admin.setspawn")
						.argument(StringParser.stringComponent(StringParser.StringMode.SINGLE).name("setspawn-name")
								.suggestionProvider(
										new SuggestionProvider<>() {
											@Override
											public @NonNull CompletableFuture<? extends @NonNull Iterable<? extends @NonNull Suggestion>> suggestionsFuture(@NonNull CommandContext<Object> context, @NonNull CommandInput input) {
												return CompletableFuture.supplyAsync(() -> Arrays.stream(SpawnCommand.Spawn.values()).map(value -> ComponentTooltipSuggestion.suggestion(value.getName(), Component.text(value.getName()))).collect(Collectors.toList()));
											}
										}
								)
						)
						.flag(CommandFlag.builder("permission"))
						.handler(context -> {

							Player sender = context.sender();
							Location location = sender.getLocation();
							String spawnName = context.get("setspawn-name");
							boolean hasPermission = context.flags().hasFlag("permission");
							String permission = "goldendupe.spawn." + context.flags().get("permission");
							if (!hasPermission) permission = "";


							if (goldenDupe().getSpawnDatabase().exists(spawnName)) {
								messenger.message(sender, Translations.SPAWN_ALREADY_SET,
										Placeholder.of("spawn", spawnName));
								return;
							}

							goldenDupe().getSpawnDatabase().create(
									new GDSpawn(
											spawnName.toLowerCase(),
											permission,
											location.getX(),
											location.getY(),
											location.getZ(),
											location.getYaw(),
											location.getWorld()
									)
							);

							messenger.message(sender, Translations.SPAWN_SET,
									Placeholder.of("spawn", spawnName),
									Placeholder.of("x", format(location.getX())),
									Placeholder.of("y", format(location.getY())),
									Placeholder.of("z", format(location.getZ())),
									Placeholder.of("world", location.getWorld().getName()));
						})
		);
	}

}