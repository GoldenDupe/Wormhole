package xyz.goldendupe.messenger;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import xyz.goldendupe.GoldenDupe;

public class GoldenPlaceholderManager {

	private static final GoldenDupe gd;
	private static final boolean vaultExists;
	private static final boolean luckPermsExists;
	static  {
		gd = GoldenDupe.getPlugin(GoldenDupe.class);
		vaultExists = gd.vaultChat() != null;
		luckPermsExists = gd.luckPerms() != null;
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

}
