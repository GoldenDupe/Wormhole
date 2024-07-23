package xyz.goldendupe.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GDSavedData {
	private long timesDuped;
	private long itemsDuped;
	private long itemsGenerated;

	public GDSavedData(long timesDuped, long itemsDuped, long itemsGenerated) {
		this.timesDuped = timesDuped;
		this.itemsDuped = itemsDuped;
		this.itemsGenerated = itemsGenerated;
	}
}
