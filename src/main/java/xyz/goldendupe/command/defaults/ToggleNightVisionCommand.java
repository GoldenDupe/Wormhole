package xyz.goldendupe.command.defaults;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class ToggleNightVisionCommand extends GDCloudCommand {
	public ToggleNightVisionCommand(GoldenDupeCommandRegister register, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
		super(register, commandManager);
		commandManager.command(
				commandManager.commandBuilder(
								"togglenightvision",
								Description.of("Allows a player to toggle night vision.")
						)
						.permission(MemberType.DEFAULT.cloudOf("toggle-night-vision"))
						.senderType(Player.class)
						.handler(context -> {
							Player sender = context.sender();
							GDPlayer player = goldenDupe().playerDatabase().fromPlayer(sender);
							boolean toggle = player.isToggleNightVision();
							player.setToggleNightVision(!toggle);

							if (!toggle){
								messenger.message(sender, Translations.COMMAND_TOGGLE_NIGHT_VISION_TRUE);
								sender.getScheduler().run(goldenDupe(), t->{
									sender.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 600, 1));
								}, null);
							} else {
								messenger.message(sender, Translations.COMMAND_TOGGLE_NIGHT_VISION_FALSE);
								sender.getScheduler().run(goldenDupe(), t->{
									sender.removePotionEffect(PotionEffectType.NIGHT_VISION);
								}, null);
							}
						})
		);
	}
}
