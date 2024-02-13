package br.com.hfs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HefestoSpringGraphqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(HefestoSpringGraphqlApplication.class, args);
	}
/*
	@SuppressWarnings("deprecation")
	@Bean
	public GraphQLScalarType longType() {
	    return Scalars.GraphQLLong;
	}
*/	
}
