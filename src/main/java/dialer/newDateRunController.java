package dialer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
//@SessionAttributes("campaign")
public class newDateRunController {
	

	
	@RequestMapping(value="/newDateRun", method=RequestMethod.GET)
	public String newDateRun(Model model){
//		Campaign campaign = new Campaign();
		
//		model.addAttribute("campaign", campaign);
		

		return "newDateRun";
	}
	
	@RequestMapping(value="/newDateRun", method=RequestMethod.POST)
	public String addDateRun(Model model){
//		model.addAttribute("campaign", campaign);
		
		
		return "newDateRun";
	}

}