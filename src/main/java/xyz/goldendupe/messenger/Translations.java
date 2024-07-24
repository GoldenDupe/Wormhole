package xyz.goldendupe.messenger;

import bet.astral.messenger.v2.component.ComponentPart;
import bet.astral.messenger.v2.component.ComponentType;
import bet.astral.messenger.v2.component.TitleComponentPart;
import bet.astral.messenger.v2.translation.TranslationKey;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Translations {
	private static final Map<String, Translation> translation = new HashMap<>();
	private static final Gson gson = new Gson();
	private static final MiniMessage mm = MiniMessage.miniMessage();
	// DELETE SPAWN
	public static final Translation SPAWN_ALREADY_REMOVED = new Translation("commands.removespawn.already-removed").add(ComponentType.CHAT, text("<red>Couldn't remove spawn <white>%spawn%<red>, as it doesn't exist."));
	public static final Translation SPAWN_REMOVED = new Translation("commands.removespawn.removed").add(ComponentType.CHAT, text("<green>Removed spawn <white>%spawn%<green> from the server."));
	// SET SPAWN
	public static final Translation SPAWN_ALREADY_SET = new Translation("commands.setspawn.already-set").add(ComponentType.CHAT, text("<red>Spawn named <white>%spawn%<red> is already set."));
	public static final Translation SPAWN_SET = new Translation("commands.setspawn.set").add(ComponentType.CHAT, text("<green>Set spawn <white>%spawn%<green> to <white>%x%<gray>, <white>%y%<gray>, <white>%z%<gray>, <white>%world%<green>."));
	// Command spy CSPY
	public static final Translation COMMAND_SPY_HELP = new Translation("commands.commandspy.help").add(ComponentType.CHAT, text("Command Spy\n<gray> - <white>/commandspy toggle\n<gray> - <white>/commandspy block-player <player>\n<gray> - <white>/commandspy unblock-player <player>\n<gray> - <white>/commandspy block-command <command>\n<gray> - <white>/commandspy unblock-command <command>\n"));
	public static final Translation COMMAND_SPY_TOGGLE_TRUE = new Translation("commands.commandspy.toggle.true").add(ComponentType.CHAT, text("<green>Toggled command spy to on"));
	public static final Translation COMMAND_SPY_TOGGLE_FALSE = new Translation("commands.commandspy.toggle.false").add(ComponentType.CHAT, text("<green>Toggled command spy to off"));
	public static final Translation COMMAND_SPY_PLAYER_ALREADY_BLOCKED = new Translation("commands.commandspy.player-already-blocked").add(ComponentType.CHAT, text("<white>%player% <red>already has been blocked."));
	public static final Translation COMMAND_SPY_PLAYER_BLOCKED = new Translation("commands.commandspy.player-blocked").add(ComponentType.CHAT, text("<white>%player% <green>is now blocked from your command spy."));
	public static final Translation COMMAND_SPY_PLAYER_ALREADY_UNBLOCKED = new Translation("commands.commandspy.player-already-unblocked").add(ComponentType.CHAT, text("<white>%player% <red>already has been unblocked."));
	public static final Translation COMMAND_SPY_PLAYER_UNBLOCKED = new Translation("commands.commandspy.player-unblocked").add(ComponentType.CHAT, text("<white>%player% <green>is now unblocked from your command spy."));
	public static final Translation COMMAND_SPY_COMMAND_ALREADY_BLOCKED = new Translation("commands.commandspy.command-already-blocked").add(ComponentType.CHAT, text("<white>%command% <red>already has been blocked."));
	public static final Translation COMMAND_SPY_COMMAND_BLOCKED = new Translation("commands.commandspy.command-blocked").add(ComponentType.CHAT, text("<white>%command% <green>is now blocked from your command spy."));
	public static final Translation COMMAND_SPY_COMMAND_ALREADY_UNBLOCKED = new Translation("commands.commandspy.command-already-unblocked").add(ComponentType.CHAT, text("<white>%command% <red>already has been unblocked."));
	public static final Translation COMMAND_SPY_COMMAND_UNBLOCKED = new Translation("commands.commandspy.command-unblocked").add(ComponentType.CHAT, text("<white>%command% <green>is now unblocked from your command spy."));
	public static final Translation LISTENER_COMMAND_SPY_EXECUTED = new Translation("listeners.commandsspy.executed").add(ComponentType.CHAT, text("<gold><bold>[CSPY]<!bold> <yellow>%player% <white>executed command <yellow>%command_suggest%"));
	public static final Translation LISTENER_COMMAND_SPY_EXECUTED_SIGN = new Translation("listeners.commandsspy.executed").add(ComponentType.CHAT, text("<gold><bold>[CSPY]<!bold> <yellow>%player% <white>executed sign command (%x%, %y%, %z%, %world%) <yellow>%command_suggest%"));
	// Dupable modification
	public static final Translation COMMAND_DUPABLE_AIR = new Translation("commands.dupable.air").add(ComponentType.CHAT, text("<red>You cannot change if air is dupable or undupable."));
	public static final Translation COMMAND_DUPABLE_MADE_DUPABLE = new Translation("commands.dupable.made-dupable").add(ComponentType.CHAT, text("<red>Made your item dupable."));
	public static final Translation COMMAND_DUPABLE_MADE_UNDUPABLE = new Translation("commands.dupable.made-undupable").add(ComponentType.CHAT, text("<red>Made your item undupable."));
	// Feed
	public static final Translation COMMAND_FEED_SELF = new Translation("commands.feed.fed-self").add(ComponentType.CHAT, text("<green>You have fed yourself!"));
	public static final Translation COMMAND_FEED_OTHER = new Translation("commands.feed.fed-somebody").add(ComponentType.CHAT, text("<green>Fed <white>%player%!"));
	// gamemode
	public static final Translation COMMAND_GMC_ALREADY_SAME = new Translation("commands.gmc.already-same").add(ComponentType.CHAT, text("<green>Your gamemode is already creative."));
	public static final Translation COMMAND_GMC_ALREADY_SAME_ADMIN = new Translation("commands.gmc.already-same-admin").add(ComponentType.CHAT, text("<white>%player%<green>'s gamemode is already creative."));
	public static final Translation COMMAND_GMC_ENABLED = new Translation("commands.gmc.already-same").add(ComponentType.CHAT, text("<green>Switched your gamemode to creative."));
	public static final Translation COMMAND_GMC_ENABLED_ADMIN = new Translation("commands.gmc.already-same").add(ComponentType.CHAT, text("<green>Switched <white>%player%<green> gamemode to creative."));
	public static final Translation COMMAND_GMS_ALREADY_SAME = new Translation("commands.gms.already-same").add(ComponentType.CHAT, text("<green>Your gamemode is already survival."));
	public static final Translation COMMAND_GMS_ALREADY_SAME_ADMIN = new Translation("commands.gms.already-same-admin").add(ComponentType.CHAT, text("<white>%player%<green>'s gamemode is already survival."));
	public static final Translation COMMAND_GMS_ENABLED = new Translation("commands.gms.already-same").add(ComponentType.CHAT, text("<green>Switched your gamemode to survival."));
	public static final Translation COMMAND_GMS_ENABLED_ADMIN = new Translation("commands.gms.already-same").add(ComponentType.CHAT, text("<green>Switched <white>%player%<green> gamemode to survival."));
	public static final Translation COMMAND_GMSP_ALREADY_SAME = new Translation("commands.gmsp.already-same").add(ComponentType.CHAT, text("<green>Your gamemode is already spectator."));
	public static final Translation COMMAND_GMSP_ALREADY_SAME_ADMIN = new Translation("commands.gmsp.already-same-admin").add(ComponentType.CHAT, text("<white>%player%<green>'s gamemode is already spectator."));
	public static final Translation COMMAND_GMSP_ENABLED = new Translation("commands.gmsp.already-same").add(ComponentType.CHAT, text("<green>Switched your gamemode to spectator."));
	public static final Translation COMMAND_GMSP_ENABLED_ADMIN = new Translation("commands.gmsp.already-same").add(ComponentType.CHAT, text("<green>Switched <white>%player%<green> gamemode to spectator."));
	public static final Translation COMMAND_GMA_ALREADY_SAME = new Translation("commands.gma.already-same").add(ComponentType.CHAT, text("<green>Your gamemode is already adventure."));
	public static final Translation COMMAND_GMA_ALREADY_SAME_ADMIN = new Translation("commands.gma.already-same-admin").add(ComponentType.CHAT, text("<white>%player%<green>'s gamemode is already adventure."));
	public static final Translation COMMAND_GMA_ENABLED = new Translation("commands.gma.already-same").add(ComponentType.CHAT, text("<green>Switched your gamemode to adventure."));
	public static final Translation COMMAND_GMA_ENABLED_ADMIN = new Translation("commands.gma.already-same").add(ComponentType.CHAT, text("<green>Switched <white>%player%<green> gamemode to adventure."));
	// Heal
	public static final Translation COMMAND_HEAL_OTHER = new Translation("commands.heal.other").add(ComponentType.CHAT, text("<green>Healed <white>%player%<green>."));
	public static final Translation COMMAND_HEAL_SELF = new Translation("commands.heal.self").add(ComponentType.CHAT, text("<green>Healed yourself."));
	// Loop
	public static final Translation COMMAND_LOOP_ALREADY_LOOPING = new Translation("commands.loop.already-looping").add(ComponentType.CHAT, text("<red>You already have a command loop executing."));
	public static final Translation COMMAND_LOOP_WARN = new Translation("commands.loop.warn").add(ComponentType.TITLE, text("<dark_red>LOOP WARNING")).add(ComponentType.SUBTITLE, text("<red>You are looping too many times!"));
	public static final Translation COMMAND_LOOP_BEGIN = new Translation("commands.loop.begin").add(ComponentType.CHAT, text("<green>Executing loop <white>%times% times"));
	public static final Translation COMMAND_LOOP_DONE = new Translation("commands.loop.ended").add(ComponentType.CHAT, text("<green>Done looping <white>%command% <red>, <white>%times% times"));
	public static final Translation COMMAND_LOOP_NO_TASK_FOUND = new Translation("commands.loop.ended").add(ComponentType.CHAT, text("<red>Couldn't find a loop task."));
	public static final Translation COMMAND_LOOP_CANCELED = new Translation("commands.loop.ended").add(ComponentType.CHAT, text("<green>Canceled your loop task"));
	// Reboot
	public static final Translation COMMAND_REBOOT_ALREADY_RESTARTING = new Translation("commands.restart.already-restarting").add(ComponentType.CHAT, text("<red>Server is already restarting"));
	public static final Translation COMMAND_REBOOT_RESTARTING = new Translation("commands.restart.restarting").add(ComponentType.CHAT, text("<red>Aborted server restart")).add(ComponentType.TITLE, text("<dark_red>REBOOT")).add(ComponentType.SUBTITLE, text("<gray>The server restarting!"));
	public static final Translation COMMAND_REBOOT_NOT_RESTARTING = new Translation("commands.restart.not-restarting").add(ComponentType.CHAT, text("<red>Server is already restarting"));
	public static final Translation COMMAND_REBOOT_ABORT = new Translation("commands.restart.aborted-restart").add(ComponentType.CHAT, text("<red>Aborted server restart")).add(ComponentType.TITLE, text("<dark_red>REBOOTING CANCELED")).add(ComponentType.SUBTITLE, text("<gray>The server restart was canceled!"));
	// Skull
	public static final Translation COMMAND_SKULL_GIVE = new Translation("commands.skull.give").add(ComponentType.CHAT, text("<green>Gave you head of <white>%player%"));
	// Weather
	public static final Translation COMMAND_SUN_CHANGED = new Translation("commands.sun.changed").add(ComponentType.CHAT, text("<green>Changed weather to sunny."));
	public static final Translation COMMAND_RAIN_CHANGED = new Translation("commands.rain.changed").add(ComponentType.CHAT, text("<green>Changed weather to raining."));
	public static final Translation COMMAND_THUNDER_CHANGED = new Translation("commands.thunder.changed").add(ComponentType.CHAT, text("<green>Changed weather to thundering."));
	// Delete home
	public static final Translation COMMAND_DELETE_HOME_DOESNT_EXIST = new Translation("commands.delete-home.doesnt-exist").add(ComponentType.CHAT, text("<red>You do not have a home named <white>%home%<red>."));
	public static final Translation COMMAND_DELETE_HOME_REMOVED = new Translation("commands.delete-home.removed").add(ComponentType.CHAT, text("<green>Deleted your home <white>%home%"));
	// Home
	public static final Translation COMMAND_HOME_DOESNT_EXIST = new Translation("commands.home.doesnt-exist").add(ComponentType.CHAT, text("<red>You do not have a home named <white>%home%<red>."));
	public static final Translation COMMAND_HOME_TELEPORTING = new Translation("commands.home.teleporting").add(ComponentType.CHAT, text("<green>Teleporting soon..."));
	public static final Translation COMMAND_HOME_TELEPORT_CANCELED_MOVED = new Translation("commands.home.canceled-moved").add(ComponentType.CHAT, text("<red>Canceled teleport you moved!"));
	public static final Translation COMMAND_HOME_TELEPORTED = new Translation("commands.home.teleported").add(ComponentType.CHAT, text("<yellow>Teleporting to your home!"));
	public static final Translation COMMAND_HOMES_LIST = new Translation("commands.homes.list").add(ComponentType.CHAT, text("<gray>- %home% <gray>x<white>%x%<gray>, <gray>xy<white>%y%<gray>, <gray>z<white>%z%<gray>, <white>%world%"));
	// Set home
	public static final Translation COMMAND_SET_HOME_ALREADY_EXISTS = new Translation("commands.set-home.already-exists").add(ComponentType.CHAT, text("<red>You already have a home named <white>%home%"));
	public static final Translation COMMAND_SET_HOME_MAX_HOMES = new Translation("commands.set-home.too-many-homes").add(ComponentType.CHAT, text("<red>You have already set too many homes!"));
	public static final Translation COMMAND_SET_HOME_SUCCESS = new Translation("commands.set-home.success").add(ComponentType.CHAT, text("<green>Set home <white>%home%<green> to <gray>x<white>%x%<gray>, y<white>%y%<gray>, z<white>%z%<gray>, <white>%world%<green>."));
	// Clear inv
	public static final Translation COMMAND_CLEAR_INVENTORY_CLEARED = new Translation("commands.clear-inventory.success").add(ComponentType.CHAT, text("<green>Cleared your inventory successfully."));
	public static final Translation COMMAND_CLEAR_INVENTORY_CANCEL = new Translation("commands.clear-inventory.cancel").add(ComponentType.CHAT, text("<green>Canceled your inventory clear."));
	public static final Translation COMMAND_CLEAR_INVENTORY_TOGGLE_TRUE = new Translation("commands.clear-inventory.toggle_true").add(ComponentType.CHAT, text("<green>You will now automatically clear your inventory when you use <white>/clear<green>."));
	public static final Translation COMMAND_CLEAR_INVENTORY_TOGGLE_FALSE = new Translation("commands.clear-inventory.toggle_false").add(ComponentType.CHAT, text("<green>You will no longer automatically clear your inventory when you use <white>/clear<green>."));
	// Clear my own chat
	public static final Translation COMMAND_CHAT_CLEAR_SELF = new Translation("commands.clear-my-chat.success").add(ComponentType.CHAT, text("<green>Cleared your chat!"));
	// Dupe
	public static final Translation COMMAND_DUPE_UNDUPABLE = new Translation("commands.dupe.undupable").add(ComponentType.CHAT, text("<red>You cannot dupe your item as it's undupable."));
	public static final Translation COMMAND_DUPE_BUNDLE = new Translation("commands.dupe.bundle").add(ComponentType.CHAT, text("<red>You cannot dupe your bundle as it contains undupable items."));
	public static final Translation COMMAND_DUPE_SHULKER = new Translation("commands.dupe.shulker").add(ComponentType.CHAT, text("<red>You cannot dupe your item as it's undupable."));
	public static final Translation COMMAND_DUPE_COMBAT = new Translation("commands.dupe.combat").add(ComponentType.CHAT, text("<red>You cannot dupe your item while in combat."));
	public static final Translation COMMAND_DUPE_SUPER_DUPER = new Translation("commands.dupe.multi").add(ComponentType.CHAT, text("<aqua><bold>SUPER DUPER<!bold> <white>Duped your item %super-duper% times."));
	// Mending
	public static final Translation COMMAND_MENDING_AIR = new Translation("commands.mending.cannot-enchant-air").add(ComponentType.CHAT, text("<red>Cannot enchant air with mending."));
	public static final Translation COMMAND_MENDING_CANNOT_ENCHANT = new Translation("commands.mending.cannot-enchant-item").add(ComponentType.CHAT, text("<red>Your item does not support mending."));
	public static final Translation COMMAND_MENDING_SUCCESS = new Translation("commands.mending.success").add(ComponentType.CHAT, text("<green>Enchanted your item with mending."));
	// Ping
	public static final Translation COMMAND_PING_SELF = new Translation("commands.ping.self").add(ComponentType.CHAT, text("<green>Your ping is <white>%ping_colored%ms"));
	public static final Translation COMMAND_PING_OTHER = new Translation("commands.ping.other").add(ComponentType.CHAT, text("<white>%player%<green>'s ping is <white>%ping_colored%ms"));
	// Playtime
	public static final Translation COMMAND_PLAYTIME_SELF = new Translation("commands.playtime.self").add(ComponentType.CHAT, text("<green>Your playtime is <white>%days% <gray>days, <white>%hours% <gray>hours, <white>%minutes% <gray>minutes, <white>%seconds% <gray>seconds,"));
	public static final Translation COMMAND_PLAYTIME_OTHER = new Translation("commands.playtime.other").add(ComponentType.CHAT, text("<white>%player%<green>'s playtime is <white>%days% <gray>days, <white>%hours% <gray>hours, <white>%minutes% <gray>minutes, <white>%seconds% <gray>seconds,"));
	// Rules
	public static final Translation COMMAND_RULES = new Translation("commands.rules.rules").add(ComponentType.CHAT, text("1. Don't hack\n2. Don't be racist"));
	// Ship
	public static final Translation COMMAND_SHIP = new Translation("commands.ship.ship").add(ComponentType.CHAT, text("\n <gray>Calculating <light_purple>%who_prefix_name% <gray>and <light_purpl e>%who-2_prefix_name%\nAre they the perfect GoldenDupe match? Let's find out!\n\n<white>Personality %progress-bar-0-int%/100\n  <white>%progress-bar-0-bar%\n<white>Passion %progress-bar-1-int%/100\n  <white>%progress-bar-1-bar%\n<white>Discrimination %progress-bar-2-int%/100\n  <white>%progress-bar-2-bar%\n<white>Hatred %progress-bar-3-int%/100\n  <white>%progress-bar-3-bar%\n<white>Love %progress-bar-4-int%/100\n  <white>%progress-bar-4-bar%"));
	// Toggle Items
	public static final Translation COMMAND_TOGGLE_ITEMS_TRUE = new Translation("commands.toggle-items.enabled").add(ComponentType.CHAT, text("<green>You will now receive random items!"));
	public static final Translation COMMAND_TOGGLE_ITEMS_FALSE = new Translation("commands.toggle-items.disabled").add(ComponentType.CHAT, text("<red>You will no longer receive random items!"));
	// Toggle Night Vision
	public static final Translation COMMAND_TOGGLE_NIGHT_VISION_TRUE = new Translation("commands.toggle-night-vision.enabled").add(ComponentType.CHAT, text("<green>You will now have permanent night vision"));
	public static final Translation COMMAND_TOGGLE_NIGHT_VISION_FALSE = new Translation("commands.toggle-night-vision.disabled").add(ComponentType.CHAT, text("<green>You will no longer have permanent night vision"));
	// Uptime
	public static final Translation COMMAND_UPTIME = new Translation("commands.uptime.uptime").add(ComponentType.CHAT, text("<green>The server has been up for <white>%time%"));
	// Nickname
	public static final Translation COMMAND_NICKNAME_TOO_LONG = new Translation("commands.nickname.too-long").add(ComponentType.CHAT, text("<red>You've tried to enter a too long nickname."));
	public static final Translation COMMAND_NICKNAME_ILLEGAL = new Translation("commands.nickname.illegal").add(ComponentType.CHAT, text("<red>Your nickname contains characters which are not allowed"));
	public static final Translation COMMAND_NICKNAME_SUCCESS = new Translation("commands.nickname.success").add(ComponentType.CHAT, text("<green>Changed your nickname to <white>%nickname%"));
	// Rename
	public static final Translation COMMAND_RENAME_AIR = new Translation("commands.rename.cannot-edit-air").add(ComponentType.CHAT, text("<red>Cannot change the name of air."));
	public static final Translation COMMAND_RENAME_TOO_LONG = new Translation("commands.rename.too-long").add(ComponentType.CHAT, text("<red>You've tried to use too long name for your item."));
	public static final Translation COMMAND_RENAME_SUCCESS = new Translation("commands.rename.too-long").add(ComponentType.CHAT, text("<green>Changed your item's name to <white>%name% <green>from <white>%old_name%"));
	// Toggle potion bottles
	public static final Translation COMMAND_TOGGLE_POTION_BOTTLES_TRUE = new Translation("commands.toggle-bottles.enabled").add(ComponentType.CHAT, text("<green>You will no longer receive potion bottles."));
	public static final Translation COMMAND_TOGGLE_POTION_BOTTLES_FALSE = new Translation("commands.toggle-bottles.disabled").add(ComponentType.CHAT, text("<green>You will now receive potion bottles."));
	// Toggle dropping
	public static final Translation COMMAND_TOGGLE_DROPPING_TRUE = new Translation("commands.toggle-drop.enabled").add(ComponentType.CHAT, text("<green>You will no longer be able to drop items."));
	public static final Translation COMMAND_TOGGLE_DROPPING_FALSE = new Translation("commands.toggle-drop.disabled").add(ComponentType.CHAT, text("<green>You will now be able to drop items."));
	// Toggle pickup
	public static final Translation COMMAND_TOGGLE_PICKUP_TRUE = new Translation("commands.toggle-pickup.enabled").add(ComponentType.CHAT, text("<green>You will no longer be able to pickup items."));
	public static final Translation COMMAND_TOGGLE_PICKUP_FALSE = new Translation("commands.toggle-pickup.disabled").add(ComponentType.CHAT, text("<green>You will now be able to pickup items."));
	// Flight
	public static final Translation COMMAND_FLY_TRUE = new Translation("commands.fly.enabled").add(ComponentType.CHAT, text("<green>You can now fly."));
	public static final Translation COMMAND_FLY_FALSE = new Translation("commands.fly.disabled").add(ComponentType.CHAT, text("<green>You can no longer fly."));
	public static final Translation COMMAND_FLY_ADMIN_TRUE = new Translation("commands.fly.admin-enabled").add(ComponentType.CHAT, text("<white>%player%<green> can fly now."));
	public static final Translation COMMAND_FLY_ADMIN_FALSE = new Translation("commands.fly.admin-disabled").add(ComponentType.CHAT, text("<white>%player%<green> can no longer fly."));
	// Speed toggle
	public static final Translation COMMAND_TOGGLE_SPEED_TRUE = new Translation("commands.toggle-speed.enabled").add(ComponentType.CHAT, text("<green>You will now have permanent speed 1."));
	public static final Translation COMMAND_TOGGLE_SPEED_FALSE = new Translation("commands.toggle-speed.disabled").add(ComponentType.CHAT, text("<green>You will no longer have permanent speed 1."));
	// Clear chat
	public static final Translation COMMAND_CHAT_CLEAR_STAFF = new Translation("commands.clear-chat.success").add(ComponentType.CHAT, text("<green>The chat was cleared by <white>%who%<green>!"));
	// Staff chat
	public static final Translation COMMAND_STAFF_CHAT_TRUE = new Translation("commands.staff-chat.enabled").add(ComponentType.CHAT, text("<green>Your chat messages will now be forwarded to staff chat."));
	public static final Translation COMMAND_STAFF_CHAT_FALSE = new Translation("commands.staff-chat.disabled").add(ComponentType.CHAT, text("<green>Your chat messages will no longer forwarded to staff chat."));
	public static final Translation COMMAND_STAFF_CHAT_MESSAGE = new Translation("commands.staff-chat.chat").add(ComponentType.CHAT, text("<aqua><bold>[STAFF]<!bold> <white>%player% >> <white>%message%"));
	public static final Translation COMMAND_STAFF_CHAT_MESSAGE_CONSOLE = new Translation("commands.staff-chat.chat-console").add(ComponentType.CHAT, text("<aqua><bold>[STAFF]<!bold> <red>CONSOLE >> <white>%message%"));
	public static final Translation COMMAND_DONATOR_CHAT_TRUE = new Translation("commands.donator-chat.enabled").add(ComponentType.CHAT, text("<green>Your chat messages will now be forwarded to donator chat."));
	public static final Translation COMMAND_DONATOR_CHAT_FALSE = new Translation("commands.donator-chat.disabled").add(ComponentType.CHAT, text("<green>Your chat messages will no longer forwarded to donator chat."));
	public static final Translation COMMAND_DONATOR_CHAT_MESSAGE = new Translation("commands.donator-chat.chat").add(ComponentType.CHAT, text("<gold><bold>[DONOR]<!bold> <white>%player% >> <white>%message%"));
	public static final Translation COMMAND_DONATOR_CHAT_MESSAGE_CONSOLE = new Translation("commands.donator-chat.chat-console").add(ComponentType.CHAT, text("<gold><bold>[DONOR]<!bold> <red>CONSOLE >> <white>%message%"));
	public static final Translation COMMAND_BOOSTER_CHAT_TRUE = new Translation("commands.booster-chat.enabled").add(ComponentType.CHAT, text("<green>Your chat messages will now be forwarded to booster chat."));
	public static final Translation COMMAND_BOOSTER_CHAT_FALSE = new Translation("commands.booster-chat.disabled").add(ComponentType.CHAT, text("<green>Your chat messages will no longer forwarded to booster chat."));
	public static final Translation COMMAND_BOOSTER_CHAT_MESSAGE = new Translation("commands.booster-chat.chat").add(ComponentType.CHAT, text("<purple><bold>[BOOSTER]<!bold> <white>%player% >> <white>%message%"));
	public static final Translation COMMAND_BOOSTER_CHAT_MESSAGE_CONSOLE = new Translation("commands.booster-chat.chat-console").add(ComponentType.CHAT, text("<purple><bold>[BOOSTER]<!bold> <red>CONSOLE >> <white>%message%"));
	public static final Translation COMMAND_ADMIN_CHAT_TRUE = new Translation("commands.admin-chat.enabled").add(ComponentType.CHAT, text("<green>Your chat messages will now be forwarded to admin chat."));
	public static final Translation COMMAND_ADMIN_CHAT_FALSE = new Translation("commands.admin-chat.disabled").add(ComponentType.CHAT, text("<green>Your chat messages will no longer forwarded to admin chat."));
	public static final Translation COMMAND_ADMIN_CHAT_MESSAGE = new Translation("commands.admin-chat.chat").add(ComponentType.CHAT, text("<red><bold>[ADMIN]<!bold> <white>%player% >> <white>%message%"));
	public static final Translation COMMAND_ADMIN_CHAT_MESSAGE_CONSOLE = new Translation("commands.admin-chat.chat-console").add(ComponentType.CHAT, text("<red><bold>[ADMIN]<!bold> <red>CONSOLE >> <white>%message%"));
	// Illegals
	public static final Translation LISTENER_ILLEGAL = new Translation("listeners.illegal.cannot-place").add(ComponentType.CHAT, text("<red>You cannot place <white>%block%<red>."));

	// mute chat
	public static final Translation COMMAND_MUTECHAT_UNMUTED = new Translation("commands.mutechat.unmuted").add(ComponentType.CHAT, text("<green>The chat has been unmuted!"));
	public static final Translation COMMAND_MUTECHAT_MUTED = new Translation("commands.mutechat.muted").add(ComponentType.CHAT, text("<red>The chat has been muted!"));
	public static final Translation COMMAND_MUTECHAT_BYPASS = new Translation("commands.mutechat.bypass").add(ComponentType.CHAT, text("<yellow>You have permission to bypass chat mute."));
	public static final Translation LISTENER_MUTECHAT_MUTED = new Translation("listener.mutechat.muted").add(ComponentType.CHAT, text("<yellow>The chat is currently muted!."));
	public static final Translation TIMED_MUTECHAT_REMINDER_1 = new Translation("timed.mutechat.second").add(ComponentType.ACTION_BAR, text("<red>The chat is currently muted!"));
	public static final Translation TIMED_MUTECHAT_REMINDER_30 = new Translation("timed.mutechat.half_minute").add(ComponentType.CHAT, text("<red>The chat is currently muted!"));

	// Creative GUI
	public static final Translation GUI_CREATIVE_TITLE = new Translation("gui.creative_blocks.title").add(ComponentType.CHAT, text("Blocks <yellow>%page%<dark_gray>/<red>%page_max%<"));
	public static final Translation GUI_CREATIVE_ITEM_NAME = new Translation("gui.creative_blocks.block_name").add(ComponentType.CHAT, text("<yellow>%block%"));
	public static final Translation GUI_CREATIVE_BACK = new Translation("gui.creative_blocks.back").add(ComponentType.CHAT, text("<green>Previous Page"));
	public static final Translation GUI_CREATIVE_CLOSE = new Translation("gui.creative_blocks.close").add(ComponentType.CHAT, text("<red>Close"));
	public static final Translation GUI_CREATIVE_NEXT = new Translation("gui.creative_blocks.next").add(ComponentType.CHAT, text("<green>Next Page"));

	private static Component text(@NotNull String val){
		return MiniMessage.miniMessage().deserialize(val);
	}

	public static Translation get(@NotNull String key){
		return translation.containsKey(key) ? translation.get(key) : new Translation(key).add(ComponentType.CHAT, text(key));
	}

	public static Collection<Translation> translations(){
		return translation.values();
	}

	public static JsonObject createDefaults() throws IllegalAccessException {
		JsonObject fullObject = new JsonObject();
		Class<Translations> translationsClass = Translations.class;
		Field[] fields = translationsClass.getFields();
		for (Field field : fields){
			if (field.canAccess(null)){
				Object object = field.get(null);
				if (object instanceof Translation translation){
					Message message = translation.messages;
					JsonElement element = null;
					if (message.componentPart.size() > 1 || message.componentPart.get(ComponentType.CHAT) == null) {
						JsonObject current = new JsonObject();
						for (Map.Entry<ComponentType, ComponentPart> entry : message.componentPart.entrySet()) {
							ComponentType type = entry.getKey();
							ComponentPart part = entry.getValue();

							if (part instanceof TitleComponentPart title){
								JsonObject titleObj = new JsonObject();
								titleObj.addProperty("in", title.getFadeIn().toMillis());
								titleObj.addProperty("stay", title.getStay().toMillis());
								titleObj.addProperty("out", title.getFadeOut().toMillis());
								titleObj.addProperty("value", mm.serialize(part.getTextComponent()));
								current.add(type.getName(), titleObj);
							} else {
								current.addProperty(type.getName(), mm.serialize(part.getTextComponent()));
							}
						}
					} else {
						element = gson.toJsonTree(mm.serialize(((ComponentPart) (List.of(message.componentPart.values().toArray()).getFirst())).getTextComponent()));
					}
					fullObject.add(translation.key, element);
				}
			}
		}
		return fullObject;
	}

	public static class Message {
		private final Map<ComponentType, ComponentPart> componentPart = new HashMap<>();
		private final Translation translation;
		public Message(Translation translation){
			this.translation = translation;
		}

		public Message add(ComponentType componentType, Component component){
			componentPart.put(componentType, ComponentPart.of(component));
			return this;
		}
		public Message add(ComponentType componentType, Component component, Title.Times times){
			componentPart.put(componentType, ComponentPart.of(component, times));
			return this;
		}

		public Translation asTranslation(){
			return translation;
		}
	}

	public static class Translation implements TranslationKey{
		private final String key;
		private final Message messages;

		public Translation(String key) {
			translation.put(key, this);
			this.key = key;
			this.messages = new Message(this);
		}

		@Override
		public @NotNull String getKey() {
			return key;
		}

		public Translation add(ComponentType componentType, Component component){
			messages.add(componentType, component);
			return this;
		}
		public Translation add(ComponentType componentType, Component component, Title.Times times){
			messages.add(componentType, component, times);
			return this;
		}
	}
}
