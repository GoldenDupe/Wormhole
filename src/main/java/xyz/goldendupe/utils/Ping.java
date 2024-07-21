package xyz.goldendupe.utils;

import bet.astral.tuples.Triplet;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class Ping {
	public static Ping defaultPing = new Ping(
			Component.text("Loading...", NamedTextColor.RED),
			Triplet.immutable(0, 75, NamedTextColor.DARK_GREEN),
			Triplet.immutable(76, 110, NamedTextColor.GREEN),
			Triplet.immutable(111, 160, NamedTextColor.YELLOW),
			Triplet.immutable(161, 220, NamedTextColor.RED),
			NamedTextColor.DARK_RED,
			true
			);
	private final Component loading;
	private final Triplet<Integer, Integer, NamedTextColor> bestValue;
	private final Triplet<Integer, Integer, NamedTextColor> goodValue;
	private final Triplet<Integer, Integer, NamedTextColor> okValue;
	private final Triplet<Integer, Integer, NamedTextColor> worseValue;
	private final NamedTextColor worstValue;
	private final boolean showMS;

	public Ping(
			Component loading,
			Triplet<Integer, Integer, NamedTextColor> bestValue,
			Triplet<Integer, Integer, NamedTextColor> goodValue,
			Triplet<Integer, Integer, NamedTextColor> okValue,
			Triplet<Integer, Integer, NamedTextColor> worseValue,
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


	public Triplet<Integer, Integer, NamedTextColor> bestValue() {
		return bestValue;
	}

	public Triplet<Integer, Integer, NamedTextColor> goodValue() {
		return goodValue;
	}

	public Triplet<Integer, Integer, NamedTextColor> okValue() {
		return okValue;
	}

	public Triplet<Integer, Integer, NamedTextColor> worseValue() {
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
		if (ping>=bestValue.getFirst() && ping <= bestValue.getSecond()) {
			color = bestValue.getThird();
		} else if (ping>=goodValue.getFirst() && ping <= goodValue.getSecond()) {
			color = goodValue.getThird();
		} else if (ping>=okValue.getFirst() && ping <= okValue.getSecond()) {
			color = okValue.getThird();
		} else if (ping>=worseValue.getFirst() && ping <= worseValue.getSecond()) {
			color = worseValue.getThird();
		} else {
			color = worstValue;
		}
		if (showMS) {
			return Component.text(ping, color).append(Component.text("ms", color));
		}
		return Component.text(ping, color);
	}
}