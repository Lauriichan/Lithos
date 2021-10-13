package org.playuniverse.minecraft.core.lithos.feature.shop.entity;

import java.util.UUID;

import org.bukkit.entity.Entity;
import org.playuniverse.minecraft.core.lithos.feature.shop.ShopHandler;

public abstract class ShopEntity {

    protected final Entity entity;
    protected final UUID uniqueId;

    protected final ShopHandler handler;

    public ShopEntity(ShopHandler handler, UUID uniqueId, Entity entity) {
        this.entity = entity;
        this.handler = handler;
        this.uniqueId = uniqueId;
    }

}
