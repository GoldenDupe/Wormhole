package xyz.goldendupe.listeners;

import io.papermc.paper.event.player.AsyncChatDecorateEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import xyz.goldendupe.GoldenDupe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class ChatUwUListener implements GDListener{
	private static final Random random = new Random(System.nanoTime()*321);
	private static GoldenDupe goldenDupe;
	private static List<Pattern> uwuPatterns = new ArrayList<>();
	private static List<String> randomUwuStrings = new ArrayList<>();
	protected ChatUwUListener(GoldenDupe goldenDupe) {
		this.goldenDupe = goldenDupe;

		for (String patternStr : goldenDupe.getSettings().getUwuString()){
			uwuPatterns.add(Pattern.compile("(?i)("+patternStr+")"));
			randomUwuStrings.add(patternStr);
		}
		uwuPatterns.add(Pattern.compile("(?i)uwu"));
	}

	public static Component uwuString() {
		boolean reverse = random.nextInt(1, 2) == 1;
		String newString = randomUwuStrings.get((randomUwuStrings.size()>2? random.nextInt(1, randomUwuStrings.size())-1 : 0));
		return MiniMessage.miniMessage().deserialize("<rainbow:"+(reverse ? "!" : "") + random.nextInt(1, 7)+">"+newString+"</rainbow>");
	}

	@Override
	public GoldenDupe goldenDupe() {
		return goldenDupe;
	}

	@EventHandler
	private void onChat(AsyncChatDecorateEvent chatDecorateEvent){
		if (randomUwuStrings.isEmpty()){
			return;
		}
		boolean reverse = random.nextInt(1, 2) == 1;
		Component message = chatDecorateEvent.result();
		String newString = randomUwuStrings.get((randomUwuStrings.size()>2? random.nextInt(1, randomUwuStrings.size())-1 : 0));
		Component component = MiniMessage.miniMessage().deserialize("<rainbow:"+(reverse ? "!" : "") + random.nextInt(1, 7)+">"+newString+"</rainbow>");
		for (Pattern pattern : uwuPatterns){
			message = message.replaceText(builder -> {
				builder.match(pattern).replacement(component);
			});
		}
		chatDecorateEvent.result(message);
	}
}
