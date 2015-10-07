package dialer;

import org.crsh.console.jline.internal.Log;
import org.springframework.beans.factory.annotation.Value;

import it.sauronsoftware.jave.FFMPEGLocator;

public class ffmpegLocator extends FFMPEGLocator {
	
	@Value("${directory.ffmpeg}")
	String dir;

	@Override
	protected String getFFMPEGExecutablePath() {
		Log.info("Localização do FFMPEG: " + dir);
		return dir;
	}
	

}
