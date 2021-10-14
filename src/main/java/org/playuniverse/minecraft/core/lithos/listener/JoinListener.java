package org.playuniverse.minecraft.core.lithos.listener;

import java.util.UUID;

import org.bukkit.event.player.PlayerJoinEvent;
import org.playuniverse.minecraft.mcs.shaded.syapi.event.EventHandler;
import org.playuniverse.minecraft.mcs.shaded.syapi.utils.java.tools.Container;
import org.playuniverse.minecraft.mcs.spigot.module.extension.IListenerExtension;
import org.playuniverse.minecraft.mcs.spigot.module.extension.info.EventInfo;
import org.playuniverse.minecraft.vcompat.reflection.VersionControl;
import org.playuniverse.minecraft.vcompat.reflection.entity.NmsNpc;

import com.syntaxphoenix.avinity.module.extension.Extension;

@Extension
@EventInfo(bukkit = true)
public final class JoinListener implements IListenerExtension {

    public static final UUID NPC_ID = UUID.fromString("02f816f7-7fc0-4f2e-b7f0-f958356280ab");

    private final Container<NmsNpc> container = Container.of();

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        if (container.isEmpty()) {
            NmsNpc npc = VersionControl.get().getPlayerProvider().getNpc(NPC_ID);
            container.replace(npc);
            if (npc.getLevel() != null) {
                npc.loadPosition();
            }
            npc.updateMetadata();
        }
        NmsNpc npc = container.get();
        if (npc.isShown(event.getPlayer())) {
            npc.hide(event.getPlayer());
        }
        npc.show(event.getPlayer());
    }

}
