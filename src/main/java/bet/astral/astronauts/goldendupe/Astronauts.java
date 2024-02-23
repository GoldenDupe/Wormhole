package bet.astral.astronauts.goldendupe;

import org.bukkit.Bukkit;

import java.lang.annotation.*;

/**
 * Classes under this annotation are subject to be moved to astronauts as they are tested.
 * GoldenDupe is used as an experiment to see which commands work the best and which commands should be different.
 * <p>
 * Astronauts is a staff command plugin.
 * It will contain each of the staff commands in this plugin.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
public @interface Astronauts {
	boolean goingToBeMoved() default true;
	class AstronautsPlugin {
		public static boolean isAstronautsFound() {
			return Bukkit.getPluginManager().getPlugin("astronauts") != null && Bukkit.getPluginManager().getPlugin("astronauts").isEnabled();
		}
	}
}