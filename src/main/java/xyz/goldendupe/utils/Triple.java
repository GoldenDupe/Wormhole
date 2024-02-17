package xyz.goldendupe.utils;

public class Triple<A, B, C> extends Pair<A, B>{
	public final C c;
	public Triple(A a, B b, C c) {
		super(a, b);
		this.c = c;
	}

	public C c() {
		return c;
	}
}
