package xyz.goldendupe.chat.games;

import bet.astral.chatgamecore.dispatcher.ChatEventDispatcher;
import bet.astral.chatgamecore.game.ChatGame;
import bet.astral.chatgamecore.game.RunData;
import bet.astral.chatgamecore.game.State;
import xyz.goldendupe.GoldenDupe;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class GameScheduler {
    private final GoldenDupe goldenDupe;
    private final ChatEventDispatcher dispatcher;
    private int untilNextGame = 1;

    public GameScheduler(GoldenDupe goldenDupe) {
        this.goldenDupe = goldenDupe;
        this.dispatcher = goldenDupe.getChatEventDispatcher();
        run();
    }

    public void run(){
        goldenDupe.getServer().getAsyncScheduler().runAtFixedRate(goldenDupe, t->{
            untilNextGame--;
            if (untilNextGame<=0) {
                ChatGame game = dispatcher.getLatest();
                if (game != null && game.getState() == State.CREATED){
                    game.end();
                }
                int randomGame = GoldenDupe.RANDOM.nextInt(0, dispatcher.getRegistred().size()-1);
                Class<? extends ChatGame> currentGame = dispatcher.getRegistred().get(randomGame).getFirst();
                dispatcher.run(currentGame, new RunData(goldenDupe.messenger()){});
                untilNextGame = GoldenDupe.RANDOM.nextInt(240, 600);
            }
            List.copyOf(dispatcher.getRegistred()).forEach(pair->{
                ChatGame game = dispatcher.getLatest();
                if (game != null){
                    game.tick();
                }
            });
        }, 1000/20, 1, TimeUnit.SECONDS);
    }
}
