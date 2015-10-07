package dialer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.TimeoutException;
import org.crsh.console.jline.internal.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@ComponentScan(basePackages = { "dialer.*" })
@PropertySource("classpath:application.properties")
@SessionAttributes({ "campanha", "listings", "actions", "audios" })
public class EditCampaignController {

	@Autowired
	CampaignDAO campaignDAO;

	@Autowired
	ListingDAO listingDAO;
	
	@Autowired
	AudioDAO audioDAO;
	
	@Value("${directory.extension}")
	String extensionConf;

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

		if (c.getActions() == null) {
			a = new Actions();
			Action r = new Action();
			a.addAction(r);
			c.setActions(a);
		} else {
			a = c.getActions();
		}
		
		Audios s = new Audios(audioDAO.findAll());

		model.addAttribute("actions", a);
		model.addAttribute("campanha", c);
		model.addAttribute("listings", l);
		model.addAttribute("audios", s);
		
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
	public String editCampaignActions(Model model, SessionStatus status,
			@ModelAttribute("campanha") Campaign campanha,
			@ModelAttribute("listings") List<Listing> l,
			@ModelAttribute("actions") Actions a) {

		Log.info("Acoes alteradas: " + a);

		String r = analisar(a);
		@SuppressWarnings("unused")
		String c = null;
		Log.info("Resultado da analise:" + r);
		
		
		if (r == "Analise OK: não foram encontrados erros") {
			try {
				c = gerarCodigo(campanha);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		model.addAttribute("analise", r);

		campanha.setActions(a);

		campaignDAO.save(campanha);

		model.addAttribute("save", true);

		return "editCampaign";

	}

	@RequestMapping(value = "/editCampaignActions", method = RequestMethod.POST, params = { "addRow" })
	public String addRow(Model model, SessionStatus status,
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

	@RequestMapping(value = "/editCampaignActions", method = RequestMethod.POST, params = { "deleteRow" })
	public String deleteRow(Model model, SessionStatus status,
			@ModelAttribute("campanha") Campaign campanha,
			@ModelAttribute("listings") List<Listing> l,
			@ModelAttribute("actions") Actions a,
			@RequestParam("deleteRow") int index) {

		Log.info("Deletando acao: " + index);

		a.deleteAcion(index);

		Log.info("Acoes alteradas: " + a);

		campanha.setActions(a);

		campaignDAO.save(campanha);

		model.addAttribute("save", true);

		return "editCampaign";

	}

	@RequestMapping(value = "/campaignCallTest", method = RequestMethod.POST, params = { "testNumber" })
	public String callTest(Model model, SessionStatus status,
			@ModelAttribute("campanha") Campaign campanha,
			@ModelAttribute("listings") List<Listing> l,
			@ModelAttribute("actions") Actions a,
			@RequestParam("testNumber") String n) {

		Log.info("Iniciando chamada de teste para o número: " + n);

		CallerHelper t = new CallerHelper();
		
		String rReload = t.reload();
		
		if(rReload != null){
			model.addAttribute("callTestResult", rReload);
			return "editCampaign";
		}

		String r;
		try {
			r = t.run(n,campanha.getIdCampanha().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			r = e.toString();
			e.printStackTrace();
		} catch (AuthenticationFailedException e) {
			// TODO Auto-generated catch block
			r = e.toString();

			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			r = e.toString();
			e.printStackTrace();
		}

		model.addAttribute("callTestResult", r);

		return "editCampaign";

	}

	private String analisar(Actions a) {
		if (a == null) {
			return "Ocorreu um erro com a analise [a==null]";

		}

		// As açoes configuradas:
		// o.add(new Opcao(0, "Atender"));
		// o.add(new Opcao(1, "Desligar"));
		// o.add(new Opcao(2, "Coletar resposta"));
		// o.add(new Opcao(3, "Coletar resposta com reconhecimento de voz"));
		// o.add(new Opcao(4, "Transferir"));
		// o.add(new Opcao(5, "Reproduzir audio"));

		List<Action> l = a.getL();
		@SuppressWarnings("unused")
		int size = l.size();

		Iterator<Action> i = l.iterator();

		Action x = null; // Acao atual
		Action y = null; // Acao anterior em relacao a atual

		// verifca se existem ações configuradas

		// verifica a primeira acao
		if (i.hasNext()) {
			x = i.next();
			if (x.getTipo() != 0) {
				return "Erro[Linha: " + (l.indexOf(x) + 1)
						+ "]: a primeira ação deve ser 'Atender'";
			}
		} else {
			return "Erro: não há ações configuradas";
		}

		// percorre toda a lista
		while (i.hasNext()) {
			y = x;
			x = i.next();

			if (x.getTipo() == 5) {
				if (y == null || (y.getTipo() != 2 && y.getTipo() != 3)) {
					Log.info(y);
					return "Erro[Linha: "
							+ (l.indexOf(x) + 1)
							+ "]: A ação 'TransferirSe' deve sucesseder uma ação do tipo 'coletar resposta'";
				}
			}

			if (x.getTipo() == 1 && i.hasNext() == true) {
				return "Erro[Linha: "
						+ (l.indexOf(x) + 1)
						+ "]: Ação 'Desligar' deve ser a última ação da chamada";
			}

			if (x.getTipo() == 0) {
				return "Erro[Linha: "
						+ (l.indexOf(x) + 1)
						+ "]: Ação 'Atender' deve ser a primeira ação da chamada";
			}
		}

		// verifica a última açao
		if (x.getTipo() != 1) {
			return "Erro[Linha: " + (l.indexOf(x) + 1)
					+ "]: a última ação deve ser 'Desligar'";
		}

		// não encontramos erros
		return "Analise OK: não foram encontrados erros";

	}

	private String gerarCodigo(Campaign c) throws IOException {

		Actions a = c.getActions();
		List<Action> l = a.getL();

		Iterator<Action> i = l.iterator();

		Action x = null; // Acao atual
		@SuppressWarnings("unused")
		Action y = null; // Acao anterior em relacao a atual

		File file = new File(extensionConf);
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		FileWriter writer = new FileWriter(file);

		// comeca a geração de código
	    //escreve o id da campanha como titulo do context 
		writer.write("\n\n[" + c.getIdCampanha() + "]\n");
		
		// percorre toda a lista, sendo x acáo atual e y açao anterior
		while (i.hasNext()) {
			y = x;
			x = i.next();

			switch (x.getTipo()) {

			case 0: // atender
				writer.write("exten => s,1,Answer()\n"
//						+ "exten => s,n,System(\"MYSQL\")\n"
						+ "exten => s,n,NoOp(${dialingNumber})\n"
						+ "exten => s,n,Wait(1)\n");
				break;

			case 1: //desligar
				writer.write("exten => s,n(desligar),Hangup()\n");
				break;

			case 2: // coletar resposta
				writer.write("exten => s,n,Read(auxX,,1,n,1,10)\n"
						+ "exten => s,n,System(\"MYSQL\")\n");
				break;
			case 3: // coletar resposta com reconhecimento de voz
				writer.write("exten => s,n,Set(c=3)\n"
						+ "exten => s,n,GoTo(inicio)\n"
						+ "exten => s,n(n_entendi),Playback(custom/n_entendi)\n"
						+ "exten => s,n,Set(c=$[${c} - 1])\n"
						+ "exten => s,n,GotoIf($[\"${c}\" = \"0\"]?desligar)\n"
						+ "exten => s,n(inicio),EAGI(pahh.py)\n"
						+ "exten => s,n,GotoIf($[\"${GoogleUtterance}\" = \"<STRING_DESEJADA>\"]?entendi)\n"
						+ "exten => s,n,GotoIf($[\"${GoogleUtterance}\" = \"<STRING_DESEJADA>\"]?entendi)\n"
						+ "(...)\n"
						+ "exten => s,n,GotoIf($[\"${GoogleUtterance}\" = \"<STRING_DESEJADA>\"]?entendi)\n"
						+ "exten => s,n,GoTo(n_entendi)\n"
						+ "exten => s,n(entendi),NoOP(Resposta = ${GoogleUtterance})\n"
//						+ "exten => s,n,System(MYSQL_QUERY)\n"
						+ "exten => s,n,Set(auxX = ${GoogleUtterance})\n");
				break;

			case 4: // transferir
				writer.write("exten => s,n,System(\"MYSQL\")\n"
						+ "exten => s,n,GoTo(from-internal,<NUMERO_PARA_TRANSFERIR>,1)\n");
				break;

			case 5: // transferir se
				writer.write("exten => s,n,GotoIf($[\"${auxX}\" = \"<RESULTADO_ESPERADO>\"]?transf)\n"
						+ "exten => s,n,Goto(n_transf)\n"
						+ "exten => s,n(transf),NoOp(\"Transferindo!\"))\n"
						+ "exten => s,n,System(MYSQL_QUERY)\n"
						+ "exten => s,n,GoTo(from-internal,<NUMERO_DESTINO>,1)\n"
						+ "exten => s,n(n_transf),NoOp(\"Não transferiu\"))\n"
						+ "exten => s,n,Hangup()\n");
				break;

			case 6: // reproduzir audio
				Audio t = audioDAO.findOne(Long.parseLong(x.getVar()));
				writer.write("exten => s,n,Playback(custom/"+t.getNome()+")\n");
				break;

			}

		}
		
		writer.flush(); 
		writer.close();

		return null;
	}
}
