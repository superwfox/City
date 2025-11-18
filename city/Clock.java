package sudark2.Sudark.city;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static sudark2.Sudark.city.World.WorldManager.resetWorld;

public class Clock {
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public static void start() {
        long delay = computeInitialDelay();
        scheduler.scheduleAtFixedRate(Clock::tick, delay, 60, TimeUnit.SECONDS);
    }

    private static long computeInitialDelay() {
        LocalDateTime now = LocalDateTime.now();
        return 60 - now.getSecond();
    }

    private static void tick() {
        int hour = LocalDateTime.now().getHour();
        int min = LocalDateTime.now().getMinute();

        if (hour == 4 && min == 0) {
            resetWorld();
        }
    }
}
