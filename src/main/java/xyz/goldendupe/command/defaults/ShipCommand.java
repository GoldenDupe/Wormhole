package xyz.goldendupe.command.defaults;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.placeholder.Placeholder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.utils.MemberType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


@Cloud
public class ShipCommand extends GDCloudCommand {
	private final Random random = new Random(System.currentTimeMillis());

	public ShipCommand(GoldenDupeBootstrap bootstrap, PaperCommandManager<CommandSender> commandManager) {
		super(bootstrap, commandManager);
		commandManager.command(
				commandManager.commandBuilder("ship", Description.of("Allows a player to ship 2 different players together"),
						"fuck")
						.permission(MemberType.DEFAULT.permissionOf("ship"))
						.argument(StringParser.stringComponent(StringParser.StringMode.QUOTED).name("player-one"))
						.argument(StringParser.stringComponent(StringParser.StringMode.QUOTED).name("player-two"))
						.handler(context->{
							String playerOne = context.get("player-one");
							String playerTwo = context.get("player-two");
							List<Placeholder> placeholders = new LinkedList<>();
							placeholders.add(Placeholder.of("who", playerOne));
							placeholders.add(Placeholder.of("who-2", playerTwo));
							placeholders.addAll(createMatch("progress-bar-0"));
							placeholders.addAll(createMatch("progress-bar-1"));
							placeholders.addAll(createMatch("progress-bar-2"));
							placeholders.addAll(createMatch("progress-bar-3"));
							placeholders.addAll(createMatch("progress-bar-4"));
							placeholders.addAll(createMatch("progress-bar-5"));
							placeholders.addAll(createMatch("progress-bar-6"));
							placeholders.addAll(createMatch("progress-bar-7"));
							placeholders.addAll(createMatch("progress-bar-8"));
							placeholders.addAll(createMatch("progress-bar-9"));
							placeholders.addAll(createMatch("progress-bar-10"));
							commandMessenger.message(context.sender(), Translations.COMMAND_SHIP, placeholders);
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
		placeholders.add(Placeholder.of(name+"-bar", MiniMessage.miniMessage().deserialize(bar)));
		placeholders.add(Placeholder.of(name+"-int", random));
		return placeholders;
	}

}
