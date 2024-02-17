package xyz.goldendupe.listeners;

import org.bukkit.event.Listener;
import xyz.goldendupe.command.GDCommandInfo;
import xyz.goldendupe.GoldenDupe;


@GDCommandInfo.DoNotReflect
public class GDListener implements Listener {
	protected final GoldenDupe goldenDupe;
	public GDListener(GoldenDupe goldenDupe) {
		this.goldenDupe = goldenDupe;
	}
}
