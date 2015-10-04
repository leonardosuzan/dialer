package dialer;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.TimeoutException;
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
@SessionAttributes({ "campanha", "listings", "actions", "analise" })
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
		
		String r = analisar(a);
		Log.info("Resultado da analise:" + r);
		
		model.addAttribute("analise", r);
		
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
	
	@RequestMapping(value = "/editCampaignActions", method = RequestMethod.POST, params={"deleteRow"})
	public String deleteRow(
			Model model,
			SessionStatus status,
			@ModelAttribute("campanha") Campaign campanha,
			@ModelAttribute("listings") List<Listing> l,
			@ModelAttribute("actions") Actions a, 
			@RequestParam("deleteRow") int index){

		
		Log.info("Deletando acao: "+index);
		
		a.deleteAcion(index);
		
		Log.info("Acoes alteradas: " + a);
		
		campanha.setActions(a);
		
		campaignDAO.save(campanha);
        
		model.addAttribute("save", true);

		return "editCampaign";

	}
	
	
	@RequestMapping(value = "/campaignCallTest", method = RequestMethod.POST, params={"testNumber"})
	public String callTest(
			Model model,
			SessionStatus status,
			@ModelAttribute("campanha") Campaign campanha,
			@ModelAttribute("listings") List<Listing> l,
			@ModelAttribute("actions") Actions a, 
			@RequestParam("testNumber") String n){
		
		Log.info("Iniciando chamada de teste para o número: "+n);
		
		CallerHelper t = new CallerHelper();
		
		String r;
		try {
			r = t.run(n);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			r=e.toString();
			e.printStackTrace();
		} catch (AuthenticationFailedException e) {
			// TODO Auto-generated catch block
			r=e.toString();

			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			r=e.toString();
			e.printStackTrace();
		}
		
		model.addAttribute("callTestResult", r);
		

		return "editCampaign";

	}
	
	
	private String analisar(Actions a){
		if (a == null){
			return "Ocorreu um erro com a analise [a==null]";
			
		}
		
//		As açoes configuradas:		
//		o.add(new Opcao(0, "Atender"));
//		o.add(new Opcao(1, "Desligar"));
//		o.add(new Opcao(2, "Coletar resposta"));
//		o.add(new Opcao(3, "Coletar resposta com reconhecimento de voz"));
//		o.add(new Opcao(4, "Transferir"));
//		o.add(new Opcao(5, "Reproduzir audio"));
		
		List<Action> l = a.getL();
		@SuppressWarnings("unused")
		int size = l.size();
		
		Iterator<Action> i = l.iterator();
		
		Action x;
		
		//verifca se existem ações configuradas
		
		
		//verifica a primeira acao
		if(i.hasNext()){
			x = i.next();
			if(x.getTipo() != 0){
				return "Erro[Linha: "+(l.indexOf(x)+1)+"]: a primeira ação deve ser 'Atender'";
			}
		} else {
			return "Erro: não há ações configuradas";
		}
		
		//percorre toda a lista
		while(i.hasNext()){
			x = i.next();
		}
		
		
		//verifica a última açao
		if(x.getTipo()!=1){
			return "Erro[Linha: "+(l.indexOf(x)+1)+"]: a última ação deve ser 'Desligar'";
		}
		
		
		
		//não encontramos erros
		return "Analise OK: não foram encontrados erros";
		
	}
	
	

}
