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
@SessionAttributes({ "campanha", "listings", "actions" })
public class EditCampaignController {

	@Autowired
	CampaignDAO campaignDAO;

	@Autowired
	ListingDAO listingDAO;

	@RequestMapping(value = "/editCampaign", method = RequestMethod.GET)
	public String editCampaign(
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

		Campaign c = campaignDAO.findByidCampanha(i_id);
		List<Listing> l = listingDAO.findAll();

		if (c == null) {
			Log.error("Campanha " + i_id + " não existe!");
			return null;
		}
		
		Actions a;
		
		if(c.getActions() == null){
			a = new Actions();
			Action r = new Action();
			a.addAction(r);
			c.setActions(a);
		} else {
			a = c.getActions();
		}

		model.addAttribute("actions", a);
		model.addAttribute("campanha", c);
		model.addAttribute("listings", l);

		Log.info(a);
		Log.info(l);
		Log.info(model);

		return "editCampaign";

	}

	@RequestMapping(value = "/editCampaign", method = RequestMethod.POST)
	public String writeCampaign(
			Model model,
			SessionStatus status,
			@RequestParam(value = "id", required = false, defaultValue = "0") String id,
			@ModelAttribute("campanha") Campaign campanha,
			@ModelAttribute("listings") List<Listing> l) {

		Log.info("Campanha alterada: " + campanha);

		campaignDAO.save(campanha);

		model.addAttribute("save", true);

		return "editCampaign";

	}
	
	@RequestMapping(value = "/editCampaignActions", method = RequestMethod.POST)
	public String editCampaignActions(
			Model model,
			SessionStatus status,
			@ModelAttribute("campanha") Campaign campanha,
			@ModelAttribute("listings") List<Listing> l,
			@ModelAttribute("actions") Actions a) {

		Log.info("Acoes alteradas: " + a);
		
		campanha.setActions(a);

		campaignDAO.save(campanha);

		model.addAttribute("save", true);

		return "editCampaign";

	}
	
	@RequestMapping(value = "/editCampaignActions", method = RequestMethod.POST, params={"addRow"})
	public String addRow(
			Model model,
			SessionStatus status,
			@ModelAttribute("campanha") Campaign campanha,
			@ModelAttribute("listings") List<Listing> l,
			@ModelAttribute("actions") Actions a) {

		Log.info("Acoes alteradas: " + a);
		

		Action r = new Action();
		a.addAction(r);
		
		campanha.setActions(a);
		
		campaignDAO.save(campanha);
        
		model.addAttribute("save", true);

		return "editCampaign";

	}
	
	

}
