package com.floleproto.thetower.events;

import com.floleproto.thetower.Main;
import com.floleproto.thetower.game.GameStates;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageEvent implements Listener {
    private final Main main;

    public DamageEvent(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent ev){
        if(main.gameManager.isStates(GameStates.WAITING)){
            if(ev.getEntityType().equals(EntityType.PLAYER)){
                ev.setCancelled(true);
            }
        }
    }
}