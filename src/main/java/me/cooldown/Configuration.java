package me.cooldown;

import lombok.Getter;
import me.cooldown.enums.ConnectionType;
import me.cooldown.exceptions.ConfigurationError;
import me.cooldown.manager.ConnectionManager;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

public class Configuration {

    private static Configuration INSTANCE = new Configuration();

    @Getter
    private static Optional<ConnectionManager> connectionManager = Optional.empty();


    private Configuration() {

    }

    public void defineDatabase(Plugin plugin, ConnectionType type, String host, String user, int port, String password, String database){
        connectionManager = Optional.of(new ConnectionManager(plugin, type, host, user, password, database, port));
        new Checker();
    }

    public static Configuration getINSTANCE() {
        return INSTANCE;
    }

    public ConnectionManager getConnectionManager() {
        if (!connectionManager.isPresent()) throw new ConfigurationError("The MYSQL is not configured appropriately.");
        return connectionManager.get();
    }
}
