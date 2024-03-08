package xyz.goldendupe.utils.reference;

import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.goldendupe.utils.annotations.RequiresOnlinePlayer;

public interface FactionPlayerReference extends PlayerReference {
	@NotNull
	@RequiresOnlinePlayer
	FPlayer factionPlayer();
	@Nullable
	@RequiresOnlinePlayer
	java.util.UUID factionId();
	@Nullable
	@RequiresOnlinePlayer
	Faction faction();

}
