package dialer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.io.FilenameUtils;
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
	
	
	@RequestMapping(value="/upload", method=RequestMethod.GET)
    public String provideUploadInfo(
    		Model model, SessionStatus status, @ModelAttribute("campaign") Campaign campaign){
		model.addAttribute("status","You can upload a file by posting to this same URL.");
		model.addAttribute("campaign", campaign);
        return "importList";
    }

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public String handleFileUpload(@RequestParam("fileName") String fileName,
            @RequestParam("file") MultipartFile file,
            Model model,
            SessionStatus status,
            @ModelAttribute("campaign") Campaign campaign) {
    	
    	model.addAttribute("campaign", campaign);
    	
        if (!file.isEmpty()) {
            try {
            	String ext = FilenameUtils.getExtension(file.getOriginalFilename());
            	if(!ext.contentEquals("csv")){
            		model.addAttribute("status","O upload falhou: seu arquivo deve estar no formato CSV. Formato recebido: " + ext + ". Tente novamente.");
            		return "importList";
            	}
            	
            	if(!file.getContentType().contentEquals("text/csv")){
            		model.addAttribute("status","O upload falhou: seu arquivo deve estar no formato CSV. Formato recebido: " + file.getContentType() + ". Tente novamente.");
            		return "importList";
            	}
            	
            	System.out.print(file.getContentType());
            	
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(fileName)));
                stream.write(bytes);
                stream.close();
                model.addAttribute("status","You successfully uploaded " + fileName + "!");
                return "showCSV";
            } catch (Exception e) {
            	model.addAttribute("status","You failed to upload " + fileName + " => " + e.getMessage());
                return "importList";
            }
        } else {
        	model.addAttribute("status","You failed to upload " + fileName + " because the file was empty.");
            return "importList";
        }
    }

}
