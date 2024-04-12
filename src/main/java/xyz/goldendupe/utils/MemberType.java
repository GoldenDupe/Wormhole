package xyz.goldendupe.utils;

import org.bukkit.entity.Player;
import org.incendo.cloud.permission.Permission;
import org.jetbrains.annotations.Nullable;
import xyz.goldendupe.messenger.GoldenMessenger;
import xyz.goldendupe.models.GDChat;

public enum MemberType {
	DEFAULT("all", null, GDChat.GLOBAL),
	BOOSTER("booster", GoldenMessenger.MessageChannel.BOOSTER, GDChat.BOOSTER),
	DONATOR("donator", GoldenMessenger.MessageChannel.DONATOR, GDChat.DONATOR),
	OG("og", GoldenMessenger.MessageChannel.OG, GDChat.OG),
	MODERATOR("staff", GoldenMessenger.MessageChannel.STAFF, GDChat.STAFF),
	ADMINISTRATOR("admin", GoldenMessenger.MessageChannel.ADMIN, GDChat.ADMIN),
	OWNER("owner", GoldenMessenger.MessageChannel.ADMIN, GDChat.ADMIN),

	;

	public static MemberType of(Player player){
		if (player.hasPermission("goldendupe.group.owner")){
			return OWNER;
		} else if (player.hasPermission("goldendupe.group.admin")){
			return ADMINISTRATOR;
		} else if (player.hasPermission("goldendupe.group.staff")){
			return MODERATOR;
		} else if (player.hasPermission("goldendupe.group.og")){
			return OG;
		} else if (player.hasPermission("goldendupe.group.donator")){
			return DONATOR;
		} else if (player.hasPermission("goldendupe.group.booster")){
			return BOOSTER;
		} else {
			return DEFAULT;
		}
	}

	public Permission messenger(){
		return Permission.of("goldendupe.messenger."+type);
	}
	public org.incendo.cloud.permission.Permission cloudOf(String permission){
		return org.incendo.cloud.permission.Permission.permission("goldendupe."+type+"."+permission);
	}

	private final GoldenMessenger.MessageChannel channel;
	private final GDChat chat;
	private final String type;

	MemberType(String type, GoldenMessenger.MessageChannel channel, GDChat chat) {
		this.type = type;
		this.channel = channel;
		this.chat = chat;
	}

	@Nullable
	public GoldenMessenger.MessageChannel asChannel(){
		return channel;
	}
	@Nullable
	public GDChat asChat(){
		return chat;
	}
	public String permission(){
		return "goldendupe.group."+type;
	}

	public String permissionOf(String permission){
		return "goldendupe."+type+"."+permission;
	}
}
