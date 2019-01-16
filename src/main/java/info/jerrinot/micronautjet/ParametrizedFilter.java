package info.jerrinot.micronautjet;

import javax.inject.Singleton;

@Singleton
public class ParametrizedFilter {
    public boolean test(long value) {
        return (value > 0);
    }
}
