package com.danzir.scambio.figurine.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
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

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
