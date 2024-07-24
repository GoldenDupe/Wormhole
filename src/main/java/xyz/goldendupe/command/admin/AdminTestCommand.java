package xyz.goldendupe.command.admin;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.fusionflare.FusionFlare;
import bet.astral.fusionflare.models.CircleModel;
import bet.astral.fusionflare.models.CubeModel;
import bet.astral.fusionflare.particles.AnimatedParticle;
import bet.astral.fusionflare.particles.FFParticle;
import bet.astral.fusionflare.utils.Rotation;
import com.mojang.brigadier.LiteralMessage;
import io.papermc.paper.adventure.AdventureComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.brigadier.suggestion.TooltipSuggestion;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.minecraft.extras.suggestion.ComponentTooltipSuggestion;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.DoubleParser;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;
import org.jetbrains.annotations.ApiStatus;
import xyz.goldendupe.GoldenDupeBootstrap;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.models.chatcolor.Color;
import xyz.goldendupe.utils.MemberType;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@ApiStatus.Internal
@Cloud
public class AdminTestCommand extends GDCloudCommand {
	public AdminTestCommand(GoldenDupeCommandRegister register, PaperCommandManager<CommandSender> commandManager) {
		super(register, commandManager);

		commandPlayer(
				commandBuilderPlayer("rolltest", Description.of("admin-only"))
						.senderType(Player.class)
						.handler(context -> {
							Player player = context.sender();
							context.sender().sendMessage("Current Yaw: " + player.getYaw());
							context.sender().sendMessage("Current Pitch: " + player.getPitch());
							context.sender().sendMessage("Current Roll: 0");
						})
		);

		commandManager.command(
				commandManager.commandBuilder("brigadiertest",
								"bt"
						)
						.argument(
								StringParser.stringComponent(
										StringParser.StringMode.SINGLE
								).suggestionProvider(
										new SuggestionProvider<>() {
											@Override
											public @NonNull CompletableFuture<@NonNull Iterable<@NonNull Suggestion>> suggestionsFuture(@NonNull CommandContext<Object> context, @NonNull CommandInput input) {
												return CompletableFuture.supplyAsync(() -> {
													List<Suggestion> suggestions = new LinkedList<>();
													suggestions.add(
															TooltipSuggestion.suggestion(
																	"Hello!",
																	new LiteralMessage("Bye!")));
													suggestions.add(
															TooltipSuggestion.suggestion(
																	"Hopefully-new-line!",
																	new LiteralMessage("New\nLine")));
													suggestions.add(
															TooltipSuggestion.suggestion(
																	"Do components work?!",
																	new LiteralMessage(GsonComponentSerializer.gson().serialize(Component.text("Hello", NamedTextColor.RED)))));
													suggestions.add(
															TooltipSuggestion.suggestion(
																	"Translatable?",
																	new LiteralMessage(GsonComponentSerializer.gson().serialize(Component.translatable(Material.DIAMOND_SWORD.translationKey())))));

													suggestions.add(
															ComponentTooltipSuggestion
																	.suggestion("Cloud update added component tool tips.", Component.text("Tooltip suggestions in cloud minecraft extras")));
													// Looked at cloud, and they are not done implementing component-based tooltips

													suggestions.add(
															TooltipSuggestion.suggestion("NMS Component?", new AdventureComponent(Component.text("This is a NMS component", Color.SHIT))));
													suggestions.add(
															TooltipSuggestion.suggestion("NMS Component? w/new line?", new AdventureComponent(Component.text("Checking for new line", Color.DIAMOND).appendNewline().append(Component.text("If it works it works.", Color.REDSTONE)))));
													return suggestions;
												});

											}
										}
								).name("hello")
						)
						.permission(MemberType.OWNER.cloudOf("brigadier-test"))
						.handler(context -> {
							context.sender().sendMessage("Hi!");
						})
		);
		command(
				commandBuilderPlayer(
						"particle-test",
						Description.of("admin-only"))
						.permission(MemberType.OWNER.cloudOf("particle-test"))
						.senderType(Player.class)
						.argument(DoubleParser.doubleComponent().name("height"))
						.argument(DoubleParser.doubleComponent().name("size"))
						.argument(DoubleParser.doubleComponent().name("between"))
//						.argument(IntegerParser.integerComponent().name("amount"))
//						.argument(IntegerParser.integerComponent().name("layers"))
						.handler(context -> {
							Player sender = context.sender();

							FusionFlare fusionFlare = goldenDupe().getFusionFlare();
							FFParticle<?> particle = new AnimatedParticle<>(List.of(Particle.WAX_ON, Particle.WAX_OFF));
							//FFParticle<?> particle = new LiteralParticle<>(Particle.DRIP_LAVA);

							Location location = sender.getEyeLocation();
							Block block = sender.getTargetBlockExact(50);
							if (block == null) {
								sender.sendMessage("No target block found.");
								return;
							}
							Location location2 = block.getLocation().add(0.5, 0.5, 0.5);
							double between = context.get("between");
							double size = context.get("size");
//							int layers = context.get("layers");

							Rotation rotation = new Rotation(0, 0, 0);
							Rotation rotation2 = new Rotation(0, 0, -1);
							CircleModel model = /*new SingleModel(fusionFlare, particle, location, 50);*/
									/*new LineModel(fusionFlare, particle, location, location2, 20, size);*/
									/*new CubeModel(fusionFlare, particle, location2, -1, 5, between, size);*/
									new CircleModel(fusionFlare, particle, location2, 1, size, rotation, (float) between);
							CubeModel model2 = new CubeModel(fusionFlare, particle, location2, 1, 4, between, size);
							final boolean[] goingDown = {false};
							goldenDupe().getServer().getScheduler().runTaskTimer(goldenDupe(),
									() -> {
										double currentSize = model.getSize();
										double sizeDiff;
										if (goingDown[0]) {
											sizeDiff = -0.05;
											if (currentSize + sizeDiff < -5) {
												goingDown[0] = false;
											}
										} else {
											sizeDiff = 0.05;
											if (currentSize + sizeDiff > 5) {
												goingDown[0] = true;
											}
										}

										model.expand(sizeDiff);
										model2.expand(-sizeDiff*3);
									},
									0, 2);

							sender.sendMessage("Should be showing!");

							model.addReceiver(sender);
							model2.addReceiver(sender);

							fusionFlare.run(model);
							fusionFlare.run(model2);
						})
		);
	}
}