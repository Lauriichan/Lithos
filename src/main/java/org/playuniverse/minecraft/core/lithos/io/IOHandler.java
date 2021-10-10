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

    private static final Function<String, ArrayList<DataInfo>> FUNC = (ignore) -> new ArrayList<>();

    private final HashMap<String, ArrayList<DataInfo>> map = new HashMap<>();
    private final ArrayList<Integer> hashes = new ArrayList<>();

    boolean register(DataInfo info) {
        if (hashes.contains(info.hashCode())) {
            return false;
        }
        return map.computeIfAbsent(info.hasId() ? info.getId() : null, FUNC).add(info);
    }

    public void unregister(ModuleWrapper<?> wrapper) {
        String[] keys = map.keySet().toArray(String[]::new);
        for (String key : keys) {
            ArrayList<DataInfo> infos = map.get(key);
            for (int index = 0; index < infos.size(); index++) {
                DataInfo info = infos.get(index);
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

    public Object convert(String id, Object object) {
        ArrayList<DataInfo> list = map.get(id);
        if (list == null) {
            return null;
        }
        for (int index = 0; index < list.size(); index++) {
            DataInfo info = list.get(index);
            Object out = info.convert(object);
            if (out == null) {
                continue;
            }
            return out;
        }
        return null;
    }

    public <E> E convert(Object object, Class<E> abstraction) {
        return convertImpl(object, abstraction, null);
    }

    private <E> E convertImpl(Object object, Class<E> abstraction, Container<String> id) {
        ArrayList<DataInfo> nullInfo = map.get(null);
        for (ArrayList<DataInfo> infos : map.values()) {
            if (nullInfo == infos) {
                continue;
            }
            E val = convertImpl(infos, object, abstraction, id);
            if (val == null) {
                continue;
            }
            return val;
        }
        return nullInfo == null ? null : convertImpl(nullInfo, object, abstraction, id);
    }

    private <E> E convertImpl(ArrayList<DataInfo> infos, Object object, Class<E> abstraction, Container<String> id) {
        for (DataInfo info : infos) {
            if (!abstraction.isAssignableFrom(info.getOutputType())) {
                continue;
            }
            Object out = info.convert(object);
            if (out == null) {
                continue;
            }
            id.replace(info.getId());
            return abstraction.cast(out);
        }
        return null;
    }

    public NbtCompound serialize(Object object) {
        Container<String> id = Container.of();
        NbtTag tag = convertImpl(object, NbtTag.class, id);
        if (id.isEmpty()) {
            return null;
        }
        NbtCompound compound = new NbtCompound();
        compound.set("id", id.get());
        compound.set("data", tag);
        return compound;
    }

    public Object deserialize(NbtCompound compound) {
        if (!compound.hasKey("id", NbtType.STRING) || !compound.hasKey("data")) {
            return null;
        }
        String id = compound.getString("id");
        NbtTag data = compound.get("data");
        return convert(id, data);
    }

}
