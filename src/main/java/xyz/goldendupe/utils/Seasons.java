package xyz.goldendupe.utils;

import lombok.AccessLevel;
import lombok.Getter;

public enum Seasons {

    NONE(0, 0),
    SEASON_1(1591254000L, 1)
    ;

    @Getter(AccessLevel.PUBLIC) private final long unix;
    private final int asInt;

    Seasons(long unix, int asInt) {
        this.unix = unix;
        this.asInt = asInt;
    }

    public int asInt() {
        return asInt;
    }

    public static Seasons fromInt(int se) {
        int i = 0;
        for (Seasons season : Seasons.values()) {
            i++;
            if (i >= se) return season;
        }
        return Seasons.NONE;
    }

}
