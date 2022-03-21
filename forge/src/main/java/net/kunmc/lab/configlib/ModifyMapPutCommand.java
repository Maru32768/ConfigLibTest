package net.kunmc.lab.configlib;

import net.kunmc.lab.configlib.command.AccessibleCommand;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Field;
import java.util.List;

class ModifyMapPutCommand extends AccessibleCommand {
    public ModifyMapPutCommand(Field field, MapValue value, BaseConfig config) {
        super("put");

        String entryName = field.getName();

        usage(builder -> {
            value.appendKeyArgumentForPut(builder);
            value.appendValueArgumentForPut(builder);

            builder.executes(ctx -> {
                List<Object> argument = ctx.getTypedArgs();
                CommandSender sender = ctx.getSender();
                if (!value.isCorrectKeyArgumentForPut(argument, sender)) {
                    ctx.fail(value.incorrectKeyArgumentMessageForPut(argument));
                    return;
                }
                if (!value.isCorrectValueArgumentForPut(argument, sender)) {
                    ctx.fail((value.incorrectValueArgumentMessageForPut(argument)));
                    return;
                }

                Object k = value.argumentToKeyForPut(argument, sender);
                Object v = value.argumentToValueForPut(argument, sender);
                if (!value.validateKeyForPut(k)) {
                    ctx.fail(value.invalidKeyMessageForPut(entryName, k));
                    return;
                }
                if (!value.validateValueForPut(v)) {
                    ctx.fail(value.invalidValueMessageForPut(entryName, v));
                    return;
                }

                if (value.onPutValue(k, v, ctx)) {
                    return;
                }

                value.put(k, v);
                ctx.success(value.succeedMessageForPut(entryName, k, v));

                config.saveConfigIfPresent();
            });
        });
    }
}
