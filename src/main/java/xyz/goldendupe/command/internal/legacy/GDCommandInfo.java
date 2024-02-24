package xyz.goldendupe.command.internal.legacy;

import bet.astral.messenger.permission.Permission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated(forRemoval = true)
@GDCommandInfo.DoNotReflect
public class GDCommandInfo {
	public static GDCommandInfo defaultValues(YamlConfiguration configuration){
		return new GDCommandInfo(configuration, "internalcommandinfo");
	}
	private final String name;
	private final Map<String, Long> permissionedCooldowns;
	private final Map<CommandSender, Long> cooldowns = new HashMap<>();
	public final MemorySection configSection;
	private final long cooldown;
	private final SenderType senderType;
	private final MemberType memberType;
	private final String permission;
	private final Component permissionMessage;
	private final String usage;
	private final String cooldownMessageKey;
	private final String cannotUseMessageKey;
	private final List<String> aliases;
	private final int minArguments;

	public GDCommandInfo(GDCommandInfo defaultValues, MemorySection memorySection, Command command){
		this.name = command.name();
		permission = "goldendupe."+command.memberType().type+"."+command.name();
		this.senderType = command.senderType();
		this.memberType = command.memberType();
		this.aliases = command.aliases().length == 0 ? Collections.emptyList() : Arrays.stream(command.aliases()).toList();
		this.minArguments = command.minArgs();
		this.cooldown = memorySection.getLong("cooldown", 0L);
		this.permissionedCooldowns = new HashMap<>();
		String permMessage = memorySection.getString("permission-message");
		permissionMessage = permMessage != null ? MiniMessage.miniMessage().deserialize(permMessage) : defaultValues.permissionMessage;
		this.usage = memorySection.getString("usage", defaultValues.usage);
		cooldownMessageKey = memorySection.getString("cooldown-message", defaultValues.usage);
		cannotUseMessageKey = memorySection.getString("cannot-use", defaultValues.usage);
		this.configSection = memorySection;
	}
	protected GDCommandInfo(MemorySection memorySection, String name){
		this.name = name;
		this.aliases = Collections.emptyList();
		permission = "";
		this.senderType = SenderType.ALL;
		this.memberType = MemberType.ADMINISTRATOR;
		this.cooldown = memorySection.getLong("default-command-cooldown", 0L);
		this.permissionedCooldowns = new HashMap<>();
		String permMessage = memorySection.getString("default-permission-message");
		permissionMessage = permMessage != null ? MiniMessage.miniMessage().deserialize(permMessage) : Bukkit.permissionMessage();
		this.usage = "/%command%";
		cooldownMessageKey = "default-message-cooldown";
		cannotUseMessageKey = "default-message-cannot-use-command";
		this.configSection = memorySection;
		this.minArguments = 0;
	}


	public boolean hasCooldown(CommandSender sender){
		return cooldowns.get(sender) != null && System.currentTimeMillis()<cooldowns.get(sender);
	}

	public long getCooldown(CommandSender sender){
		return cooldowns.get(sender) != null ? cooldowns.get(sender) : 0L;
	}

	public void setCooldown(CommandSender sender){
		cooldowns.put(sender, cooldown);
	}

	private long findMatchingCooldown(CommandSender commandSender){
		long lowest = cooldown;
		for (String permission : permissionedCooldowns.keySet()){
			if (commandSender.hasPermission(permission)){
				long cooldown = permissionedCooldowns.get(permission);
				if (lowest>cooldown){
					lowest = cooldown;
				}
			}
		}
		return lowest;
	}


	public String name() {
		return name;
	}


	public Map<String, Long> permissionedCooldowns() {
		return permissionedCooldowns;
	}

	public Map<CommandSender, Long> cooldowns() {
		return cooldowns;
	}

	public long cooldown() {
		return cooldown;
	}

	public SenderType senderType() {
		return senderType;
	}

	public MemberType memberType() {
		return memberType;
	}

	public String permission() {
		return permission;
	}

	public Component permissionMessage() {
		return permissionMessage;
	}

	public String cooldownMessageKey() {
		return cooldownMessageKey;
	}

	public String cannotUseMessageKey() {
		return cannotUseMessageKey;
	}

	public String usage() {
		return usage;
	}

	public List<String> aliases() {
		return aliases;
	}

	public int minArguments() {
		return minArguments;
	}

	public enum SenderType {
		ALL("all"),
		PLAYER("players"),
		CONSOLE("console")
		;
		private final String what;

		SenderType(String what) {
			this.what = what;
		}

		public String what() {
			return what;
		}
	}
	public enum MemberType {
		DEFAULT("all"),
		DONATOR("donator"),
		MODERATOR("staff"),
		ADMINISTRATOR("admin"),
		OWNER("owner")
		;

		public static MemberType of(Player player){
			if (player.hasPermission("goldendupe.group.owner")){
				return OWNER;
			} else if (player.hasPermission("goldendupe.group.admin")){
				return ADMINISTRATOR;
			} else if (player.hasPermission("goldendupe.group.staff")){
				return MODERATOR;
			} else if (player.hasPermission("goldendupe.group.donator")){
				return DONATOR;
			} else {
				return DEFAULT;
			}
		}

		public Permission messenger(){
			return Permission.of("goldendupe.messenger."+type);
		}

		public Permission messengerOf(String permission){
			return Permission.of("goldendupe."+type+"."+permission);
		}

		public org.incendo.cloud.permission.Permission cloudOf(String permission){
			return org.incendo.cloud.permission.Permission.permission("goldendupe."+type+"."+permission);
		}

		private final String type;

		MemberType(String type) {
			this.type = type;
		}

		public String permission(){
			return "goldendupe.group."+type;
		}

		public String permissionOf(String permission){
			return "goldendupe."+type+"."+permission;
		}
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface Command {
		String name();
		SenderType senderType();
		MemberType memberType() default MemberType.DEFAULT;
		String[] aliases() default {};
		int minArgs() default 0;
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface DoNotReflect { }
}
