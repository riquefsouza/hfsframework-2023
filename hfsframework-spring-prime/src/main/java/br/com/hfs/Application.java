package br.com.hfs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootApplication
//@EnableSpringDataWebSupport
//@EnableCaching
//@EnableAutoConfiguration(exclude = LiquibaseAutoConfiguration.class)
public class Application {

        private static final Logger log = LoggerFactory.getLogger(Application.class);

        public static void main(String[] args) {
                log.info("[step:to-be-init] [id:1] Inicializando o Spring");
                System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "development");
                SpringApplication.run(Application.class, args);
                log.info("[step:inittialized] [id:2] Spring inicializado..");
/*
                log.info("### VARIAVEIS DE AMBIENTE ####");
                log.info("SPRING_PROFILES_ACTIVE: " + System.getenv("SPRING_PROFILES_ACTIVE"));

                log.info("DATABASE_POSTGRESQL_USERNAME: " + System.getenv("DATABASE_POSTGRESQL_USERNAME"));
                log.info("DATABASE_POSTGRESQL_URL: " + System.getenv("DATABASE_POSTGRESQL_URL"));

                log.info("KEYCLOAK_REALM: " + System.getenv("KEYCLOAK_REALM"));
                log.info("KEYCLOAK_HOST: " + System.getenv("KEYCLOAK_HOST"));
*/
        }

    	@Bean
    	public MessageSource messageSource() {
    		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    		messageSource.setBasename("messages");
    		messageSource.setDefaultEncoding("UTF-8");
    		return messageSource;
    	}
        
}