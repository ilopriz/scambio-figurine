package com.danzir.scambio.figurine.scrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@PropertySources({
		@PropertySource("classpath:scambio-figurine-data.properties")
})
@ComponentScan("com.danzir")
@EnableJpaRepositories("com.danzir.scambio.figurine.data")
@EntityScan("com.danzir.scambio.figurine.data")
public class Application {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		long time = System.currentTimeMillis();
		ApplicationContext context = SpringApplication.run(Application.class, args);
		time = System.currentTimeMillis() - time;
		logger.trace("Runtime: {} seconds.", ((double)time/1000));
		SpringApplication.exit(context);
	}

}
