package xyz.goldendupe.command.admin.spawn;

import bet.astral.cloudplusplus.ComponentSuggestion;
import bet.astral.cloudplusplus.ComponentTooltipSuggestion;
import bet.astral.messenger.placeholder.Placeholder;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;
import xyz.goldendupe.GoldenDupe;
import bet.astral.cloudplusplus.annotations.Cloud;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.command.defaults.SpawnCommand;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Cloud
public class RemoveSpawnCommand extends GDCloudCommand {

    public RemoveSpawnCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
        super(goldenDupe, commandManager);
        commandManager.command(
                commandManager.commandBuilder(
                                "removespawn",
                                Description.of("Removes an existing spawn from the spawn database.")
                        )
                        .senderType(Player.class)
                        .permission("goldendupe.admin.removespawn")
                        .argument(StringParser.stringComponent(StringParser.StringMode.SINGLE).name("removespawn-name")
                                .suggestionProvider(
                                        new SuggestionProvider<>() {
                                            @Override
                                            public @NonNull CompletableFuture<? extends @NonNull Iterable<? extends @NonNull Suggestion>> suggestionsFuture(@NonNull CommandContext<Object> context, @NonNull CommandInput input) {
                                                return CompletableFuture.supplyAsync(()-> Arrays.stream(SpawnCommand.Spawn.values()).map(value-> new ComponentTooltipSuggestion(ComponentSuggestion.Mode.PLAIN, Component.text(value.getName()))).collect(Collectors.toList()));
                                            }
                                        }
                                )
                        )
                        .handler(context -> {

                            Player sender = context.sender();
                            String spawnName = context.get("removespawn-name");

                            if (goldenDupe.getSpawnDatabase().exists(spawnName)) {
                                goldenDupe.getSpawnDatabase().delete(spawnName);
                                commandMessenger.message(sender, "setspawn.message-remove",
                                        new Placeholder("spawn", spawnName));
                                return;
                            }

                            commandMessenger.message(sender, "removespawn.message-already-removed",
                                    new Placeholder("spawn", spawnName));
                        })
        );
    }

}
