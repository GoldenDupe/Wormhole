package xyz.goldendupe.chat.games;

import bet.astral.chatgamecore.dispatcher.ChatEventDispatcher;
import bet.astral.chatgamecore.game.Create;
import bet.astral.chatgamecore.game.GameData;
import bet.astral.chatgamecore.game.RunData;
import bet.astral.chatgamecore.game.builtin.type.CopyTheWordChatGame;
import bet.astral.messenger.v2.Messenger;
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

public class CopyFastestChatGame extends CopyTheWordChatGame {
    private static final List<String> possibleStrings = new LinkedList<>();
    @GameData.DefaultData
    public static GameData GAME_DATA = new GameData(new Random((long) (System.currentTimeMillis()*2.5)), true, 100);
    @Create(name="copy")
    public static CopyFastestChatGame create(GameData gameData, RunData runData){
        if (runData instanceof CopyRunData copyRunData){
            return new CopyFastestChatGame(copyRunData.getValue(), gameData, runData);
        }
        String word = possibleStrings.get(gameData.getRandom().nextInt(0, possibleStrings.size()-1));
        return new CopyFastestChatGame(word, gameData, runData);
    }

    public static void register(ChatEventDispatcher eventDispatcher){
        eventDispatcher.register(CopyFastestChatGame.class);
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

    public CopyFastestChatGame(String correctAnswer, GameData gameData, RunData runData) {
        super(correctAnswer, gameData, runData);
    }

    @Override
    public void rewardPlayer(Player player) {

    }

    @Override
    public void nobodyGuessedCorrectly() {

    }

    public static class CopyRunData extends RunData {
        private final String string;

        public CopyRunData(Messenger messenger, String string) {
            super(messenger);
            this.string = string;
        }

        public String getValue() {
            return string;
        }
    }
}
