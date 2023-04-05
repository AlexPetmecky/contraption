package alexpetmecky.contraption;

import alexpetmecky.contraption.events.BlockEvents;
import alexpetmecky.contraption.inventories.dropperInventory;
import alexpetmecky.contraption.listeners.DispenserListener;
import alexpetmecky.contraption.listeners.InventoryListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Contraption extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        //dropperInventory customInv = new dropperInventory();
        //customInv.
        //getServer().getPluginManager().registerEvents(new DispenserListener(this),this);
        getServer().getPluginManager().registerEvents(new InventoryListener(this),this);
        getServer().getPluginManager().registerEvents(new BlockEvents(),this);



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
