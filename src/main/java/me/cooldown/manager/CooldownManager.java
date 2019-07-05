package me.cooldown.manager;

import lombok.Getter;
import me.cooldown.Configuration;
import me.cooldown.entities.Cooldown;
import me.cooldown.entities.User;
import me.cooldown.interfaces.Browser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Optional;

public class CooldownManager implements Browser<User, Cooldown>, Listener {

    private static final CooldownManager INSTANCE = new CooldownManager();
    @Getter
    private HashSet<User> users;
    private ConnectionManager connectionManager = Configuration.getINSTANCE().getConnectionManager();

    public CooldownManager() {
        users = new HashSet<>();
    }


    private Optional<User> loadUser(String playerName){
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
        return user.getCooldownable().get().stream().filter(c -> c.getName().equalsIgnoreCase(cooldownName)).findFirst();
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e){
        loadUser(e.getPlayer().getName());
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent e){
        findByName(e.getPlayer().getName()).ifPresent(User::saveCooldowns);
    }

    public static CooldownManager getINSTANCE() {
        return INSTANCE;
    }
}
