package xyz.goldendupe.scoreboard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import xyz.goldendupe.messenger.GoldenPlaceholderManager;
import xyz.goldendupe.models.chatcolor.Color;

import java.text.DecimalFormat;
import java.util.List;

public interface ScoreboardSection {
    DecimalFormat format = new DecimalFormat("0.#");

    default String format(double value){
        return format.format(value);
    }

    default Component name(Player player){
        return player.name();
    }
    default Component prefix(Player player){
        return GoldenPlaceholderManager.prefixComp(player);
    }

    default Component hex(String line, String hex, TextDecoration... decoration){
        return Component.text(line, Color.ofHex(hex), decoration);
    }

    default Component gray(String line, TextDecoration... decoration){
        return Component.text(line, Color.GRAY, decoration);
    }
    default Component white(String line, TextDecoration... decoration){
        return Component.text(line, Color.WHITE, decoration);
    }
    default Component gold(String line, TextDecoration... decoration) {
        return Component.text(line, Color.GOLD, decoration);
    }

    default Component of(Component... components){
        Component base = Component.text("");
        for (Component component : components){
            base = base.append(component);
        }
        return base;
    }
}
