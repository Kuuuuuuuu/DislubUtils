package kuu.dislub.Events;

import kuu.dislub.Loader;
import kuu.dislub.Misc.AbstractListener;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.type.Campfire;
import org.bukkit.block.data.type.Candle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class FireAspectEvent extends AbstractListener {

    @EventHandler(priority = EventPriority.HIGH)
    public void playerInteractEvent(PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.useInteractedBlock() != Result.DENY) {
                final ItemStack hand = player.getInventory().getItemInMainHand();
                if (hand.containsEnchantment(Enchantment.FIRE_ASPECT)) {
                    final ItemMeta meta = hand.getItemMeta();
                    final Block block = e.getClickedBlock();
                    assert block != null;
                    if (block.getBlockData() instanceof Campfire) {
                        if (((Lightable) block.getBlockData()).isLit()) {
                            return;
                        }
                        Lightable data = (Lightable) block.getBlockData();
                        data.setLit(true);
                        block.setBlockData(data);
                    } else {
                        if (!(block.getBlockData() instanceof Candle)) {
                            return;
                        }
                        if (((Candle) block.getBlockData()).isLit()) {
                            return;
                        }
                        int i = 1;
                        (new BukkitRunnable() {
                            public void run() {
                                if (((Candle) block.getBlockData()).isLit()) {
                                    Damageable damageable = (Damageable) meta;
                                    assert damageable != null;
                                    int newDamage = damageable.getDamage() - 1;
                                    damageable.setDamage(Math.max(0, newDamage));
                                    hand.setItemMeta(damageable);
                                } else {
                                    Candle data2 = (Candle) block.getBlockData();
                                    data2.setLit(true);
                                    block.setBlockData(data2);
                                }
                            }
                        }).runTaskLater(Loader.getInstance(), i);
                    }
                    player.swingMainHand();
                    player.playSound(block.getLocation(), Sound.ITEM_FLINTANDSTEEL_USE, 100.0F, 1.0F);
                    if (meta instanceof Damageable && player.getGameMode() != GameMode.CREATIVE) {
                        if (((Damageable) meta).getDamage() >= hand.getType().getMaxDurability()) {
                            hand.setAmount(hand.getAmount() - 1);
                            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
                        } else {
                            Damageable damageable = (Damageable) meta;
                            damageable.setDamage(Math.min(hand.getType().getMaxDurability(), damageable.getDamage() + 1));
                            hand.setItemMeta(damageable);
                        }
                    }
                }
            }
        }
    }
}