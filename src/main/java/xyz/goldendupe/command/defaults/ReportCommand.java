package xyz.goldendupe.command.defaults;

import bet.astral.astronauts.goldendupe.Astronauts;
import bet.astral.messenger.Message;
import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.messenger.utils.PlaceholderUtils;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.samjakob.spigui.menu.SGMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.incendo.cloud.Command;
import org.incendo.cloud.bukkit.parser.OfflinePlayerParser;
import org.incendo.cloud.bukkit.parser.PlayerParser;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.permission.PredicatePermission;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.command.internal.cloud.Cloud;
import xyz.goldendupe.command.internal.cloud.GDCloudCooldownCommand;
import xyz.goldendupe.database.astronauts.ReportDatabase;
import xyz.goldendupe.database.astronauts.ReportUserDatabase;
import xyz.goldendupe.models.astronauts.RUser;
import xyz.goldendupe.models.astronauts.Report;
import xyz.goldendupe.messenger.AstronautPlaceholders;
import xyz.goldendupe.messenger.GoldenMessenger;

import java.util.*;

import static xyz.goldendupe.command.internal.legacy.GDCommandInfo.MemberType.*;

// TODO
@Astronauts
@Cloud
public class ReportCommand extends GDCloudCooldownCommand {
	private final Map<UUID, SGMenu> lookupMainMenus = new HashMap<>();
	private final Map<UUID, SGMenu> lookupSentMenus = new HashMap<>();
	private final Map<UUID, SGMenu> lookupReceivedMenus = new HashMap<>();
	private ReportCommand(GoldenDupe goldenDupe, PaperCommandManager<CommandSender> commandManager) {
		super(goldenDupe, commandManager, 250);
		Command.Builder<CommandSender> reportBuilder =
				commandManager
						.commandBuilder(
								"report", Description.description(""), "helpop")
						.permission(DEFAULT.cloudOf("report"))
						.senderType(CommandSender.class)
						.handler(context->{
							CommandSender sender = context.sender();
							if (hasCooldown(sender)){
								commandMessenger.message(sender, "report.message-cooldown");
								return;
							}

							commandMessenger.message(sender, "report.message-help");
						});
		commandManager.command(reportBuilder);

		commandManager.command(reportBuilder
				.literal("player", Description.of("Report a player"))
				.argument(PlayerParser.playerComponent().name("who").description(Description.of("Who to report.")))
				.argument(StringParser.stringComponent(StringParser.StringMode.GREEDY).name("reason").description(Description.of("Reason to report the player for.")))
				.senderType(Player.class)
				.permission(PredicatePermission.of(
						sender-> !goldenDupe.reportUserDatabase().asUser(sender).bannedFromBugReports())
				)
				.handler(context->{
					Player player = context.sender();
					if (hasCooldown(player)){
						commandMessenger.message(player, "report.message-cooldown");
						return;
					}
					ReportUserDatabase reportUserDatabase = goldenDupe.reportUserDatabase();
					RUser rUser = reportUserDatabase.asUser(player);

					if (rUser.bannedFromPlayerReports()){
						commandMessenger.message(player, "report-message-banned-from-player-reports");
						return;
					}

					Player who = context.get("who");
					String reason = context.get("reason");

					if (who.equals(player) && !goldenDupe.isDebug()){
						commandMessenger.message(player, "report.message-cannot-report-self");
						return;
					}

					Report report = new Report(UUID.randomUUID(), player.getUniqueId(), who.getUniqueId(), null, reason, Report.Type.PLAYER_REPORT);
					report.isSolved = false;

					ReportDatabase reportDatabase = goldenDupe.reportDatabase();
					reportDatabase.addReport(report);
					reportDatabase.save(report);

					rUser.setPlayerReportsSent(rUser.playerReportsSent()+1);

					List<Placeholder> placeholders = new ArrayList<>(GoldenMessenger.playerPlaceholders("player", player));
					placeholders.addAll(AstronautPlaceholders.userReportPlaceholders("who", rUser));
					placeholders.addAll(AstronautPlaceholders.reportPlaceholders("report", report));

					placeholders.add(new Placeholder("who", who.name()));
					placeholders.addAll(GoldenMessenger.playerPlaceholders("who", who));
					placeholders.add(new Placeholder("reason", reason));

					if (rUser.knownToAbuseReports()) {
						commandMessenger.message(player, "report.message-reported-player-known-abuser", placeholders);
						commandMessenger.broadcast(GoldenMessenger.MessageChannel.STAFF, "report.message-reported-player-staff-known-abuser", placeholders);
					} else {
						commandMessenger.message(player, "report.message-reported-player", placeholders);
						commandMessenger.broadcast(GoldenMessenger.MessageChannel.STAFF, "report.message-reported-player-staff", placeholders);
					}

				})
		);
		commandManager.command(reportBuilder
				.literal("bug", Description.of("Report a bug"))
				.argument(StringParser.stringComponent(StringParser.StringMode.GREEDY).name("what-being-reported").description(Description.of("What to report")))
				.senderType(Player.class)
				.permission(PredicatePermission.of(
						sender-> !goldenDupe.reportUserDatabase().asUser(sender).bannedFromBugReports())
				)
				.handler(context->{
					Player player = context.sender();
					if (hasCooldown(player)){
						commandMessenger.message(player, "report.message-cooldown");
						return;
					}
					ReportUserDatabase reportUserDatabase = goldenDupe.reportUserDatabase();
					RUser rUser = reportUserDatabase.asUser(player);

					if (rUser.bannedFromPlayerReports()){
						commandMessenger.message(player, "report-message-banned-from-bug-reports");
						return;
					}

					String reason = context.get("what-being-reported");


					Report report = new Report(UUID.randomUUID(), player.getUniqueId(), null, null, reason, Report.Type.BUG_REPORT);
					report.isSolved = false;

					ReportDatabase reportDatabase = goldenDupe.reportDatabase();
					reportDatabase.addReport(report);
					reportDatabase.save(report);

					rUser.setBugReportsSent(rUser.bugReportsSent()+1);

					List<Placeholder> placeholders = new ArrayList<>(PlaceholderUtils.createPlaceholders(player));
					placeholders.addAll(AstronautPlaceholders.userReportPlaceholders("who", rUser));
					placeholders.addAll(AstronautPlaceholders.reportPlaceholders("report", report));

					placeholders.add(new Placeholder("%who%", player.name()));
					placeholders.add(new Placeholder("%reason%", reason));

					commandMessenger.message(player, "report.message-reported-bug", placeholders);
					if (rUser.knownToAbuseReports()) {
						commandMessenger.broadcast(GoldenMessenger.MessageChannel.STAFF, "report.message-reported-bug-staff-known-abuser", placeholders);
					} else {
						commandMessenger.broadcast(GoldenMessenger.MessageChannel.STAFF, "report.message-reported-bug-staff", placeholders);
					}
				})
		);
		String permPlayerLookup = "report.lookup";
		String permPlayerSent = "report.lookup.sent";
		String permPlayerReceived = "report.lookup.received";

		Command.Builder<CommandSender> reportDatabase = reportBuilder.literal("lookup", Description.of("Allows staff members to lookup information about players and such."), "lookup")
				.permission(MODERATOR.cloudOf(permPlayerLookup))
				.argument(OfflinePlayerParser.offlinePlayerComponent().name("who").description(Description.of("Who to lookup")))
				.handler(context->{
					CommandSender sender = context.sender();
					OfflinePlayer player = context.get("who");
				});
		commandManager.command(reportDatabase);
		commandManager.command(reportDatabase
				.literal("sent", Description.of("Allows staff members to see reports sent by a player"))
				.handler(context->{
					CommandSender sender = context.sender();
					OfflinePlayer player = context.get("who");
					commandMessenger.message(sender, "report.message-database-report-sent-loading");
					goldenDupe.reportUserDatabase().load(player).thenAcceptAsync(user->{
						List<Placeholder> placeholders = new ArrayList<>();
						placeholders.addAll(PlaceholderUtils.createPlaceholders("player", player));
						placeholders.addAll(AstronautPlaceholders.userReportPlaceholders("player", user));
						commandMessenger.message(sender, "report.message-database-report-sent", placeholders);
						commandMessenger.message(sender, "report.message-database-report-sent-loading-sent", placeholders);
						goldenDupe.reportDatabase().sent(player).thenAcceptAsync(reports->{
							for (Report report : reports) {
								List<Placeholder> reportPlaceholders = AstronautPlaceholders.reportPlaceholders("report", report);
								commandMessenger.message(sender, "report-message-database-report-sent-report-info", reportPlaceholders);
							}
						});
					});
				})
		);
		commandManager.command(reportDatabase
				.literal("received", Description.of("Allows staff members to see reports against a player"))
				.handler(context->{
					CommandSender sender = context.sender();
					OfflinePlayer player = context.get("who");
					commandMessenger.message(sender, "report.message-database-report-received-loading");
					goldenDupe.reportUserDatabase().load(player).thenAcceptAsync(user->{
						List<Placeholder> placeholders = new ArrayList<>();
						placeholders.addAll(PlaceholderUtils.createPlaceholders("player", player));
						placeholders.addAll(AstronautPlaceholders.userReportPlaceholders("player", user));
						commandMessenger.message(sender, "report.message-database-report-received", placeholders);
						commandMessenger.message(sender, "report.message-database-report-received-loading-sent", placeholders);
						goldenDupe.reportDatabase().sent(player).thenAcceptAsync(reports->{
							for (Report report : reports) {
								List<Placeholder> reportPlaceholders = AstronautPlaceholders.reportPlaceholders("report", report);
								commandMessenger.message(sender, "report-message-database-report-received-report-info", reportPlaceholders);
							}
						});
					});
				})
		);
	}

