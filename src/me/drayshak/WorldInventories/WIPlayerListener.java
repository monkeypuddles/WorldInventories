package me.drayshak.WorldInventories;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class WIPlayerListener extends PlayerListener
{
    private static WorldInventories plugin;
 
    public WIPlayerListener(final WorldInventories tplugin)
    {
        plugin = tplugin;
    }
    
    @Override
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
        Player player = event.getPlayer();
        
        String fromworld = event.getFrom().getWorld().getName();
        String toworld = event.getTo().getWorld().getName();
        
        if(!fromworld.equals(toworld))
        {
            WorldInventories.logStandard("Player " + player.getName() + " teleported from " + fromworld + " to " + toworld);
            
            Group fromgroup = WorldInventories.findFirstGroupForWorld(fromworld);
            Group togroup = WorldInventories.findFirstGroupForWorld(toworld);
            
            plugin.savePlayerInventory(player, fromgroup, plugin.getPlayerInventory(player));
            plugin.setPlayerInventory(player, plugin.loadPlayerInventory(player, togroup));
            
            String groupname = "default";
            if(togroup != null) groupname = togroup.getName();
            
            player.sendMessage(ChatColor.GREEN + "Changed inventory set to group: " + groupname);
        }
    }
    
    @Override
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        String world = player.getLocation().getWorld().getName();
        
        WorldInventories.logStandard("Player joined world: " + world);
        
        Group tGroup = WorldInventories.findFirstGroupForWorld(world);
        
        // Don't load if we don't care where we are (default group)
        if(tGroup != null)
        {
            WorldInventories.logStandard("Loading inventory of " + player.getName());
            plugin.setPlayerInventory(player, plugin.loadPlayerInventory(player, tGroup));
            
            String groupname = "default";
            if(tGroup != null) groupname = tGroup.getName();
            
            player.sendMessage(ChatColor.GREEN + "Changed inventory to group: " + groupname);
        }
    }
    
    @Override
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        String world = player.getLocation().getWorld().getName();
        
        WorldInventories.logStandard("Player " + player.getName() + " quit from world: " + world);
        
        Group tGroup = WorldInventories.findFirstGroupForWorld(world);
        
        // Don't save if we don't care where we are (default group)
        if(tGroup != null)
        {    
            WorldInventories.logStandard("Saving inventory of " + player.getName());
            plugin.savePlayerInventory(player, WorldInventories.findFirstGroupForWorld(world), plugin.getPlayerInventory(player));
        }
    }
}
