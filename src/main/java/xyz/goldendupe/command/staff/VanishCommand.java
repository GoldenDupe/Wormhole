package xyz.goldendupe.command.staff;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class VanishCommand {
    public static List<Player> getVisiblePlayers(Player player){
        return Bukkit.getOnlinePlayers().stream().filter(player::canSee).map(p->(Player) p).toList();
    }
}
