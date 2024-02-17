package bet.astral.goldenmessenger;

import bet.astral.messagemanager.MessageManager;

public interface MessageLoader {
	default void loadMessage(String key, MessageManager<?> messageManager){
		messageManager.loadMessage(key);
	}
}
