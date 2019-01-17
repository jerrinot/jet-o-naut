package info.jerrinot.micronautjet;

import io.micronaut.context.annotation.Value;

import javax.inject.Singleton;

@Singleton
public class ParametrizedFilter {

    @Value("${application.threshold:0}")
    private long threshold;

    public boolean test(long value) {
        return (value > threshold);
    }
}
