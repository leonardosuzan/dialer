package dialer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

@Controller
@SessionAttributes("campaign")
public class FileUploadController {

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String provideUploadInfo(Model model, SessionStatus status,
			@ModelAttribute("campaign") Campaign campaign) {
		model.addAttribute("status",
				"You can upload a file by posting to this same URL.");
		model.addAttribute("campaign", campaign);
		return "importList";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String handleFileUpload(@RequestParam("fileName") String fileName,
			@RequestParam("file") MultipartFile file, Model model,
			SessionStatus status, @ModelAttribute("campaign") Campaign campaign)
			throws Exception {

		model.addAttribute("campaign", campaign);

		if (!file.isEmpty()) {
			try {
				String ext = FilenameUtils.getExtension(file
						.getOriginalFilename());
				if (!ext.contentEquals("csv")) {
					model.addAttribute("status",
							"O upload falhou: seu arquivo deve estar no formato CSV. Formato recebido: "
									+ ext + ". Tente novamente.");
					return "importList";
				}

				if (!file.getContentType().contentEquals("text/csv")) {
					model.addAttribute("status",
							"O upload falhou: seu arquivo deve estar no formato CSV. Formato recebido: "
									+ file.getContentType()
									+ ". Tente novamente.");
					return "importList";
				}

				System.out.print(file.getContentType());

				byte[] bytes = file.getBytes();
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(new File(fileName)));
				stream.write(bytes);
				stream.close();
				model.addAttribute("status", "Arquivo "
						+ fileName + "carregado com sucesso!");

				ArrayList<Contato> temp = loadData(fileName);
				
				model.addAttribute("contatos", temp);
				
				//grava nome do arquivo na sessão
				model.addAttribute("fileName", fileName);

				return "showCSV";

			} catch (Exception e) {
				// throw e;
				e.printStackTrace();
				model.addAttribute("status", "Falha ao carregar arquivo " + fileName
						+ " => " + e.getMessage());

				return "importList";
			}
		} else {
			model.addAttribute("status", "Falha ao carrregar arquivo " + fileName
					+ ". O arquivo está vazio.");
			return "importList";
		}
	}

	private ArrayList<Contato> loadData(String fileName) throws UnexpectedInputException,
			ParseException, Exception {
		

		
		// Começa leitura do arquivo
		FlatFileItemReader<Contato> itemReader = new FlatFileItemReader<Contato>();
		itemReader.setResource(new FileSystemResource(fileName));
		// DelimitedLineTokenizer defaults to comma as its delimiter
		LineMapper<Contato> lineMapper = new DefaultLineMapper<Contato>();
		((DefaultLineMapper<Contato>) lineMapper)
				.setLineTokenizer(new DelimitedLineTokenizer());
		((DefaultLineMapper<Contato>) lineMapper)
				.setFieldSetMapper(new ContatoFieldSetMapper());
		itemReader.setLineMapper(lineMapper);
		itemReader.open(new ExecutionContext());

		ArrayList<Contato> temp = new ArrayList<Contato>();
		
		
		//carrega os 3 primeiros valores a um array para mostrar na página temporária.
		for(int i = 0; i<=3; i++){
			Contato justRead = itemReader.read();
			
			if(justRead != null){
				temp.add(justRead);
			}
			
			
		}
		
		return temp;

	}

}
