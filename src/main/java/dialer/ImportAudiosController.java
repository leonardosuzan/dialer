package dialer;
  
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
 
@Controller
public class ImportAudiosController {
	
	@Autowired
	AudioDAO audioDAO;
 
    @RequestMapping(value = "/newAudios", method = RequestMethod.GET)
    public String crunchifyDisplayForm() {
        return "uploadAudios";
    }
 
    @RequestMapping(value = "/saveAudioFiles", method = RequestMethod.POST)
    public String crunchifySave(
            @ModelAttribute("uploadForm") CrunchifyFileUpload uploadForm,
            Model map) throws IllegalStateException, IOException {
        String saveDirectory = "./";
 
        List<MultipartFile> crunchifyFiles = uploadForm.getFiles();
 
        List<String> fileNames = new ArrayList<String>();
 
        if (null != crunchifyFiles && crunchifyFiles.size() > 0) {
            for (MultipartFile multipartFile : crunchifyFiles) {
 
                String fileName = multipartFile.getOriginalFilename();
                if (!"".equalsIgnoreCase(fileName)) {
                    // Handle file content - multipartFile.getInputStream()
                    multipartFile.transferTo(new File(saveDirectory + fileName));
                    fileNames.add(fileName);
                    
                    Audio a = new Audio();
                    
                    a.setDir(saveDirectory + fileName);
                    a.setNome(fileName);
                    
                    audioDAO.save(a);
                    
                    map.addAttribute("save", true);
                    
                    
                    
                }
            }
        }
 
        map.addAttribute("files", fileNames);
        return "uploadAudios";
    }
}