package br.com.hfs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.faces.event.PostConstructApplicationEvent;

@ApplicationScoped
public class Application {

	private static final Logger log = LogManager.getLogger(Application.class);

	public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
		log.info("Init " + this.getClass().getName());
	}
	
	public void postConstruct(@Observes PostConstructApplicationEvent event){ 
		log.info("PostConstruct " + this.getClass().getName());
	}
	
	public void destroy(@Observes @Destroyed(ApplicationScoped.class) Object init) {
		log.info("Destroy " + this.getClass().getName());
	}
	
	
}
