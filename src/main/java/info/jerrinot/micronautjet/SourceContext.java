package info.jerrinot.micronautjet;

import javax.inject.Singleton;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Singleton
public final class SourceContext {
    private static final long INTERVAL_NANOS = TimeUnit.MILLISECONDS.toNanos(500);

    private final Random r = new Random();
    private long lastTimestamp;

    public Long nextOrNull() {
        long now = System.nanoTime();
        if (lastTimestamp + INTERVAL_NANOS < now) {
            lastTimestamp = now;
            return r.nextLong();
        }
        return null;
    }
}