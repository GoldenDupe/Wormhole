package xyz.goldendupe.messenger;

import bet.astral.messenger.Messenger;
import bet.astral.messenger.message.adventure.serializer.ComponentTypeSerializer;
import bet.astral.messenger.placeholder.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.permission.Permission;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupe;

import java.time.Duration;
import java.util.*;


public class GoldenMessenger extends Messenger<GoldenDupe> implements MessageLoader {
	private static final GoldenDupe gd;
	static  {
		gd = GoldenDupe.getPlugin(GoldenDupe.class);
	}
	public GoldenMessenger(CommandManager<CommandSender> commandManager, FileConfiguration messageConfig) {
		super(gd, commandManager, new HashMap<>(), new ComponentTypeSerializer(), messageConfig);
		setPlaceholderManager(new GoldenPlaceholderManager());
	}

	@Override
	@NotNull
	public GoldenPlaceholderManager getPlaceholderManager() {
		return (GoldenPlaceholderManager) super.getPlaceholderManager();
	}


	public List<Placeholder> createCooldownPlaceholders(long left){
		List<Placeholder> placeholders = new LinkedList<>();
		Duration durationLeft = Duration.ofMillis(left);
		placeholders.add(new Placeholder("%cooldown_millis%", durationLeft.toMillis()));
		placeholders.add(new Placeholder("%cooldown_seconds%", durationLeft.toSeconds()));
		placeholders.add(new Placeholder("%cooldown_minutes%", durationLeft.toMinutes()));
		placeholders.add(new Placeholder("%cooldown_hours%", durationLeft.toHours()));
		return placeholders;
	}

	public void broadcast(MessageChannel channel, String messageKey, int delay, List<Placeholder> placeholders){
		placeholders = new LinkedList<>(placeholders);
		broadcast(Permission.of(channel.permission), messageKey, delay, placeholders);
	}
	public void broadcast(MessageChannel channel, String messageKey, int delay, Placeholder... placeholders){
		broadcast(channel, messageKey, delay, Arrays.stream(placeholders).toList());
	}
	public void broadcast(MessageChannel channel, String messageKey, List<Placeholder> placeholders){
		broadcast(channel, messageKey, 0, placeholders);
	}
	public void broadcast(MessageChannel channel, String messageKey, Placeholder... placeholders){
		broadcast(channel, messageKey, 0, placeholders);
	}


	public enum MessageChannel {
		DONATOR("goldendupe.channel.donator", "channels.donator"),
		STAFF("goldendupe.channel.staff", "channels.staff"),
		ADMIN("goldendupe.channel.admin", "channels.admin"),
		OG("goldendupe.channel.og", "channels.og"),
		BOOSTER("goldendupe.channel.booster", "channels.booster")
		;

		private final String permission;
		private final String key;

		MessageChannel(String permission, String key) {
			this.permission = permission;
			this.key = key;
		}

		public String permission() {
			return permission;
		}

		public String key() {
			return key;
		}
	}
}














