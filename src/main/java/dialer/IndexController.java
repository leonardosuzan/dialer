package dialer;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.crsh.console.jline.internal.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	
	private class LAgendamento{
		private Agendamento a;
		
		private Campaign c;

		public Agendamento getA() {
			return a;
		}

		public void setA(Agendamento a) {
			this.a = a;
		}

		@SuppressWarnings("unused")
		public Campaign getC() {
			return c;
		}

		public void setC(Campaign c) {
			this.c = c;
		}
		
	}

	@Autowired
	private CampaignDAO campaignDAO;

	@Autowired
	private ListingDAO listingDAO;

	@Autowired
	private AgendamentoDAO agendamentoDAO;
	
	@RequestMapping(value = { "", "/", "home", "index", "dashboard" })
	public String index(Model model) {
		model.addAttribute("pg_name", "dialer.mt | home");

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		String cur_username = auth.getName(); // get logged in username

		model.addAttribute("user", cur_username);

		List<Campaign> campanhas = campaignDAO.findAll();
		List<Listing> listagens = listingDAO.findAll();
		List<Agendamento> agendamentos = agendamentoDAO.findAll();
		
		List<LAgendamento> l = new ArrayList<LAgendamento>();
		
		ListIterator<Agendamento> it = agendamentos.listIterator();
		
		
		while(it.hasNext()){
			LAgendamento a = new LAgendamento();
			a.setA(it.next());
			a.setC(campaignDAO.findOne(a.getA().getIdCampanha()));
			l.add(a);
		}
	
		model.addAttribute("campanhas", campanhas);
		model.addAttribute("listings", listagens);
		model.addAttribute("agendamentos", agendamentos);
		model.addAttribute("lagendamentos", l);
		
		Log.info("Campanhas: " + campanhas.toString());
		Log.info("Listagens: " + listagens.toString());
		Log.info("Agendamentos: " + agendamentos.toString());
		

		return "index";
	}

}
