package xyz.goldendupe.listeners;

import org.bukkit.event.Listener;
import xyz.goldendupe.command.internal.legacy.GDCommandInfo;
import xyz.goldendupe.GoldenDupe;


@GDCommandInfo.DoNotReflect
public interface GDListener extends Listener {
	GoldenDupe goldenDupe();
}
