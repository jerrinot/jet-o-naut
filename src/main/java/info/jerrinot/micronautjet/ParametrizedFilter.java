package info.jerrinot.micronautjet;

import io.micronaut.context.annotation.Property;

import javax.inject.Singleton;

@Singleton
public class ParametrizedFilter {

    @Property(name = "application.threshold")
    private long threshold;

    public boolean test(long value) {
        return (value > threshold);
    }
}
