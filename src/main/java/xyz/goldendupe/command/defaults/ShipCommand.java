package xyz.goldendupe.command.defaults;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.placeholder.Placeholder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.bukkit.parser.PlayerParser;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.cloud.GDCloudCommand;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


@Cloud
public class ShipCommand extends GDCloudCommand {
	private final Random random = new Random(System.currentTimeMillis());

	public ShipCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
		super(goldenDupe, commandManager);
		commandManager.command(
				commandManager.commandBuilder("ship", Description.of("Allows a player to ship 2 different players together"),
						"fuck")
						.argument(PlayerParser.playerComponent().name("player-one"))
						.argument(PlayerParser.playerComponent().name("player-two"))
						.handler(context->{
							Player playerOne = context.get("player-one");
							Player playerTwo = context.get("player-two");
							List<Placeholder> placeholders = new LinkedList<>();
							placeholders.add(new Placeholder("who", playerOne.name()));
							placeholders.add(new Placeholder("who-2", playerTwo.name()));
							placeholders.addAll(commandMessenger.createPlaceholders("who", playerOne));
							placeholders.addAll(commandMessenger.createPlaceholders("who-2", playerOne));
							placeholders.addAll(createMatch("personality"));
							placeholders.addAll(createMatch("passion"));
							placeholders.addAll(createMatch("friendship"));
							placeholders.addAll(createMatch("chemistry"));
							commandMessenger.message(context.sender(), "ship.message-shipped", placeholders);
						})
		);
	}


	private List<Placeholder> createMatch(String name){
		int random = this.random.nextInt(0, 100);
		String bar = "|".repeat(100);
		if (random != 0) {
			StringBuilder builder = new StringBuilder();
			builder.append("<rainbow:");
			if (this.random.nextInt(1, 2) == 1)
				builder.append("!");
			builder.append(this.random.nextInt(1, 18)).append(">");
			for (int i = 1; i < 100; i++) {
				if (i == random) {
					builder.append("</rainbow>");
				}
				builder.append("|");
			}
			bar = builder.toString();
		}
		bar = "<gray>"+bar;
		List<Placeholder> placeholders = new ArrayList<>();
		placeholders.add(new Placeholder(name+"_match", MiniMessage.miniMessage().deserialize(bar)));
		placeholders.add(new Placeholder(name+"_int", random));
		return placeholders;
	}

}
