package net.kunmc.lab.value;

import dev.kotx.flylib.command.CommandContext;

public interface Value<T> {
    T value();

    void value(T value);

    void onSetValue(T newValue);

    boolean validate(T newValue);

    boolean listable();

    boolean writableByCommand();

    default void sendListMessage(CommandContext ctx, String entryName) {
        ctx.success(entryName + ": " + value());
    }

    default String failSetMessage(String entryName, T newValue) {
        return "引数の値が不正です.";
    }

    default String succeedSetMessage(String entryName) {
        return entryName + "の値を" + value() + "に設定しました.";
    }
}