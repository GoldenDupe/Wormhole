package xyz.goldendupe;

import lombok.Value;

public enum test {
	VALUE;
	public static void main(String[] args){
		System.out.println(VALUE.getClass().getName());
	}
}
