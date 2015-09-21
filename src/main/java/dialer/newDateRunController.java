package dialer;

import java.util.List;

import org.crsh.console.jline.internal.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@SessionAttributes({ "campaign", "agendamento", "c_selecionada" })
public class newDateRunController {
	
	private class TidCampanha{
		private Long idCampanha;

		public Long getIdCampanha() {
			return idCampanha;
		}

		@SuppressWarnings("unused")
		public void setIdCampanha(Long idCampanha) {
			this.idCampanha = idCampanha;
		}
		
	}

	@Autowired
	CampaignDAO campaignDAO;

	@Autowired
	AgendamentoDAO agendamentoDAO;

	@RequestMapping(value = "/dateRun", method = RequestMethod.GET)
	public String dateRun(@ModelAttribute("campaign") Campaign campaign,
			Model model, SessionStatus status) {

		if (campaign == null) {
			campaign = new Campaign();
		}

		model.addAttribute("campaign", campaign);

		return "dateRun";
	}

	@RequestMapping(value = "/newDateRun", method = RequestMethod.GET)
	public String newDateRun(Model model) {

		List<Campaign> c = campaignDAO.findAll();
		model.addAttribute("campaign", c);
		Log.info(c);

		Agendamento a = new Agendamento();
		model.addAttribute("agendamento", a);
		
		TidCampanha c_selecionada = new TidCampanha();

//		Long c_selecionada = new Long(0);

		model.addAttribute("c_selecionada", c_selecionada);

		return "newDateRun";
	}

	@RequestMapping(value = "/newDateRun", method = RequestMethod.POST)
	public String addDateRun(Model model,
			@ModelAttribute("agendamento") Agendamento a,
			@ModelAttribute("c_selecionada") TidCampanha c_selecionada) {
		// model.addAttribute("campaign", campaign);
		
//		Long c_selecionada = new Long(l);
		
		Log.info(model);
		Log.info("Campanha selecionada para agendamento: " + c_selecionada);
		Log.info("Tentando criar agendamento: " + a);
		

		Boolean e = false;

		if (a.getInicio().after(a.getFim())) {
			// inicio depois do fim
			e = true;
		}

		if (a.getMeta() <= 0) {
			// meta invalida
			e = true;
		}
		
		if (e == false) {
			a.setIdCampanha(c_selecionada.getIdCampanha());
			agendamentoDAO.save(a);
			model.addAttribute("save", true);
		}

		List<Campaign> c = campaignDAO.findAll();

		model.addAttribute("campaign", c);

		return "newDateRun";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveDateRun(Model model) {
		// model.addAttribute("campaign", campaign);

		return "importData";
	}

}