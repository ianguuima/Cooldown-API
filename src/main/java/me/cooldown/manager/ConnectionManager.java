package me.cooldown.manager;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import lombok.Getter;
import me.cooldown.connection.type.ConnectionBase;
import me.cooldown.connection.type.MySQL;
import me.cooldown.connection.type.SQLite;
import me.cooldown.entities.Cooldown;
import me.cooldown.entities.User;
import me.cooldown.enums.ConnectionType;
import me.cooldown.interfaces.Cooldownable;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.HashSet;
import java.util.Optional;

public class ConnectionManager {

    @Getter
    private ConnectionBase data;

    @Getter
    private Gson gson;

    private String host, user, password, database;
    private int port;


    public ConnectionManager(Plugin plugin, ConnectionType type, String host, String user, String password, String database, int port) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.database = database;
        this.port = port;
        data = getMainConnection(plugin, type);
        criarTabela("playercooldown", "nome varchar(32), cooldown mediumtext");
        gson = new Gson();
    }


    /**
     * A API precisa descobrir que tipo de armazenamento a pessoa que está utilizando o seu plugin quer.
     * Aqui a verificação será feita. Verifique a documentação que está no Github.
     * Ela te ensinará a como configurar essa área!
     */

    private ConnectionBase getMainConnection(Plugin plugin, ConnectionType type) {

        // Serão feitas as configurações devidas para o armazenamento específico selecionado.

        if (type == ConnectionType.MYSQL) {
            String host = this.host;
            int port = this.port;
            String username = this.user;
            String password = this.password;
            String database = this.database;

            return new MySQL(host, port, username, password, database);
        }


        if (type == ConnectionType.SQL) {
            return new SQLite(plugin);
            // A conexão escolhida foi o SQLite, então a conexão base adotará o SQLite como principal!

        }


        return null;
    }






    public boolean playerExists(User user) {

        data.openConnection();
        final Connection connection = data.getConnection();

        String query = "SELECT * FROM playercooldown WHERE nome = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, user.getPlayerName());
            ResultSet resultSet = ps.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            data.closeConnection();
        }
        return false;

    }

    public Optional<User> insertPlayer(User user){
        data.openConnection();
        final Connection connection = data.getConnection();

        String query = "INSERT INTO playercooldown (nome, cooldown) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {


            String playerCooldowns = gson.toJson(user.getCooldownable().get());

            ps.setString(1, user.getPlayerName());
            ps.setString(2, playerCooldowns);

            ps.executeUpdate();
            return Optional.of(new User(user.getPlayerName()));


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            data.closeConnection();
        }

        return Optional.empty();
    }

    public Optional<User> loadUser(String username){
        data.openConnection();
        Connection connection = data.getConnection();
        String query = "SELECT * FROM playercooldown WHERE nome = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()){

                Type collectionType = new TypeToken<HashSet<Cooldown>>() {}.getType();
                HashSet<Cooldown> enums = gson.fromJson(resultSet.getString("cooldown"), collectionType);

                User user = new User(username, enums);
                return Optional.of(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            data.closeConnection();
        }

        return Optional.empty();

    }

    /**
     * O metódo fará o metódo de criação de tabelas, não mexa nele, a não ser que saiba o que está fazendo.
     * Crie suas tabelas no constructor acima! (A partir da linha 23)
     */

    private void criarTabela(String tabela, String colunas) {
        data.openConnection();
        try {
            Connection connection = data.getConnection();
            if ((data.getConnection() != null) && (!connection.isClosed())) {
                Statement stm = connection.createStatement();
                stm.executeUpdate("CREATE TABLE IF NOT EXISTS " + tabela + " (" + colunas + ");");
            }
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao salvar o MYSQL!");
        } finally {
            if (data.getConnection() != null) data.closeConnection();
        }
    }

}
