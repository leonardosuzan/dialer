package dialer;

import javax.batch.operations.JobRestartException;
import javax.batch.operations.NoSuchJobException;
import javax.inject.Inject;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.web.bind.annotation.RequestMapping;

public class ImportController {
	@Inject
	private JobLocator jobLocator;

	@Inject
	private JobLauncher jobLauncher;

	@RequestMapping(value = "/upload")
	public void runJob() throws JobExecutionAlreadyRunningException, JobRestartException,        JobInstanceAlreadyCompleteException, JobParametersInvalidException, NoSuchJobException {
	    try {
			jobLauncher.run(jobLocator.getJob("importContactsJob"), new JobParameters());
		} catch (org.springframework.batch.core.repository.JobRestartException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (org.springframework.batch.core.launch.NoSuchJobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
