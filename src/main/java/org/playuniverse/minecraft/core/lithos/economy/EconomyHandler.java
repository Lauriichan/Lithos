package org.playuniverse.minecraft.core.lithos.economy;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.playuniverse.minecraft.core.lithos.io.IOHandler;
import org.playuniverse.minecraft.core.lithos.util.InventoryHelper;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.NbtCompound;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.NbtNamedTag;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.NbtTag;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.NbtType;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.tools.NbtDeserializer;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.tools.NbtSerializer;
import org.playuniverse.minecraft.mcs.shaded.syapi.utils.java.Files;
import org.playuniverse.minecraft.mcs.spigot.bukkit.inventory.item.ItemEditor;

public final class EconomyHandler {

    private static final ItemStack ONE = ItemEditor.of(Material.GOLD_NUGGET).setModel(8).name().add("&6&bGold").apply().asItemStack();
    private static final ItemStack TEN = ItemEditor.of(Material.GOLD_INGOT).setModel(8).name().add("&6&bGold").apply().asItemStack();
    private static final ItemStack HUNDRED = ItemEditor.of(Material.GOLD_BLOCK).setModel(8).name().add("&6&bGold").apply().asItemStack();

    public static final ItemStack[] VALUES = {
        ONE,
        TEN,
        HUNDRED
    };

    private final HashMap<UUID, EconomyBank> banks = new HashMap<>();
    private final File folder;

    private final IOHandler ioHandler;

    public EconomyHandler(final IOHandler ioHandler, final File folder) {
        this.ioHandler = ioHandler;
        this.folder = new File(folder, "economy");
    }

    /*
     * Loading / Saving
     */

    public void load() {
        banks.clear();
        Files.createFolder(folder);
        for (final File file : folder.listFiles()) {
            if (!file.getName().endsWith(".nbt")) {
                continue;
            }
            NbtCompound compound;
            try {
                final NbtTag tag = NbtDeserializer.COMPRESSED.fromFile(file).getTag();
                if (tag.getType() != NbtType.COMPOUND) {
                    continue;
                }
                compound = (NbtCompound) tag;
            } catch (final IOException e) {
                // Ignore for now
                continue;
            }
            final Object object = ioHandler.deserializeNbt(compound);
            if (!(object instanceof EconomyBank)) {
                continue;
            }
            final EconomyBank bank = (EconomyBank) object;
            banks.put(bank.getUniqueId(), bank);
        }
    }

    public void save() {
        for (EconomyBank bank : banks.values()) {
            File file = new File(bank.getUniqueId().toString() + ".nbt");
            Files.createFile(file);
            NbtCompound compound = ioHandler.serializeNbt(bank);
            if (compound == null) {
                continue;
            }
            try {
                NbtSerializer.COMPRESSED.toFile(new NbtNamedTag("bank", compound), file);
            } catch (IOException e) {
                // Ignore for now
            }
        }
    }

    /*
     * Handling
     */

    public EconomyBank get(final UUID uniqueId) {
        return banks.computeIfAbsent(uniqueId, EconomyBank::new);
    }

    public long transfer(final UUID fromId, final UUID toId, final long value) {
        final EconomyBank from = get(fromId);
        final long diff = from.get() - value;
        if (diff < 0) {
            return diff * -1;
        }
        from.set(diff);
        get(toId).add(value);
        return 0;
    }

    public long[] amount(final Player player) {
        final long[] output = new long[3];
        output[0] = get(player.getUniqueId()).get();
        output[1] = asTotal(count(player.getInventory()));
        output[2] = asTotal(count(player.getEnderChest()));
        return output;
    }

    public long deposit(final Player player, final long value) {
        final Inventory inventory = player.getInventory();
        final long[] total = count(inventory);
        final long result = value != -1 ? Math.min(asTotal(total), value) : asTotal(total);
        if (result == 0) {
            return result;
        }
        remove(player.getLocation(), inventory, restrict(total, result));
        get(player.getUniqueId()).add(result);
        return result;
    }

    public long withdraw(final Player player, final long value) {
        final EconomyBank bank = get(player.getUniqueId());
        final long result = value != -1 ? Math.min(bank.get(), value) : bank.get();
        if (result == 0) {
            return result;
        }
        bank.subtract(result);
        add(player.getLocation(), player.getInventory(), result);
        return result;
    }

    /*
     * Inventory handling
     */

    private long[] count(final Inventory inventory) {
        final ItemStack[] items = inventory.getContents();
        final long[] amount = new long[3];
        for (int index = 0; index < items.length; index++) {
            final ItemStack item = items[index];
            if (item == null || item.getType() == Material.AIR) {
                continue;
            }
            if (item.isSimilar(ONE)) {
                amount[0] += item.getAmount();
                continue;
            }
            if (item.isSimilar(TEN)) {
                amount[1] += item.getAmount();
                continue;
            }
            if (item.isSimilar(HUNDRED)) {
                amount[2] += item.getAmount();
                continue;
            }
        }
        return amount;
    }

    private long asTotal(final long[] items) {
        return items[0] + items[1] * 10 + items[2] * 100;
    }

    private long[] restrict(final long[] items, final long value) {
        final long[] needed = {
            value % 10,
            Math.floorDiv(value % 100, 10),
            Math.floorDiv(value, 100)
        };
        final long[] result = new long[3];
        for (int index = items.length - 1; index >= 0; index--) {
            final long val = items[index] - needed[index];
            if (val >= 0) {
                result[index] += items[index] - val;
                continue;
            }
            result[index] += val;
        }
        for (int index = 0; index < items.length; index++) {
            long val = result[index];
            if (val >= 0 || index + 1 == items.length) {
                continue;
            }
            val = items[index + 1] - (1 + result[index + 1]);
            if (val >= 0) {
                result[index + 1] = items[index + 1] - val;
                continue;
            }
            result[index + 1] = val;
        }
        return result;
    }

    private void remove(final Location location, final Inventory inventory, final long[] items) {
        for (int index = 0; index < items.length; index++) {
            final long count = items[index];
            if (count < 0) {
                continue;
            }
            InventoryHelper.remove(inventory, VALUES[index], count);
        }
        for (int index = 0; index < items.length; index++) {
            final long count = items[index];
            if (count > 0) {
                continue;
            }
            InventoryHelper.add(location, inventory, VALUES[index], count);
        }
    }

    private void add(final Location location, final Inventory inventory, final long value) {
        InventoryHelper.add(location, inventory, HUNDRED, Math.floorDiv(value, 100));
        InventoryHelper.add(location, inventory, TEN, Math.floorDiv(value % 100, 10));
        InventoryHelper.add(location, inventory, ONE, value % 10);
    }

}
