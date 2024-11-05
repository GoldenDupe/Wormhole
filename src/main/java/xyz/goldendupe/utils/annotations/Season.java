package xyz.goldendupe.utils.annotations;

import xyz.goldendupe.utils.Seasons;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Season {
	Seasons value();
}
