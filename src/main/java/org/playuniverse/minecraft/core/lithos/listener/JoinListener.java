package org.playuniverse.minecraft.core.lithos.listener;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerJoinEvent;
import org.playuniverse.minecraft.mcs.shaded.syapi.event.EventHandler;
import org.playuniverse.minecraft.mcs.shaded.syapi.utils.java.tools.Container;
import org.playuniverse.minecraft.mcs.spigot.module.extension.IListenerExtension;
import org.playuniverse.minecraft.mcs.spigot.module.extension.info.EventInfo;
import org.playuniverse.minecraft.vcompat.reflection.VersionControl;
import org.playuniverse.minecraft.vcompat.reflection.entity.NmsNpc;
import org.playuniverse.minecraft.vcompat.skin.DefaultMojangProvider;
import org.playuniverse.minecraft.vcompat.skin.DefaultSkinStore;
import org.playuniverse.minecraft.vcompat.skin.Mojang;

import com.syntaxphoenix.avinity.module.extension.Extension;

@Extension
@EventInfo(bukkit = true)
public final class JoinListener implements IListenerExtension {

    public static final UUID NPC_ID = UUID.fromString("02f816f7-7fc0-4f2e-b7f0-f958356280ab");

    private final Container<NmsNpc> container = Container.of();

    private final Mojang mojang = new Mojang(null, new DefaultMojangProvider(NPC_ID), new DefaultSkinStore());

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        if (container.isEmpty()) {
            NmsNpc npc = VersionControl.get().getPlayerProvider().getNpc(NPC_ID);
            container.replace(npc);
            if (npc.getLevel() != null) {
                npc.loadPosition();
            } else {
                Location loc = event.getPlayer().getLocation();
                npc.setLocation(loc);
                npc.setRotation(loc.getYaw(), loc.getPitch());
            }
        }
        NmsNpc npc = container.get();
        if (npc.isShown(event.getPlayer())) {
            npc.hide(event.getPlayer());
        }
        npc.show(event.getPlayer());
        npc.setName("Alice");
        npc.setSkin(mojang.getSkinOf("Lauriichan"));
        npc.update();
    }

}
