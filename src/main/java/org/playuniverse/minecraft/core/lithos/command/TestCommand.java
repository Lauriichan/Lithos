package org.playuniverse.minecraft.core.lithos.command;

import org.playuniverse.minecraft.core.lithos.extension.ICommandExtension;
import org.playuniverse.minecraft.mcs.spigot.command.CommandContext;
import org.playuniverse.minecraft.mcs.spigot.command.listener.MinecraftInfo;
import org.playuniverse.minecraft.mcs.spigot.command.nodes.CommandNode;
import org.playuniverse.minecraft.mcs.spigot.command.nodes.RootNode;

public final class TestCommand implements ICommandExtension {

    @Override
    @CommandInfo(name = "test")
    public RootNode<MinecraftInfo> buildRoot(String name) {
        return new CommandNode<>(name, this::test);
    }

    public void test(CommandContext<MinecraftInfo> context) {
        
        context.getSource().getReceiver().send("Hello");
        
    }

}
