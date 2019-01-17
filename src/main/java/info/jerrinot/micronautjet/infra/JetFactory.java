package info.jerrinot.micronautjet.infra;

import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;

import javax.inject.Inject;
import javax.inject.Singleton;


@Factory
public class JetFactory {

    @Inject
    private ApplicationContext applicationContext;

    @Bean
    @Singleton
    public JetInstance jetInstance() {
        // we want Jet to pick-up its config from classpath when it exists
        // -> we have to use the non-arg factory method and attach the context later
        JetInstance jet = Jet.newJetInstance();
        MicronautUtils.attachAppContext(jet, applicationContext);
        return jet;
    }
}
