package org.playuniverse.minecraft.core.lithos.feature.shop.entity;

import java.util.UUID;

import org.playuniverse.minecraft.core.lithos.feature.shop.ShopHandler;

public abstract class ShopEntity {

    protected final UUID uniqueId;
    protected final ShopHandler handler;

    public ShopEntity(ShopHandler handler, UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.handler = handler;
    }

}
