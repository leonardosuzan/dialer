package dialer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@SessionAttributes("campaign")
public class NewCampaignController {
	

	
	@RequestMapping(value="/newCampaign", method=RequestMethod.GET)
	public String newCampaign(Model model, SessionStatus status){
		Campaign campaign = new Campaign();
		
		model.addAttribute("campaign", campaign);
		

		return "newCampaign";
	}
	
	@RequestMapping(value="/newCampaign", method=RequestMethod.POST)
	public String addCampaign(@ModelAttribute("campaign") Campaign campaign, Model model, SessionStatus status){
		model.addAttribute("campaign", campaign);
		
		
		return "importList";
	}

}