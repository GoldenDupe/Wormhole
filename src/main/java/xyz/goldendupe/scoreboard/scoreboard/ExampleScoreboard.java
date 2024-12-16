package xyz.goldendupe.scoreboard.scoreboard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.megavex.scoreboardlibrary.api.sidebar.component.ComponentSidebarLayout;
import net.megavex.scoreboardlibrary.api.sidebar.component.LineDrawable;
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent;
import net.megavex.scoreboardlibrary.api.sidebar.component.animation.CollectionSidebarAnimation;
import net.megavex.scoreboardlibrary.api.sidebar.component.animation.SidebarAnimation;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.scoreboard.Scoreboard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

public class ExampleScoreboard extends Scoreboard {
    private final ComponentSidebarLayout componentSidebar;
    private final SidebarAnimation<Component> titleAnimation;

    public ExampleScoreboard(@NotNull GoldenDupe goldenDupe) {

        this.titleAnimation = createGradientAnimation(Component.text("Sidebar Example", Style.style(TextDecoration.BOLD)));
        var title = SidebarComponent.animatedLine(titleAnimation);

        SimpleDateFormat dtf = new SimpleDateFormat("HH:mm:ss");

        // Custom SidebarComponent, see below for how an implementation might look like
        SidebarComponent onlinePlayers = new KeyValueSidebarComponent(
                Component.text("Online players"),
                () -> Component.text(goldenDupe.getServer().getOnlinePlayers().size())
        );

        SidebarComponent lines = SidebarComponent.builder()
                .addDynamicLine(() -> {
                    var time = dtf.format(new Date());
                    return Component.text(time, NamedTextColor.GRAY);
                })
                .addBlankLine()
                .addStaticLine(Component.text("A static line"))
                .addComponent(onlinePlayers)
                .addBlankLine()
                .addStaticLine(Component.text("epicserver.net", NamedTextColor.AQUA))
                .build();


        this.componentSidebar = new ComponentSidebarLayout(title, lines);
    }

    // Called every tick
    @Override
    public void tick() {
        // Advance title animation to the next frame
        titleAnimation.nextFrame();

        // Update sidebar title & lines
        componentSidebar.apply(getSidebar());
    }

    private @NotNull SidebarAnimation<Component> createGradientAnimation(@NotNull Component text) {
        float step = 1f / 8f;

        TagResolver.Single textPlaceholder = Placeholder.component("text", text);
        List<Component> frames = new ArrayList<>((int) (2f / step));

        float phase = -1f;
        while (phase < 1) {
            frames.add(MiniMessage.miniMessage().deserialize("<gradient:yellow:gold:" + phase + "><text>", textPlaceholder));
            phase += step;
        }

        return new CollectionSidebarAnimation<>(frames);
    }

    public static class KeyValueSidebarComponent implements SidebarComponent {
        private final Component key;
        private final Supplier<Component> valueSupplier;

        public KeyValueSidebarComponent(@NotNull Component key, @NotNull Supplier<Component> valueSupplier) {
            this.key = key;
            this.valueSupplier = valueSupplier;
        }

        @Override
        public void draw(@NotNull LineDrawable drawable) {
            var value = valueSupplier.get();
            var line = Component.text()
                    .append(key)
                    .append(Component.text(": "))
                    .append(value.colorIfAbsent(NamedTextColor.AQUA))
                    .build();

            drawable.drawLine(line);
        }
    }
}