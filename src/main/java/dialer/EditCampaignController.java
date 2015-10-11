package dialer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
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
	public String editCampaign(Model model, SessionStatus status,
			@RequestParam(value = "id", required = false, defaultValue = "0") String id) {

		Long i_id;

		try {
			i_id = Long.valueOf(id);
		} catch (NumberFormatException e) {
			Log.error("[editCampaign] Impossível converter String para Integer: " + id);
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
	public String writeCampaign(Model model, SessionStatus status,
			@RequestParam(value = "id", required = false, defaultValue = "0") String id,
			@ModelAttribute("campanha") Campaign campanha, @ModelAttribute("listings") List<Listing> l) {

		Log.info("Campanha alterada: " + campanha);

		campaignDAO.save(campanha);

		model.addAttribute("save", true);

		return "editCampaign";

	}

	@RequestMapping(value = "/editCampaignActions", method = RequestMethod.POST)
	public String editCampaignActions(Model model, SessionStatus status, @ModelAttribute("campanha") Campaign campanha,
			@ModelAttribute("listings") List<Listing> l, @ModelAttribute("actions") Actions a) {

		Log.info("Acoes alteradas: " + a);

		String r = analisar(a);
		@SuppressWarnings("unused")
		String c = null;
		Log.info("Resultado da analise:" + r);

		if (r == "Analise OK: não foram encontrados erros") {

			String v = analisarVariaveis(a);

			if (v == "") {
				try {
					c = gerarCodigo(campanha);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				model.addAttribute("analise", r);

			} else {
				model.addAttribute("analise", v);
			}
		} else {
			model.addAttribute("analise", r);
		}

		campanha.setActions(a);

		campaignDAO.save(campanha);

		model.addAttribute("save", true);

		return "editCampaign";

	}

	@RequestMapping(value = "/editCampaignActions", method = RequestMethod.POST, params = { "addRow" })
	public String addRow(Model model, SessionStatus status, @ModelAttribute("campanha") Campaign campanha,
			@ModelAttribute("listings") List<Listing> l, @ModelAttribute("actions") Actions a) {

		Log.info("Acoes alteradas: " + a);

		Action r = new Action();
		a.addAction(r);

		campanha.setActions(a);

		campaignDAO.save(campanha);

		model.addAttribute("save", true);

		return "editCampaign";

	}

	@RequestMapping(value = "/editCampaignActions", method = RequestMethod.POST, params = { "deleteRow" })
	public String deleteRow(Model model, SessionStatus status, @ModelAttribute("campanha") Campaign campanha,
			@ModelAttribute("listings") List<Listing> l, @ModelAttribute("actions") Actions a,
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
	public String callTest(Model model, SessionStatus status, @ModelAttribute("campanha") Campaign campanha,
			@ModelAttribute("listings") List<Listing> l, @ModelAttribute("actions") Actions a,
			@RequestParam("testNumber") String n) {

		Log.info("Iniciando chamada de teste para o número: " + n);

		CallerHelper t = new CallerHelper();

		String rReload = t.reload();

		if (rReload != null) {
			model.addAttribute("callTestResult", rReload);
			return "editCampaign";
		}

		String r;
		try {
			r = t.run(n, campanha.getIdCampanha().toString(), campanha, new Long(-1));
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

	private String analisarVariaveis(Actions a) {

		List<Action> l = a.getL();

		Iterator<Action> i = l.iterator();

		Action x = null; // Acao atual
		Action y = null; // Acao anterior em relacao a atual

		Integer count = 0;

		while (i.hasNext()) {
			count++;
			y = x;
			x = i.next();

			switch (x.getTipo()) {

			case 0: // atender
				if (x.getVar() != "") {
					Log.error(x.getVar());
					return "Erro[Linha: " + count + "]: A ação 'atender' não recebe argumentos";
				}
				break;

			case 1: // desligar
				if (x.getVar() != "") {
					return "Erro[Linha: " + count + "]: A ação 'desligar' não recebe argumentos";
				}
				break;

			case 2: // coletar resposta
				if (x.getVar() == "") {
					return "Erro[Linha: " + count
							+ "]: A ação 'Coletar Respostas' deve receber as respostas esperadas, separadas por vírgula";
				}

				// remove todos os espaços
				x.setVar(x.getVar().replaceAll("\\s+", ""));

				StringTokenizer st = new StringTokenizer(x.getVar(), ",");
				Integer countVar = 0;

				while (st.hasMoreTokens()) {
					countVar++;
					String g = st.nextToken();
					if (isNumeric(g) == false) {
						return "Erro[Linha: " + count + "]: O " + countVar
								+ "º argumento da ação 'Coletar Respostas' deve ser um valor numérico entre 1 a 9";
					} else {
						Integer d = whichNumber(g);

						if (d > 9 || d < 0) {
							return "Erro[Linha: " + count + "]: O " + countVar
									+ "º argumento da ação 'Coletar Respostas' deve ser um valor numérico entre 1 a 9";
						}
					}
				}

				break;
			case 3: // coletar resposta com reconhecimento de voz

				if (x.getVar() == "") {
					return "Erro[Linha: " + count
							+ "]: A ação 'coletar resposta com reconhecimento de voz' deve receber as respostas esperadas, separadas por vírgula";
				}

				// remove mais de um espaço conssecutivo e converte para minúsculas
				x.setVar(x.getVar().replaceAll("\\s+", "").trim().toLowerCase(Locale.forLanguageTag("pt-BR")));

				StringTokenizer st3 = new StringTokenizer(x.getVar(), ",");
				Integer countVar3 = 0;

				while (st3.hasMoreTokens()) {
					countVar3++;
					String g = st3.nextToken();
					if (StringUtils.isAlphanumeric(g) == false) {
						return "Erro[Linha: " + count + "]: O " + countVar3
								+ "º argumento da ação 'coletar resposta com reconhecimento de voz' contém caracteres inválidos";
					}
				}

				break;

			case 4: // transferir
				
				if (x.getVar() == "") {
					return "Erro[Linha: " + count
							+ "]: A ação 'transferir' deve receber um número de telefone válido";
				}

				// remove os espaços do início e do fim do texto
				x.setVar(x.getVar().trim());
				
				//verifica se é um número válido
				if(isNumeric(x.getVar()) == false){
					return "Erro[Linha: " + count
							+ "]: A ação 'transferir' deve receber um número de telefone válido";
				}
				break;

			case 5: // transferir se
				
				if (x.getVar() == "") {
					return "Erro[Linha: " + count
							+ "]: A ação 'Transferir Se' deve receber dois argumentos separados por vírgula. Primeiro a resposta esperada, segundo o número telefonico de destino";
				}

				// remove todos os espaços e converte para letras minúsculas
				x.setVar(x.getVar().replaceAll("\\s+", "").toLowerCase(Locale.forLanguageTag("pt-BR")));
				

				StringTokenizer st5 = new StringTokenizer(x.getVar(), ",");
				Integer countVar5 = 0;
				
				
				while (st5.hasMoreTokens()) {
					countVar5++;
					String g = st5.nextToken();
					
					if(countVar5 == 1){
						//trata o primeiro argumento
						if(y.getTipo() == 2){
							//trata um 'transferir se' sucedendo um 'coletar resposta'
							
						} else if (y.getTipo() == 3) {
							//trata um 'transferir se' sucedendo um 'coletar resposta com reconhecimento de voz'
							
						} else {
							//trata um 'transferir se' sucedendo uma ação que não deveria suceder
							return "Erro[Linha: " + (l.indexOf(x) + 1)
									+ "]: A ação 'TransferirSe' deve sucesseder uma ação do tipo 'coletar resposta'";
						}
					}
					
					else if(countVar5 == 2){
						//trata o segundo argumento 
						if(isNumeric(g) == false){
							return "Erro[Linha: " + count + "]: O " + countVar5
									+ "º argumento da ação 'Transferir Se' deve ser um número telefonico válido";
						}
					}
					
					else {
						//caso default para mais de 2 argumentos
						return "Erro[Linha: " + count
								+ "]: A ação 'Transferir Se' deve receber apenas dois argumentos separados por vírgula. Primeiro a resposta esperada, segundo o número telefonico de destino";
					}
				}
				

				break;

			case 6: // reproduzir audio
				
				if(x.getVar() == "" || x.getVar() == null){
					return "Erro[Linha: " + count
							+ "]: Para ação 'reproduzir audio' você deve selecionar um audio cadastrado";
				}
				break;

			case 7: // renderizar voz
				if (x.getVar() == "") {
					return "Erro[Linha: " + count
							+ "]: A ação 'renderizar voz' deve receber uma sentença para renderizar";
				}

				break;

			}

		}

		return "";

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
				return "Erro[Linha: " + (l.indexOf(x) + 1) + "]: a primeira ação deve ser 'Atender'";
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
					return "Erro[Linha: " + (l.indexOf(x) + 1)
							+ "]: A ação 'TransferirSe' deve sucesseder uma ação do tipo 'coletar resposta'";
				}

			}

			if (x.getTipo() == 1 && i.hasNext() == true) {
				return "Erro[Linha: " + (l.indexOf(x) + 1) + "]: Ação 'Desligar' deve ser a última ação da chamada";
			}

			if (x.getTipo() == 0) {
				return "Erro[Linha: " + (l.indexOf(x) + 1) + "]: Ação 'Atender' deve ser a primeira ação da chamada";
			}
		}

		// verifica a última açao
		if (x.getTipo() != 1) {
			return "Erro[Linha: " + (l.indexOf(x) + 1) + "]: a última ação deve ser 'Desligar'";
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
		// escreve o id da campanha como titulo do context
		writer.write("\n\n[" + c.getIdCampanha() + "]\n");

		Integer count = new Integer(0);
		Integer resp = new Integer(0);
		Integer transf = new Integer(0);

		// percorre toda a lista, sendo x acáo atual e y açao anterior
		while (i.hasNext()) {
			count++;
			y = x;
			x = i.next();

			switch (x.getTipo()) {

			case 0: // atender
				writer.write("exten => s,1,Set(ODBC_DIALER_CALLTRY(${idContato},${idCampanha})=1)\n"
						+"exten => s,n,Answer()\n"
						+"exten => s,n,Set(ODBC_DIALER_ANS(${idContato},${idCampanha})=1)\n"
						+ "exten => s,n,NoOp(${dialingNumber})\n" + "exten => s,n,Wait(1)\n");
				break;

			case 1: // desligar
				writer.write("exten => s,n(desligar),Hangup()\n");
				break;

			case 2: // coletar resposta
				resp++;
				writer.write("exten => s,n,Read(aux" + count + ",,1,n,1,10)\n"
				+"exten => s,n,Set(ODBC_DIALER_SETR(${idContato},${idCampanha})=resposta"+resp+",${aux"+count+"})\n");
				break;
			case 3: // coletar resposta com reconhecimento de voz
				resp++;

				StringTokenizer st = new StringTokenizer(x.getVar(), ",");

				writer.write("exten => s,n,Set(c=3)\n" + "exten => s,n,GoTo(inicio" + count + ")\n"
						+ "exten => s,n(n_entendi" + count + "),Playback(custom/pm-invalid-option)\n"
						+ "exten => s,n,Set(c=$[${c} - 1])\n" + "exten => s,n,GotoIf($[\"${c}\" = \"0\"]?desligar)\n"
						+ "exten => s,n(inicio" + count + "),agi(speech-recog.agi, pt-BR)\n"
						+ "exten => s,n,NoOP(Detectado = ${utterance})\n"
						+ "exten => s,n,NoOP(Confiabilidade = ${confidence})\n");

				while (st.hasMoreTokens()) {
					writer.write("exten => s,n,GotoIf($[\"${TOLOWER(${utterance})}\" = \"" + st.nextToken() + "\"]?entendi" + count
							+ ")\n");
				}

				writer.write("exten => s,n,GoTo(n_entendi" + count + ")\n" + "exten => s,n(entendi" + count
						+ "),NoOP(Resposta = ${utterance})\n"
						+"exten => s,n,Set(ODBC_DIALER_SETR(${idContato},${idCampanha})=resposta"+resp+",${aux"+count+"})\n"
						+ "exten => s,n,Set(aux" + count + "=${utterance})\n");

				break;

			case 4: // transferir
				transf++;
				writer.write("exten => s,n,Set(ODBC_DIALER_SETR(${idContato},${idCampanha})=transferiu"+transf+",1)\n"
						+"exten => s,n,Dial(SIP/minutostelecom/" + x.getVar() + ",20,,)\n");
				break;

			case 5: // transferir se
				transf++;
				
				StringTokenizer st5 = new StringTokenizer(x.getVar(), ",");

				writer.write("exten => s,n,GotoIf($[\"${aux" + (count - 1) + "}\" = \"" + st5.nextToken() + "\"]?transf"
						+ count + ")\n" + "exten => s,n,Goto(n_transf" + count + ")\n" + "exten => s,n(transf" + count
						+ "),NoOp(\"Transferindo!\"))\n"
						+ "exten => s,n,Set(ODBC_DIALER_SETR(${idContato},${idCampanha})=transferiu"+transf+",1)\n"
						+ "exten => s,n,Dial(SIP/minutostelecom/" + st5.nextToken() + ",20,,)\n"
						+ "exten => s,n(n_transf" + count + "),NoOp(\"Não transferiu\"))\n");
				break;

			case 6: // reproduzir audio
				Audio t = audioDAO.findOne(Long.parseLong(x.getVar()));
				writer.write("exten => s,n,Playback(custom/" + t.getNome() + ")\n");
				break;

			case 7: // renderizar voz
				writer.write("exten => s,n,agi(googletts.agi,\"" + x.getVar() + "\",pt-BR)\n");
				break;

			}

		}

		writer.flush();
		writer.close();

		return null;
	}

	private static boolean isNumeric(String str) {
		try {
			@SuppressWarnings("unused")
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	private static Integer whichNumber(String str) {
		Integer d;
		try {
			d = Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			return -1;
		}
		return d;
	}
}
