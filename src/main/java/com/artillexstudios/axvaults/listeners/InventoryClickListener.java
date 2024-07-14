package com.artillexstudios.axvaults.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

import com.artillexstudios.axvaults.guis.VaultNavManager;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        // don't conflict with other plugins
        InventoryHolder holder = event.getInventory().getHolder();
        if ((holder instanceof VaultNavManager)) {
            final VaultNavManager manager = (VaultNavManager) holder;
            int slotIndex = event.getSlot();
            if (manager.isContentSlot(slotIndex)) {
                manager.onContentSlotClick(event);
                return;
            }
            if (manager.isNavRowSlot(slotIndex)) {
                manager.onNavRowSlotClick(event);
                return;
            }
        }

    }
}
