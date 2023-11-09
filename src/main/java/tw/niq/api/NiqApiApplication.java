package tw.niq.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class NiqApiApplication {

	public static ApplicationContext CTX;
	
	public static void main(String[] args) {
		CTX = SpringApplication.run(NiqApiApplication.class, args);
	}

}
