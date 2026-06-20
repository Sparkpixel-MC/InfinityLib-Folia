package io.github.mooy1.infinitylib.common;

import javax.annotation.ParametersAreNonnullByDefault;

import lombok.experimental.UtilityClass;

import org.bukkit.Bukkit;

import io.github.mooy1.infinitylib.core.AbstractAddon;

import java.util.concurrent.TimeUnit;

/**
 * A class for scheduling tasks
 *
 * @author Mooy1
 */
@UtilityClass
@ParametersAreNonnullByDefault
public final class Scheduler {

    public static void run(Runnable runnable) {
        Bukkit.getGlobalRegionScheduler().run(AbstractAddon.instance(), _ -> runnable.run());
    }

    public static void runAsync(Runnable runnable) {
        Bukkit.getAsyncScheduler().runNow(AbstractAddon.instance(), _ -> runnable.run());
    }

    public static void run(int delayTicks, Runnable runnable) {
        Bukkit.getGlobalRegionScheduler().runDelayed(AbstractAddon.instance(), _ -> runnable.run(), delayTicks);
    }

    public static void runAsync(int delayTicks, Runnable runnable) {
        long delayInMillis = delayTicks * 50L;
        Bukkit.getAsyncScheduler().runDelayed(AbstractAddon.instance(), _ -> runnable.run(), delayInMillis, TimeUnit.MILLISECONDS);
    }

    public static void repeat(int intervalTicks, Runnable runnable) {
        repeat(intervalTicks, 1, runnable);
    }

    public static void repeatAsync(int intervalTicks, Runnable runnable) {
        repeatAsync(intervalTicks, 1, runnable);
    }

    public static void repeat(int intervalTicks, int delayTicks, Runnable runnable) {
        long delay = delayTicks * 50L;
        long interval = Math.max(1, intervalTicks) * 50L;

        Bukkit.getGlobalRegionScheduler().runAtFixedRate(
                AbstractAddon.instance(),
                _ -> runnable.run(),
                delay,
                interval
        );
    }
    public static void repeatAsync(int intervalTicks, int delayTicks, Runnable runnable) {
        long delayInMillis = delayTicks * 50L;
        long intervalInMillis = Math.max(1, intervalTicks) * 50L;
        Bukkit.getAsyncScheduler().runAtFixedRate(AbstractAddon.instance(), _ -> runnable.run(), delayInMillis, intervalInMillis, TimeUnit.MILLISECONDS);
    }

}
