package xyz.goldendupe.utils;

import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class EntityMap<V> extends HashMap<Entity, V> implements Identified {
	private final Identity identity;

	public EntityMap(int initialCapacity, float loadFactor, Identity identity) {
		super(initialCapacity, loadFactor);
		this.identity = identity;
	}

	public EntityMap(int initialCapacity, Identity identity) {
		super(initialCapacity);
		this.identity = identity;
	}

	public EntityMap(Identity identity) {
		this.identity = identity;
	}

	public EntityMap(Map<? extends Entity, ? extends V> m, Identity identity) {
		super(m);
		this.identity = identity;
	}

	@Override
	public @NotNull Identity identity() {
		return identity;
	}
}
