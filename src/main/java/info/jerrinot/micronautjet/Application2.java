package info.jerrinot.micronautjet;

import io.micronaut.runtime.Micronaut;

/**
 * Exists only to be able to easily start 2nd instance from IDE
 *
 */
public class Application2 {
    public static void main(String[] args) {
        Micronaut.run(Application2.class);
    }
}