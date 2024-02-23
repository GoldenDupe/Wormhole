package xyz.goldendupe.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class Ping {
	public static Ping defaultPing = new Ping(
			Component.text("Loading...", NamedTextColor.RED),
			Triple.of(0, 75, NamedTextColor.DARK_GREEN),
			Triple.of(76, 110, NamedTextColor.GREEN),
			Triple.of(111, 160, NamedTextColor.YELLOW),
			Triple.of(161, 220, NamedTextColor.RED),
			NamedTextColor.DARK_RED,
			true
			);
	private final Component loading;
	private final Triple<Integer, Integer, NamedTextColor> bestValue;
	private final Triple<Integer, Integer, NamedTextColor> goodValue;
	private final Triple<Integer, Integer, NamedTextColor> okValue;
	private final Triple<Integer, Integer, NamedTextColor> worseValue;
	private final NamedTextColor worstValue;
	private final boolean showMS;

	public Ping(
			Component loading,
			Triple<Integer, Integer, NamedTextColor> bestValue,
			Triple<Integer, Integer, NamedTextColor> goodValue,
			Triple<Integer, Integer, NamedTextColor> okValue,
			Triple<Integer, Integer, NamedTextColor> worseValue,
			NamedTextColor worstValue,
			boolean showMS
			) {
		this.loading = loading;
		this.bestValue = bestValue;
		this.goodValue = goodValue;
		this.okValue = okValue;
		this.worseValue = worseValue;
		this.worstValue = worstValue;
		this.showMS = showMS;
	}


	public Triple<Integer, Integer, NamedTextColor> bestValue() {
		return bestValue;
	}

	public Triple<Integer, Integer, NamedTextColor> goodValue() {
		return goodValue;
	}

	public Triple<Integer, Integer, NamedTextColor> okValue() {
		return okValue;
	}

	public Triple<Integer, Integer, NamedTextColor> worseValue() {
		return worseValue;
	}

	public NamedTextColor worstValue() {
		return worstValue;
	}

	public boolean showMS() {
		return showMS;
	}

	public Component format(int ping){
		if (ping <= 0){
			return Component.text("Loading...", NamedTextColor.RED);
		}
		NamedTextColor color = NamedTextColor.DARK_RED;
		if (ping>=bestValue.first && ping <= bestValue.second) {
			color = bestValue.third();
		} else if (ping>=goodValue.first && ping <= goodValue.second) {
			color = goodValue.third();
		} else if (ping>=okValue.first && ping <= okValue.second) {
			color = okValue.third();
		} else if (ping>=worseValue.first && ping <= worseValue.second) {
			color = worseValue.third();
		} else {
			color = worstValue;
		}
		if (showMS) {
			return Component.text(ping, color).append(Component.text("ms", color));
		}
		return Component.text(ping, color);
	}
}