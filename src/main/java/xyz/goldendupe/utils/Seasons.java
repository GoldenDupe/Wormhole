package xyz.goldendupe.utils;

import lombok.AccessLevel;
import lombok.Getter;

public enum Seasons {

    OG(15912540000000L, -1),
    NONE(0, 0),
    SEASON_1(17175132000000L, 1),
    ;

    @Getter(AccessLevel.PUBLIC) private final long released;
    private final int asInt;

    Seasons(long released, int asInt) {
        this.released = released;
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
