# Cooldown-API
Uma api para criar cooldowns facilmente!


### EXEMPLO:

```

final Player player = e.getPlayer();
CooldownManager.getINSTANCE().findByName(player.getName()).ifPresent(t -> {
            final Optional<Cooldown> kitpvp = CooldownManager.getINSTANCE().findCooldownByName(t, "block");
            kitpvp.ifPresent(cooldown -> {
                if (!cooldown.isCooldown()){
                    cooldown.start();
                    player.sendMessage("Voce colocou o bloco!");
                    return;

                }

                cooldown.ifCooldown(cooldownable -> {
                    e.setCancelled(true);
                    player.sendMessage("Voce ja colocou " + cooldownable.getRemainingTimeFormatted());
                });
            });
        });

```