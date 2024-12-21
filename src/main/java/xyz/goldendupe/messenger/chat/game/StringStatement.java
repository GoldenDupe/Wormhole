package xyz.goldendupe.messenger.chat.game;

import bet.astral.messenger.v2.translation.Translation;
import lombok.Getter;

@Getter
public class StringStatement extends Translation {
    private final String value;

    public StringStatement(String key, String value) {
        super(key);
        this.value = value;
    }
}
