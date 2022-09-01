package kuu.dislub.Events;

import kuu.dislub.Loader;
import kuu.dislub.Misc.AbstractListener;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class TreeCapitatorEvent extends AbstractListener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void breakingBlock(BlockBreakEvent e) {
        final Block block = e.getBlock();
        final Player player = e.getPlayer();
        if (!e.isCancelled()) {
            if (block.getType().toString().contains("_LOG") || block.getType().toString().contains("LEAVES")) {
                if (player.getInventory().getItemInMainHand().getType().toString().contains("_AXE")) {
                    if (player.getGameMode().equals(GameMode.SURVIVAL) && e.getPlayer().isSneaking()) {
                        this.breakBlock(block, player);
                    }
                }
            }
        }
    }

    private void breakBlock(Block block, Player p) {
        final ItemStack onHand = p.getInventory().getItemInMainHand();
        final ItemMeta meta = onHand.getItemMeta();
        if (meta != null && p.getGameMode() != GameMode.CREATIVE) {
            for (int y = block.getY() - 5; y < block.getY() + 50; ++y) {
                for (int x = block.getX() - 2; x <= block.getX() + 2; ++x) {
                    for (int z = block.getZ() - 2; z <= block.getZ() + 2; ++z) {
                        Block b = Loader.getInstance().getServer().getWorld(block.getWorld().getName()).getBlockAt(x, y, z);
                        if (b.getType().toString().contains("_LOG")) {
                            b.breakNaturally();
                            if (((Damageable) meta).getDamage() >= onHand.getType().getMaxDurability()) {
                                onHand.setAmount(onHand.getAmount() - 1);
                                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
                            } else {
                                Damageable damageable = (Damageable) meta;
                                damageable.setDamage(Math.min(onHand.getType().getMaxDurability(), damageable.getDamage() + 1));
                                onHand.setItemMeta(damageable);
                            }
                        }
                        if (b.getType().toString().contains("LEAVES")) {
                            b.breakNaturally();
                            Random rand = new Random();
                            int randomInteger = rand.nextInt(149);
                            int randomInt2 = rand.nextInt(149);
                            if (randomInteger == randomInt2) {
                                b.getWorld().dropItem(b.getLocation(), new ItemStack(Material.APPLE));
                                b.getWorld().dropItem(b.getLocation(), new ItemStack(Material.STICK));
                            }
                        }
                    }
                }
            }
        }
    }
}
