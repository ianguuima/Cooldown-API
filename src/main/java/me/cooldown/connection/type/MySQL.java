package me.cooldown.connection.type;

import java.sql.DriverManager;
import java.sql.SQLException;


public class MySQL extends ConnectionBase {

    /**
     * O banco de dados MySQL, estabeleceremos as conexões respectivas, abrir e fechar.
     * O metódo GET já está embutido no ConnectionBase, afinal de contas, SQLite e MySQL tem a mesma base.
     */

    private String host;
    private int port;
    private String username;
    private String password;
    private String database;
    private int query;


    public MySQL(String host, int port, String username, String password, String database) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
        this.query = 0;
    }

    @Override
    public void openConnection() {
        try {
            query++;
            if ((connection != null) && (!connection.isClosed()))
                return;

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false", username, password);
        } catch (ClassNotFoundException | SQLException e) {
            query--;
            e.printStackTrace();
            System.out.println(
                    "Ocorreu um erro ao abrir a conexão!");
        }
    }

    @Override
    public void closeConnection() {
        query--;
        if (query <= 0) {
            try {
                if (connection != null && !connection.isClosed())
                    connection.close();
            } catch (Exception e) {
                System.out.println(
                        "Houve um erro ao fechar a conexão!");
            }
        }
    }
}
