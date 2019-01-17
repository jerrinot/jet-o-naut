package info.jerrinot.micronautjet.infra;

import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.pipeline.Pipeline;

public final class PipelineAndConfig {
    private final Pipeline pipeline;
    private final JobConfig jobConfig;

    PipelineAndConfig(Pipeline pipeline, JobConfig jobConfig) {
        this.pipeline = pipeline;
        this.jobConfig = jobConfig;
    }

    public Pipeline getPipeline() {
        return pipeline;
    }

    public JobConfig getJobConfig() {
        return jobConfig;
    }
}
