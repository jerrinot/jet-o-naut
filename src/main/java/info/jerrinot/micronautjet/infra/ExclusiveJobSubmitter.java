package info.jerrinot.micronautjet.infra;

import com.hazelcast.core.ILock;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.Job;
import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.pipeline.Pipeline;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * Make sure the job exists once and once only in a cluster. This is a hack which won't be
 * needed in the next version of Hazelcast Jet.
 *
 */
@Singleton
public class ExclusiveJobSubmitter {
    private static final String LOCK_NAME = "jobSubmitterLock";

    @Inject
    private JetInstance instance;

    public Job submitExclusively(Pipeline pipeline, JobConfig config) {
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
