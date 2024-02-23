package xyz.goldendupe.models.chatcolor;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.regex.Pattern;

public class Color implements Cloneable, TextColor {
	/*
	 * RED
	 */
	@ChatColor(slot = 0, material = Material.REDSTONE, formattedName = "<color:#971607>Redstone")
	public static Color REDSTONE = Color.ofHex("#971607");
	@ChatColor(slot = 1, material = Material.NETHER_WART, formattedName = "<dark_red>Dark Red")
	public static Color DARK_RED = Color.ofHex("#AA0000");
	@ChatColor(slot = 2, material = Material.RED_DYE, formattedName = "<red>Red")
	public static Color RED = Color.ofHex("#FF5555");
	/*
	 * ORANGE & YELLOW
	 */
	@ChatColor(slot = 3, material = Material.RAW_GOLD, formattedName = "<gold>Gold")
	public static Color GOLD = Color.ofHex("#FFAA00");
	@ChatColor(slot = 4, material = Material.ORANGE_DYE, formattedName = "<color:#FF9933>Orange")
	public static Color ORANGE = Color.ofHex("#FF9933");
	@ChatColor(slot = 5, material = Material.YELLOW_DYE, formattedName = "<yellow>Yellow")
	public static Color YELLOW = Color.ofHex("#FFFF55");
	@ChatColor(slot = 6, material = Material.GOLD_INGOT, formattedName = "<color:#DDD605>Yellow (Minecoin)")
	public static Color MINECOIN = Color.ofHex("#DDD605");
	/*
	 * GREEN
	 */
	@ChatColor(slot = 7, material = Material.GREEN_DYE, formattedName = "<dark_green>Dark Green")
	public static Color DARK_GREEN = Color.ofHex("#00AA00");
	@ChatColor(slot = 8, material = Material.EMERALD, formattedName = "<color:#47A036>Emerald")
	public static Color EMERALD = Color.ofHex("#47A036");
	@ChatColor(slot = 9, material = Material.LIME_DYE, formattedName = "<green>Green")
	public static Color GREEN = Color.ofHex("#55FF55");
	/*
	 * BLUE
	 */

	@ChatColor(slot = 10, material = Material.LIGHT_BLUE_DYE, formattedName = "<aqua>Aqua")
	public static Color AQUA = Color.ofHex("#55FFFF");
	@ChatColor(slot = 11, material = Material.DIAMOND, formattedName = "<color:#2CBAA8>Diamond")
	public static Color DIAMOND = Color.ofHex("#2CBAA8");
	@ChatColor(slot = 12, material = Material.HEART_OF_THE_SEA, formattedName = "<dark_aqua>Dark Aqua")
	public static Color DARK_AQUA = Color.ofHex("#00AAAA");
	@ChatColor(slot = 13, material = Material.CYAN_DYE, formattedName = "<blue>Blue")
	public static Color BLUE = Color.ofHex("#5555FF");
	@ChatColor(slot = 14, material = Material.LAPIS_LAZULI, formattedName = "<color:#21497B>Lapis")
	public static Color LAPIS = Color.ofHex("#21497B");
	@ChatColor(slot = 15, material = Material.BLUE_DYE, formattedName = "<dark_blue>Dark Blue")
	public static Color DARK_BLUE = Color.ofHex("#0000AA");
	/*
	 * PURPLE
	 */
	@ChatColor(slot = 16, material = Material.PINK_DYE, formattedName = "<light_purple>Light Purple")
	public static Color LIGHT_PURPLE = Color.ofHex("#FF55FF");
	@ChatColor(slot = 17, material = Material.AMETHYST_SHARD, formattedName = "<color:#9A5CC6>Amethyst")
	public static Color AMETHYST = Color.ofHex("#9A5CC6");
	@ChatColor(slot = 18, material = Material.PURPLE_DYE, formattedName = "<dark_purple>Purple")
	public static Color DARK_PURPLE = Color.ofHex("#AA00AA");

	/*
	 * BLACK - WHITE
	 */

	@ChatColor(slot = 19, material = Material.BLACK_DYE, formattedName = "<black>Black")
	public static Color BLACK = Color.ofHex("#000000");
	@ChatColor(slot = 20, material = Material.NETHERITE_INGOT, formattedName = "<color:#443A3B>Netherite")
	public static Color NETHERITE = Color.ofHex("#443A3B");
	@ChatColor(slot = 21, material = Material.GRAY_DYE, formattedName = "<dark_gray>Dark Gray")
	public static Color DARK_GRAY = Color.ofHex("#555555");
	@ChatColor(slot = 22, material = Material.LIGHT_GRAY_DYE, formattedName = "<gray>Gray")
	public static Color GRAY = Color.ofHex("#AAAAAA");
	@ChatColor(slot = 23, material = Material.QUARTZ, formattedName = "<color:#E3D4D1>Quartz")
	public static Color QUARTZ = Color.ofHex("#E3D4D1");
	@ChatColor(slot = 24, material = Material.IRON_INGOT, formattedName = "<color:#CECACA>Iron")
	public static Color IRON = Color.ofHex("#CECACA");
	@ChatColor(slot = 25, material = Material.WHITE_DYE, formattedName = "<white>White")
	public static Color WHITE = Color.ofHex("#FFFFFF");
	/*
	 * Shit colors
	 */
	@ChatColor(slot = 26, material = Material.COPPER_INGOT, formattedName = "<color:#>Copper")
	public static Color COPPER = Color.ofHex("#FFFFFF");
	@ChatColor(slot = 26, material = Material.BROWN_DYE, formattedName = "<color:#6F4E37>Brown")
	public static Color BROWN = Color.ofHex("#6F4E37");
	@ChatColor(slot = 27, material = Material.COCOA_BEANS, formattedName = "<color:#7F7A00>Shit")
	public static Color SHIT = Color.ofHex("#7F7A00");
	private int red;
	private int green;
	private int blue;

	public static Color ofHex(String hex){
		return new Color(
				Integer.valueOf(hex.substring(1, 3), 16),
				Integer.valueOf(hex.substring(3, 5), 16),
				Integer.valueOf(hex.substring(5, 7), 16));
	}

	public static Color ofRGB(int rgb){
		int red = (rgb >> 16) & 0xFF;
		int green = (rgb >> 8) & 0xFF;
		int blue = rgb & 0xFF;
		return new Color(red, green, blue);
	}

	public static String toHex(Color color){
		return color.asHex();
	}

	public Color(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public Color setRed(int red) {
		this.red = red;
		return this;
	}

	public Color setGreen(int green) {
		this.green = green;
		return this;
	}

	public Color setBlue(int blue) {
		this.blue = blue;
		return this;
	}

	@Override
	public int value() {
		return ((red & 255) << 16 | (green & 255) << 8 | blue & 255);
	}

	public int red() {
		return red;
	}

	public int green() {
		return green;
	}

	public int blue() {
		return blue;
	}

	public String asHex(){
		return asHexString();
	}

	@SuppressWarnings("MethodDoesntCallSuperMethod")
	@Override
	public Color clone() {
		return new Color(red, green, blue);
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface ChatColor {
		Material material();
		String formattedName();
		int slot();
	}

	@Override
	public int hashCode() {
		return value();
	}
}