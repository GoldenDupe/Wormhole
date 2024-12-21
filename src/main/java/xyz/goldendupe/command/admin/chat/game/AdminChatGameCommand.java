package xyz.goldendupe.command.admin.chat.game;

import bet.astral.chatgamecore.game.ChatGame;
import bet.astral.chatgamecore.game.RunData;
import bet.astral.chatgamecore.game.State;
import bet.astral.chatgamecore.game.builtin.math.MathChatGame;
import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.v2.Messenger;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import xyz.goldendupe.GoldenDupeCommandRegister;
import xyz.goldendupe.command.cloud.GDCloudCommand;
import xyz.goldendupe.utils.MemberType;

@Cloud
public class AdminChatGameCommand extends GDCloudCommand {
    public AdminChatGameCommand(GoldenDupeCommandRegister register, PaperCommandManager.Bootstrapped<CommandSender> commandManager) {
        super(register, commandManager);
        command("adminchatgame",
                Description.empty(),
                f->f
                        .permission(MemberType.ADMINISTRATOR.permissionOf("chatgame"))
                        .argument(StringParser.stringComponent(StringParser.StringMode.GREEDY).name("chatgame"))
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
                            if (latest instanceof MathChatGame mathChatGame){
                                System.out.println(
                                        mathChatGame.getAnswer()
                                );
                            }
                        })
        ).register();
    }
}
