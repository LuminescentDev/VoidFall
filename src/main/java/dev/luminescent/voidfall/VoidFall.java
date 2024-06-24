package dev.luminescent.voidfall;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class VoidFall extends JavaPlugin implements Listener {

    List<UUID> invinciblePlayers = new ArrayList<>();
    // comment uwu
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        invinciblePlayers.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            invinciblePlayers.remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            if (invinciblePlayers.contains(event.getEntity().getUniqueId())) {
                invinciblePlayers.remove(event.getEntity().getUniqueId());
                event.setCancelled(true);
            }
        } else {
            if ((!event.getEntity().getWorld().getName().equalsIgnoreCase("world_aether") && !event.getEntity().getWorld().getName().equalsIgnoreCase("world_the_end")) || event.getCause() != EntityDamageEvent.DamageCause.VOID)
                return;
            if (event.getEntity() instanceof Player player) {
                event.setCancelled(true);
                Location newLoc;
                if(player.getWorld().getName().equalsIgnoreCase("world_aether")){
                    newLoc = new Location(getServer().getWorld("world"), player.getLocation().getX(), 256, player.getLocation().getZ());
                }else if (player.getWorld().getName().equalsIgnoreCase("world_the_end")) {
                    newLoc = new Location(getServer().getWorld("world_aether"), player.getLocation().getX(), 256, player.getLocation().getZ());
                }else{
                    return;
                }
                player.teleport(newLoc, PlayerTeleportEvent.TeleportCause.PLUGIN);
                invinciblePlayers.add(player.getUniqueId());
                new BukkitRunnable() {
                    public void run() {
                        invinciblePlayers.remove(player.getUniqueId());
                    }
                }.runTaskLater(this, 20 * 10);
            }
        }
    }

}
