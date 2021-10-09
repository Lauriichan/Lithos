package org.playuniverse.minecraft.core.lithos.custom.structure;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import org.bukkit.block.data.BlockData;
import org.playuniverse.minecraft.core.lithos.custom.structure.util.Axis;
import org.playuniverse.minecraft.core.lithos.custom.structure.util.Checks;
import org.playuniverse.minecraft.core.lithos.custom.structure.util.Rotation;

public final class StructureBlockData {

    private final HashMap<String, String> states = new HashMap<>();
    private final String namespace;
    private String id;

    private StructureBlockData(StructureBlockData data) {
        this.states.putAll(data.states);
        this.namespace = data.namespace;
        this.id = data.id;
    }

    private StructureBlockData(String blockdata) {
        String[] parts = Objects.requireNonNull(blockdata, "String blockdata can't be null!").split(":", 2);
        namespace = parts[0];
        if (parts[1].contains("[")) {
            parts = parts[1].split("\\[", 2);
            id = parts[0];
            String[] tmp = parts[1].split("\\]", 2);
            parts = tmp[0].split(",");
            for (String part : parts) {
                String[] state = part.trim().split("=");
                states.put(state[0], state[1]);
            }
            return;
        }
        id = parts[1];
    }

    /*
     * Getter
     */

    public String getNamespace() {
        return namespace;
    }

    public String getId() {
        return id;
    }

    public HashMap<String, String> getStates() {
        return states;
    }

    /*
     * Editor
     */

    public StructureBlockData rename(String name) {
        this.id = Objects.requireNonNull(name, "String name can't be null!").toLowerCase();
        return this;
    }

    public StructureBlockData map(Function<String, String> mapper, String... states) {
        Objects.requireNonNull(mapper, "String name can't be null!");
        Checks.isNotNullOrEmpty(states, "String[] states");
        for (String state : states) {
            this.states.computeIfPresent(state, (key, value) -> mapper.apply(value));
        }
        return this;
    }

    public StructureBlockData put(String name, String state) {
        Objects.requireNonNull(name, "String name can't be null!");
        if (state == null) {
            return remove(name);
        }
        states.put(name, state);
        return this;
    }

    public StructureBlockData rotateLeft(int amount) {
        return rotate(amount * 3);
    }

    public StructureBlockData rotate(int amount) {
        return mapRotation(rotation -> rotation.rotate(amount)).mapAxis(axis -> axis.rotate(amount));
    }

    public StructureBlockData apply(Rotation rotation) {
        return put("facing", Objects.requireNonNull(rotation, "Rotation can't be null!").name().toLowerCase());
    }

    public StructureBlockData mapRotation(Function<Rotation, Rotation> mapper) {
        Optional.ofNullable(getRotation()).map(mapper).ifPresent(this::apply);
        return this;
    }

    public Rotation getRotation() {
        return has("facing") ? Rotation.fromString(get("facing")) : null;
    }

    public StructureBlockData apply(Axis axis) {
        return put("axis", Objects.requireNonNull(axis, "Axis can't be null!").name().toLowerCase());
    }

    public StructureBlockData mapAxis(Function<Axis, Axis> mapper) {
        Optional.ofNullable(getAxis()).map(mapper).ifPresent(this::apply);
        return this;
    }

    public Axis getAxis() {
        return has("axis") ? Axis.fromString(get("axis")) : null;
    }

    public boolean has(String state) {
        return states.containsKey(state);
    }

    public String get(String state) {
        return states.get(state);
    }

    public StructureBlockData remove(String state) {
        states.remove(state);
        return this;
    }

    /*
     * To String
     */

    private String statesAsString() {
        StringBuilder builder = new StringBuilder("[");
        for (Entry<String, String> entry : states.entrySet()) {
            builder.append(entry.getKey()).append('=').append(entry.getValue()).append(", ");
        }
        return builder.substring(0, builder.length() - 2) + ']';
    }

    public String asBlockData() {
        if (states.isEmpty()) {
            return namespace + ':' + id;
        }
        return namespace + ':' + id + statesAsString();
    }

    /*
     * Copy
     */

    public StructureBlockData clone() {
        return new StructureBlockData(this);
    }

    /*
     * Creation
     */

    public static StructureBlockData of(String value) {
        return new StructureBlockData(value);
    }

    public static StructureBlockData of(BlockData data) {
        return of(Objects.requireNonNull(data, "BlockData can't be null!").getAsString(false));
    }

}
