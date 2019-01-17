package info.jerrinot.micronautjet;

import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.SourceBuilder;
import com.hazelcast.jet.pipeline.StreamSource;
import info.jerrinot.micronautjet.infra.MicronautUtils;
import info.jerrinot.micronautjet.infra.PipelineAndConfig;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;

import javax.inject.Singleton;

import java.util.concurrent.ThreadLocalRandom;

import static com.hazelcast.jet.pipeline.Sinks.logger;
import static info.jerrinot.micronautjet.infra.MicronautUtils.*;

@Factory
public final class PipelineFactory {
    private static final String JOB_NAME = "myJob";

    @Bean
    public PipelineAndConfig createPipeline() {
        StreamSource<Long> globalSingletonSource = customSource();
        Pipeline pipeline = pipeline(globalSingletonSource);

        return MicronautUtils.pipelineWithName(pipeline, JOB_NAME);
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
