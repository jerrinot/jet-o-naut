package info.jerrinot.micronautjet.infra.impl;

import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import info.jerrinot.micronautjet.infra.MicronautUtils;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;

import javax.inject.Singleton;


@Factory
@Requires(classes = JetInstance.class)
public class JetFactory {

    @Bean
    @Singleton
    public JetInstance jetInstance(ApplicationContext applicationContext) {
        // we want Jet to pick-up its config from classpath when it exists
        // -> we have to use the non-arg factory method and attach the context later
        JetInstance jet = Jet.newJetInstance();
        MicronautUtils.attachAppContext(jet, applicationContext);
        return jet;
    }
}
