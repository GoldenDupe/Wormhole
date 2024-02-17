package xyz.goldendupe.utils;

public final class StringUtils {
	private StringUtils() {}

	public static String properCase(String input) {
		String[] parts = input.split("_");
		StringBuilder result = new StringBuilder();

		for (String part : parts) {
			if (!part.isEmpty()) {
				result.append(Character.toUpperCase(part.charAt(0)))
						.append(part.substring(1).toLowerCase()).append(" ");
			}
		}

		return result.toString().trim();
	}
}
