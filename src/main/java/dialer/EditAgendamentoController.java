package dialer;

import java.util.List;

import org.crsh.console.jline.internal.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@SessionAttributes({ "campanhas", "agendamento", "c_selecionada" })
public class EditAgendamentoController {

	private class TidCampanha {
		private Long idCampanha;

		public Long getIdCampanha() {
			return idCampanha;
		}

		public void setIdCampanha(Long idCampanha) {
			this.idCampanha = idCampanha;
		}

	}

	@Autowired
	CampaignDAO campaignDAO;

	@Autowired
	ListingDAO listingDAO;

	@Autowired
	AgendamentoDAO agendamentoDAO;

	@RequestMapping(value = "/editDateRun", method = RequestMethod.GET)
	public String editDateRun(
			Model model,
			SessionStatus status,
			@RequestParam(value = "id", required = false, defaultValue = "0") String id) {

		Long i_id;

		try {
			i_id = Long.valueOf(id);
		} catch (NumberFormatException e) {
			Log.error("[editCampaign] Impossível converter String para Integer: "
					+ id);
			e.printStackTrace();

			return null;
		}

		Agendamento a = agendamentoDAO.findOne(i_id);
		List<Campaign> c = campaignDAO.findAll();
		

		if (a == null) {
			Log.error("Agendamento " + i_id + " não existe!");
			return null;
		}

		TidCampanha t = new TidCampanha();
		t.setIdCampanha(a.getIdCampanha());

		model.addAttribute("campanhas", c);
		model.addAttribute("agendamento", a);
		model.addAttribute("c_selecionada", t);

		Log.info(model);

		return "editDateRun";

	}

	@RequestMapping(value = "/editDateRun", method = RequestMethod.POST)
	public String writeDateRun(
			Model model,
			SessionStatus status,
			@RequestParam(value = "idCampanha", required = false, defaultValue = "0") String id,
			@ModelAttribute("agendamento") Agendamento agendamento,
			@ModelAttribute("c_selecionada") TidCampanha c_selecionada) {
		
		

		Log.info(model);
		Log.info("Agendamento alterada: " + agendamento + c_selecionada);

		agendamento.setIdCampanha(c_selecionada.getIdCampanha());

		agendamentoDAO.save(agendamento);

		model.addAttribute("save", true);

		return "editDateRun";

	}

}
