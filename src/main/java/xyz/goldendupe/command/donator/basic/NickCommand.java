package xyz.goldendupe.command.donator.basic;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.minecraft.extras.suggestion.ComponentTooltipSuggestion;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.flag.CommandFlag;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.permission.PredicatePermission;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.utils.MemberType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Cloud
public class NickCommand extends GDCloudCommand {
    public NickCommand(GoldenDupeCommandRegister register, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
        super(register, commandManager);
        commandManager.command(
                commandManager.commandBuilder(
                                "nickname",
                                Description.of("Allows a player to use a nickname."),
                        "nick", "displayname"
                        )
                        .permission(MemberType.DONATOR.cloudOf("nick"))
                        .argument(StringParser.stringComponent(StringParser.StringMode.GREEDY_FLAG_YIELDING)
                                .name("nickname")
                                .suggestionProvider(new SuggestionProvider<>() {
                                    @Override
                                    public @NonNull CompletableFuture<@NonNull Iterable<@NonNull Suggestion>> suggestionsFuture(@NonNull CommandContext<Object> context, @NonNull CommandInput input) {
                                        return CompletableFuture.supplyAsync(() -> {
                                            List<Suggestion> suggestions = new ArrayList<>();
                                            if (context.sender() instanceof Player player) {
                                                if (!MiniMessage.miniMessage().serialize(player.displayName()).equalsIgnoreCase(player.getName())) {
                                                    suggestions.add(
                                                            ComponentTooltipSuggestion.suggestion(
                                                                    LegacyComponentSerializer.legacyAmpersand().serialize(player.displayName()),
                                                                    player.displayName()));
                                                }
                                            }
                                            return suggestions;
                                        });
                                    }
                                })
                        )
                        .flag(CommandFlag.builder("reset").withPermission(PredicatePermission.of(p->{
                            Player player = (Player) p;
                            return !MiniMessage.miniMessage().serialize(player.displayName()).equalsIgnoreCase(player.getName());
                        })).build())
                        .senderType(Player.class)
                        .handler(context -> {
                            Player sender = context.sender();
                            if (context.flags().isPresent("reset")){
                                sender.displayName(null);
                                sender.sendMessage("Reset your nickanme.");
                                return;
                            }
                            Component nickname = LegacyComponentSerializer.legacyAmpersand().deserialize(context.get("nickname"));
                            String colorless = PlainTextComponentSerializer.plainText().serialize(nickname);
                            if (colorless.length()>16){
                                messenger.message(sender, Translations.COMMAND_NICKNAME_TOO_LONG, Placeholder.of("nickname", colorless), Placeholder.of("length", colorless.length()));
                                return;
                            }
                            if (!colorless.matches("[a-zA-Z0-9_-]*")){
                                messenger.message(sender, Translations.COMMAND_NICKNAME_ILLEGAL, Placeholder.of("nickname", colorless), Placeholder.of("length", colorless.length()));
                                return;
                            }
                            Component component;
                            PlaceholderList placeholders = new PlaceholderList();
                            if (sender.hasPermission(MemberType.DONATOR.permissionOf("nick.color"))) {
                                component = nickname;
                            } else {
                                component = Component.text(colorless);
                            }

                            // Remove obfuscation, so people cannot have names others cannot read
                            if (!sender.hasPermission(MemberType.DONATOR.permissionOf("nick.obfuscation"))) {
                                component = component.decoration(TextDecoration.OBFUSCATED, false);
                            }
                            placeholders.add("nickname", component);

                            messenger.message(sender, Translations.COMMAND_NICKNAME_SUCCESS, placeholders);

                            sender.displayName(component);
                        })
        );
    }
}
