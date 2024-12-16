package xyz.goldendupe.scoreboard.scoreboard;

import me.lucko.spark.api.Spark;
import me.lucko.spark.api.SparkProvider;
import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.types.DoubleStatistic;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.megavex.scoreboardlibrary.api.sidebar.component.ComponentSidebarLayout;
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.staff.VanishCommand;
import xyz.goldendupe.models.chatcolor.Color;
import xyz.goldendupe.scoreboard.Scoreboard;
import xyz.goldendupe.scoreboard.ScoreboardSection;
import xyz.goldendupe.utils.Ping;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

public class DefaultScoreboard extends Scoreboard implements ScoreboardSection {
    public DefaultScoreboard(Player player) {
        super();
        SidebarComponent.Builder builder = SidebarComponent.builder();
        if (GoldenDupe.instance().isDebug()) {
            builder.addStaticLine(text("DEVELOPMENT SERVER", Color.DARK_RED, BOLD));
        }
        builder.addStaticLine(of(gold("You"), gray(" (" + player.getName() + ")", TextDecoration.ITALIC)))
                .addDynamicLine(() -> of(gray(" Rank: "), prefix(player)))
                .addDynamicLine(() -> of(gray(" Ping: "), Ping.defaultPing.format(player.getPing())))
                .addBlankLine()
                .addStaticLine(of(gold("Server", BOLD)))
                .addDynamicLine(() -> of(gray(" TPS: "), tps()))
                .addDynamicLine(() -> of(gray(" Online: "), white(VanishCommand.getVisiblePlayers(player).size() + ""), gray("/"), white("" + Bukkit.getMaxPlayers())))
                .addDynamicLine(() -> of(gray(" Unique Joins: "), white(GoldenDupe.instance().getSavedData().getTotalJoins() + "")))
                .addAnimatedLine(GOLDEN_DUPE_FOOTER)
        ;

        setLayout(new ComponentSidebarLayout(
                SidebarComponent.animatedLine(GOLDEN_DUPE),
                builder.build()
        ));
        getLayout().apply(getSidebar());
    }


    public Component tps() {
        if (GoldenDupe.spark == null) {
            GoldenDupe.spark = SparkProvider.get();
        }
        Spark spark = GoldenDupe.spark;
        DoubleStatistic<StatisticWindow.TicksPerSecond> stat = spark.tps();
        double tps = stat.poll(StatisticWindow.TicksPerSecond.SECONDS_5);
        double lag = tps / 20 * 100;
        boolean fixed = false;
        if (lag < 99.4) {
            lag = 100;
        }
        lag = 100-lag;
        if (lag < 0.1){
            lag = 0;
        }
        if (tps > 19.7){
            tps = 20;
            fixed = true;
        }
        Component tpsComponent = Ping.tps.format(tps);
        TextColor color = tpsComponent.color();

        return of(text(tps + (fixed ? "*" : "") + " ", color), gray("(" + format(lag) + "%)"));
    }

    public void tick() {
        getLayout().apply(getSidebar());
    }
}