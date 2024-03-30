package xyz.goldendupe.utils;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

public enum OriginalMemberType {
	BASIC("&8[&5Basic&8]&d"),
	LEGEND("&8[&6Legend&8]&6"),
	SUPERIOR("&8[&cSuperior&8]&c"),
	DEVELOPER("&8[&6Developer&8]&6"),
	OWNER("&8[&4Owner&8]&c")
	;

	@Getter
	private final Component prefix;
	@Getter
	private final String serializedPrefix;

	OriginalMemberType(String serializedPrefix) {
		this.prefix = LegacyComponentSerializer.legacyAmpersand().deserialize(serializedPrefix);
		this.serializedPrefix = serializedPrefix;
	}

	public Component displayname(Player player){
		return LegacyComponentSerializer.legacyAmpersand().deserialize(
				serializedPrefix+" "+player.getName());
	}

	public String displaynameLegacy(Player player){
		return serializedPrefix+" "+player.getName();
	}
}
