package xyz.goldendupe.chat.games;

import bet.astral.chatgamecore.dispatcher.ChatEventDispatcher;
import bet.astral.chatgamecore.game.Create;
import bet.astral.chatgamecore.game.GameData;
import bet.astral.chatgamecore.game.RunData;
import bet.astral.chatgamecore.game.builtin.true_or_false.TrueOrFalseChatGame;
import com.google.gson.*;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TrueFalseChatGame extends TrueOrFalseChatGame {
    @GameData.DefaultData
    public static GameData GAME_DATA = new GameData(new Random(System.currentTimeMillis()), true, 30);
    @Create(name="truefalse")
    public static TrueFalseChatGame create(GameData gameData, RunData runData){
        Statement word = statements.get(gameData.getRandom().nextInt(0, statements.size()-1));
        return new TrueFalseChatGame(word, gameData, runData);
    }

    public static void register(ChatEventDispatcher eventDispatcher){
        eventDispatcher.register(TrueFalseChatGame.class);
    }

    public static final List<Statement> statements = new LinkedList<>();
    public static void parseStatements(File file){
        try {
            FileReader reader = new FileReader(file);
            Gson gson = new GsonBuilder().disableHtmlEscaping().registerTypeAdapter(Statement.class, new StatementSerializer()).create();
            JsonArray array = gson.fromJson(reader, JsonArray.class);

            reader.close();
            for (JsonElement jsonElement : array){
                if (jsonElement == null || jsonElement.isJsonNull()){
                    continue;
                }
                statements.add(gson.fromJson(jsonElement, Statement.class));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public TrueFalseChatGame(Statement statement, GameData gameData, RunData runData) {
        super(statement, gameData, runData);
    }

    @Override
    public void rewardPlayer(Player player) {

    }

    @Override
    public void nobodyGuessedCorrectly() {

    }
}
