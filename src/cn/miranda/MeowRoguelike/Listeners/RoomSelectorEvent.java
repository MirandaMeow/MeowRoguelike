package cn.miranda.MeowRoguelike.Listeners;

import cn.miranda.MeowCraft.Manager.MessageManager;
import cn.miranda.MeowRoguelike.Manager.Session;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

import static cn.miranda.MeowRoguelike.MeowRoguelike.plugin;

public class RoomSelectorEvent implements Listener {
    private boolean checkSize(Location first, Location second) {
        double fx = first.getX();
        double fy = first.getY();
        double fz = first.getZ();
        double sx = second.getX();
        double sy = second.getY();
        double sz = second.getZ();
        return Math.abs(fx - sx) + 1 == 25 && Math.abs(fy - sy) + 1 == 15 && Math.abs(fz - sz) + 1 == 25;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void RoomSelector(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack playerHold = player.getInventory().getItemInMainHand();
        if (!playerHold.getType().equals(Material.STICK)) {
            return;
        }
        if (plugin.sessions.get(player) == null) {
            return;
        }
        Session playerSession = plugin.sessions.get(player);
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (!playerSession.getInSelect()) {
            return;
        }
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Location location = Objects.requireNonNull(event.getClickedBlock()).getLocation();
            playerSession.setFirstLocation(location);
            MessageManager.Message(player, String.format("§e选区起点为 §b(%d, %d, %d)", ((int) location.getX()), ((int) location.getY()), ((int) location.getZ())));
            event.setCancelled(true);
            return;
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!playerSession.getFirstLocationReady()) {
                MessageManager.Message(player, "§e请先选择选区起点");
                return;
            }
            Location location = Objects.requireNonNull(event.getClickedBlock()).getLocation();
            playerSession.setSecondLocation(location);
            MessageManager.Message(player, String.format("§e选区终点为 §b(%d, %d, %d)", ((int) location.getX()), ((int) location.getY()), ((int) location.getZ())));
            event.setCancelled(true);
            Location playerSelectFirst = playerSession.getFirstLocation();
            Location playerSelectSecond = playerSession.getSecondLocation();
            if (!checkSize(playerSelectFirst, playerSelectSecond)) {
                playerSession.resetLocation();
                MessageManager.Message(player, "§e选区大小错误, 请重新选择");
                return;
            }
            MessageManager.Message(player, "§e选区选择成功, 为防止误操作已经关闭选区功能");
            playerSession.setSelect(false);
            playerSession.setReady(true);
        }
    }
}
