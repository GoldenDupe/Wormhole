package xyz.goldendupe.models.chatcolor;

import xyz.goldendupe.utils.annotations.temporal.RequireSave;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static xyz.goldendupe.models.chatcolor.GDChatColor.Mode.SINGLE;

@RequireSave
public class GDChatColor {
	public static GDChatColor DEFAULT = new GDChatColor(
			SINGLE, Color.WHITE);

	private Map<Integer, Color> colors;
	private Mode mode;
	private int rainbowMode = -1;
	private boolean rainbowReversed = false;
	private boolean bold = false;
	private boolean italic = false;
	private boolean underlined = false;
	private boolean strikethrough = false;

	public GDChatColor(Mode mode, Map<Integer, Color> colors) {
		this.colors = new HashMap<>(colors);
		this.mode = mode;
	}
	public GDChatColor(Mode mode, Color color){
		this.colors = new HashMap<>();
		this.colors.put(0, color);
		this.mode = mode;
	}

	public static List<Color> generateList(){
		List<Color> colors = new ArrayList<>(7);
		for (int i = 0; i < 7; i++) {
			colors.add(null);
		}
		return colors;
	}

	public GDChatColor setColors(Map<Integer, Color> colors) {
		if (colors == null) {
			colors = new HashMap<>();
		}
		this.colors = colors;
		return this;
	}

	public GDChatColor setMode(Mode mode) {
		this.mode = mode;
		return this;
	}

	public Map<Integer, Color> colors() {
		return colors;
	}

	public Mode mode() {
		return mode;
	}

	public boolean rainbowReversed() {
		return rainbowReversed;
	}

	public GDChatColor setRainbowReversed(boolean rainbowReversed) {
		this.rainbowReversed = rainbowReversed;
		return this;
	}

	public int rainbowMode() {
		return rainbowMode;
	}

	public GDChatColor setRainbowMode(int rainbowMode) {
		this.rainbowMode = rainbowMode;
		return this;
	}

	public boolean strikethrough() {
		return strikethrough;
	}

	public GDChatColor setStrikethrough(boolean strikethrough) {
		this.strikethrough = strikethrough;
		return this;
	}

	public boolean italic() {
		return italic;
	}

	public GDChatColor setItalic(boolean italic) {
		this.italic = italic;
		return this;
	}

	public boolean bold() {
		return bold;
	}

	public GDChatColor setBold(boolean bold) {
		this.bold = bold;
		return this;
	}

	public boolean underlined() {
		return underlined;
	}

	public GDChatColor setUnderlined(boolean underlined) {
		this.underlined = underlined;
		return this;
	}

	public void reset() {
		this.underlined = false;
		this.strikethrough = false;
		this.italic = false;
		this.rainbowReversed = false;
		this.rainbowMode = 0;
		this.colors = new HashMap<>();
	}

	public enum Mode {
		SINGLE,
		NONE,
		GRADIENT,
		RAINBOW
	}
}
