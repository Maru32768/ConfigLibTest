package net.kunmc.lab.configlib;


import net.kunmc.lab.commandlib.CommandContext;

public abstract class Value<E, T extends Value<E, T>> {
    private String description;
    protected E value;
    private transient boolean listable = true;

    public Value(E value) {
        this.value = value;
    }

    public E value() {
        return value;
    }

    public void value(E value) {
        this.value = value;
    }

    public T description(String description) {
        this.description = description;
        return ((T) this);
    }

    protected boolean listable() {
        return listable;
    }

    public T listable(boolean listable) {
        this.listable = listable;
        return ((T) this);
    }

    protected abstract void sendListMessage(CommandContext ctx, String entryName);
}
