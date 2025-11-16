package sudark2.Sudark.city.Rewards;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import sudark2.Sudark.city.FileManager;

import static sudark2.Sudark.city.FileManager.Rewards;
import static sudark2.Sudark.city.Rewards.RewardsManager.TITLE;

public class RewardsListener implements Listener {

    @EventHandler
    public void onMenuClose(InventoryCloseEvent e) {
        String title = e.getView().getTitle();

        if (!title.startsWith(TITLE)) return;

        int index = Integer.parseInt(title.split(" ")[1]);

        ItemStack[] items = e.getInventory().getContents();
        for (ItemStack item : items) {
            if (item != null) {
                System.out.println(index + " " + item + " " + Rewards.size());
                if (index < Rewards.size())
                    Rewards.set(index, item);
                else Rewards.add(item);
            }
            index++;
        }
        FileManager.writeRewards(Rewards);

        Player pl = (Player) e.getPlayer();
        pl.sendMessage("[§eCity§f] 战利品已保存");

    }

}
