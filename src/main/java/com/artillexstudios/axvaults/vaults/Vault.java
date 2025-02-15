package com.artillexstudios.axvaults.vaults;

import com.artillexstudios.axapi.reflection.ClassUtils;
import com.artillexstudios.axapi.utils.StringUtils;
import com.artillexstudios.axvaults.utils.SoundUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static com.artillexstudios.axvaults.AxVaults.MESSAGES;

public class Vault {
    private final UUID uuid;
    private Inventory storage;
    private final int id;
    private Material icon;

    public Vault(UUID uuid, int num, Material icon) {
        this.uuid = uuid;
        this.id = num;

        String title = MESSAGES.getString("guis.vault.title").replace("%num%", "" + num);
        if (ClassUtils.INSTANCE.classExists("me.clip.placeholderapi.PlaceholderAPI")) {
            title = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(uuid), title);
        }

        this.storage = Bukkit.createInventory(null, VaultManager.getPlayer(uuid).getRows() * 9, StringUtils.formatToString(title));
        this.icon = icon;
        VaultManager.getVaults().add(this);
    }

    public Vault(UUID uuid, int num, ItemStack[] items, Material icon) {
        this(uuid, num, icon);
        storage.setContents(items);
    }

    public Inventory getStorage() {
        return storage;
    }

    public UUID getUUID() {
        return uuid;
    }

    public int getId() {
        return id;
    }

    public void setIcon(Material icon) {
        this.icon = icon;
    }

    public Material getIcon() {
        if (icon == null) return Material.valueOf(MESSAGES.getString("guis.selector.item-owned.material", "BARREL"));
        return icon;
    }

    @Nullable
    public Material getRealIcon() {
        return icon;
    }

    public int getSlotsFilled() {
        int am = 0;
        for (ItemStack it : storage.getContents()) {
            if (it == null) continue;
            am++;
        }
        return am;
    }

    public void open(@NotNull Player player) {
        if (VaultManager.getPlayer(uuid).getRows() != storage.getSize()) {
            reload();
        }
        player.openInventory(storage);
        SoundUtils.playSound(player, MESSAGES.getString("sounds.open"));
        // todo: on close reopen selector (only when it was used)
    }

    public void reload() {
        String title = MESSAGES.getString("guis.vault.title").replace("%num%", "" + id);
        if (ClassUtils.INSTANCE.classExists("me.clip.placeholderapi.PlaceholderAPI")) {
            title = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(uuid), title);
        }

        final Inventory newStorage = Bukkit.createInventory(null, VaultManager.getPlayer(uuid).getRows() * 9, StringUtils.formatToString(title));
        final ItemStack[] contents = storage.getContents();

        int n = -1;
        for (ItemStack it : contents) {
            n++;
            if (n > newStorage.getSize() - 1) {
                if (it == null) continue;
                newStorage.addItem(it);
                continue;
            }
            newStorage.setItem(n, it);
        }

        final List<HumanEntity> viewers = new ArrayList<>(storage.getViewers());
        final Iterator<HumanEntity> viewerIterator = viewers.iterator();

        while (viewerIterator.hasNext()) {
            viewerIterator.next().openInventory(newStorage);
            viewerIterator.remove();
        }

        storage = newStorage;
    }
}
