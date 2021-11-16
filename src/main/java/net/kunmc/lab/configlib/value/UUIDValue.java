package net.kunmc.lab.configlib.value;

import dev.kotx.flylib.command.CommandContext;
import dev.kotx.flylib.command.UsageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.Consumer;

public class UUIDValue implements SingleValue<UUID> {
    private String playerName;
    private UUID uuid;
    private boolean onlyOnline = false;
    private final transient Consumer<UUID> consumer;
    private transient boolean listable = true;
    private transient boolean writable = true;

    public UUIDValue(Player player) {
        this(player.getUniqueId());
    }

    public UUIDValue(UUID value) {
        this(value, x -> {
        });
    }

    public UUIDValue(Player player, Consumer<UUID> onSet) {
        this(player.getUniqueId(), onSet);
    }

    public UUIDValue(UUID value, Consumer<UUID> onSet) {
        this.uuid = value;
        this.playerName = playerName();
        this.consumer = onSet;
    }

    public UUIDValue onlyOnline(boolean onlyOnline) {
        this.onlyOnline = onlyOnline;
        return this;
    }

    @Override
    public UUID value() {
        return uuid;
    }

    @Override
    public void value(UUID value) {
        this.uuid = value;
        this.playerName = playerName();
    }

    public void value(Player player) {
        this.uuid = player.getUniqueId();
    }

    @Override
    public void onSetValue(UUID newValue) {
        consumer.accept(newValue);
    }

    @Override
    public boolean validateOnSet(UUID newValue) {
        return true;
    }

    @Override
    public boolean listable() {
        return listable;
    }

    public UUIDValue listable(boolean listable) {
        this.listable = listable;
        return this;
    }

    @Override
    public boolean writableByCommand() {
        return writable;
    }

    public UUIDValue writableByCommand(boolean writable) {
        this.writable = writable;
        return this;
    }

    public @Nullable OfflinePlayer toOfflinePlayer() {
        if (uuid != null) {
            return Bukkit.getOfflinePlayer(uuid);
        }

        return null;
    }

    public @Nullable Player toPlayer() {
        if (uuid != null) {
            return Bukkit.getPlayer(uuid);
        }

        return null;
    }

    public String playerName() {
        if (uuid != null) {
            return Bukkit.getOfflinePlayer(uuid).getName();
        }

        return "";
    }

    @Override
    public String toString() {
        return String.format("UUIDValue{value=%s,playerName=%s,listable=%b,writable=%b}", uuid, playerName, listable, writable);
    }

    @Override
    public String succeedSetMessage(String entryName) {
        return entryName + "の値を" + playerName() + "に設定しました.";
    }

    @Override
    public void sendListMessage(CommandContext ctx, String entryName) {
        ctx.success(entryName + ": " + playerName());
    }

    @Override
    public void appendArgument(UsageBuilder builder) {
        builder.textArgument("PlayerName", sb -> {
            Arrays.stream(Bukkit.getOfflinePlayers())
                    .filter(p -> !onlyOnline || p.isOnline())
                    .filter(p -> !p.getUniqueId().equals(uuid))
                    .map(OfflinePlayer::getName)
                    .forEach(sb::suggest);
        });
    }

    @Override
    public boolean isCorrectArgument(Object argument) {
        return Arrays.stream(Bukkit.getOfflinePlayers())
                .filter(p -> !onlyOnline || p.isOnline())
                .map(OfflinePlayer::getName)
                .anyMatch(s -> s.equals(argument));
    }

    @Override
    public UUID argumentToValue(Object argument) {
        return Arrays.stream(Bukkit.getOfflinePlayers())
                .filter(p -> p.getName().equals(argument))
                .findFirst()
                .get()
                .getUniqueId();
    }
}
