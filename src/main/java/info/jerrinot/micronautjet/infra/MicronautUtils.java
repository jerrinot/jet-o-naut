package info.jerrinot.micronautjet.infra;

import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.config.JetConfig;
import com.hazelcast.jet.core.Processor;
import com.hazelcast.jet.function.DistributedFunction;
import com.hazelcast.jet.pipeline.ContextFactory;
import io.micronaut.context.ApplicationContext;

public final class MicronautUtils {
    private static final String MICRONAUT_USER_CONTEXT_NAME = "micronaut-user-context";

    private MicronautUtils() {

    }


    public static ApplicationContext toAppContext(Processor.Context processorCtx) {
        return toAppContext0(processorCtx.jetInstance());
    }

    public static <T> DistributedFunction<Processor.Context, T> getBeanFn(Class<T> beanType) {
        return context -> getBean(context, beanType);
    }

    public static <T> ContextFactory<T> getBeanCtxFactory(Class<T> beanType) {
        return ContextFactory
                .withCreateFn((i) -> toAppContext0(i).getBean(beanType))
                .shareLocally();
    }


    static void attachAppContext(JetInstance jet, ApplicationContext context) {
        jet.getHazelcastInstance().getUserContext().put(MICRONAUT_USER_CONTEXT_NAME, context);
    }

    private static <T> T getBean(Processor.Context processorCtx, Class<T> beanType) {
        return toAppContext(processorCtx).getBean(beanType);
    }

    private static ApplicationContext toAppContext0(JetInstance jet) {
        Object micronautContext = jet.getHazelcastInstance().getUserContext().get(MICRONAUT_USER_CONTEXT_NAME);
        if (micronautContext == null) {
            throw new IllegalStateException("Micronaut context is not attached as Hazelcast User Context");
        }
        if (!(micronautContext instanceof ApplicationContext)) {
            throw new IllegalStateException("Unknown context attached as Hazelcast User Context: " + micronautContext);
        }
        return (ApplicationContext) micronautContext;
    }
}
