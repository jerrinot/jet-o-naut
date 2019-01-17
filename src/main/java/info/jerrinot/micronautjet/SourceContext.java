package info.jerrinot.micronautjet;

import io.micronaut.context.annotation.Property;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Singleton
public final class SourceContext {
    @Property(name = "application.interval-millis")
    private long intervalMillis;

    private long intervalNanos;
    private final Random r = new Random();
    private long lastTimestamp;

    @PostConstruct
    public void postConstruct() {
        intervalNanos = TimeUnit.MILLISECONDS.toNanos(intervalMillis);
    }

    public Long nextOrNull() {
        long now = System.nanoTime();
        if (lastTimestamp + intervalNanos < now) {
            lastTimestamp = now;
            return r.nextLong();
        }
        return null;
    }
}