package xyz.goldendupe.models.savable;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Savable<Key> {
	@NotNull
	PreparedStatement insertStatement(@NotNull Connection connection) throws SQLException;
	PreparedStatement updateStatement(@NotNull Connection connection) throws SQLException;
	PreparedStatement getStatement(@NotNull Connection connection, Key key) throws SQLException;
}
