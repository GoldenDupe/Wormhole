package xyz.goldendupe.utils;

public class Pair<A, B> {
	public final A first;
	public final B second;


	public Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}


	public A first() {
		return first;
	}

	public B second() {
		return second;
	}

	public static <A, B> Pair<A, B> of(A a, B b){
		return new Pair<>(a, b);
	}
}
