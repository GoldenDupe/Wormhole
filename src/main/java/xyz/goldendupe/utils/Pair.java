package xyz.goldendupe.utils;

public class Pair<A, B> {
	public final A a;
	public final B b;


	public Pair(A a, B b) {
		this.a = a;
		this.b = b;
	}


	public A a() {
		return a;
	}

	public B b() {
		return b;
	}
}
