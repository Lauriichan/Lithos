package org.playuniverse.minecraft.core.lithos.custom.craft;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.playuniverse.minecraft.core.lithos.custom.craft.animation.ICraftAnimation;
import org.playuniverse.minecraft.mcs.spigot.data.properties.IProperties;
import org.playuniverse.minecraft.mcs.spigot.helper.task.TaskHelper;
import org.playuniverse.minecraft.mcs.spigot.utils.wait.Awaiter;

public final class CraftProcess {

    private final ArrayList<Item> items;
    private final ItemStack[] ingredients;
    private final ItemStack[] results;

    private final Location location;
    private final Location center;

    private final ICraftAnimation[] animations;
    private int time;

    private final IProperties properties = IProperties.create();

    public CraftProcess(CraftingStation station, Location location, Location center, ArrayList<Item> items, ItemStack[] ingredients,
        ItemStack[] results) {
        this.items = items;
        this.ingredients = ingredients;
        this.results = results;
        this.animations = station.getAnimations();
        this.time = station.getTime();
        this.location = location;
        this.center = center;
    }

    public IProperties getProperties() {
        return properties;
    }

    public Location getLocation() {
        return location;
    }

    public Location getCenter() {
        return center;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public ItemStack[] getIngredients() {
        return ingredients;
    }

    public ItemStack[] getResults() {
        return results;
    }

    public boolean step() {
        if (time-- == 0) {
            Awaiter.of(TaskHelper.runSync(() -> {
                for (ICraftAnimation animation : animations) {
                    animation.finish(this);
                }
            })).await();
            return false;
        }
        for (ICraftAnimation animation : animations) {
            animation.step(this);
        }
        return true;
    }

}
