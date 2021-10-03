package org.playuniverse.minecraft.core.lithos.extension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.pf4j.ExtensionPoint;
import org.playuniverse.minecraft.core.lithos.Lithos;
import org.playuniverse.minecraft.core.lithos.extension.helper.ExtensionHelper;
import org.playuniverse.minecraft.mcs.spigot.SpigotCore;
import org.playuniverse.minecraft.mcs.spigot.command.listener.MinecraftCommand;
import org.playuniverse.minecraft.mcs.spigot.command.listener.MinecraftInfo;
import org.playuniverse.minecraft.mcs.spigot.command.listener.redirect.NodeRedirect;
import org.playuniverse.minecraft.mcs.spigot.command.nodes.RootNode;

public interface ICommandExtension extends ExtensionPoint {

    static final Predicate<String> COMMAND_NAME = Pattern.compile("[\\da-z_]+").asMatchPredicate();

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface CommandInfo {

        public String name();

        public String prefix() default "";

        public String[] aliases() default {};

    }

    RootNode<MinecraftInfo> buildRoot(String name);

    default void configure(MinecraftCommand command) {}

    public static int register(Lithos lithos) {
        List<ICommandExtension> extensions = lithos.getBase().getPluginManager().getExtensions(ICommandExtension.class);
        if (extensions.isEmpty()) {
            return 0;
        }
        String prefix = lithos.getId();
        SpigotCore base = lithos.getBase();
        int registered = 0;
        ArrayList<String> aliases = new ArrayList<>();
        for (ICommandExtension extension : extensions) {
            Optional<CommandInfo> infoOption = ExtensionHelper.getAnnotationOfMethod(CommandInfo.class, extension.getClass(), "buildRoot",
                String.class);
            if (infoOption.isEmpty()) {
                continue; // Invalid command
            }
            CommandInfo info = infoOption.get();
            if (!COMMAND_NAME.test(info.name())) {
                continue; // Invalid command name
            }
            RootNode<MinecraftInfo> node = extension.buildRoot(info.name());
            String fallbackPrefix = info.prefix().isBlank() ? prefix : info.prefix();
            for (String alias : info.aliases()) {
                if (!COMMAND_NAME.test(alias)) {
                    continue;
                }
                aliases.add(alias);
            }
            MinecraftCommand command = new MinecraftCommand(new NodeRedirect(node, lithos), fallbackPrefix, base, info.name(),
                aliases.toArray(String[]::new));
            aliases.clear();
            extension.configure(command);
            if (!lithos.inject(command)) {
                continue; // Unable to inject command
            }
            registered++;
        }
        return registered;
    }

}
