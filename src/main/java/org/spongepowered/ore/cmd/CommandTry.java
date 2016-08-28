package org.spongepowered.ore.cmd;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.concurrent.Callable;

@FunctionalInterface
public interface CommandTry<V> extends Callable<V> {

    @Override
    V call() throws Exception;

    default V callFor(CommandSource src) {
        try {
            return call();
        } catch (Exception e) {
            src.sendMessage(Text.of(TextColors.RED, e.getMessage()));
            throw new RuntimeException(e);
        }
    }


}
