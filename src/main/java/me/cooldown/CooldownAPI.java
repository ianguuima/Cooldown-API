package me.cooldown;

import me.cooldown.entities.User;
import me.cooldown.manager.CooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class CooldownAPI extends JavaPlugin implements Listener {


    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
       CooldownManager.getINSTANCE().loadUser(e.getPlayer().getName());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        CooldownManager.getINSTANCE().findByName(e.getPlayer().getName()).ifPresent(User::saveCooldowns);
    }


}
