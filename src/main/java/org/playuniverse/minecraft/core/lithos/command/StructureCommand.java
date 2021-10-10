package org.playuniverse.minecraft.core.lithos.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Structure;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.structure.UsageMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;
import org.playuniverse.minecraft.core.lithos.Lithos;
import org.playuniverse.minecraft.core.lithos.custom.structure.StructureBlockData;
import org.playuniverse.minecraft.core.lithos.custom.structure.StructureHandler;
import org.playuniverse.minecraft.core.lithos.custom.structure.util.Position;
import org.playuniverse.minecraft.core.lithos.custom.structure.util.Rotation;
import org.playuniverse.minecraft.mcs.spigot.command.CommandContext;
import org.playuniverse.minecraft.mcs.spigot.command.StringReader;
import org.playuniverse.minecraft.mcs.spigot.command.listener.MinecraftInfo;
import org.playuniverse.minecraft.mcs.spigot.command.nodes.CommandNode;
import org.playuniverse.minecraft.mcs.spigot.command.nodes.RootNode;
import org.playuniverse.minecraft.mcs.spigot.language.MessageWrapper;
import org.playuniverse.minecraft.mcs.spigot.language.placeholder.Placeholder;
import org.playuniverse.minecraft.mcs.spigot.module.extension.ICommandExtension;
import org.playuniverse.minecraft.mcs.spigot.module.extension.info.CommandInfo;

import com.syntaxphoenix.avinity.module.ModuleWrapper;
import com.syntaxphoenix.avinity.module.extension.Extension;

@Extension
public final class StructureCommand implements ICommandExtension {

    private final StructureHandler handler;

    public StructureCommand(ModuleWrapper<Lithos> module) {
        this.handler = module.getModule().getStructureHandler();
    }

    @Override
    @CommandInfo(name = "structure")
    public RootNode<MinecraftInfo> buildRoot(String name) {
        return new CommandNode<>(name, this::execute, this::complete);
    }

    public void execute(CommandContext<MinecraftInfo> context) {
        MessageWrapper<?> wrapper = context.getSource().getReceiver();
        CommandSender sender = context.getSource().getSender();
        if (!sender.hasPermission("lithos.structure")) {
            wrapper.send("$prefix Du kannst keine Strukturen verwenden!");
            return;
        }
        if (!(sender instanceof Player)) {
            wrapper.send("$prefix Dies kann nur ein Spieler ausführen!");
            return;
        }
        StringReader reader = context.getReader();
        if (!reader.skipWhitespace().hasNext()) {
            wrapper.send("$prefix Bitte gebe den Namen der Struktur an");
            return;
        }
        String name = reader.readUnquoted();
        if (!reader.skipWhitespace().hasNext()) {
            wrapper.send("$prefix Bitte gebe die Aktion an die du ausführen willst (save / paste)");
            return;
        }
        String action = reader.readUnquoted();
        if (!reader.skipWhitespace().hasNext()) {
            wrapper.send("$prefix Bitte gebe die Rotation an die du speichern möchtest (NORTH / EAST / SOUTH / WEST)");
            return;
        }
        String rotationRaw = reader.readUnquoted();
        Rotation rotation = Rotation.fromString(rotationRaw);
        if (!rotation.name().equalsIgnoreCase(rotationRaw)) {
            wrapper.send("$prefix Bitte gebe eine gültige Rotation an (NORTH / EAST / SOUTH / WEST)");
            return;
        }
        Player player = (Player) sender;
        if (action.equalsIgnoreCase("save")) {
            Block block = player.getTargetBlockExact(4, FluidCollisionMode.NEVER);
            if (block == null || block.getBlockData().getMaterial() != Material.STRUCTURE_BLOCK) {
                wrapper.send("$prefix Bitte schaue auf einen Structure Block");
                return;
            }
            Structure structure = (Structure) block.getState();
            if (structure.getUsageMode() != UsageMode.SAVE) {
                wrapper.send("$prefix Bitte stelle den Structure Block auf SAVE");
                return;
            }
            BlockVector size = structure.getStructureSize();
            BlockVector relative = structure.getRelativePosition();
            Location location = block.getLocation();
            int x = location.getBlockX() + relative.getBlockX();
            int y = location.getBlockY() + relative.getBlockY();
            int z = location.getBlockZ() + relative.getBlockZ();
            handler.prepare(player.getUniqueId(), name, rotation, new Position(x, y, z),
                new Position(x + size.getBlockX(), y + size.getBlockY(), z + size.getBlockZ()));
            wrapper.send("$prefix Vorbereitung gespeichert, bitte schlage den Hauptblock der Struktur mit einem Stock");
            return;
        }
        if (!action.equalsIgnoreCase("paste")) {
            wrapper.send("$prefix Bitte gebe eine gültige Aktion an (save / paste)");
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
        HashMap<Position, StructureBlockData> data = new HashMap<>(handler.get(name).getStructure(rotation).getMap()); // Copy in case of
                                                                                                                       // changes
        HashMap<String, BlockData> bukkit = new HashMap<>();
        Location location = player.getLocation();
        Position origin = new Position(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        for (Position position : data.keySet()) {
            BlockData blockData = bukkit.computeIfAbsent(data.get(position).asBlockData(), Bukkit::createBlockData);
            location.getWorld().getBlockAt(position.getX(origin), position.getY(origin), position.getZ(origin)).setBlockData(blockData);
        }
        wrapper.send(new Placeholder[] {
            Placeholder.of("name", name)
        }, "$prefix Die Struktur '$name' wurde erfolgreich geladen!");
    }
    
    public List<String> complete(CommandContext<MinecraftInfo> context) {
        ArrayList<String> list = new ArrayList<>();
        StringReader reader = context.getReader();
        reader.skipWhitespace().readUnquoted();
        if(!reader.hasNext()) {
            Collections.addAll(list, handler.getNames());
            return list;
        }
        reader.skipWhitespace().readUnquoted();
        if(!reader.hasNext()) {
            list.add("save");
            list.add("paste");
            return list;
        }
        reader.skipWhitespace().readUnquoted();
        if(reader.hasNext()) {
           return list; 
        }
        for(Rotation rotation : Rotation.values()) {
            list.add(rotation.name());
        }
        return list;
    }

}
