package dialer;
  

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.multipart.MultipartFile;




@Controller
@ComponentScan(basePackages = { "dialer.*" })
@PropertySource("classpath:application.properties")
public class ImportAudiosController {
	
	@Autowired
	AudioDAO audioDAO;
	
    
	@Value("${directory.audios}")
	String saveDirectory;
	
	@Value("${directory.ffmpeg}")
	String ffmpeg;
 
    @RequestMapping(value = "/newAudios", method = RequestMethod.GET)
    public String crunchifyDisplayForm() {
    	Log.info(saveDirectory);
        return "uploadAudios";
    }
 
    @RequestMapping(value = "/saveAudioFiles", method = RequestMethod.POST)
    public String crunchifySave(
            @ModelAttribute("uploadForm") CrunchifyFileUpload uploadForm,
            Model map) throws IllegalStateException, IOException {

//    	= "custom/";
 
        List<MultipartFile> crunchifyFiles = uploadForm.getFiles();
 
        List<String> fileNames = new ArrayList<String>();
 
        if (null != crunchifyFiles && crunchifyFiles.size() > 0) {
            for (MultipartFile multipartFile : crunchifyFiles) {
 
                String fileName = multipartFile.getOriginalFilename();
                if (!"".equalsIgnoreCase(fileName)) {
                    // Handle file content - multipartFile.getInputStream()
                	File uploadedFile = new File(saveDirectory + fileName);
                    multipartFile.transferTo(uploadedFile);
                    fileNames.add(fileName);
                    
                    List<String> encoder = new ArrayList<String>();
                    encoder.add(ffmpeg);
                    encoder.add("-i");
                    encoder.add(uploadedFile.getAbsolutePath());
                    encoder.add("-ac");
                    encoder.add("1");
                    encoder.add("-ar");
                    encoder.add("8000");
                    encoder.add("-f");
                    encoder.add("alaw");
                    encoder.add("-y");
                    encoder.add(uploadedFile.getAbsolutePath()+".alaw");
                    
                                      
                   Log.info("lista ffmpeg: "+encoder);
                    
                    ProcessBuilder pb = new ProcessBuilder(encoder);
                    pb.redirectOutput(Redirect.INHERIT);
                    pb.redirectError(Redirect.INHERIT);
                    Process p = pb.start();
                    try {
						p.waitFor();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    
                    
                    File alaw = new File(uploadedFile.getAbsolutePath()+".alaw");
                    
                    if(alaw.exists() && alaw.length() > 0){
                    	Log.info("Arquivo convertido com sucesso");
                    } else{
                    	Log.info("Erro ao converter arquivo. É um arquivo de audio?");
                    	alaw.delete();
                    }
                    
                    Audio a = new Audio();
                    
                    a.setDir(saveDirectory + fileName+".alaw");
                    a.setNome(fileName);
                    
                    audioDAO.save(a);
                    
                    //apaga o arquivo temporario
                    uploadedFile.delete();
                    
                    map.addAttribute("save", true);
                    
                    
                    
                }
            }
        }
 
        map.addAttribute("files", fileNames);
        return "uploadAudios";
    }
}