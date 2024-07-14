package com.artillexstudios.axvaults.guis;

import java.util.UUID;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.artillexstudios.axvaults.vaults.Vault;
import com.artillexstudios.axvaults.vaults.VaultManager;
import com.artillexstudios.axvaults.vaults.VaultPlayer;

public class VaultNavManager implements InventoryHolder {

    private final UUID playerUuid;
    private final VaultPlayer vaultPlayer;
    private final VaultNavInventoryBuilder builder;
    private int currentVaultNumber = 0;
    private int maxVaultNumber = 0;
    private Inventory currentVaultNavInventory = null;
    private final int contentRowCount = 5;
    private final int startContentSlot = 0;
    private final int endContentSlot = contentRowCount * 9 - 1;
    private final int startNavRowSlot = contentRowCount * 9;
    private final int endNavRowSlot = startNavRowSlot + 9;

    public VaultNavManager(@NotNull UUID playerUuid) {
        this.playerUuid = playerUuid;
        this.vaultPlayer = VaultManager.getPlayer(playerUuid);
        this.builder = new VaultNavInventoryBuilder(this);

        this.maxVaultNumber = __getMaxVaultNumberForPlayer(playerUuid);
        this.currentVaultNumber = (this.maxVaultNumber > 0) ? 1 : 0;
    }

    /**
     * InventoryHolder interface implementation.
     */
    @Override
    public Inventory getInventory() {
        return currentVaultNavInventory;
    }

    public boolean open() {
        return open(currentVaultNumber);
    }

    public boolean open(int vaultNumber) {
        if (!isVaultNumberValid(vaultNumber)) {
            return false;
        }
        Vault vault = getVault(vaultNumber);
        if (vault == null) {
            return false;
        }
        Vault previousVault = getVault(vaultNumber - 1);
        Vault nextVault = getVault(vaultNumber + 1);
        this.setCurrentNavInventory(builder.build(vault, previousVault, nextVault));
        return true;
    }

    public boolean isContentSlot(int slotIndex) {
        return (slotIndex >= startContentSlot && slotIndex <= endContentSlot);
    }

    public boolean isNavRowSlot(int slotIndex) {
        return (slotIndex >= startNavRowSlot && slotIndex <= endNavRowSlot);
    }

    public void onContentSlotClick(InventoryClickEvent event) {
        setPendingSync();
    }

    public void onNavRowSlotClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @Nullable
    private Vault getVault(int vaultNumber) {
        if (!isVaultNumberValid(vaultNumber)) {
            return null;
        }
        // TODO: ensure any changes synced from pending
        return vaultPlayer.getVault(vaultNumber);
    }

    private void setPendingSync() {
        // TODO Auto-generated method stub
    }

    private void setCurrentNavInventory(Inventory inventory) {
        // TODO: if already inventory, ensure it's synced to pending
        this.currentVaultNavInventory = inventory;
    }

    private boolean isVaultNumberValid(int vaultNumber) {
        return (vaultNumber > 0 && vaultNumber <= maxVaultNumber);
    }

    private static int __getMaxVaultNumberForPlayer(UUID playerUuid) {
        return VaultManager.getVaultsOfPlayer(playerUuid);
    }

}
