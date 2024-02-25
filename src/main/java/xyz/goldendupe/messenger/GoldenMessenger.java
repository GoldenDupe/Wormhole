package xyz.goldendupe.messenger;


import bet.astral.messenger.Message;
import bet.astral.messenger.Messenger;
import bet.astral.messenger.permission.Permission;
import bet.astral.messenger.placeholder.LegacyPlaceholder;
import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.messenger.utils.PlaceholderUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupe;

import java.time.Duration;
import java.util.*;


public class GoldenMessenger extends Messenger<GoldenDupe> implements MessageLoader, IMessenger {
	private static final GoldenDupe gd;
	private static final boolean vaultExists;
	private static final boolean luckPermsExists;
	private final boolean isDebugMessenger;
	static  {
		gd = GoldenDupe.getPlugin(GoldenDupe.class);
		vaultExists = gd.vaultChat() != null;
		luckPermsExists = gd.luckPerms() != null;
	}
	public GoldenMessenger(FileConfiguration messageConfig, boolean isDebugMessenger) {
		this(messageConfig, new HashMap<>(), isDebugMessenger);
	}
	public GoldenMessenger(FileConfiguration messageConfig, Map<String, Message> messageMap, boolean isDebugMessenger) {
		super(gd, messageConfig, messageMap);
		this.isDebugMessenger = isDebugMessenger;
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
		placeholders.add(new LegacyPlaceholder(name+"_prefix", prefix(player)));
		placeholders.add(new LegacyPlaceholder(name+"_suffix", suffix(player)));
		placeholders.add(new Placeholder(name+"_prefix_name", prefixName(player)));
		placeholders.add(new Placeholder(name+"_suffix_name", suffixName(player)));
		placeholders.add(new Placeholder(name+"_prefix_suffix_name", prefixNameSuffix(player)));
		return placeholders;
	}

	public static String prefix(Player player){
		if (vaultExists && !luckPermsExists){
			return gd.vaultChat().getPlayerPrefix(player);
		} else if (luckPermsExists){
			String prefix = gd.luckPerms().getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getPrefix();
			if (prefix == null) {
				prefix = gd.luckPerms().getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getPrimaryGroup();
				prefix = gd.luckPerms().getGroupManager().getGroup(prefix).getCachedData().getMetaData().getPrefix();
				if (prefix == null){
					prefix = "";
				}
			}
			return prefix;
		}
		return "";
	}
	public static Component prefixName(Player player){
		return LegacyComponentSerializer.legacyAmpersand().deserialize(prefix(player)+player.getName());
	}
	public static Component suffixName(Player player){
		return LegacyComponentSerializer.legacyAmpersand().deserialize(suffix(player)+player.getName());
	}
	public static Component prefixNameSuffix(Player player) {
		return LegacyComponentSerializer.legacyAmpersand().deserialize(prefix(player) + player.getName()+suffix(player));
	}
	public static String suffix(Player player){
		if (vaultExists && !luckPermsExists){
			return gd.vaultChat().getPlayerPrefix(player);
		} else if (luckPermsExists){
			String suffix = gd.luckPerms().getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getSuffix();
			if (suffix == null) {
				suffix = gd.luckPerms().getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getPrimaryGroup();
				suffix = gd.luckPerms().getGroupManager().getGroup(suffix).getCachedData().getMetaData().getSuffix();
				if (suffix == null){
					suffix = "";
				}
			}
			return suffix;
		}
		return "";
	}

	public static List<Placeholder> playerPlaceholders(String name, Player player){
		List<Placeholder> placeholders = new LinkedList<>(PlaceholderUtils.createPlaceholders(name, (LivingEntity) player));
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
		DONATOR("goldendupe.channel.donator", "channels.donator"),
		STAFF("goldendupe.channel.staff", "channels.staff"),
		ADMIN("goldendupe.channel.admin", "channels.admin"),
		OG("goldendupe.channel.og", "channels.og")
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














