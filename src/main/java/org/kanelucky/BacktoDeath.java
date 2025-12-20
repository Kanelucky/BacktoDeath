package org.kanelucky;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;

import java.util.HashMap;
import java.util.Map;

public class BacktoDeath extends PluginBase implements Listener {
    private final Map<String, Position> deathLocations = new HashMap<>();

    @Override
    public void onLoad() {
        getLogger().info(TextFormat.WHITE + "Loading BacktoDeath plugin...");
    }

    @Override
    public void onEnable() {
        getLogger().info(TextFormat.DARK_GREEN + "BacktoDeath enabled!");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        getLogger().info(TextFormat.DARK_RED + "BacktoDeath disabled!");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Position deathPos = player.getPosition();
        deathLocations.put(player.getName(), deathPos);
        player.sendMessage(TextFormat.YELLOW + "Use /back to return to your last death!");
    }
    public boolean teleportToDeath(Player player) {
        if (!deathLocations.containsKey(player.getName())) {
            player.sendMessage(TextFormat.RED + "You haven't ever died!");
            return false;
        }

        Position pos = deathLocations.get(player.getName());
        player.teleport(pos);
        player.sendMessage(TextFormat.GREEN + "Returned to your last death successfully!");
        deathLocations.remove(player.getName());
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(TextFormat.RED + "Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("back")) {
            return teleportToDeath(player);
        }

        return false;
    }
}
