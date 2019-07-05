package me.cooldown.manager;

import lombok.Getter;
import me.cooldown.Configuration;
import me.cooldown.entities.Cooldown;
import me.cooldown.entities.User;
import me.cooldown.interfaces.Browser;

import java.util.HashSet;
import java.util.Optional;

public class CooldownManager implements Browser<User, Cooldown> {

    private static final CooldownManager INSTANCE = new CooldownManager();
    @Getter
    private HashSet<User> users;
    ConnectionManager connectionManager = Configuration.getINSTANCE().getConnectionManager();

    public CooldownManager() {
        users = new HashSet<>();
    }


    public Optional<User> loadUser(String playerName){
        Optional<User> user = connectionManager.loadUser(playerName);
        if (!user.isPresent()) {
            user = connectionManager.insertPlayer(new User(playerName));
        }
        user.ifPresent(t -> users.add(t));
        return user;
    }

    @Override
    public Optional<User> findByName(String name) {
        return users.stream().filter(u -> u.getPlayerName().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public Optional<Cooldown> findCooldownByName(User user, String cooldownName) {
        if (!user.getCooldownable().isPresent()) return Optional.empty();
        final Optional<Cooldown> first = user.getCooldownable().get().stream().filter(c -> c.getName().equalsIgnoreCase(cooldownName)).findFirst();
        return first;
    }


    public static CooldownManager getINSTANCE() {
        return INSTANCE;
    }
}
