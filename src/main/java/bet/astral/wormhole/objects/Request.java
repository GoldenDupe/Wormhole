package bet.astral.wormhole.objects;

import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.messenger.v2.placeholder.Placeholderable;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.wormhole.objects.data.PlayerHome;
import bet.astral.wormhole.objects.data.Warp;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Request implements Placeholderable {
    @NotNull
    private final Type type;
    @NotNull
    private final UUID player;
    @NotNull
    private final UUID requested;
    @Nullable
    private final Object extraInfo;
    @Setter(AccessLevel.NONE)
    private int ticksLeft;
    private boolean accepted = false;
    private boolean denied = false;
    private final long timeSent = System.currentTimeMillis();

    public Request(@NotNull Type type, @NotNull UUID player, @NotNull UUID requested, @Nullable Object extraInfo, int ticksLeft) {
        this.type = type;
        this.player = player;
        this.requested = requested;
        this.extraInfo = extraInfo;
        this.ticksLeft = ticksLeft;
    }

    public void tick() {
        ticksLeft--;
    }

    public boolean isEitherPlayerOffline() {
        return isPlayerOffline() || isRequestedOffline();
    }

    public boolean isPlayerOffline() {
        OfflinePlayer player = getPlayer();
        return !(player instanceof Player);
    }

    public boolean isRequestedOffline() {
        OfflinePlayer player = getRequested();
        return !(player instanceof Player);
    }

    public OfflinePlayer getPlayer() {
        return Bukkit.getPlayer(player);
    }

    public OfflinePlayer getRequested() {
        return Bukkit.getOfflinePlayer(requested);
    }

    @Override
    public @NotNull Collection<@NotNull Placeholder> toPlaceholders(@Nullable String prefix) {
        PlaceholderList placeholderList = new PlaceholderList(List.of(
                Placeholder.of(prefix, "player", Component.text(getPlayer().getName())),
                Placeholder.of(prefix, "requested", Component.text(getRequested().getName())),
                Placeholder.of(prefix, "ticks", Component.text(getTicksLeft())),
                Placeholder.of(prefix, "seconds", Component.text(Warp.DECIMAL_FORMAT.format(getTicksLeft() / 20)))
        ));
        if (extraInfo instanceof PlayerHome playerHome) {
            placeholderList.add("home", playerHome.getName());
        } else if (extraInfo instanceof String string) {
            placeholderList.add("extra", string);
        }
        return placeholderList;
    }

    public enum Type {
        TO_PLAYER,
        PLAYER_HERE,
        TO_OWN_HOME
    }
}
