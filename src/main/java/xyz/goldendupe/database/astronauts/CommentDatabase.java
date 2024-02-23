package xyz.goldendupe.database.astronauts;

import bet.astral.astronauts.goldendupe.Astronauts;
import xyz.goldendupe.models.astronauts.Comment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Astronauts
public class CommentDatabase {
	private final Map<UUID, Comment> cachedComments = new HashMap<>();

	public CompletableFuture<Comment> load(UUID uuid){
		return CompletableFuture.supplyAsync(new Supplier<Comment>() {
			@Override
			public Comment get() {
				return null;
			}
		});
	}
	public void save(UUID uuid){
	}
	public void add(Comment comment){
		cachedComments.put(comment.id(), comment);
	}

	public void unload(Comment comment){
		cachedComments.remove(comment.id());
	}
	public void unload(UUID id){
		cachedComments.remove(id);
	}
}
