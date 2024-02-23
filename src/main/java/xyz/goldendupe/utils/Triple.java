package xyz.goldendupe.utils;

public class Triple<A, B, C> extends Pair<A, B>{
	public final C third;
	public Triple(A first, B second, C third) {
		super(first, second);
		this.third = third;
	}

	public C third() {
		return third;
	}

	public static <A, B, C> Triple<A, B, C> of(A a, B b, C c){
		return new Triple<>(a, b, c);
	}
	public static <A, B, C> Triple<A, B, C> of(Pair<A, B> pair, C c){
		return new Triple<>(pair.first, pair.second, c);
	}
}
