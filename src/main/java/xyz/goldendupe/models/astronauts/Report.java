package xyz.goldendupe.models.astronauts;

import bet.astral.astronauts.goldendupe.Astronauts;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Astronauts
public class Report {
	private final UUID id;
	private final UUID whoReported;
	private final UUID whoWasReported;
	private final UUID whoClosed;
	private final String info;
	private final Type type;
	public boolean isSolved = false;
	public boolean falseReport = false;
	private final List<Comment> comments = new ArrayList<>();

	public Report(UUID id, UUID whoReported, @Nullable UUID whoWasReported, @Nullable UUID whoClosed, String info, Type type) {
		this.id = id;
		this.whoReported = whoReported;
		this.whoWasReported = whoWasReported;
		this.whoClosed = whoClosed;
		this.info = info;
		this.type = type;
	}

	public Report addComment(Comment comment){
		comments.add(comment);
		return this;
	}

	public List<Comment> comments(){
		return comments;
	}

	public UUID id() {
		return id;
	}

	public UUID whoReported() {
		return whoReported;
	}

	@Nullable
	public UUID whoWasReported() {
		return whoWasReported;
	}

	@Nullable
	public UUID whoClosed() {
		return whoClosed;
	}

	public String info() {
		return info;
	}

	public Type type() {
		return type;
	}

	public enum Type {
		PLAYER_REPORT,
		BUG_REPORT,
	}
}
