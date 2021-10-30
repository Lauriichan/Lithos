package org.playuniverse.minecraft.core.lithos.feature.shop.entity;

import java.util.UUID;

import org.bukkit.entity.Entity;
import org.playuniverse.minecraft.core.lithos.feature.shop.ShopHandler;

public final class DefaultShopEntity extends ShopEntity {
    
    private final Entity entity = null;

    public DefaultShopEntity(ShopHandler handler, UUID uniqueId) {
        super(handler, uniqueId);
    }

}
