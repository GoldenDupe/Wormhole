package xyz.goldendupe.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.Configuration;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Deprecated(forRemoval = true)
@Getter
@Setter
public class GDMaintenance {
	@Setter(AccessLevel.NONE)
	private final Set<UUID> whitelisted;
	private boolean enabled;
	private boolean kickNonWhitelisted;
	private boolean announceKicked;
	private boolean announceFailedJoin;

	public GDMaintenance(Set<UUID> whitelisted, boolean enabled, boolean kickNonWhitelisted, boolean announceKicked, boolean announceFailedJoin) {
		this.whitelisted = new HashSet<>(whitelisted);
		this.enabled = enabled;
		this.kickNonWhitelisted = kickNonWhitelisted;
		this.announceKicked = announceKicked;
		this.announceFailedJoin = announceFailedJoin;
	}

	public GDMaintenance(Configuration configuration){
		this.enabled = configuration.getBoolean("maintenance.enabled");
		this.kickNonWhitelisted = configuration.getBoolean("maintenance.kick-non-whitelisted");
		this.announceKicked = configuration.getBoolean("maintenance.announce-kicked");
		this.announceFailedJoin = configuration.getBoolean("maintenance.announce-failed-login");
		this.whitelisted = configuration.getStringList("maintenance.whitelisted").stream().map(UUID::fromString).collect(Collectors.toSet());
	}

	public void save(Configuration configuration, File file){
		configuration.set("maintenance.enabled", enabled);
		configuration.set("maintenance.kick-non-whitelisted", kickNonWhitelisted);
		configuration.set("maintenance.announce-kicked", announceKicked);
		configuration.set("maintenance.announce-failed-login", announceFailedJoin);
		configuration.set("maintenance.whitelisted", whitelisted);
	}
}
