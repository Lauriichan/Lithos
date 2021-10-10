package org.playuniverse.minecraft.core.lithos.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.NbtCompound;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.NbtTag;
import org.playuniverse.minecraft.mcs.shaded.syapi.nbt.NbtType;
import org.playuniverse.minecraft.mcs.shaded.syapi.utils.java.tools.Container;

import com.syntaxphoenix.avinity.module.ModuleWrapper;

public final class IOHandler {

    private static final Function<String, ArrayList<DataInfo>> FUNC = ignore -> new ArrayList<>();

    private final HashMap<String, ArrayList<DataInfo>> map = new HashMap<>();
    private final ArrayList<Integer> hashes = new ArrayList<>();

    boolean register(final DataInfo info) {
        if (hashes.contains(info.hashCode())) {
            return false;
        }
        return map.computeIfAbsent(info.hasId() ? info.getId() : null, FUNC).add(info);
    }

    public void unregister(final ModuleWrapper<?> wrapper) {
        final String[] keys = map.keySet().toArray(String[]::new);
        for (final String key : keys) {
            final ArrayList<DataInfo> infos = map.get(key);
            for (int index = 0; index < infos.size(); index++) {
                final DataInfo info = infos.get(index);
                if (!wrapper.isFromModule(info.getHandleType())) {
                    continue;
                }
                index--;
                infos.remove(index);
            }
            if (infos.isEmpty()) {
                map.remove(key);
            }
        }
    }

    public Object convert(final String id, final Object object) {
        final ArrayList<DataInfo> list = map.get(id);
        if (list == null) {
            return null;
        }
        for (int index = 0; index < list.size(); index++) {
            final DataInfo info = list.get(index);
            final Object out = info.convert(object);
            if (out == null) {
                continue;
            }
            return out;
        }
        return null;
    }

    public <E> E convert(final Object object, final Class<E> abstraction) {
        return convertImpl(object, abstraction, null);
    }

    private <E> E convertImpl(final Object object, final Class<E> abstraction, final Container<String> id) {
        final ArrayList<DataInfo> nullInfo = map.get(null);
        for (final ArrayList<DataInfo> infos : map.values()) {
            if (nullInfo == infos) {
                continue;
            }
            final E val = convertImpl(infos, object, abstraction, id);
            if (val == null) {
                continue;
            }
            return val;
        }
        return nullInfo == null ? null : convertImpl(nullInfo, object, abstraction, id);
    }

    private <E> E convertImpl(final ArrayList<DataInfo> infos, final Object object, final Class<E> abstraction,
        final Container<String> id) {
        for (final DataInfo info : infos) {
            if (!abstraction.isAssignableFrom(info.getOutputType())) {
                continue;
            }
            final Object out = info.convert(object);
            if (out == null) {
                continue;
            }
            id.replace(info.getId());
            return abstraction.cast(out);
        }
        return null;
    }

    public NbtCompound serialize(final Object object) {
        final Container<String> id = Container.of();
        final NbtTag tag = convertImpl(object, NbtTag.class, id);
        if (id.isEmpty()) {
            return null;
        }
        final NbtCompound compound = new NbtCompound();
        compound.set("id", id.get());
        compound.set("data", tag);
        return compound;
    }

    public Object deserialize(final NbtCompound compound) {
        if (!compound.hasKey("id", NbtType.STRING) || !compound.hasKey("data")) {
            return null;
        }
        final String id = compound.getString("id");
        final NbtTag data = compound.get("data");
        return convert(id, data);
    }

}
