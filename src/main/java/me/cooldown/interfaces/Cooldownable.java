package me.cooldown.interfaces;

import org.bukkit.entity.Player;

import java.util.function.Consumer;

public interface Cooldownable{

    String getName();

    long cooldownTime();

    long getWhenPutCooldown();

    void start();

    void stop(Consumer<Player> consumer);
    void stop();

    void ifCooldown(Consumer<Cooldownable> consumer);

    String getRemainingTimeFormatted();

}
