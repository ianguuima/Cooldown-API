package me.cooldown.connection.type;

import org.bukkit.plugin.Plugin;

import java.sql.Connection;

public abstract class ConnectionBase {

    /**
     * Essa é a base do sistema.
     * Aqui iremos facilitar a criação de qualquer nova database para o nosso sistema, incluindo as existentes.
     * Não MEXA se não sabe o que está fazendo.
     */

    Connection connection;

    abstract public void openConnection();

    abstract public void closeConnection();

    public Connection getConnection() {
        return connection;
    }

}
