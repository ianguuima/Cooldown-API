package me.cooldown.interfaces;

import me.cooldown.entities.Cooldown;
import me.cooldown.entities.User;

import java.util.Optional;

public interface Browser<T, B> {

    public Optional<T> findByName(String name);

    public Optional<B> findCooldownByName(User user, String cooldownName);

}
