package xyz.goldendupe.models;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GDMessageGroup {
	private final UUID uniqueId;
	private final List<UUID> members = new LinkedList<>();
	private String nickname;

	public GDMessageGroup(UUID uniqueId, String nickname) {
		this.uniqueId = uniqueId;
		this.nickname = nickname;
	}


}
