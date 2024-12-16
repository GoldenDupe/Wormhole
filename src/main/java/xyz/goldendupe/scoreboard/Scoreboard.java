package xyz.goldendupe.scoreboard;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.megavex.scoreboardlibrary.api.sidebar.Sidebar;
import net.megavex.scoreboardlibrary.api.sidebar.component.ComponentSidebarLayout;
import net.megavex.scoreboardlibrary.api.sidebar.component.animation.CollectionSidebarAnimation;
import net.megavex.scoreboardlibrary.api.sidebar.component.animation.SidebarAnimation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.goldendupe.GoldenDupe;

import java.util.*;

@Setter
@Getter
public class Scoreboard {
    private final Sidebar sidebar;
    private ComponentSidebarLayout layout;
    private static int GOLDEN_DUPE_TICK = 0;
    public static final SidebarAnimation<Component> GOLDEN_DUPE = createGradientAnimation("GoldenDupe", true, "gold", "white");
    public static final SidebarAnimation<Component> GOLDEN_DUPE_FOOTER = createGradientAnimation("   GoldenDupe.xyz   ", false, "gold", "white");
    private static final Map<UUID, Scoreboard> scoreboardMap = new HashMap<>();
    public static void tickGlobal() {
        GOLDEN_DUPE_TICK++;
        if (GOLDEN_DUPE_TICK==3) {
            GOLDEN_DUPE.nextFrame();
            GOLDEN_DUPE_FOOTER.nextFrame();
            GOLDEN_DUPE_TICK = 0;
        }
        if (!scoreboardMap.isEmpty()){
            for (Map.Entry<UUID, Scoreboard> entry : scoreboardMap.entrySet()){
                if (Bukkit.getPlayer(entry.getKey()) == null){
                    entry.getValue().close();
                    scoreboardMap.remove(entry.getKey());
                } else {
                    entry.getValue().tick();
                }
            }
        }
    }
    @SafeVarargs
    public static <T> List<T> of(T... values){
        List<T> list = new ArrayList<>();
        for (int i = 0; i < values.length; i++){
            list.add(i, values[i]);
        }
        return list;
    }
    public static void apply(Player player, Scoreboard scoreboard){
        if (scoreboardMap.get(player.getUniqueId()) != null){
            Scoreboard scoreboard1 = scoreboardMap.get(player.getUniqueId());
            scoreboard.hide(player);
            if (scoreboard1.sidebar.players().isEmpty()){
                scoreboard1.close();
            }
        }
        scoreboardMap.put(player.getUniqueId(), scoreboard);
        scoreboard.display(player);
        scoreboard.tick();
    }
    public static SidebarAnimation<Component> createGradientAnimation(String text, boolean bold, String... colors) {
        return createGradientAnimation(Component.text(text), bold, colors);
    }
    public static SidebarAnimation<Component> createGradientAnimation(Component component, boolean bold, String... colors) {
        float step = 1f / 8f;
        StringBuilder bldr = new StringBuilder();
        for (String color : colors){
            if (!bldr.isEmpty()){
                bldr.append(":");
            }
            bldr.append(color);
        }
        String color = bldr.toString();

        TagResolver.Single textPlaceholder = Placeholder.component("text", component);
        List<Component> frames = new ArrayList<>((int) (2f / step));

        float phase = -1f;
        while (phase < 1) {
            frames.add(MiniMessage.miniMessage().deserialize("<gradient:"+color+":" + phase + ">"+(bold ? "<bold>" : "")+"<text>", textPlaceholder));
            phase += step;
        }

        frames.reversed();
        return new CollectionSidebarAnimation<>(frames);
    }

    public Scoreboard() {
        this.sidebar = GoldenDupe.instance().getScoreboardLibrary().createSidebar();
    }

    public void display(Player player){
        sidebar.addPlayer(player);
    }
    public void hide(Player player){
        sidebar.removePlayer(player);
    }

    public void tick(){}
    public void close(){
        sidebar.close();
    }
}
