package dialer;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;


@Controller
@SessionAttributes({"fileName", "campaign"})
public class ImportController {
	
	String localFileName;
	
	
	@Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;
    
    @Autowired
    ListingDAO listingDAO;
    
    @Autowired
    CampaignDAO campaignDAO;
    
    private Listing listing;

    @RequestMapping("/importData")
    public String importData(@ModelAttribute("fileName") String fileName, @ModelAttribute("campaign") Campaign campaign, Model model, SessionStatus status){
    	
    	
    	listing = new Listing();
    	
    	listing.setName(campaign.getName() + "list");
    	campaign.setIdListagem(listing.getId_listing());
    	listingDAO.save(listing);
    	campaignDAO.save(campaign);
    	
    	

    	localFileName = fileName;
    	
    	model.addAttribute("campaign", campaign);
    	
		try {
			handle();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "dateRun";
	}
    
    
    @RequestMapping("/importDataList")
    public String importDataList(@ModelAttribute("fileName") String fileName, Model model, SessionStatus status){
    	
    	
    	listing = new Listing();
    	
    	listing.setName(fileName + "_list");
    	listingDAO.save(listing);

    	localFileName = fileName;
    	
    	
		try {
			handle();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "newListSuccess";
	}
    
    
    public void handle() throws Exception{
    	
    	
    	Map<String, JobParameter> map = new HashMap<String, JobParameter>();
    	
    	map.put("fileName",new JobParameter(this.localFileName));
    	map.put("listingId",new JobParameter(this.listing.getId_listing()));
    	
    	jobLauncher.run(job, new JobParameters(map));
        

    }
	
}
