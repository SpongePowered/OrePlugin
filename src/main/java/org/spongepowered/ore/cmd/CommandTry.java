package org.spongepowered.ore.cmd;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.concurrent.Callable;

/**
 * A helper interface to more easily execute code that throws exceptions
 * within command executors.
 *
 * @param <V> Return type
 */
@FunctionalInterface
public interface CommandTry<V> extends Callable<V> {

    @Override
    V call() throws Exception;

    /**
     * Calls the {@link Callable} and sends an error message to the given
     * {@link CommandSource} if an exception is thrown.
     *
     * @param src CommandSource
     * @return Return type
     */
    default V callFor(CommandSource src) {
        try {
            return call();
        } catch (Exception e) {
            src.sendMessage(Text.of(TextColors.RED, e.getMessage()));
            throw new RuntimeException(e);
        }
    }


}
