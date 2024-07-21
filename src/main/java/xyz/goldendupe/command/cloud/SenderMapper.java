package xyz.goldendupe.command.cloud;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

public class SenderMapper implements org.incendo.cloud.SenderMapper<CommandSourceStack, CommandSender> {
	private final Map<CommandSender, CommandSourceStack> sourceStackMap = new HashMap<>();
	@Override
	public @NonNull CommandSender map(@NonNull CommandSourceStack base) {
		sourceStackMap.put(base.getSender(), base);
		return base.getSender();
	}

	@Override
	public @NonNull CommandSourceStack reverse(@NonNull CommandSender mapped) {
		return sourceStackMap.get(sourceStackMap);
	}
}
