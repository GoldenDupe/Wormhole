package xyz.goldendupe.utils;

import bet.astral.messenger.permission.Permission;
import org.bukkit.entity.Player;

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
