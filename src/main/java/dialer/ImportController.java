package dialer;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;


@Controller
public class ImportController {
	
	
	@Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;

    @RequestMapping("/importData")
    public String importData(Model model, SessionStatus status){

		try {
			handle();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "importData";
	}
    
    
    public void handle() throws Exception{
        jobLauncher.run(job, new JobParameters());
        

    }
	
}
