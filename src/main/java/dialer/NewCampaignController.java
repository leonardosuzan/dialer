package dialer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class NewCampaignController {
	
	@RequestMapping(value="/newCampaign", method=RequestMethod.GET)
	public String newCampaign(Model model){
		model.addAttribute("campaign", new Campaign());

		return "newCampaign";
	}
	
	@RequestMapping(value="/newCampaign", method=RequestMethod.POST)
	public String addCampaign(@ModelAttribute Campaign campaign, Model model){
		model.addAttribute("campaign", campaign);
//		model.addAttribute("print", true);
		
//		System.out.print(campaign);

		
		return "importList";
	}

}