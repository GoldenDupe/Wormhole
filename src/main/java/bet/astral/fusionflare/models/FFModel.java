package bet.astral.fusionflare.models;

import bet.astral.fusionflare.particles.FFParticle;
import bet.astral.fusionflare.FusionFlare;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Getter
public abstract class FFModel {
	protected final JavaPlugin javaPlugin;
	protected final FFParticle<?> particle;
	protected final FusionFlare fusionFlare;
	protected Location location;
	protected final int tickTime;
	protected final int drawTime;

	@Setter
	protected int untilNextTick;
	@Setter
	protected int untilNextDraw;
	private final Set<UUID> receivers = new HashSet<>();

	public FFModel(FusionFlare fusionFlare, FFParticle<?> particle, Location location, int timeBetweenTicks, int timeBetweenDraws) {
		this.location = location;
		this.particle = particle;
		this.javaPlugin = fusionFlare.getJavaPlugin();
		this.fusionFlare = fusionFlare;
		this.tickTime = timeBetweenTicks;
		this.untilNextTick = timeBetweenTicks;
		this.drawTime = timeBetweenDraws;
		this.untilNextDraw = timeBetweenDraws;
	}

	public abstract void tick();
	public abstract void draw();
	public void cancel(){
		fusionFlare.cancel(this);
	}

	public FFModel addReceiver(Player player){
		receivers.add(player.getUniqueId());
		return this;
	}

	public FFModel removeReceive(Player player){
		receivers.remove(player.getUniqueId());
		return this;
	}

	public void validateReceivers(){
		receivers.removeIf(player-> !Bukkit.getOfflinePlayer(player).isOnline());
	}

	public Set<Player> receiverPlayers(){
		validateReceivers();
		return receivers.stream().map(Bukkit::getPlayer).collect(Collectors.toSet());
	}


	public void run(){
		fusionFlare.run(this);
	}

	 protected void drawParticle(Location location){
		fusionFlare.spawnParticle(new LinkedList<>(receiverPlayers()), location, particle.amount(), particle);
	 }

	public void move(Location center){
		this.location = center;
	}

	protected Logger getLogger(){
		return javaPlugin.getLogger();
	}
}
