package org.playuniverse.minecraft.core.lithos.io;

@SuppressWarnings("rawtypes")
final class DataInfo {

    private final TypeId id;
    private final IDataExtension handle;

    public DataInfo(TypeId id, IDataExtension handle) {
        this.id = id;
        this.handle = handle;
    }

    public Class<?> getHandleType() {
        return handle.getClass();
    }

    public String getId() {
        return id.name();
    }

    public Class<?> getInputType() {
        return id.input();
    }

    public Class<?> getOutputType() {
        return id.output();
    }

    public boolean hasId() {
        return id.name() != null && !id.name().isBlank();
    }

    public boolean isValid() {
        return id.input() != null && id.output() != null;
    }

    @SuppressWarnings("unchecked")
    public Object convert(Object input) {
        if (!getInputType().isAssignableFrom(input.getClass())) {
            return null;
        }
        Object out = handle.convert(id.input().cast(input));
        return out == null ? null : id.output().cast(out);
    }

    public int hashCode() {
        return (id.name().hashCode() << 24) | (id.input().hashCode() << 12) | (id.output().hashCode() << 0);
    }

}
