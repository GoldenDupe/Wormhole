package xyz.goldendupe.chat.games;

import bet.astral.chatgamecore.dispatcher.ChatEventDispatcher;
import bet.astral.chatgamecore.game.Create;
import bet.astral.chatgamecore.game.GameData;
import bet.astral.chatgamecore.game.RunData;
import bet.astral.chatgamecore.game.builtin.scramble.UnscrambleWordChatGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class UnscrambleChatGame extends UnscrambleWordChatGame {
    public static final List<String> possibleStrings = new LinkedList<>();
    @GameData.DefaultData
    public static GameData GAME_DATA = new GameData(new Random(System.currentTimeMillis()), false, 100);
    @Create(name="unscramble")
    public static UnscrambleChatGame create(GameData gameData, RunData runData){
        String word = possibleStrings.get(gameData.getRandom().nextInt(0, possibleStrings.size()-1));
        return new UnscrambleChatGame(word, scramble(gameData.getRandom(), word), gameData, runData);
    }

    public static void register(ChatEventDispatcher eventDispatcher){
        eventDispatcher.register(UnscrambleChatGame.class);
    }
    public UnscrambleChatGame(String correctAnswer, String scrambled, GameData gameData, RunData runData) {
        super(correctAnswer, scrambled, gameData, runData);
    }

    public static void parseStrings(File file) {
        try {
            FileReader reader = new FileReader(file);
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            JsonArray array = gson.fromJson(reader, JsonArray.class);

            reader.close();
            for (JsonElement jsonElement : array){
                if (jsonElement == null || jsonElement.isJsonNull()){
                    continue;
                }
                possibleStrings.add(jsonElement.getAsString());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rewardPlayer(Player player) {

    }

    @Override
    public void nobodyGuessedCorrectly() {

    }
}
