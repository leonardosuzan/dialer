package dialer;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;


@Controller
@SessionAttributes("fileName")
public class ImportController {
	
	String localFileName;
	
	
	@Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;

    @RequestMapping("/importData")
    public String importData(@ModelAttribute("fileName") String fileName, Model model, SessionStatus status){
    	

    	localFileName = fileName;
    	
		try {
			handle();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "importData";
	}
    
    
    public void handle() throws Exception{
    	
    	jobLauncher.run(job, new JobParametersBuilder().addString("fileName", this.localFileName).toJobParameters());
        

    }
	
}
