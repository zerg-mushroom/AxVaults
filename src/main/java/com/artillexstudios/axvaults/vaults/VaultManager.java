package com.artillexstudios.axvaults.vaults;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VaultManager {

    private static final HashMap<UUID, VaultPlayer> players = new HashMap<>();
    private static final Set<Vault> vaults = Collections.newSetFromMap(new WeakHashMap<>()); // todo: currently this WeakHashMap does nothing

    /**
     * Adds a (new?) player (as a VaultPlayer) to the active player list. Is
     * this even needed? I don't think so.
     *
     * @deprecated This method is deprecated because it's redundant. Use
     * {@link #getPlayer(UUID)} instead, which handles adding new players more
     * effectively by checking for existence and loading data as necessary.
     * @param uuid The UUID of the player.
     */
    @Deprecated
    public static void addPlayer(@NotNull UUID uuid) {
        final VaultPlayer vaultPlayer = new VaultPlayer(uuid);
        players.put(uuid, vaultPlayer);
    }

    /**
     * Returns the VaultPlayer object of a player. If player has VaultPlayer
     * already in the active player list, returns that. If not, creates a new
     * VaultPlayer object and returns that, adds to the active player list, and
     * loads the player's vaults from the database (if any).
     *
     * @param uuid The UUID of the player.
     * @return The VaultPlayer object of the player.
     */
    public static VaultPlayer getPlayer(@NotNull UUID uuid) {
        if (players.containsKey(uuid)) {
            return players.get(uuid);
        }
        final VaultPlayer vaultPlayer = new VaultPlayer(uuid);
        players.put(uuid, vaultPlayer);
        vaultPlayer.loadSync();
        return vaultPlayer;
    }

    /**
     * Removes a player from the active player list and saves their vaults.
     *
     * @param player The player (online only!)
     */
    public static void removePlayer(@NotNull Player player) {
        final VaultPlayer vaultPlayer = players.remove(player.getUniqueId());
        if (vaultPlayer == null) {
            return;
        }
        vaultPlayer.save();
    }

    /**
     * Returns a specific Vault for the player.
     *
     * @param player The player (online only)
     * @param num The number of the vault to get.
     * @return The numbered Vault for the player.
     */
    @Nullable
    public static Vault getVaultOfPlayer(@NotNull Player player, int num) {
        final VaultPlayer vaultPlayer = players.get(player.getUniqueId());
        return vaultPlayer.getVault(num);
    }

    /**
     * Get a map of all VaultPlayer objects, keyed by player UUID.
     *
     * @return A map of all VaultPlayer objects, keyed by player UUID.
     */
    public static HashMap<UUID, VaultPlayer> getPlayers() {
        return players;
    }

    /**
     * Adds a vault to the player's vault list.
     *
     * @param vault The vault to add.
     */
    public static void addVault(@NotNull Vault vault) {
        players.get(vault.getUUID()).addVault(vault);
    }

    /**
     * Removes a vault from the player's vault list.
     *
     * @param vault The vault to remove.
     */
    public static void removeVault(@NotNull Vault vault) {
        players.get(vault.getUUID()).removeVault(vault);
    }

    /**
     * Returns the number of vaults a player has.
     *
     * @param player The player whose vault count is being queried.
     * @return The number of vaults the player has.
     */
    public static int getVaultsOfPlayer(@NotNull Player player) {
        if (!players.containsKey(player.getUniqueId())) {
            return 0;
        }
        return players.get(player.getUniqueId()).getVaultMap().values().size();
    }

    /**
     * Returns the number of vaults a player has.
     *
     * @param uuid The UUID of the player whose vault count is being queried.
     * @return The number of vaults the player has.
     */
    public static int getVaultsOfPlayer(@NotNull UUID uuid) {
        if (!players.containsKey(uuid)) {
            return 0;
        }
        return players.get(uuid).getVaultMap().values().size();
    }

    /**
     * Reloads all vaults.
     */
    public static void reload() {
        for (VaultPlayer vaultPlayer : players.values()) {
            for (Vault vault : vaultPlayer.getVaultMap().values()) {
                vault.reload();
            }
        }
    }

    /**
     * Returns the vaults.
     *
     * @return The vaults.
     */
    public static Set<Vault> getVaults() {
        return vaults;
    }
}
