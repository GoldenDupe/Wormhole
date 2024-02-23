package xyz.goldendupe.messenger;

import bet.astral.messenger.Messenger;

public interface MessageLoader {
	default void loadMessage(String key, Messenger<?> messageManager){
		messageManager.loadMessage(key);
	}
}
