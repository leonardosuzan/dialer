package dialer;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	
	@RequestMapping(value={"", "/", "home", "index", "dashboard"})
	public String index(Model model) {
        model.addAttribute("pg_name", "dialer.mt | home");
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String cur_username = auth.getName(); //get logged in username
        
        model.addAttribute("user", cur_username);
        
        return "index";
    }
	

}
