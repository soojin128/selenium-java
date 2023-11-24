package kr.co.selenium;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableCaching
@EnableBatchProcessing
@SpringBootApplication(scanBasePackages= {"kr.co.selenium"})
public class SeleniumTestApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(SeleniumTestApplication.class, args);

		int exitCode = SpringApplication.exit(applicationContext);

		log.info("exitCode : " + exitCode);
		
		if (exitCode != 0) {
			exitCode = 1;
		} 

		System.exit(exitCode);
	}

}
