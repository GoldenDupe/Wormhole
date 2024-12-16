package xyz.goldendupe.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GDSavedData {
	private long timesDuped;
	private long itemsDuped;
	private long itemsGenerated;
	private int totalJoins;
}
