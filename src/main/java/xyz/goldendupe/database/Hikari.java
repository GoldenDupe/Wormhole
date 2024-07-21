package xyz.goldendupe.database;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class Hikari {
	private HikariDataSource hikariDataSource;

	public Connection getConnection() throws SQLException {
		return hikariDataSource.getConnection();
	}
	public <T> CompletableFuture<T> handleExceptions(CompletableFuture<T> future){
		return future.ex
	}
}
