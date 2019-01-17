package info.jerrinot.micronautjet.infra.impl;

import info.jerrinot.micronautjet.infra.PipelineAndConfig;
import io.micronaut.context.annotation.Context;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.stream.Stream;

@Singleton
@Context
class PipelineConsumer {
    @Inject
    private ExclusiveJobSubmitter exclusiveJobSubmitter;

    @Inject
    void consume(Stream<PipelineAndConfig> jobs) {
        jobs.forEach((p) -> exclusiveJobSubmitter.submitExclusively(p.getPipeline(), p.getJobConfig()));
    }
}
