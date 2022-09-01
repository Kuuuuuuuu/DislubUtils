package kuu.dislub.Events;

import kuu.dislub.Chest.ChestStoreFile;
import kuu.dislub.Chest.DeathChestUtils;
import kuu.dislub.Loader;
import kuu.dislub.Misc.AbstractListener;
import kuu.dislub.CoreUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DeathChestEvent extends AbstractListener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplode(final EntityExplodeEvent e) {
        final Iterator<Block> it = e.blockList().iterator();
        while (it.hasNext()) {
            final Block b = it.next();
            if (b.getState() instanceof Chest) {
                final Chest c = (Chest) b.getState();
                if (ChestStoreFile.getSection().contains(CoreUtils.LocationToString(c.getLocation()))) {
                    it.remove();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        if (!event.getKeepInventory() && event.getDrops().size() >= 1) {
            Chest chest = DeathChestUtils.getNearbyChest(event.getEntity().getLocation());
            if (event.getDrops().size() > 28) {
                if (chest == null) {
                    DeathChestUtils.sendPlayerMessage(event.getEntity(), Loader.getInstance().getConfig().getString("prefix") + "§aItems Dropped", null);
                    return;
                }
                for (int i = 0; i < 28; ++i) {
                    chest.getInventory().addItem(event.getDrops().get(0));
                    event.getDrops().remove(0);
                }
                DeathChestUtils.storeChest(chest.getLocation(), event.getEntity().getName());
                chest = DeathChestUtils.getNearbyChest(chest.getLocation());
            }
            if (chest == null) {
                DeathChestUtils.sendPlayerMessage(event.getEntity(), event.getDrops().size() > 28 ? Loader.getInstance().getConfig().getString("prefix") + "§aSome Items Was Dropped" : Loader.getInstance().getConfig().getString("prefix") + "§aItem Dropped", null);
            } else {
                for (ItemStack drop : event.getDrops()) {
                    chest.getInventory().addItem(drop);
                }
                DeathChestUtils.storeChest(chest.getLocation(), event.getEntity().getName());
                event.getDrops().clear();
                DeathChestUtils.sendPlayerMessage(event.getEntity(), Loader.getInstance().getConfig().getString("prefix") + "§aChest Placed", null);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryCloseEvent(InventoryCloseEvent event) {
        if (event.getInventory().getType() == InventoryType.CHEST) {
            List<Chest> chests = new ArrayList<>();
            if (event.getInventory().getHolder() instanceof DoubleChest) {
                DoubleChest chest = (DoubleChest) event.getInventory().getHolder();
                chests.add((Chest) chest.getRightSide());
                chests.add((Chest) chest.getLeftSide());
            } else if (event.getInventory().getHolder() instanceof Chest) {
                chests.add((Chest) event.getInventory().getHolder());
            }
            Iterator<Chest> var5 = chests.iterator();
            if (var5.hasNext()) {
                Chest chest = var5.next();
                if (ChestStoreFile.getSection().contains(CoreUtils.LocationToString(chest.getLocation())) && DeathChestUtils.getItemsInChest(chest) <= 0) {
                    chest.getLocation().getBlock().setType(Material.AIR);
                    ChestStoreFile.getSection().set(CoreUtils.LocationToString(chest.getLocation()), null);
                    ChestStoreFile.save();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryBreakEvent(BlockBreakEvent event) {
        if (!event.isCancelled()) {
            if (event.getBlock().getType() == Material.CHEST) {
                final Chest chest = (Chest) event.getBlock().getState();
                if (ChestStoreFile.getSection().contains(CoreUtils.LocationToString(chest.getLocation()))) {
                    final String chestOwner = ChestStoreFile.getSection().getString(CoreUtils.LocationToString(chest.getLocation()));
                    if (!event.getPlayer().getName().equals(chestOwner)) {
                        DeathChestUtils.sendPlayerMessage(event.getPlayer(), Loader.getInstance().getConfig().getString("prefix") + "§aChest Locked", chestOwner);
                        event.setCancelled(true);
                    } else {
                        ChestStoreFile.getSection().set(CoreUtils.LocationToString(chest.getLocation()), null);
                        ChestStoreFile.save();
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryOpenEvent(InventoryOpenEvent event) {
        if (event.getInventory().getType() == InventoryType.CHEST) {
            List<Chest> chests = new ArrayList<>();
            if (event.getInventory().getHolder() instanceof DoubleChest) {
                DoubleChest chest = (DoubleChest) event.getInventory().getHolder();
                chests.add((Chest) chest.getRightSide());
                chests.add((Chest) chest.getLeftSide());
            } else if (event.getInventory().getHolder() instanceof Chest) {
                chests.add((Chest) event.getInventory().getHolder());
            }
            for (Chest chest : chests) {
                if (ChestStoreFile.getSection().contains(CoreUtils.LocationToString(chest.getLocation()))) {
                    final String chestOwner = ChestStoreFile.getSection().getString(CoreUtils.LocationToString(chest.getLocation()));
                    if (!event.getPlayer().getName().equals(chestOwner)) {
                        DeathChestUtils.sendPlayerMessage((Player) event.getPlayer(), Loader.getInstance().getConfig().getString("prefix") + "§aChest Locked", chestOwner);
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }
}
