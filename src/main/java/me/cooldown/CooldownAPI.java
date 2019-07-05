package me.cooldown;

import me.cooldown.manager.CooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class CooldownAPI extends JavaPlugin implements Listener {


    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new CooldownManager(), this);
    }


}
