package dialer;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LogoutController {
	
	@RequestMapping("/logout")
	public ModelAndView logout(){
		
		SecurityContextHolder.clearContext();
		
		return new ModelAndView("redirect:login?logout");
		
	}

}
