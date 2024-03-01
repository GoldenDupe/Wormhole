package xyz.goldendupe.models.astronauts;

import xyz.goldendupe.utils.annotations.temporal.RequireSave;

import java.util.UUID;

@RequireSave
public class Comment {
	private final UUID id;
	private final UUID who;
	private final String contents;

	public Comment(UUID id, UUID who, String contents) {
		this.id = id;
		this.who = who;
		this.contents = contents;
	}

	public UUID id() {
		return id;
	}

	public UUID who() {
		return who;
	}

	public String contents() {
		return contents;
	}
}
