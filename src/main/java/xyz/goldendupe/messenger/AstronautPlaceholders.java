package xyz.goldendupe.messenger;

import bet.astral.astronauts.goldendupe.Astronauts;
import bet.astral.messenger.placeholder.Placeholder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.models.astronauts.Comment;
import xyz.goldendupe.models.astronauts.RUser;
import xyz.goldendupe.models.astronauts.Report;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static bet.astral.messenger.utils.PlaceholderUtils.createPlaceholder;

@Astronauts
public class AstronautPlaceholders {
	private static final GoldenDupe goldenDupe = GoldenDupe.getPlugin(GoldenDupe.class);
	public static List<Placeholder> userReportPlaceholders(String name, RUser user){
		List<Placeholder> placeholders = new ArrayList<>();
		placeholders.add(createPlaceholder(name, "uniqueId", user.uuid().toString()));

		placeholders.add(createPlaceholder(name, "player_reports", user.playerReportsSent()));
		placeholders.add(createPlaceholder(name, "player_reports_true", user.punishedPlayerReports()));
		placeholders.add(createPlaceholder(name, "player_reports_false", user.amountOfFalsePlayerReports()));
		placeholders.add(createPlaceholder(name, "player_reports_not_solved", user.playerReportsSent()-user.amountOfFalsePlayerReports()-user.punishedPlayerReports()));
		placeholders.add(createPlaceholder(name, "player_reports_banned_color", user.bannedFromPlayerReports() ? Component.text(true, NamedTextColor.RED) : Component.text(false, NamedTextColor.GREEN)));
		if (user.playerReportsSent() == 0){
			placeholders.add(createPlaceholder(name, "bug_report_success_rate", 0+"%%"));
		} else {
			placeholders.add(createPlaceholder(name, "player_report_success_rate", (user.amountOfFalsePlayerReports()/user.playerReportsSent())*100+"%%"));
		}

		placeholders.add(createPlaceholder(name, "bug_reports", user.bugReportsSent()));
		placeholders.add(createPlaceholder(name, "bug_reports_false", user.amountOfFalseBugReports()));
		placeholders.add(createPlaceholder(name, "bug_reports_banned", user.bannedFromBugReports()));
		placeholders.add(createPlaceholder(name, "bug_reports_banned_color", user.bannedFromBugReports() ? Component.text(true, NamedTextColor.RED) : Component.text(false, NamedTextColor.GREEN)));
		if (user.bugReportsSent() == 0){
			placeholders.add(createPlaceholder(name, "bug_report_success_rate", 0+"%%"));
		} else {
			placeholders.add(createPlaceholder(name, "bug_report_success_rate", (user.amountOfFalseBugReports()/user.bugReportsSent())*100+"%%"));
		}

		placeholders.add(createPlaceholder(name, "reports", user.bugReportsSent()+user.playerReportsSent()));

		placeholders.add(createPlaceholder(name, "against", user.reportsReceived()));
		placeholders.add(createPlaceholder(name, "against_true", user.reportsReceived()));
		placeholders.add(createPlaceholder(name, "against_false", user.reportsReceived()));

		placeholders.add(createPlaceholder(name, "dealt", user.reportsDealtWith()));
		placeholders.add(createPlaceholder(name, "dealt_true", user.reportsDealtWithSuccess()));
		placeholders.add(createPlaceholder(name, "dealt_false", user.reportsDealtWithFalse()));
		placeholders.add(createPlaceholder(name, "dealt_commented", user.reportsDealtWithCommented()));

		return placeholders;
	}
	public static List<Placeholder> reportPlaceholders(String name, Report report){
		List<Placeholder> placeholders = new ArrayList<>();
		placeholders.add(createPlaceholder(name, "uniqueId", report.id().toString()));
		placeholders.add(createPlaceholder(name, "whoReported", report.whoReported().toString()));
		placeholders.add(createPlaceholder(name, "whoClosed", report.whoClosed() != null ? report.whoClosed().toString() : "Not Closed / NPE"));
		placeholders.add(createPlaceholder(name, "whoWasReported", report.whoWasReported() != null ? report.whoWasReported().toString() : "None"));

		placeholders.add(createPlaceholder(name, "solved", report.isSolved));
		placeholders.add(createPlaceholder(name, "info", report.info()));
		placeholders.add(createPlaceholder(name, "comments", report.comments().size()));

		return placeholders;
	}

	public static List<Placeholder> commentPlaceholders(String name, Comment comment){
		List<Placeholder> placeholders = new ArrayList<>();
		placeholders.add(createPlaceholder(name, "uniqueId", comment.id().toString()));
		placeholders.add(createPlaceholder(name, "who_uniqueId", comment.id().toString()));
		placeholders.add(createPlaceholder(name, "who", Objects.requireNonNull(Bukkit.getPlayer(comment.who())).name()));
		placeholders.add(createPlaceholder(name, "contents", comment.contents()));

		return placeholders;
	}
}
