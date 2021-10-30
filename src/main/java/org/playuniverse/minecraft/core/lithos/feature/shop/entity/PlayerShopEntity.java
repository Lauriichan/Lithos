package org.playuniverse.minecraft.core.lithos.feature.shop.entity;

import java.util.UUID;

import org.playuniverse.minecraft.core.lithos.feature.shop.ShopHandler;
import org.playuniverse.minecraft.vcompat.reflection.VersionControl;
import org.playuniverse.minecraft.vcompat.reflection.entity.NmsNpc;

public final class PlayerShopEntity extends ShopEntity {

    private final NmsNpc npc;

    public PlayerShopEntity(ShopHandler handler, UUID uniqueId) {
        super(handler, uniqueId);
        npc = VersionControl.get().getPlayerProvider().getNpc(uniqueId);
    }

    public NmsNpc getNpc() {
        return npc;
    }

}
