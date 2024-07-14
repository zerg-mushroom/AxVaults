package com.artillexstudios.axvaults.guis;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.artillexstudios.axvaults.vaults.Vault;

public class VaultNavInventoryBuilder {

    private final VaultNavManager owner;
    private int size = 9;
    private String title = "Vaults";

    public VaultNavInventoryBuilder(@NotNull VaultNavManager owner) {
        this.owner = owner;

    }

    public Inventory build(@NotNull Vault vault, @Nullable Vault previousVault, @Nullable Vault nextVault) {
        return Bukkit.createInventory(owner, size, title);
    }

}
