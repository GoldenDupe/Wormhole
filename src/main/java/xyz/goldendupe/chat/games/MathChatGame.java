package xyz.goldendupe.chat.games;

import bet.astral.chatgamecore.dispatcher.ChatEventDispatcher;
import bet.astral.chatgamecore.game.Create;
import bet.astral.chatgamecore.game.GameData;
import bet.astral.chatgamecore.game.RunData;
import bet.astral.chatgamecore.game.builtin.math.HardMathArguments;
import bet.astral.chatgamecore.game.builtin.math.SimpleMathArguments;
import bet.astral.messenger.v2.Messenger;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

public class MathChatGame extends bet.astral.chatgamecore.game.builtin.math.MathChatGame {
    @GameData.DefaultData
    public static final GameData GAME_DATA = new GameData(new Random(System.nanoTime()), false, 200);
    @Create(name="math")
    public static MathChatGame create(GameData gameData, RunData runData){
        if (runData instanceof MathChatGame.MathRunData mathRunData){
            return new MathChatGame(mathRunData.getParentheses(), mathRunData.getVariables(), gameData, runData);
        }
        List<Variable> variables = new LinkedList<>();
        Parentheses parentheses = first(gameData.getRandom(), variables);;
        return new MathChatGame(parentheses, variables, gameData, runData);
    }

    public static void register(ChatEventDispatcher eventDispatcher){
        eventDispatcher.register(MathChatGame.class);
    }

    public static Parentheses first(Random random, List<Variable> variables) {
        if (random.nextBoolean()) {
                    // 3 value
                    return new Parentheses(
                            random,
                            valueThird(variables, random),
                            MathEquationType.randomSimple(random),
                            valueThird(variables, random),
                            MathEquationType.randomSimple(random),
                            valueThird(variables, random)
                    );
        } else {
            // 2 value
            return new Parentheses(
                    random,
                    valueThird(variables, random),
                    MathEquationType.random(random),
                    valueThird(variables, random)
            );
        }
    }

    public static Variable randomVariable(List<Variable> variables, Random random){
        if (variables==null || variables.isEmpty()){
            return null;
        }
        if (random.nextDouble()>0.5){
            return null;
        }
        return variables.get(random.nextInt(0, variables.size()));
    }

    public static Parentheses parentheses(List<Variable> variables, Random random){
        Object value1 = valueOrElse(variables, random, MathChatGame::randomVariable, MathChatGame::valueThird);
        Object value2 = valueOrElse(variables, random, MathChatGame::randomVariable, MathChatGame::valueThird);
        if (random.nextDouble()>0.3){
            return new Parentheses(random, value1, MathEquationType.random(random), value2);
        }
        Object value3 = valueOrElse(variables, random, MathChatGame::randomVariable, MathChatGame::valueThird);
        return new Parentheses(
                random,
                value1,
                MathEquationType.randomSimple(random),
                value2,
                MathEquationType.randomSimple(random),
                value3
        );
    }

    public static Object valueOrElse(List<Variable> variables, Random random, BiFunction<List<Variable>, Random, Object> value, BiFunction<List<Variable>, Random, Object> elseReturn){
        Object valueVal = value.apply(variables, random);
        return valueVal != null ? valueVal : elseReturn.apply(variables, random);
    }

    public static Object valueSecond(List<Variable> variables, Random random){
        if (random.nextDouble()>0.99){
            return parentheses(variables, random);
        } else if (random.nextDouble()>0.99){
            if (variables.isEmpty()){
                return HardMathArguments.assignValue(variables, random, -50, 50, ValueType.VARIABLE);
            }
            return valueOrElse(variables, random, MathChatGame::randomVariable, MathChatGame::valueThird);
        } else {
            return SimpleMathArguments.assignBase(random);
        }
    }
    public static Object valueThird(List<Variable> variables, Random random){
        return HardMathArguments.assignSimpleValue(random);
    }
    public static Object valueFirst(List<Variable> variables, Random random){
        if (random.nextDouble()>0.95){
            return HardMathArguments.assignValue(variables, random, -313, -323, ValueType.PARENTHESES);
        } else if (random.nextDouble()>0.95) {
            return new Parentheses(random, parentheses(variables, random), MathEquationType.randomSimple(random), parentheses(variables, random));
        } else if (random.nextDouble() > 0.8) {
            return parentheses(variables, random);
        } else if (random.nextDouble()>0.7){
            if (variables.isEmpty()){
                return HardMathArguments.assignValue(variables, random, -323, 323, ValueType.VARIABLE);
            }
            return valueOrElse(variables, random, MathChatGame::randomVariable, MathChatGame::valueThird);
        } else {
            return SimpleMathArguments.assignBase(random);
        }
    }

    public MathChatGame(Parentheses parentheses, List<Variable> variables, GameData gameData, RunData runData) {
        super(parentheses, variables, gameData, runData);
    }

    @Override
    public void rewardPlayer(Player player) {

    }

    @Override
    public void nobodyGuessedCorrectly() {

    }

    @Getter
    public static class MathRunData extends RunData {
        private final Parentheses parentheses;
        private final List<Variable> variables;

        public MathRunData(Messenger messenger, Parentheses parentheses, List<Variable> variables) {
            super(messenger);
            this.parentheses = parentheses;
            this.variables = variables == null ? Collections.emptyList() : variables;
        }
    }
}