	private ItemStack skull(OfflinePlayer player){
		ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();

		PlayerProfile profile = player.getPlayerProfile();

		skullMeta.setPlayerProfile(profile);
		skullMeta.setOwningPlayer(player);

		playerHead.setItemMeta(skullMeta);
		return playerHead;
	}

	private void openLookupMainMenu(Player to, OfflinePlayer player) {
		SGMenu menu = lookupMainMenus.get(player.getUniqueId());
		if (menu == null) {
			menu = goldenDupe.spiGUI().create("Reports > " + player.getName(), 3);
			lookupMainMenus.put(player.getUniqueId(), menu);
		}
		goldenDupe.reportUserDatabase().load(player)
				.thenAccept(user -> {
							List<Placeholder> placeholders = new ArrayList<>();
							placeholders.addAll(PlaceholderUtils.createPlaceholders("player", player));
							placeholders.addAll(AstronautPlaceholders.userReportPlaceholders("report", user));

							ItemStack mainHead = skull(player);
							SkullMeta mainHeadMeta = (SkullMeta) mainHead.getItemMeta();
							List<Component> mainHeadLore = new ArrayList<>(commandMessenger.parseAsList("report.lookup-menu.main-info.lore", placeholders));
							mainHeadMeta.lore(mainHeadLore);
							mainHeadMeta.displayName(commandMessenger.parse(commandMessenger.getMessage("report.lookup-menu.main-info.name"), Message.Type.CHAT, placeholders));


						}
				);
	}

	private void openLookupSentMenu(Player to, OfflinePlayer player){
	}

	private void openLookupReceivedMenu(Player to, OfflinePlayer player){
	}


}
