package bet.astral.goldenmessenger;

import bet.astral.messagemanager.Message;
import bet.astral.messagemanager.MessageManager;
import bet.astral.messagemanager.permission.Permission;
import bet.astral.messagemanager.placeholder.LegacyPlaceholder;
import bet.astral.messagemanager.placeholder.MessagePlaceholder;
import bet.astral.messagemanager.placeholder.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupe;

import java.time.Duration;
import java.util.*;


public class GoldenMessenger extends MessageManager<GoldenDupe> implements MessageLoader, IMessenger {
	private final GoldenDupe gd;
	private final boolean isDebugMessenger;
	private final boolean vaultExists;
	public GoldenMessenger(GoldenDupe main, FileConfiguration messageConfig, boolean isDebugMessenger) {
		this(main, messageConfig, new HashMap<>(), isDebugMessenger);
	}
	public GoldenMessenger(GoldenDupe main, FileConfiguration messageConfig, Map<String, Message> messageMap, boolean isDebugMessenger) {
		super(main, messageConfig, messageMap, "placeholders");
		this.gd = main;
		this.isDebugMessenger = isDebugMessenger;
		this.vaultExists = gd.vaultChat() != null;
	}

	@Override
	public List<Placeholder> createPlaceholders(Player player) {
		return this.createPlaceholders("player", player);
	}

	@Override
	public List<Placeholder> createPlaceholders(String name, LivingEntity entity) {
		if (entity instanceof Player player){
			return this.createPlaceholders(name, player);
		}
		return super.createPlaceholders(name, entity);
	}

	@Override
	public List<Placeholder> createPlaceholders(String name, Player player) {
		List<Placeholder> placeholders = new LinkedList<>(super.createPlaceholders(name, player));
		if (vaultExists){
			String prefix = gd.vaultChat().getPlayerPrefix(player);
			placeholders.add(new LegacyPlaceholder(name+"_prefix", prefix));
			String suffix = gd.vaultChat().getPlayerPrefix(player);
			placeholders.add(new LegacyPlaceholder(name+"_suffix", suffix));
		}
		return placeholders;
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
		MessagePlaceholder placeholder = placeholderMessage("message-channel-info", messageKey, Message.Type.CHAT);

		Message message = getMessage(channel.key);
		if (message == null){
			broadcast(Permission.of(channel.permission), messageKey, delay, placeholders);
			return;
		}
		placeholders = new LinkedList<>(placeholders);
		placeholders.add(placeholder);
		broadcast(Permission.of(channel.permission), channel.key, delay, placeholders);
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


	@Override
	protected void sendConsole(@NotNull CommandSender to, @NotNull Message message, Message.@NotNull Type type, int delay, boolean senderSpecificPlaceholders, Placeholder... placeholders) {
		if (isDebugMessenger && !plugin().isDebug())
			return;
		super.sendConsole(to, message, type, delay, senderSpecificPlaceholders, placeholders);
	}

	@Override
	protected void sendConsole(@NotNull CommandSender to, @NotNull Message message, Message.@NotNull Type type, int delay, boolean senderSpecificPlaceholders, List<Placeholder> placeholders) {
		if (isDebugMessenger && !plugin().isDebug())
			return;
		super.sendConsole(to, message, type, delay, senderSpecificPlaceholders, placeholders);
	}

	@Override
	protected void send(@NotNull CommandSender to, @NotNull Message message, Message.@NotNull Type type, int delay, boolean senderSpecificPlaceholders, Placeholder... placeholders) {
		if (isDebugMessenger && !plugin().isDebug())
			return;
		super.send(to, message, type, delay, senderSpecificPlaceholders, placeholders);
	}

	@Override
	protected void send(@NotNull CommandSender to, @NotNull Message message, Message.@NotNull Type type, int delay, boolean senderSpecificPlaceholders, List<Placeholder> placeholders) {
		if (isDebugMessenger && !plugin().isDebug())
			return;
		super.send(to, message, type, delay, senderSpecificPlaceholders, placeholders);
	}



	public enum MessageChannel {
		DONATOR("goldendupe.channel.donator", "channel.donator"),
		STAFF("goldendupe.channel.staff", "channel.staff"),
		ADMIN("goldendupe.channel.admin", "channel.admin"),
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














