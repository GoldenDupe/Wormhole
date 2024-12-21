package xyz.goldendupe.utils;

import bet.astral.more4j.tuples.Triplet;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class Ping {
	public static Ping tps = new Ping(
			Component.text("Uh Oh...", NamedTextColor.RED),
			Triplet.immutable(0D, 7D, NamedTextColor.DARK_RED),
			Triplet.immutable(7D, 10D, NamedTextColor.RED),
			Triplet.immutable(10D, 14.0D, NamedTextColor.YELLOW),
			Triplet.immutable(14.0D, 17.0D, NamedTextColor.GREEN),
			NamedTextColor.DARK_GREEN,
			false
	);
	public static Ping defaultPing = new Ping(
			Component.text("Loading...", NamedTextColor.RED),
			Triplet.immutable(0D, 75D, NamedTextColor.DARK_GREEN),
			Triplet.immutable(76D, 110D, NamedTextColor.GREEN),
			Triplet.immutable(111D, 160D, NamedTextColor.YELLOW),
			Triplet.immutable(161D, 220D, NamedTextColor.RED),
			NamedTextColor.DARK_RED,
			true
			);
	private final Component loading;
	private final Triplet<Double, Double, NamedTextColor> bestValue;
	private final Triplet<Double, Double, NamedTextColor> goodValue;
	private final Triplet<Double, Double, NamedTextColor> okValue;
	private final Triplet<Double, Double, NamedTextColor> worseValue;
	private final NamedTextColor worstValue;
	private final boolean showMS;

	public Ping(
			Component loading,
			Triplet<Double, Double, NamedTextColor> bestValue,
			Triplet<Double, Double, NamedTextColor> goodValue,
			Triplet<Double, Double, NamedTextColor> okValue,
			Triplet<Double, Double, NamedTextColor> worseValue,
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


	public Triplet<Double, Double, NamedTextColor> bestValue() {
		return bestValue;
	}

	public Triplet<Double, Double, NamedTextColor> goodValue() {
		return goodValue;
	}

	public Triplet<Double, Double, NamedTextColor> okValue() {
		return okValue;
	}

	public Triplet<Double, Double, NamedTextColor> worseValue() {
		return worseValue;
	}

	public NamedTextColor worstValue() {
		return worstValue;
	}

	public boolean showMS() {
		return showMS;
	}

	public Component format(double ping) {
		return format(ping, false);
	}
	public Component format(int ping) {
		if (ping < 0) {
			return loading; // Use the predefined loading message.
		}

		NamedTextColor color = worstValue; // Default worst case.
		if (ping >= bestValue.getFirst() && ping <= bestValue.getSecond()) {
			color = bestValue.getThird();
		} else if (ping >= goodValue.getFirst() && ping <= goodValue.getSecond()) {
			color = goodValue.getThird();
		} else if (ping >= okValue.getFirst() && ping <= okValue.getSecond()) {
			color = okValue.getThird();
		} else if (ping >= worseValue.getFirst() && ping <= worseValue.getSecond()) {
			color = worseValue.getThird();
		}

		Component result = Component.text(ping, color);
		if (showMS) {
			result = result.append(Component.text("ms", color));
		}
		return result;
	}
	public Component format(double ping, boolean integer) {
		if (ping < 0) {
			return loading; // Use the predefined loading message.
		}

		NamedTextColor color = worstValue; // Default worst case.
		if (ping >= bestValue.getFirst() && ping <= bestValue.getSecond()) {
			color = bestValue.getThird();
		} else if (ping >= goodValue.getFirst() && ping <= goodValue.getSecond()) {
			color = goodValue.getThird();
		} else if (ping >= okValue.getFirst() && ping <= okValue.getSecond()) {
			color = okValue.getThird();
		} else if (ping >= worseValue.getFirst() && ping <= worseValue.getSecond()) {
			color = worseValue.getThird();
		}

		Component result = Component.text((!integer) ? ping : (int) ping, color);
		if (showMS) {
			result = result.append(Component.text("ms", color));
		}
		return result;
	}

}