package net.kunmc.lab.configlib.command;

import dev.kotx.flylib.command.Command;
import net.kunmc.lab.configlib.config.BaseConfig;
import net.kunmc.lab.configlib.value.SingleValue;
import net.kunmc.lab.configlib.value.Value;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

enum SubCommandType {
    Reload("reload",
            x -> !(x.getSingleValueFields().isEmpty() && x.getCollectionValueFields().isEmpty()),
            ConfigReloadCommand::new,
            ConfigReloadCommand::new),
    List("list",
            x -> Stream.concat(x.getSingleValues().stream(), x.getCollectionValues().stream()).anyMatch(Value::listable),
            ConfigListCommand::new,
            ConfigListCommand::new),
    Modify("modify",
            x -> x.getCollectionValues().stream()
                    .anyMatch(v -> v.addableByCommand() || v.removableByCommand() || v.clearableByCommand()) || x.getSingleValues().stream().anyMatch(SingleValue::writableByCommand),
            ConfigModifyCommand::new,
            ConfigModifyCommand::new);

    public final String name;
    private final Predicate<BaseConfig> hasEntryFor;
    private final Function<BaseConfig, Command> instantiator;
    private final Function<Set<BaseConfig>, Command> instantiator2;

    SubCommandType(String name, Predicate<BaseConfig> hasEntryFor, Function<BaseConfig, Command> instantiator, Function<Set<BaseConfig>, Command> instantiator2) {
        this.name = name;
        this.hasEntryFor = hasEntryFor;
        this.instantiator = instantiator;
        this.instantiator2 = instantiator2;
    }

    public boolean hasEntryFor(BaseConfig config) {
        return hasEntryFor.test(config);
    }

    public Map<BaseConfig, Boolean> hasEntryFor(Set<BaseConfig> configSet) {
        return configSet.stream()
                .collect(Collectors.toMap(baseConfig -> baseConfig, this::hasEntryFor));
    }

    public Command of(BaseConfig config) {
        return instantiator.apply(config);
    }

    public Command of(Set<BaseConfig> config) {
        return instantiator2.apply(config);
    }
}
