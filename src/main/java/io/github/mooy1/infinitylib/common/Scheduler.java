package io.github.mooy1.infinitylib.common;

import javax.annotation.ParametersAreNonnullByDefault;

import lombok.experimental.UtilityClass;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import io.github.mooy1.infinitylib.core.AbstractAddon;

import java.util.concurrent.TimeUnit;

/**
 * A class for scheduling tasks
 *
 * All delay/interval parameters are in ticks, converted internally where needed.
 * On Folia, entity/region tasks always incur at least 1 tick of delay (inherent
 * to the scheduling model), so delays are clamped to a minimum of 1 tick.
 *
 * @author Mooy1
 */
@UtilityClass
@ParametersAreNonnullByDefault
public final class Scheduler {

    // Folia detection, cached once and reused for every check
    private static volatile Boolean IS_FOLIA;

    public static boolean isFolia() {
        Boolean folia = IS_FOLIA;
        if (folia != null) return folia;
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            folia = true;
        } catch (ClassNotFoundException e) {
            folia = false;
        }
        IS_FOLIA = folia;
        return folia;
    }

    public static void run(Runnable runnable) {
        if (isFolia()) {
            Bukkit.getGlobalRegionScheduler().run(AbstractAddon.instance(), _ -> runnable.run());
        } else {
            Bukkit.getScheduler().runTask(AbstractAddon.instance(), runnable);
        }
    }

    public static void runAsync(Runnable runnable) {
        if (isFolia()) {
            Bukkit.getAsyncScheduler().runNow(AbstractAddon.instance(), _ -> runnable.run());
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(AbstractAddon.instance(), runnable);
        }
    }

    public static void run(int delayTicks, Runnable runnable) {
        int delay = Math.max(1, delayTicks);
        if (isFolia()) {
            Bukkit.getGlobalRegionScheduler().runDelayed(AbstractAddon.instance(), _ -> runnable.run(), delay);
        } else {
            Bukkit.getScheduler().runTaskLater(AbstractAddon.instance(), runnable, delay);
        }
    }

    public static void runAsync(int delayTicks, Runnable runnable) {
        long delayInMillis = Math.max(1, delayTicks) * 50L;
        if (isFolia()) {
            Bukkit.getAsyncScheduler().runDelayed(AbstractAddon.instance(), _ -> runnable.run(), delayInMillis, TimeUnit.MILLISECONDS);
        } else {
            Bukkit.getScheduler().runTaskLaterAsynchronously(AbstractAddon.instance(), runnable, delayTicks);
        }
    }

    public static void repeat(int intervalTicks, Runnable runnable) {
        repeat(intervalTicks, 1, runnable);
    }

    public static void repeatAsync(int intervalTicks, Runnable runnable) {
        repeatAsync(intervalTicks, 1, runnable);
    }

    public static void repeat(int intervalTicks, int delayTicks, Runnable runnable) {
        long interval = Math.max(1, intervalTicks);
        long delay = Math.max(1, delayTicks);

        if (isFolia()) {
            // runAtFixedRate takes TICKS, not milliseconds
            Bukkit.getGlobalRegionScheduler().runAtFixedRate(
                    AbstractAddon.instance(),
                    _ -> runnable.run(),
                    delay,
                    interval
            );
        } else {
            Bukkit.getScheduler().runTaskTimer(AbstractAddon.instance(), runnable, delay, interval);
        }
    }

    public static void repeatAsync(int intervalTicks, int delayTicks, Runnable runnable) {
        long delayInMillis = Math.max(1, delayTicks) * 50L;
        long intervalInMillis = Math.max(1, intervalTicks) * 50L;
        if (isFolia()) {
            Bukkit.getAsyncScheduler().runAtFixedRate(AbstractAddon.instance(), _ -> runnable.run(), delayInMillis, intervalInMillis, TimeUnit.MILLISECONDS);
        } else {
            Bukkit.getScheduler().runTaskTimerAsynchronously(AbstractAddon.instance(), runnable, delayTicks, intervalTicks);
        }
    }

    /**
     * Runs a task on the entity's scheduler (Folia) or the main thread (Paper).
     * The task runs immediately on the entity's thread, no artificial delay is added.
     */
    public static void runAtEntity(Entity entity, Runnable task) {
        if (isFolia()) {
            try {
                entity.getScheduler().run(AbstractAddon.instance(), t -> task.run(), null);
            } catch (IllegalStateException e) {
                // entity was retired before the task could be scheduled
            }
        } else {
            Bukkit.getScheduler().runTask(AbstractAddon.instance(), task);
        }
    }

    /**
     * Runs a task on the entity's scheduler after at least 1 tick.
     */
    public static void runAtEntity(Entity entity, int delayTicks, Runnable task) {
        int delay = Math.max(1, delayTicks);
        if (isFolia()) {
            try {
                entity.getScheduler().runDelayed(AbstractAddon.instance(), t -> task.run(), null, delay);
            } catch (IllegalStateException e) {
                // entity was retired before the task could be scheduled
            }
        } else {
            Bukkit.getScheduler().runTaskLater(AbstractAddon.instance(), task, delay);
        }
    }

    /**
     * Runs a task on the region owning the given location (Folia) or the main thread (Paper).
     * The task runs on the next tick of the region.
     */
    public static void runAtRegion(Location location, Runnable task) {
        if (isFolia()) {
            Bukkit.getRegionScheduler().run(AbstractAddon.instance(), location, t -> task.run());
        } else {
            Bukkit.getScheduler().runTask(AbstractAddon.instance(), task);
        }
    }

    /**
     * Runs a task on the region owning the given location after at least 1 tick.
     */
    public static void runAtRegion(Location location, int delayTicks, Runnable task) {
        int delay = Math.max(1, delayTicks);
        if (isFolia()) {
            Bukkit.getRegionScheduler().runDelayed(AbstractAddon.instance(), location, t -> task.run(), delay);
        } else {
            Bukkit.getScheduler().runTaskLater(AbstractAddon.instance(), task, delay);
        }
    }

}
