package bet.astral.wormhole.managers.data.sql;

import bet.astral.wormhole.plugin.WormholePlugin;
import lombok.Getter;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter
public class SQLDatabase {
    private Connection connection;

    public void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:"+ new File(WormholePlugin.getProvidingPlugin(WormholePlugin.class).getDataFolder(), "database.db"));
    }

    public void disconnect() throws SQLException {
        connection.close();
    }
}
