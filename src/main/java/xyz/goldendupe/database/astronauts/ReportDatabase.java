package xyz.goldendupe.database.astronauts;

import bet.astral.astronauts.goldendupe.Astronauts;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.models.astronauts.Report;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Astronauts
public class ReportDatabase {
	private final Map<UUID, Report> cachedReports = new HashMap<>();
	private final Set<Report> requestSaves = new HashSet<>();
	private final GoldenDupe goldenDupe;
	public ReportDatabase(GoldenDupe goldenDupe) {
		this.goldenDupe = goldenDupe;
	}

	public void addReport(Report report){
		cachedReports.put(report.id(), report);
	}

	public void removeReport(Report report){
		cachedReports.put(report.id(), report);
	}

	public void delete(Report report){
	}
	public void save(Report report){
		if (requestSaves.contains(report)){
			return;
		}
		requestSaves.add(report);
	}

	public Set<Report> reports(Report.Type type){
		switch (type){
			case BUG_REPORT -> {
				return cachedReports.values().stream().filter(report->report.type()== Report.Type.BUG_REPORT).collect(Collectors.toSet());
			}
			case PLAYER_REPORT -> {
				return cachedReports.values().stream().filter(report->report.type()== Report.Type.PLAYER_REPORT).collect(Collectors.toSet());
			}
		}
		return Collections.emptySet();
	}

	public CompletableFuture<List<Report>> against(Player player){
		return CompletableFuture.supplyAsync(new Supplier<List<Report>>() {
			@Override
			public List<Report> get() {
				return null;
			}
		});
	}
	public CompletableFuture<List<Report>> sent(OfflinePlayer player){
		return CompletableFuture.supplyAsync(new Supplier<List<Report>>() {
			@Override
			public List<Report> get() {
				return null;
			}
		});	}
}
