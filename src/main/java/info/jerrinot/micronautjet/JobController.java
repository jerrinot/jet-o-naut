package info.jerrinot.micronautjet;

import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.Job;
import io.micronaut.core.annotation.Blocking;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import javax.inject.Inject;
import java.util.List;

import static io.micronaut.http.HttpStatus.NOT_FOUND;

@Controller("/jobs")
public class JobController {

    @Inject
    private JetInstance jetInstance;

    @Get
    @Blocking
    public List<Job> getAllJobs() {
        return jetInstance.getJobs();
    }

    @Get("/{name}")
    @Blocking
    public Object getJob(String name) {
        Job job = jetInstance.getJob(name);
        return job == null ? NOT_FOUND : job;
    }

}
