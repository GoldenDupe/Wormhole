package xyz.goldendupe.utils.reference;

import bet.astral.unity.Factions;
import bet.astral.unity.libs.bet.astral.messenger.Messenger;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.refrence.FactionReference;
import bet.astral.unity.utils.refrence.PlayerReference;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.goldendupe.utils.annotations.RequiresOnlinePlayer;

import java.util.UUID;

@RequiresOnlinePlayer
public interface FactionPlayerReference extends FactionReference, PlayerReference {
	Factions getFactions();
	@RequiresOnlinePlayer
	@NotNull
	default FPlayer asFactionPlayer() {
		return getFactions().getPlayerManager().convert(player());
	}
	@RequiresOnlinePlayer
	@Nullable
	default UUID getFactionID() {
		return asFactionPlayer().getFactionId();
	}

	@RequiresOnlinePlayer
	@Nullable
	default Faction getFaction() {
		java.util.UUID id = getFactionID();
		if (id == null){
			return null;
		}

		return getFactions().getFactionManager().get(id);
	}

	@Override
	default java.util.@Nullable UUID getFactionId() {
		return null;
	}

	@Override
	default Messenger<Factions> messenger() {
		return FactionReference.super.messenger();
	}

	@Override
	@NotNull
	default Iterable<? extends Audience> audiences() {
		return FactionReference.super.audiences();
	}

	@Override
	default java.util.@NotNull UUID uuid() {
		return getUniqueId();
	}
}
