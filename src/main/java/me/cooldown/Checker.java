package me.cooldown;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static me.cooldown.manager.CooldownManager.getINSTANCE;

public class Checker extends Thread {

    public Checker() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(this, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        getINSTANCE().getUsers().forEach(playerCooldowns -> {

            playerCooldowns.getCooldownable().ifPresent(cooldownsList -> {

                cooldownsList.forEach(cooldown -> cooldown.ifCooldown(activeCooldowns -> {


                    long whenPutCooldown = cooldown.getWhenPutCooldown();
                    long cooldownTime = TimeUnit.SECONDS.toMillis(cooldown.cooldownTime());
                    long fullTime = whenPutCooldown + cooldownTime;


                    if (System.currentTimeMillis() >= fullTime) {
                        System.out.println(new Date() + " Player lost cooldown!");
                        cooldown.stop();
                    }

                }));
            });
        });
    }
}
