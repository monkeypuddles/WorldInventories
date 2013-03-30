package me.drayshak.WorldInventories.listener;

import me.drayshak.WorldInventories.Group;
import me.drayshak.WorldInventories.helper.InventoryHelper;
import me.drayshak.WorldInventories.helper.InventoryTypeHelper;
import me.drayshak.WorldInventories.PlayerData;
import me.drayshak.WorldInventories.WorldInventories;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class EntityListener implements Listener
{
    private final WorldInventories plugin;
    
    public EntityListener(final WorldInventories plugin)
    {
       this.plugin = plugin;
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event)
    {
        Entity entity = event.getEntity();
        if(entity instanceof Player)
        {
            Player player = (Player)event.getEntity();
            String world = player.getWorld().getName();
            
            if(WorldInventories.exempts.contains(player.getName().toLowerCase()))
            {
                WorldInventories.logDebug("Ignoring exempt player death: " + player.getName());
                return;
            }
            
            Group togroup = WorldInventories.findGroup(world);       

            WorldInventories.logDebug("Player " + player.getName() + " died in world " + world + ", emptying inventory for group: " + togroup.getName());

            // Make the saved inventory blank so players can't duplicate by switching worlds and picking items back up
            InventoryHelper helper = new InventoryHelper();
            helper.setArmour(new ItemStack[4]);
            helper.setInventory(new ItemStack[36]);
            plugin.savePlayerInventory(player.getName(), togroup, InventoryTypeHelper.INVENTORY, helper);;
            
            if(plugin.getConfig().getBoolean("dostats"))
            {
                plugin.savePlayerStats(player.getName(), togroup, new PlayerData(20, 20, 0, 0, 0, 0F, null));
            }
        }
    }
}