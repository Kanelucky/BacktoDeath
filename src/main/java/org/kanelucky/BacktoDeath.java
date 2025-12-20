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
        getLogger().info(TextFormat.WHITE + "Loading...");
    }

    @Override
    public void onEnable() {
        getLogger().info(TextFormat.DARK_GREEN + "Enabled");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        getLogger().info(TextFormat.DARK_RED + "Disabled");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Position deathPos = player.getPosition();
        deathLocations.put(player.getName(), deathPos);
        player.sendMessage("Use /back to return to your last death!");
    }

    public boolean teleportToDeath(Player player) {
        Position pos = deathLocations.get(player.getName());
        if (pos == null) {
            player.sendMessage("You haven't ever died!");
            return false;
        }

        player.teleport(pos);
        player.sendMessage("Return to your last death successfully!");
        deathLocations.remove(player.getName());
        return true;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You are not a player!");
            return true;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("back")) {
            return teleportToDeath(player);
        }
        return false;
    }
}