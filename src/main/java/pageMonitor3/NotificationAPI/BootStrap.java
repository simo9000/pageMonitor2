package pageMonitor3.NotificationAPI;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@ComponentScan
@EnableAutoConfiguration
public class BootStrap {

	public static void main(String[] args) {
		SpringApplication.run(pageMonitor3.NotificationAPI.BootStrap.class, args);
	}

}
