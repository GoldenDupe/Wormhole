package bet.astral.cloudplusplus;

import com.mojang.brigadier.Message;
import io.papermc.paper.adventure.AdventureComponent;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.cloud.brigadier.suggestion.TooltipSuggestion;
import org.jetbrains.annotations.NotNull;

@Getter
public class ComponentTooltipSuggestion extends ComponentSuggestion implements TooltipSuggestion {
	private final Component tooltip;
	public ComponentTooltipSuggestion(Mode mode, Component suggestion, Component tooltip) {
		super(mode, suggestion);
		this.tooltip = tooltip;
	}

	public ComponentTooltipSuggestion(Component suggestion, Component tooltip) {
		super(suggestion);
		this.tooltip = tooltip;
	}
	public ComponentTooltipSuggestion(Mode mode, Component suggestion) {
		super(mode, suggestion);
		this.tooltip = suggestion;
	}

	public ComponentTooltipSuggestion(Component suggestion) {
		super(suggestion);
		this.tooltip = suggestion;
	}

	@Override
	public @Nullable Message tooltip() {
		return new AdventureComponent(tooltip);
	}

	@Override
	public @NonNull ComponentTooltipSuggestion withSuggestion(@NonNull String suggestion) {
		switch (getMode()){
			case LEGACY -> {
				return new ComponentTooltipSuggestion(getMode(), LegacyComponentSerializer.legacyAmpersand().deserialize(suggestion), tooltip);
			}
			case JSON -> {
				return new ComponentTooltipSuggestion(getMode(), GsonComponentSerializer.gson().deserialize(suggestion), tooltip);
			}
			case PLAIN -> {
				return new ComponentTooltipSuggestion(getMode(), PlainTextComponentSerializer.plainText().deserialize(suggestion), tooltip);
			}
			case MINI_MESSAGE -> {
				return new ComponentTooltipSuggestion(getMode(), MiniMessage.miniMessage().deserialize(suggestion), tooltip);
			}
		}
		return new ComponentTooltipSuggestion(getMode(), Component.text(suggestion), tooltip);
	}
	@Override
	public @NotNull ComponentTooltipSuggestion withSuggestion(@NotNull Component component) {
		return new ComponentTooltipSuggestion(getMode(), component);
	}
}
