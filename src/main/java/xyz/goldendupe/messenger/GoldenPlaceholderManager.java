package xyz.goldendupe.messenger;

import bet.astral.messenger.adventure.AdventurePlaceholderManager;
import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.messenger.utils.PlaceholderUtils;
import bet.astral.unity.utils.refrence.PlayerReference;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.models.GDPlayer;

import java.util.LinkedList;
import java.util.List;

public class GoldenPlaceholderManager extends AdventurePlaceholderManager {

	private static final GoldenDupe gd;
	private static final boolean vaultExists;
	private static final boolean luckPermsExists;
	static  {
		gd = GoldenDupe.getPlugin(GoldenDupe.class);
		vaultExists = gd.vaultChat() != null;
		luckPermsExists = gd.luckPerms() != null;
	}

	@Override
	public List<Placeholder> audiencePlaceholders(@Nullable String prefix, @NotNull Audience audience) {
		if (audience instanceof PlayerReference playerReference && playerReference.player() != null){
			return super.playerPlaceholders(prefix, playerReference.player());
		} else if (audience instanceof GDPlayer player){
			return super.playerPlaceholders(prefix, player.player());
		}
		return super.audiencePlaceholders(prefix, audience);
	}

	@Override
	public @NotNull List<Placeholder> playerPlaceholders(@Nullable String prefix, @NotNull Player player) {
		List<Placeholder> placeholders = new LinkedList<>(super.playerPlaceholders(prefix, player));
		placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "prefix", prefix(player)));
		placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "suffix", suffix(player)));
		placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "prefix_name", prefixName(player)));
		placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "suffix_name", suffixName(player)));
		placeholders.add(PlaceholderUtils.createPlaceholder(prefix, "prefix_suffix_name", prefixNameSuffix(player)));
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

}
