package bet.astral.wormhole.data;

import java.util.List;
import java.util.UUID;

@Getter()
@Setter()
public class PlayerData {
    private UUID uniqueId;
    private List<UUID> homes;
    private List<UUID> warps;
}
