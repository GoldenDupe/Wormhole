package xyz.goldendupe.command.admin.chat.game;

import bet.astral.chatgamecore.game.ChatGame;
import bet.astral.chatgamecore.game.RunData;
import bet.astral.chatgamecore.game.State;
import bet.astral.chatgamecore.game.builtin.math.MathChatGame;
import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.Messenger;
import com.mojang.brigadier.LiteralMessage;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.brigadier.suggestion.TooltipSuggestion;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.utils.MemberType;

import java.util.stream.Collectors;

@Cloud
public class AdminChatGameCommand extends GDCloudCommand {
    public AdminChatGameCommand(GoldenDupeCommandRegister register, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
        super(register, commandManager);
        command("adminchatgame",
                Description.empty(),
                f->f
                        .permission(MemberType.ADMINISTRATOR.permissionOf("chatgame"))
                        .argument(StringParser.stringComponent(StringParser.StringMode.GREEDY).name("chatgame")
                                .suggestionProvider(SuggestionProvider.blocking(new BlockingSuggestionProvider<>() {
                                    @Override
                                    public @NonNull Iterable<? extends @NonNull Suggestion> suggestions(@NonNull CommandContext<Object> context, @NonNull CommandInput input) {
                                        return goldenDupe().getChatEventDispatcher().getRegistred().stream().map(pair->TooltipSuggestion.suggestion(pair.getSecond(), new LiteralMessage(pair.getFirst().getName()))).collect(Collectors.toSet());
                                    }
                                }))
                        )
                        .handler(context->{
                            MathChatGame.runTest();;

                            String arg = context.get("chatgame");
                            Class<ChatGame> chatGame = goldenDupe().getChatEventDispatcher().getByName(arg);
                            ChatGame latest = goldenDupe().getChatEventDispatcher().getLatest(chatGame);
                            if (latest != null && latest.getState()== State.CREATED){
                                latest.end();
                            }
                            if (chatGame==null){
                                context.sender().sendMessage("NULL");
                            } else {
                                context.sender().sendMessage(chatGame.getName());
                            }
                            goldenDupe().getChatEventDispatcher()
                                    .run(chatGame, new RunData(null) {
                                        @Override
                                        public Messenger getMessenger() {
                                            return messenger;
                                        }
                                    });


                            latest = goldenDupe().getChatEventDispatcher().getLatest(chatGame);
                        })
        ).register();
    }
}
