package xyz.goldendupe.models.astronauts;

import xyz.goldendupe.database.astronauts.CommandSpyDatabase;
import xyz.goldendupe.utils.annotations.temporal.RequireSave;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RequireSave
public class CSPYUser {
	private final UUID uniqueId;
	private CommandSpyDatabase database;
	private final Set<UUID> blockedUsers = new HashSet<>();
	private final Set<String> blockedCommands = new HashSet<>();
	private boolean isCommandSpyToggled = false;

	public CSPYUser(CommandSpyDatabase database,  UUID uniqueId) {
		this.uniqueId = uniqueId;
		this.database = database;
	}

	public boolean isCommandSpyToggled() {
		return isCommandSpyToggled;
	}

	public CSPYUser setCommandSpyToggled(boolean commandSpyToggled) {
		isCommandSpyToggled = commandSpyToggled;
		return this;
	}

	public Set<String> blockedCommands() {
		return blockedCommands;
	}

	public Set<UUID> blockedUsers() {
		return blockedUsers;
	}

	public UUID uniqueId() {
		return uniqueId;
	}

	public void requestSave(){
		this.database.requestSave(this);
	}
}
