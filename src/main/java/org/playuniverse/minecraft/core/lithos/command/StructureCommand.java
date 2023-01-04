package org.playuniverse.minecraft.core.lithos.command;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Structure;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.structure.UsageMode;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;
import org.playuniverse.minecraft.core.lithos.Lithos;
import org.playuniverse.minecraft.core.lithos.custom.structure.StructureBlockData;
import org.playuniverse.minecraft.core.lithos.custom.structure.StructureHandler;
import org.playuniverse.minecraft.core.lithos.custom.structure.util.Position;
import org.playuniverse.minecraft.core.lithos.custom.structure.util.Rotation;
import org.playuniverse.minecraft.mcs.shaded.avinity.command.CommandContext;
import org.playuniverse.minecraft.mcs.shaded.avinity.command.node.Argument;
import org.playuniverse.minecraft.mcs.shaded.avinity.command.node.Literal;
import org.playuniverse.minecraft.mcs.shaded.avinity.command.node.Root;
import org.playuniverse.minecraft.mcs.shaded.avinity.command.type.IArgumentType;
import org.playuniverse.minecraft.mcs.shaded.avinity.module.ModuleWrapper;
import org.playuniverse.minecraft.mcs.shaded.avinity.module.extension.Extension;
import org.playuniverse.minecraft.mcs.spigot.command.BukkitSource;
import org.playuniverse.minecraft.mcs.spigot.language.MessageWrapper;
import org.playuniverse.minecraft.mcs.spigot.language.placeholder.Placeholder;
import org.playuniverse.minecraft.mcs.spigot.module.extension.ICommandExtension;
import org.playuniverse.minecraft.mcs.spigot.module.extension.info.CommandInfo;

@Extension
public final class StructureCommand implements ICommandExtension {

    private final StructureHandler handler;

    public StructureCommand(final ModuleWrapper<Lithos> module) {
        this.handler = module.getModule().getStructureHandler();
    }

    @Override
    @CommandInfo(name = "structure")
    public Root<BukkitSource> buildRoot(String name) {
        return Root.<BukkitSource>of(name).argument("name", Argument.at(0, IArgumentType.STRING, false))
            .append(Literal.<BukkitSource>of("save").argument("rotation", Argument.at(0, IArgumentType.STRING, false))
                .execute(context -> execute(context, true)))
            .append(Literal.<BukkitSource>of("paste").argument("rotation", Argument.at(0, IArgumentType.STRING, false))
                .execute(context -> execute(context, false)));
    }

    private void execute(final CommandContext<BukkitSource> context, final boolean save) {
        final BukkitSource source = context.getSource();
        final MessageWrapper<?> wrapper = source.getWrapper();
        if (!source.isPlayer()) {
            wrapper.send("$prefix Du kannst keine Strukturen verwenden!");
            return;
        }
        final Player player = source.getPlayer();
        final String name = context.get("name", String.class);
        final String rotationRaw = context.get("rotation", String.class).toUpperCase();
        final Rotation rotation = Rotation.fromString(rotationRaw);
        if (!rotation.name().equals(rotationRaw)) {
            wrapper.send("$prefix Bitte eine valide Rotation an (NORTH / EAST / SOUTH / WEST)");
            return;
        }
        if (save) {
            final Block block = player.getTargetBlockExact(4, FluidCollisionMode.NEVER);
            if (block == null || block.getBlockData().getMaterial() != Material.STRUCTURE_BLOCK) {
                wrapper.send("$prefix Bitte schaue auf einen Structure Block");
                return;
            }
            final Structure structure = (Structure) block.getState();
            if (structure.getUsageMode() != UsageMode.SAVE) {
                wrapper.send("$prefix Bitte stelle den Structure Block auf SAVE");
                return;
            }
            final BlockVector size = structure.getStructureSize();
            final BlockVector relative = structure.getRelativePosition();
            final Location location = block.getLocation();
            final int x = location.getBlockX() + relative.getBlockX();
            final int y = location.getBlockY() + relative.getBlockY();
            final int z = location.getBlockZ() + relative.getBlockZ();
            handler.prepare(player.getUniqueId(), name, rotation, new Position(x, y, z),
                new Position(x + size.getBlockX(), y + size.getBlockY(), z + size.getBlockZ()));
            wrapper.send("$prefix Vorbereitung gespeichert, bitte schlage den Hauptblock der Struktur mit einem Stock");
            return;
        }
        if (!handler.has(name)) {
            wrapper.send(new Placeholder[] {
                Placeholder.of("name", name)
            }, "$prefix Die Struktur '$name' existiert nicht!");
            return;
        }
        wrapper.send(new Placeholder[] {
            Placeholder.of("name", name)
        }, "$prefix Die Struktur '$name' wird geladen...");
        // Copy in case of changes
        final HashMap<Position, StructureBlockData> data = new HashMap<>(handler.get(name).getStructure(rotation).getMap());
        final HashMap<String, BlockData> bukkit = new HashMap<>();
        final Location location = player.getLocation();
        final Position origin = new Position(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        for (final Position position : data.keySet()) {
            final BlockData blockData = bukkit.computeIfAbsent(data.get(position).asBlockData(), Bukkit::createBlockData);
            location.getWorld().getBlockAt(position.getX(origin), position.getY(origin), position.getZ(origin)).setBlockData(blockData);
        }
        wrapper.send(new Placeholder[] {
            Placeholder.of("name", name)
        }, "$prefix Die Struktur '$name' wurde erfolgreich geladen!");
    }

}
