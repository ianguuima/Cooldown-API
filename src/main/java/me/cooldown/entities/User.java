package me.cooldown.entities;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import me.cooldown.Configuration;
import me.cooldown.connection.type.ConnectionBase;
import me.cooldown.interfaces.Cooldownable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Getter @Setter
public class User {

    private String playerName;
    private Optional<HashSet<Cooldown>> cooldownable;

    public User(String playerName) {
        this.playerName = playerName;
        cooldownable = Optional.of(new HashSet<>());
    }

    public User(String playerName, HashSet<Cooldown> cooldowns) {
        this.playerName = playerName;
        this.cooldownable = Optional.of(cooldowns);
    }

    public Cooldown insertCooldown(Cooldown cooldown) {
        return cooldownable.map(playerCooldowns -> {
            playerCooldowns.add(cooldown);
            return cooldown;
        }).orElse(null);
    }

    public List<Cooldown> insertCooldowns(Cooldown... cooldowns){
        return cooldownable.map(playerCooldowns -> {
            List<Cooldown> cooldownsList = Arrays.asList(cooldowns);
            playerCooldowns.addAll(cooldownsList);
            return cooldownsList;
        }).orElse(null);
    }

    public User removeCooldown(String cooldownName) {
        cooldownable.ifPresent(playerCooldowns -> {
            Optional<Cooldown> cooldown = playerCooldowns.stream().filter(cooldowns -> cooldowns.getName().equalsIgnoreCase(cooldownName)).findFirst();
            cooldown.ifPresent(playerCooldowns::remove);

        });

        return this;
    }

    public User saveCooldown(Cooldownable cooldown){
        ConnectionBase data = Configuration.getINSTANCE().getConnectionManager().getData();
        data.openConnection();
        final Connection connection = data.getConnection();
        String query = "UPDATE playercooldown SET cooldown = ? WHERE nome = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {


            final Gson gson = Configuration.getINSTANCE().getConnectionManager().getGson();
            String formatedString = gson.toJson(this.getCooldownable().get());

            preparedStatement.setString(1, formatedString);
            preparedStatement.setString(2, this.getPlayerName());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this;
    }

    public void saveCooldowns(){
        cooldownable.ifPresent(cooldowns -> cooldowns.forEach(this::saveCooldown));
    }


}
