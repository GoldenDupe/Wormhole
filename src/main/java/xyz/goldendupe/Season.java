package xyz.goldendupe;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Season {
	int added();
	int[] unlock();
	boolean alwaysUnlocked() default false;
}
