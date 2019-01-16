package info.jerrinot.micronautjet;

import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.SourceBuilder;
import com.hazelcast.jet.pipeline.StreamSource;
import io.micronaut.context.annotation.Context;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.concurrent.ThreadLocalRandom;

import static com.hazelcast.jet.pipeline.Sinks.logger;
import static info.jerrinot.micronautjet.MicronautUtils.*;

@Singleton
@Context
public final class JetApplication {
    private static final String JOB_NAME = "myJob";

    @Inject
    private JetInstance jetInstance;

    @Inject
    private ExclusiveJobSubmitter jobSubmitter;

    @PreDestroy
    public void preDestroy() {
        if (jetInstance.getHazelcastInstance().getLifecycleService().isRunning()) {
            jetInstance.shutdown();
        }
    }

    @PostConstruct
    public void postConstruct() {
        StreamSource<Long> globalSingletonSource = customSource();
        Pipeline pipeline = pipeline(globalSingletonSource);

        JobConfig jobConfig = new JobConfig().setName(JOB_NAME);
        jobSubmitter.submitExclusively(pipeline, jobConfig);
    }

    private Pipeline pipeline(StreamSource<Long> source) {
        Pipeline pipeline = Pipeline.create();
        pipeline.drawFrom(source)
                .groupingKey((ignored) -> ThreadLocalRandom.current().nextInt())
                .filterUsingContext(getBeanCtxFactory(ParametrizedFilter.class), (c, k, e) -> c.test(e))
                .drainTo(logger());
        return pipeline;
    }

    private StreamSource<Long> customSource() {
        return SourceBuilder.stream("random-source", getBeanFn(SourceContext.class))
                    .<Long>fillBufferFn((state, buf) -> {
                        Long generated = state.nextOrNull();
                        if (generated != null) {
                            buf.add(generated);
                        }
                    }).build();
    }
}
