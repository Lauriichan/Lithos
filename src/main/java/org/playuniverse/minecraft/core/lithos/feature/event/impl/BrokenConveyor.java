package org.playuniverse.minecraft.core.lithos.feature.event.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

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

import com.syntaxphoenix.avinity.module.extension.Extension;

@Extension
public final class BrokenConveyor implements IFeatureEventExtension {

    private final LootTables[] lootTables = LootTables.values();
    private final Location location = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);

    private final ArrayList<ItemStack> items = new ArrayList<>();
    private final RandomNumberGenerator random = NumberGeneratorType.MURMUR.create();

    @Override
    public double additive(int passed) {
        return Math.exp(passed / 8) / 2;
    }

    @Override
    public void onStart(EventInfo info) {
        RandomNumberGenerator random = info.newRandom(NumberGeneratorType.PERMUTED);
        int amount = random.nextInt(1, 5);
        while (amount-- != 0) {
            items.addAll(lootTables[random.nextInt(lootTables.length)].getLootTable().populateLoot(new Random(random.nextLong()),
                new LootContext.Builder(location).luck(random.nextFloat()).build()));
        }
        this.random.setCompressedState(random.nextLong());
    }

    @Override
    public boolean isOver() {
        return items.isEmpty();
    }

    @Override
    public void onTick(long deltaTime) {
        int index = random.nextInt(items.size());
        ItemStack stack = items.get(index);
        if (stack.getAmount() != 1) {
            stack.setAmount(stack.getAmount() - 1);
            ItemStack output = stack.clone();
            output.setAmount(1);
            dropItem(output);
            return;
        }
        items.remove(index);
        dropItem(stack);
    }

    private void dropItem(ItemStack stack) {
        Collection<? extends Player> collection = Bukkit.getOnlinePlayers();
        int index = random.nextInt(collection.size());
        Iterator<? extends Player> iterator = collection.iterator();
        while (index-- != 0) {
            iterator.next();
        }
        Location location = iterator.next().getLocation();
        location.getWorld().dropItemNaturally(new Location(null, 0.5 + location.getBlockX() + random.nextInt(-5, 5), location.getBlockY(),
            0.5 + location.getBlockZ() + random.nextInt(-5, 5)), stack);
    }

    @Override
    public void onStop() {
        items.clear();
    }

}
