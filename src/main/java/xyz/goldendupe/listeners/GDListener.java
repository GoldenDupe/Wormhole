package xyz.goldendupe.listeners;

import org.bukkit.event.Listener;
import xyz.goldendupe.GoldenDupe;
import bet.astral.cloudplusplus.annotations.DoNotReflect;


@DoNotReflect
public interface GDListener extends Listener {
	GoldenDupe goldenDupe();
}
