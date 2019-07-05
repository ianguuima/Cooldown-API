package me.cooldown.entities;

import lombok.Getter;
import me.cooldown.interfaces.Cooldownable;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Cooldown implements Cooldownable {

    @Getter
    private String name;
    private int cooldownTime;
    private long whenPut;

    @Getter
    private boolean isCooldown;

    public Cooldown(String name, int cooldownTime) {
        this.name = name;
        this.cooldownTime = cooldownTime;
    }

    @Override
    public long cooldownTime() {
        return cooldownTime;
    }

    @Override
    public long getWhenPutCooldown() {
        return whenPut;
    }

    @Override
    public void start() {
        this.whenPut = System.currentTimeMillis();
        isCooldown = true;
        System.out.println(new Date() + " Cooldown started!");
    }

    public void stop(Consumer<Player> consumer){
        isCooldown = false;
    }

    @Override
    public void stop() {
        isCooldown = false;
    }

    @Override
    public void ifCooldown(Consumer<Cooldownable> consumer) {
        if (isCooldown)
            consumer.accept(this);
    }

    @Override
    public String getRemainingTimeFormatted() {
        long totalTime = whenPut + cooldownTime;
        long time = System.currentTimeMillis() - totalTime;

        long horas = TimeUnit.MILLISECONDS.toHours(time);
        long minutos = TimeUnit.MILLISECONDS.toMinutes(time);
        long segundos =  TimeUnit.MILLISECONDS.toSeconds(time) - (minutos * 60);

        return String.format("%02d:%02d:%02d", horas, minutos, segundos);
    }

    @Override
    public String toString() {
        return "Cooldown{" +
                "name='" + name + '\'' +
                ", cooldownTime=" + cooldownTime +
                ", whenPut=" + whenPut +
                ", isCooldown=" + isCooldown +
                '}';
    }
}
