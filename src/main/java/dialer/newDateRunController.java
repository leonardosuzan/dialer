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
public class newDateRunController {
	

	
	@RequestMapping(value="/newDateRun", method=RequestMethod.GET)
	public String newDateRun(Model model){
//		Campaign campaign = new Campaign();
		
//		model.addAttribute("campaign", campaign);
		

		return "newDateRun";
	}
	
	@RequestMapping(value="/dateRun", method=RequestMethod.GET)
	public String dateRun(@ModelAttribute("campaign") Campaign campaign, Model model, SessionStatus status){	
		
		if(campaign == null){
			campaign = new Campaign();
		}
		
		model.addAttribute("campaign", campaign);
		

		return "dateRun";
	}
	
	@RequestMapping(value="/newDateRun", method=RequestMethod.POST)
	public String addDateRun(Model model){
//		model.addAttribute("campaign", campaign);
		
		
		return "newDateRun";
	}
	
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public String saveDateRun (Model model){
//		model.addAttribute("campaign", campaign);
		
		
		return "importData";
	}

}