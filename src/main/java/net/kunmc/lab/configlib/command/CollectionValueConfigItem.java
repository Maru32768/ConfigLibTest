package net.kunmc.lab.configlib.command;

import dev.kotx.flylib.command.Command;
import dev.kotx.flylib.command.CommandContext;
import dev.kotx.flylib.command.UsageBuilder;
import net.kunmc.lab.configlib.config.BaseConfig;
import net.kunmc.lab.configlib.value.CollectionValue;

import java.lang.reflect.Field;
import java.util.Collection;

abstract class CollectionValueConfigItem extends Command {
    protected final Field field;
    protected final CollectionValue configValue;
    protected final BaseConfig config;

    public CollectionValueConfigItem(Field field, CollectionValue<?, ?> configValue, BaseConfig config) {
        super(field.getName());
        this.field = field;
        this.configValue = configValue;
        this.config = config;

        String entryName = field.getName();

        usage(builder -> {
            appendArgument(builder);

            builder.executes(ctx -> {
                Object argument = ctx.getTypedArgs().get(0);
                if (!isCorrectArgument(argument)) {
                    ctx.fail(incorrectArgumentMessage(argument));
                    return;
                }

                Collection newValue = argumentToValue(argument);
                if (!validate(newValue)) {
                    ctx.fail(invalidMessage(entryName, newValue));
                    return;
                }

                writeProcess(ctx, entryName, newValue);

                config.saveConfigIfPresent();
            });
        });
    }

    abstract void appendArgument(UsageBuilder builder);

    abstract boolean isCorrectArgument(Object argument);

    abstract String incorrectArgumentMessage(Object argument);

    abstract Collection argumentToValue(Object argument);

    abstract boolean validate(Collection value);

    abstract String invalidMessage(String entryName, Collection value);

    abstract void writeProcess(CommandContext ctx, String entryName, Collection value);
}
