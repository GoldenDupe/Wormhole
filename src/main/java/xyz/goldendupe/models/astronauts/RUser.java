package xyz.goldendupe.models.astronauts;


import bet.astral.astronauts.goldendupe.Astronauts;
import xyz.goldendupe.utils.annotations.temporal.RequireSave;

import java.util.UUID;

@RequireSave
@Astronauts
public class RUser {
	private final UUID uuid;
	private boolean knownToAbuseReports;
	private boolean bannedFromPlayerReports;
	private boolean bannedFromBugReports;
	private int amountOfFalsePlayerReports;
	private int amountOfFalseBugReports;
	private int playerReportsSent;
	private int punishedPlayerReports;
	private int bugReportsSent;
	private int reportsReceived;
	private int reportsDealtWith;
	private int reportsDealtWithSuccess;
	private int reportsDealtWithFalse;
	private int reportsDealtWithCommented;

	public RUser(UUID uniqueId) {
		this.uuid = uniqueId;
	}

	/**
	 * Returns the uuid of this user.
	 * @return uuid
	 */
	public UUID uuid() {
		return uuid;
	}

	/**
	 * Returns if the player is known to abuse player reports
	 * @return is known to abuse reports
	 */
	public boolean knownToAbuseReports() {
		return knownToAbuseReports;
	}

	/**
	 * Returns if the player is known to abuse bug reports.
	 * @return is known to abuse reports
	 */
	public boolean bannedFromPlayerReports() {
		return bannedFromPlayerReports;
	}

	/**
	 * Returns the number of false reports which this player has done against other players.
	 * @return number of reports
	 */
	public int amountOfFalsePlayerReports() {
		return amountOfFalsePlayerReports;
	}

	/**
	 * Times the player has reported bugs, which did not exist.
	 * @return false reports
	 */
	public int amountOfFalseBugReports() {
		return amountOfFalseBugReports;
	}

	/**
	 * Is the player banned from creating new bug reports?
	 * Most of the players banned from reports are those who spread misinformation.
	 * @return is banned from reports
	 */
	public boolean bannedFromBugReports() {
		return bannedFromBugReports;
	}

	/**
	 * Returns the number of reports sent
	 * @return number of reports sent
	 */
	public int playerReportsSent() {
		return playerReportsSent;
	}

	/**
	 * Returns the number of reports which were successful from sent reports.
	 * @return times a punishment was dealt
	 */
	public int punishedPlayerReports() {
		return punishedPlayerReports;
	}

	/**
	 * Returns the number of bug reports sent by the player.
	 * @return reports
	 */
	public int bugReportsSent() {
		return bugReportsSent;
	}

	/**
	 * Returns the number of reports this player has received against.
	 * @return number of reports
	 */
	public int reportsReceived() {
		return reportsReceived;
	}

	/**
	 * Returns the number of reports where this player has been active in.
	 * @return this
	 */
	public int reportsDealtWith() {
		return reportsDealtWith;
	}

	/**
	 * Returns the number of reports where this player has marked a report to be true.
	 * @return number of reports
	 */
	public int reportsDealtWithSuccess() {
		return reportsDealtWithSuccess;
	}

	/**
	 * Returns the number of reports where this player has marked a report to be false.
	 * @return number of reports
	 */
	public int reportsDealtWithFalse() {
		return reportsDealtWithFalse;
	}

	/**
	 * Returns the number of reports where this player has commented on a report.
	 * @return number of reports
	 */
	public int reportsDealtWithCommented() {
		return reportsDealtWithCommented;
	}

	/**
	 * Sets the abuse status of the user.
	 * @param knownToAbuseReports is known to abuse reports
	 * @return this
	 */
	public RUser setKnownToAbuseReports(boolean knownToAbuseReports) {
		this.knownToAbuseReports = knownToAbuseReports;
		return this;
	}

	/**
	 * Sets if the user is banned from the reports of players.
	 * @param bannedFromPlayerReports banned
	 * @return this
	 */
	public RUser setBannedFromPlayerReports(boolean bannedFromPlayerReports) {
		this.bannedFromPlayerReports = bannedFromPlayerReports;
		return this;
	}

	/**
	 * Sets if the user is banned from bug reports.
	 * @param bannedFromBugReports banned
	 * @return this
	 */
	public RUser setBannedFromBugReports(boolean bannedFromBugReports) {
		this.bannedFromBugReports = bannedFromBugReports;
		return this;
	}

	/**
	 * Sets the number this user has sent false reports.
	 * @param amountOfFalsePlayerReports number of times
	 * @return this
	 */
	public RUser setAmountOfFalsePlayerReports(int amountOfFalsePlayerReports) {
		this.amountOfFalsePlayerReports = amountOfFalsePlayerReports;
		return this;
	}

	/**
	 * sets the amount of false bug reports this player has sent.
	 * @param amountOfFalseBugReports times
	 * @return this
	 */
	public RUser setAmountOfFalseBugReports(int amountOfFalseBugReports) {
		this.amountOfFalseBugReports = amountOfFalseBugReports;
		return this;
	}

	/**
	 * Sets the amount of reports this user has sent.
	 * @param playerReportsSent number of reports
	 * @return this
	 */
	public RUser setPlayerReportsSent(int playerReportsSent) {
		this.playerReportsSent = playerReportsSent;
		return this;
	}

	/**
	 * Sets the number of punishments dealt for players which were reported by the player.
	 * @param punishedPlayerReports number of reports
	 * @return this
	 */
	public RUser setPunishedPlayerReports(int punishedPlayerReports) {
		this.punishedPlayerReports = punishedPlayerReports;
		return this;
	}

	/**
	 * Sets the amount of bug reports this user has sent.
	 * @param bugReportsSent number of reports
	 * @return this
	 */
	public RUser setBugReportsSent(int bugReportsSent) {
		this.bugReportsSent = bugReportsSent;
		return this;
	}

	/**
	 * Sets the number of reports received by this player
 	 * @param reportsReceived player
	 * @return this
	 */
	public RUser setReportsReceived(int reportsReceived) {
		this.reportsReceived = reportsReceived;
		return this;
	}

	/**
	 * Sets the number of reports this player has been in contact with
	 * @param reportsDealtWith number of reports
	 * @return this
	 */
	public RUser setReportsDealtWith(int reportsDealtWith) {
		this.reportsDealtWith = reportsDealtWith;
		return this;
	}

	/**
	 * Sets the number of reports where this player accepted the report as real and punished the player.
	 * @param reportsDealtWithSuccess number of reports
	 * @return this
	 */
	public RUser setReportsDealtWithSuccess(int reportsDealtWithSuccess) {
		this.reportsDealtWithSuccess = reportsDealtWithSuccess;
		return this;
	}

	/**
	 * Sets the number of reports where this player denied the report and made it a false report.
	 * @param reportsDealtWithFalse number of reports
	 * @return this
	 */
	public RUser setReportsDealtWithFalse(int reportsDealtWithFalse) {
		this.reportsDealtWithFalse = reportsDealtWithFalse;
		return this;
	}

	/**
	 * Sets the numbr of reports where this player added a comment in.
	 * @param reportsDealtWithCommented number of commented reports
	 * @return this
	 */
	public RUser setReportsDealtWithCommented(int reportsDealtWithCommented) {
		this.reportsDealtWithCommented = reportsDealtWithCommented;
		return this;
	}
}
