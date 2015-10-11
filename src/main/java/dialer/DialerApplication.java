package dialer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DialerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DialerApplication.class, args);
    }
}


//@Configuration
//@EnableAutoConfiguration
//@ComponentScan
//public class DialerApplication extends SpringBootServletInitializer {
//
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(DialerApplication.class);
//    }
//}