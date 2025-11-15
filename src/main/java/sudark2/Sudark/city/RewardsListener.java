package sudark2.Sudark.city;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import static sudark2.Sudark.city.RewardsManager.TITLE;

public class RewardsListener implements Listener {


    @EventHandler
    public void onMenuClose(InventoryCloseEvent e) {
        String title = e.getView().getTitle();
        if(!title.equals(TITLE))return;


    }
}
