package bet.astral.goldenmessenger;

import bet.astral.messagemanager.permission.Permission;
import bet.astral.messagemanager.placeholder.Placeholder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

public interface IMessenger {
	@ApiStatus.NonExtendable
	default void broadcast(String  messageKey, Placeholder... placeholders){
		broadcast(null, messageKey, 0, false, List.of(placeholders));
	}
	@ApiStatus.NonExtendable
	default void broadcast(String  messageKey, boolean senderSpecificPlaceholders, Placeholder... placeholders){
		broadcast(null, messageKey, 0, senderSpecificPlaceholders, List.of(placeholders));
	}
	@ApiStatus.NonExtendable
	default void broadcast(String messageKey, List<Placeholder> placeholders) {
		broadcast(null, messageKey, 0, false, placeholders);
	}
	@ApiStatus.NonExtendable
	default void broadcast(String messageKey, boolean senderSpecificPlaceholders, List<Placeholder> placeholders) {
		broadcast(null, messageKey, 0, senderSpecificPlaceholders, placeholders);
	}
	@ApiStatus.NonExtendable
	default void broadcast(Permission permission, String messageKey, Placeholder... placeholders){
		broadcast(permission, messageKey, 0, false, List.of(placeholders));
	}
	@ApiStatus.NonExtendable
	default void broadcast(Permission permission, String messageKey, boolean senderSpecificPlaceholders, Placeholder... placeholders){
		broadcast(permission, messageKey, 0, senderSpecificPlaceholders, List.of(placeholders));
	}
	@ApiStatus.NonExtendable
	default void broadcast(Permission permission, String messageKey, List<Placeholder> placeholders) {
		broadcast(permission, messageKey, 0, false, placeholders);
	}
	@ApiStatus.NonExtendable
	default void broadcast(Permission permission, String messageKey, boolean senderSpecificPlaceholders, List<Placeholder> placeholders) {
		broadcast(permission, messageKey, 0, senderSpecificPlaceholders, placeholders);
	}
	@ApiStatus.NonExtendable
	default void broadcast(String messageKey, int delay, List<Placeholder> placeholders){
		broadcast(null, messageKey, delay, false, placeholders);
	}
	@ApiStatus.NonExtendable
	default void broadcast(String messageKey, int delay, Placeholder... placeholders){
		broadcast(null, messageKey, delay, false, List.of(placeholders));
	}
	@ApiStatus.NonExtendable
	default void broadcast(String messageKey, int delay, boolean senderSpecificPlaceholders, Placeholder... placeholders){
		broadcast(null, messageKey, delay, senderSpecificPlaceholders, List.of(placeholders));
	}
	@ApiStatus.NonExtendable
	default void broadcast(String messageKey, int delay, boolean senderSpecificPlaceholders, List<Placeholder> placeholders){
		broadcast(null, messageKey, delay, senderSpecificPlaceholders, placeholders);
	}
	@ApiStatus.NonExtendable
	default void broadcast(Permission permission, String  messageKey, int delay, Placeholder... placeholders){
		broadcast(permission, messageKey, delay, false, List.of(placeholders));
	}
	@ApiStatus.NonExtendable
	default void broadcast(Permission permission, String messageKey, int delay, List<Placeholder> placeholders) {
		broadcast(permission, messageKey, delay, false, placeholders);
	}
	@ApiStatus.NonExtendable
	default void broadcast(Permission permission, String  messageKey, int delay, boolean senderSpecificPlaceholders, Placeholder... placeholders){
		broadcast(permission, messageKey, delay, senderSpecificPlaceholders, List.of(placeholders));
	}

	@ApiStatus.OverrideOnly
	void broadcast(Permission permission, String messageKey, int delay, boolean senderSpecificPlaceholders, List<Placeholder> placeholders);





	@ApiStatus.NonExtendable
	default void message(CommandSender to, String  messageKey, Placeholder... placeholders){
		message(null, to, messageKey, 0, false, List.of(placeholders));
	}
	@ApiStatus.NonExtendable
	default void message(CommandSender to, String  messageKey, boolean senderSpecificPlaceholders, Placeholder... placeholders){
		message(null, to, messageKey, 0, senderSpecificPlaceholders, List.of(placeholders));
	}
	@ApiStatus.NonExtendable
	default void message(CommandSender to, String messageKey, List<Placeholder> placeholders) {
		message(null, to, messageKey, 0, false, placeholders);
	}
	@ApiStatus.NonExtendable
	default void message(CommandSender to, String messageKey, boolean senderSpecificPlaceholders, List<Placeholder> placeholders) {
		message(null, to, messageKey, 0, senderSpecificPlaceholders, placeholders);
	}
	@ApiStatus.NonExtendable
	default void message(Permission permission, CommandSender to, String messageKey, Placeholder... placeholders){
		message(permission, to, messageKey, 0, false, List.of(placeholders));
	}
	@ApiStatus.NonExtendable
	default void message(Permission permission, CommandSender to, String messageKey, boolean senderSpecificPlaceholders, Placeholder... placeholders){
		message(permission, to, messageKey, 0, senderSpecificPlaceholders, List.of(placeholders));
	}
	@ApiStatus.NonExtendable
	default void message(Permission permission, CommandSender to, String messageKey, List<Placeholder> placeholders) {
		message(permission, to, messageKey, 0, false, placeholders);
	}
	@ApiStatus.NonExtendable
	default void message(Permission permission, CommandSender to, String messageKey, boolean senderSpecificPlaceholders, List<Placeholder> placeholders) {
		message(permission, to, messageKey, 0, senderSpecificPlaceholders, placeholders);
	}
	@ApiStatus.NonExtendable
	default void message(CommandSender to, String messageKey, int delay, List<Placeholder> placeholders){
		message(null, to, messageKey, delay, false, placeholders);
	}
	@ApiStatus.NonExtendable
	default void message(CommandSender to, String messageKey, int delay, Placeholder... placeholders){
		message(null, to, messageKey, delay, false, List.of(placeholders));
	}
	@ApiStatus.NonExtendable
	default void message(CommandSender to, String messageKey, int delay, boolean senderSpecificPlaceholders, Placeholder... placeholders){
		message(null, to, messageKey, delay, senderSpecificPlaceholders, List.of(placeholders));
	}
	@ApiStatus.NonExtendable
	default void message(CommandSender to, String messageKey, int delay, boolean senderSpecificPlaceholders, List<Placeholder> placeholders){
		message(null, to, messageKey, delay, senderSpecificPlaceholders, placeholders);
	}
	@ApiStatus.NonExtendable
	default void message(Permission permission, CommandSender to, String  messageKey, int delay, Placeholder... placeholders){
		message(permission, to, messageKey, delay, false, List.of(placeholders));
	}
	@ApiStatus.NonExtendable
	default void message(Permission permission, CommandSender to, String messageKey, int delay, List<Placeholder> placeholders) {
		message(permission, to, messageKey, delay, false, placeholders);
	}
	@ApiStatus.NonExtendable
	default void message(Permission permission, CommandSender to, String  messageKey, int delay, boolean senderSpecificPlaceholders, Placeholder... placeholders){
		message(permission, to, messageKey, delay, senderSpecificPlaceholders, List.of(placeholders));
	}
	@ApiStatus.OverrideOnly
	void message(Permission permission, CommandSender to, String messageKey, int delay, boolean senderSpecificPlaceholders, List<Placeholder> placeholders);
}
