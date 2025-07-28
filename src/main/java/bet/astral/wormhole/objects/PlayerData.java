package bet.astral.wormhole.objects;

import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter()
public class PlayerData {
    private final UUID uniqueId;
    @Getter(AccessLevel.NONE)
    private Map<String, PlayerHome> homes = new HashMap<>();
    @Getter(AccessLevel.NONE)
    private Map<String, PlayerWarp> warps = new HashMap<>();
    private int maxHomes = 10;
    private Set<PlayerHome> deletedWarpsAndHomes = new HashSet<>();
    private Set<String> newWarpsAndHomes = new HashSet<>();
    private Set<UUID> updatedWarpsAndHomes = new HashSet<>();

    public PlayerData(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public List<PlayerHome> getHomesAndWarps() {
        List<PlayerHome> homes = new ArrayList<>(this.homes.values());
        homes.addAll(this.warps.values());
        return homes;
    }

    public List<PlayerHome> getHomes() {
        return new ArrayList<>(homes.values());
    }

    public PlayerHome getHome(@NotNull String name) {
        PlayerHome home = homes.get(name.toLowerCase());
        if (home == null) {
            home = getWarp(name.toLowerCase());
        }
        return home;
    }

    public void addHome(PlayerHome home) {
        homes.put(home.getName().toLowerCase(), home);
        newWarpsAndHomes.add(home.getName());
        deletedWarpsAndHomes.remove(home);
        updatedWarpsAndHomes.remove(home.getUniqueId());
    }

    public void removeHome(@NotNull PlayerHome home) {
        warps.remove(home.getName().toLowerCase());
        updatedWarpsAndHomes.remove(home.getUniqueId());
        deletedWarpsAndHomes.remove(home);
        newWarpsAndHomes.remove(home.getName());
    }

    public void removeHome(@NotNull String name) {
        warps.remove(name.toLowerCase());
        updatedWarpsAndHomes.remove(homes.get(name.toLowerCase()).getUniqueId());
        deletedWarpsAndHomes.remove(homes.remove(name.toLowerCase()));
        newWarpsAndHomes.remove(name);
    }

    public List<PlayerWarp> getWarps() {
        return new ArrayList<>(warps.values());
    }

    public PlayerWarp getWarp(@NotNull String name) {
        return warps.get(name.toLowerCase());
    }

    public void addWarp(PlayerWarp warp) {
        warps.put(warp.getName().toLowerCase(), warp);
        newWarpsAndHomes.add(warp.getName());
        updatedWarpsAndHomes.remove(warp.getUniqueId());
        deletedWarpsAndHomes.remove(warp);
   }

    public void removeWarp(@NotNull PlayerWarp warp) {
        warps.remove(warp.getName().toLowerCase());
        deletedWarpsAndHomes.add(warp);
        updatedWarpsAndHomes.remove(warp.getUniqueId());
    }

    public void removeWarp(@NotNull String name) {
        PlayerHome home = warps.remove(name.toLowerCase());
        deletedWarpsAndHomes.remove(home);
        newWarpsAndHomes.remove(name);
        updatedWarpsAndHomes.remove(home.getUniqueId());
    }
}
