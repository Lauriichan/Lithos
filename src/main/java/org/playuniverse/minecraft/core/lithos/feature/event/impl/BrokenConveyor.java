package org.playuniverse.minecraft.core.lithos.feature.event.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.Future;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTables;
import org.playuniverse.minecraft.core.lithos.feature.event.EventInfo;
import org.playuniverse.minecraft.core.lithos.feature.event.IFeatureEventExtension;
import org.playuniverse.minecraft.mcs.shaded.syapi.random.NumberGeneratorType;
import org.playuniverse.minecraft.mcs.shaded.syapi.random.RandomNumberGenerator;
import org.playuniverse.minecraft.mcs.spigot.helper.task.TaskHelper;

import com.syntaxphoenix.avinity.module.extension.Extension;

@Extension
public final class BrokenConveyor implements IFeatureEventExtension {

    private static final LootTables[] LOOT_TABLES;

    static {
        ArrayList<LootTables> lootTables = new ArrayList<>();
        for (LootTables lootTable : LootTables.values()) {
            if (!lootTable.getKey().getKey().startsWith("chests")) {
                continue;
            }
            lootTables.add(lootTable);
        }
        LOOT_TABLES = lootTables.toArray(LootTables[]::new);
    }

    private final Location location = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);

    private final ArrayList<ItemStack> items = new ArrayList<>();
    private final RandomNumberGenerator random = NumberGeneratorType.MURMUR.create();

    @Override
    public double additive(final int passed) {
        return Math.exp(passed / 8) / 2;
    }

    @Override
    public void onStart(final EventInfo info) {
        final RandomNumberGenerator random = info.newRandom(NumberGeneratorType.PERMUTED);
        Future<?> future = TaskHelper.runSync(() -> {
            int amount = random.nextInt(1, 10);
            while (amount-- != 0) {
                items.addAll(LOOT_TABLES[random.nextInt(LOOT_TABLES.length)].getLootTable().populateLoot(new Random(random.nextLong()),
                    new LootContext.Builder(location).luck(random.nextFloat()).build()));
            }
        });
        while (!future.isDone()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                break;
            }
        }
        this.random.setCompressedState(random.nextLong());
        info.setTickTime(2);
    }

    @Override
    public boolean isOver() {
        return items.isEmpty();
    }

    @Override
    public void onTick(final long deltaTime) {
        final int index = random.nextInt(items.size());
        final ItemStack stack = items.get(index);
        if (stack.getAmount() != 1) {
            stack.setAmount(stack.getAmount() - 1);
            final ItemStack output = stack.clone();
            output.setAmount(1);
            dropItem(output);
            return;
        }
        items.remove(index);
        dropItem(stack);
    }

    private void dropItem(final ItemStack stack) {
        final Collection<? extends Player> collection = Bukkit.getOnlinePlayers();
        if (collection.isEmpty()) {
            items.clear();
            return;
        }
        int index = random.nextInt(collection.size());
        final Iterator<? extends Player> iterator = collection.iterator();
        while (index-- != 0) {
            iterator.next();
        }
        final Location location = iterator.next().getLocation();
        TaskHelper.runSync(
            (Runnable) () -> location.getWorld().dropItemNaturally(new Location(null, 0.5 + location.getBlockX() + random.nextInt(-5, 5),
                location.getBlockY(), 0.5 + location.getBlockZ() + random.nextInt(-5, 5)), stack));
    }

    @Override
    public void onStop() {
        items.clear();
    }

}
