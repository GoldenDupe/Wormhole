package xyz.goldendupe.messenger.chat.game;

import bet.astral.chatgamecore.game.builtin.true_or_false.TrueOrFalseChatGame;
import bet.astral.messenger.v2.component.ComponentType;
import bet.astral.messenger.v2.translation.Translation;
import lombok.Getter;

public class TrueOrFalseTranslations {
    public static Statement Q1 = new Statement(true, "chat-games.true-or-false.q.1", "<gold>Golden<white>Dupe <white> is asf gay");
    public static Statement Q2 = new Statement(false, "chat-games.true-or-false.q.2", "The <yellow>original GoldenDupe</yellow> lasted for 4 years");
    public static Statement Q3 = new Statement(false, "chat-games.true-or-false.q.3", "Gold in chemical world would be called Al");
    public static Statement Q4 = new Statement(false, "chat-games.true-or-false.q.4", "<yellow>Snakes</yellow> have eyelids");
    public static Statement Q5 = new Statement(false, "chat-games.true-or-false.q.5", "The earth's radius is <yellow>5037 kilometer</yellow>/<yellow>3 129.8 miles</yellow>");
    public static Statement Q6 = new Statement(false, "chat-games.true-or-false.q.6", "<yellow>Africa</yellow> is a country");
    public static Statement Q7 = new Statement(true, "chat-games.true-or-false.q.7", "<yellow>South Africa is a country");
    public static Statement Q8 = new Statement(false, "chat-games.true-or-false.q.8", "<yellow>Titanic</yellow> sank in the black sea");
    public static Statement Q9 = new Statement(false, "chat-games.true-or-false.q.9", "<yellow>Basketball</yellow> is only for tall people");
    public static Statement Q10 = new Statement(true, "chat-games.true-or-false.q.10", "Phones usually have <yellow>bluetooth</yellow>");
    public static Statement Q11 = new Statement(true, "chat-games.true-or-false.q.11", "There is a snake and a programming language called <yellow>Cobra</yellow>");
    public static Statement Q12 = new Statement(false, "chat-games.true-or-false.q.12", "In <yellow>2002</yellow> the first ever modern mobile phone was shown");
    public static Statement Q13 = new Statement(false, "chat-games.true-or-false.q.13", "The youtuber <yellow>FlammableFlowMc</yellow> is known in this community");
    public static Statement Q14 = new Statement(false, "chat-games.true-or-false.q.14", "In the year 1984 novel <yellow>1984</yellow> was written");
    public static Statement Q15 = new Statement(true, "chat-games.true-or-false.q.15", "The hobbit was published before <yellow>The Lords Of The Rings</yellow>");
    public static Statement Q16 = new Statement(true, "chat-games.true-or-false.q.16", "<yellow>Octopuses</yellow> have three hearts");
    public static Statement Q17 = new Statement(true, "chat-games.true-or-false.q.17", "A group of <yellow>crows</yellow> is called murder");
    public static Statement Q18 = new Statement(true, "chat-games.true-or-false.q.18", "Elephants cannot <yellow>jump</yellow>");
    public static Statement Q19 = new Statement(false, "chat-games.true-or-false.q.19", "The first manned moon landing was in the year <yellow>1968</yellow>");
    public static Statement Q20 = new Statement(true, "chat-games.true-or-false.q.20", "<yellow>Light</yellow> travels faster than <yellow>Sound</yellow>");
    public static Statement Q21 = new Statement(false, "chat-games.true-or-false.q.21", "<yellow>Black</yellow> is a color");
    public static Statement Q22 = new Statement(false, "chat-games.true-or-false.q.22", "<yellow>Leap years</yellow> have 367 days");
    public static Statement Q23 = new Statement(true, "chat-games.true-or-false.q.23", "<yellow>The alphabet</yellow> has 26 letters");
    public static Statement Q24 = new Statement(true, "chat-games.true-or-false.q.24", "GoldenDupe is mostly written in <yellow>java</yellow>");
    public static Statement Q25 = new Statement(true, "chat-games.true-or-false.q.25", "GoldenDupe has Skript <yellow>installed</yellow>");
    public static Statement Q26 = new Statement(true, "chat-games.true-or-false.q.26", "GoldenDupe has GoldenDupe <yellow>installed</yellow>");
    public static Statement Q27 = new Statement(false, "chat-games.true-or-false.q.27", "Minecraft was called <yellow>Block Game</yellow> when it was created");
    public static Statement Q28 = new Statement(true, "chat-games.true-or-false.q.28", "Minecraft was called <yellow>Cave Game</yellow> when it was created");

    @Getter
    public static class Statement extends Translation {
        private boolean isTrue;
        public Statement(boolean isTrue, String key, String value) {
            super(key);
            add(ComponentType.CHAT, text(value));
        }

        public TrueOrFalseChatGame.Statement asStatement(){
            return new TrueOrFalseChatGame.Statement(getTranslationKey(), isTrue);
        }
    }
}
