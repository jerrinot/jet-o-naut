package info.jerrinot.micronautjet.infra.impl;

import com.hazelcast.core.ILock;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.Job;
import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.pipeline.Pipeline;
import info.jerrinot.micronautjet.infra.PipelineAndConfig;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Requires;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.stream.Stream;

@Singleton
@Context
@Requires(beans = JetInstance.class)
class JobSubmitter {
    private static final String LOCK_NAME = "jobSubmitterLock";

    @Inject
    private JetInstance instance;

    @Inject
    void consume(Stream<PipelineAndConfig> jobs) {
        jobs.forEach((p) -> submitExclusively(p.getPipeline(), p.getJobConfig()));
    }


    /**
     * Ensure job exists once and once only in a cluster. This is a hack which won't be
     * needed in the next version of Hazelcast Jet.
     *
     */
    private Job submitExclusively(Pipeline pipeline, JobConfig config) {
        String name = config.getName();
        if (name == null) {
            throw new IllegalArgumentException("Job name cannot be null");
        }
        Job job = instance.getJob(name);
        if (job != null) {
            return job;
        }
        ILock lock = instance.getHazelcastInstance().getLock(LOCK_NAME);
        lock.lock();
        try {
            job = instance.getJob(name);
            if (job != null) {
                return job;
            }
            return instance.newJob(pipeline, config);
        } finally {
            lock.unlock();
        }
    }
}
