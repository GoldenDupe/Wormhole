package bet.astral.cloudplusplus;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.suggestion.Suggestion;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

@Getter
public class ComponentSuggestion implements Suggestion {
	private final Mode mode;
	private final String deserialized;
	public ComponentSuggestion(Mode mode, Component suggestion){
		this.mode = mode;
		switch (mode){
			case JSON -> {
				deserialized = GsonComponentSerializer.gson().serialize(suggestion);
			}
			case LEGACY -> {
				deserialized = LegacyComponentSerializer.legacyAmpersand().serialize(suggestion);
			}
			case MINI_MESSAGE -> {
				deserialized = MiniMessage.miniMessage().serialize(suggestion);
			}
			default -> {
				deserialized = PlainTextComponentSerializer.plainText().serialize(suggestion);
			}
		}
	}
	public ComponentSuggestion(Component suggestion){
		this(Mode.PLAIN, suggestion);
	}
	@Override
	public @NonNull String suggestion() {
		return deserialized;
	}

	@Override
	public @NonNull ComponentSuggestion withSuggestion(@NonNull String suggestion) {
		switch (mode){
			case LEGACY -> {
				return new ComponentSuggestion(mode, LegacyComponentSerializer.legacyAmpersand().deserialize(suggestion));
			}
			case JSON -> {
				return new ComponentSuggestion(mode, GsonComponentSerializer.gson().deserialize(suggestion));
			}
			case PLAIN -> {
				return new ComponentSuggestion(mode, PlainTextComponentSerializer.plainText().deserialize(suggestion));
			}
			case MINI_MESSAGE -> {
				return new ComponentSuggestion(mode, MiniMessage.miniMessage().deserialize(suggestion));
			}
		}
		return new ComponentSuggestion(mode, Component.text(suggestion));
	}
	public @NotNull ComponentSuggestion withSuggestion(@NotNull Component component){
		return new ComponentSuggestion(mode, component);
	}
	public enum Mode {
		LEGACY,
		PLAIN,
		JSON,
		MINI_MESSAGE
	}
}
