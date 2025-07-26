package bet.astral.wormhole.data;

import java.util.UUID;

@Getter()
@Setter()
public class PlayerHomeData extends WarpData {
    private UUID ownerId;

    @Override
    public boolean canWarp(Player player) {
        return player.getUniqueId().equals(ownerId);
    }
}
