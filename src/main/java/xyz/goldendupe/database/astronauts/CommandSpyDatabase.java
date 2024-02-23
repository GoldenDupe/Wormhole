package xyz.goldendupe.database.astronauts;

import bet.astral.astronauts.goldendupe.Astronauts;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.models.astronauts.CSPYUser;

import java.util.*;

@Astronauts
public class CommandSpyDatabase implements Listener {
	private final GoldenDupe goldenDupe;
	private final Map<UUID, CSPYUser> users = new HashMap<>();
	private final Set<CSPYUser> requestedSaves = new HashSet<>();
	private final Set<CSPYUser> loadUsers = new HashSet<>();
	private final Set<UUID> unloadUsers = new HashSet<>();
	public CommandSpyDatabase(GoldenDupe goldenDupe){
		this.goldenDupe = goldenDupe;
		this.goldenDupe.registerListener(this);

		this.goldenDupe.getServer().getScheduler().runTaskTimer(goldenDupe, ()->{
			for (CSPYUser user : new HashSet<>(requestedSaves)){
				save(user);
			}
			requestedSaves.clear();
			for (CSPYUser user : new HashSet<>(loadUsers)){
				users.put(user.uniqueId(), user);
			}
			loadUsers.clear();


			for (UUID user : new HashSet<>(unloadUsers)){
				users.remove(user);
			}
			unloadUsers.clear();
		}, 50, 20);
	}
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		load(event.getPlayer());
	}
	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		unload(event.getPlayer());
	}

	private void load(Player player){
		loadUsers.add(new CSPYUser(this, player.getUniqueId()));
	}
	private void unload(Player player){
	}

	private void save(CSPYUser user){
	}

	public CSPYUser fromPlayer(Player player){
		return this.users.get(player.getUniqueId());
	}

	public void requestSave(CSPYUser user){
		if (requestedSaves.contains(user)){
			return;
		}
		requestedSaves.add(user);
	}
}
