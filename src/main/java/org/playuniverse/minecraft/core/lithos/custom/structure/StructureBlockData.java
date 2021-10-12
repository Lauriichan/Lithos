package org.playuniverse.minecraft.core.lithos.custom.structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import org.bukkit.block.data.BlockData;
import org.playuniverse.minecraft.core.lithos.custom.structure.util.Axis;
import org.playuniverse.minecraft.core.lithos.custom.structure.util.Checks;
import org.playuniverse.minecraft.core.lithos.custom.structure.util.Rotation;

public final class StructureBlockData {

    private final HashMap<String, String> states = new HashMap<>();
    private final String namespace;
    private String id;

    private StructureBlockData(final StructureBlockData data) {
        this.states.putAll(data.states);
        this.namespace = data.namespace;
        this.id = data.id;
    }

    private StructureBlockData(final String blockdata) {
        String[] parts = Objects.requireNonNull(blockdata, "String blockdata can't be null!").split(":", 2);
        namespace = parts[0];
        if (parts[1].contains("[")) {
            parts = parts[1].split("\\[", 2);
            id = parts[0];
            final String[] tmp = parts[1].split("\\]", 2);
            parts = tmp[0].split(",");
            for (final String part : parts) {
                final String[] state = part.trim().split("=");
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

    public StructureBlockData rename(final String name) {
        this.id = Objects.requireNonNull(name, "String name can't be null!").toLowerCase();
        return this;
    }

    public StructureBlockData map(final Function<String, String> mapper, final String... states) {
        Objects.requireNonNull(mapper, "String name can't be null!");
        Checks.isNotNullOrEmpty(states, "String[] states");
        for (final String state : states) {
            this.states.computeIfPresent(state, (key, value) -> mapper.apply(value));
        }
        return this;
    }

    public StructureBlockData put(final String name, final String state) {
        Objects.requireNonNull(name, "String name can't be null!");
        if (state == null) {
            return remove(name);
        }
        states.put(name, state);
        return this;
    }

    public StructureBlockData rotateLeft(final int amount) {
        return rotate(amount * 3);
    }

    public StructureBlockData rotate(final int amount) {
        return mapRotation(rotation -> rotation.rotate(amount)).mapAxis(axis -> axis.rotate(amount)).mapSides(rotations -> {
            for (int index = 0; index < rotations.length; index++) {
                rotations[index] = rotations[index].rotate(amount);
            }
        });
    }

    public StructureBlockData apply(final Rotation rotation) {
        return put("facing", Objects.requireNonNull(rotation, "Rotation can't be null!").name().toLowerCase());
    }

    public StructureBlockData mapRotation(final Function<Rotation, Rotation> mapper) {
        Optional.ofNullable(getRotation()).map(mapper).ifPresent(this::apply);
        return this;
    }

    public Rotation getRotation() {
        return has("facing") ? Rotation.fromString(get("facing")) : null;
    }

    public StructureBlockData apply(final Axis axis) {
        return put("axis", Objects.requireNonNull(axis, "Axis can't be null!").name().toLowerCase());
    }

    public StructureBlockData mapAxis(final Function<Axis, Axis> mapper) {
        Optional.ofNullable(getAxis()).map(mapper).ifPresent(this::apply);
        return this;
    }

    public Axis getAxis() {
        return has("axis") ? Axis.fromString(get("axis")) : null;
    }

    public StructureBlockData applySides(final Rotation[] rotations) {
        List<Rotation> list = Arrays.asList(rotations);
        for (Rotation rotation : Rotation.values()) {
            put(rotation.name().toLowerCase(), Boolean.toString(list.contains(rotation)));
        }
        return this;
    }

    public StructureBlockData mapSides(final Consumer<Rotation[]> mapper) {
        Rotation[] sides = getSides();
        if (sides == null) {
            return this;
        }
        mapper.accept(sides);
        return applySides(sides);
    }

    public Rotation[] getSides() {
        if (!has("north")) {
            return null;
        }
        ArrayList<Rotation> list = new ArrayList<>();
        for (Rotation rotation : Rotation.values()) {
            if (get(rotation.name().toLowerCase()).equals("false")) {
                continue;
            }
            list.add(rotation);
        }
        return list.toArray(Rotation[]::new);
    }

    public boolean has(final String state) {
        return states.containsKey(state);
    }

    public String get(final String state) {
        return states.get(state);
    }

    public StructureBlockData remove(final String state) {
        states.remove(state);
        return this;
    }

    /*
     * To String
     */

    private String statesAsString() {
        final StringBuilder builder = new StringBuilder("[");
        for (final Entry<String, String> entry : states.entrySet()) {
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

    @Override
    public StructureBlockData clone() {
        return new StructureBlockData(this);
    }

    /*
     * Creation
     */

    public static StructureBlockData of(final String value) {
        return new StructureBlockData(value);
    }

    public static StructureBlockData of(final BlockData data) {
        return of(Objects.requireNonNull(data, "BlockData can't be null!").getAsString(false));
    }

}
