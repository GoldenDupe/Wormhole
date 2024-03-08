package xyz.goldendupe.utils.reference;

import bet.astral.unity.Factions;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.utils.annotations.RequiresOnlinePlayer;

import java.util.UUID;

@RequiresOnlinePlayer
public interface FactionPlayerReference extends PlayerReference {
	@NotNull Factions getFactions();
	@NotNull
	GoldenDupe getGoldenDupe();

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

}
