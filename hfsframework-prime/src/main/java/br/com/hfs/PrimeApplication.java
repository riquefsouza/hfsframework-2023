package br.com.hfs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.faces.event.PostConstructApplicationEvent;

@ApplicationScoped
public class PrimeApplication {

	private static final Logger log = LogManager.getLogger(PrimeApplication.class);
/*
	@Inject
	private AdmPageService pageService;
	
	@Inject
	private AdmParameterCategoryService parameterCategoryService;

	@Inject
	private AdmParameterService parameterService;
	
	@Inject
	private AdmProfileService profileService;
	
	@Inject
	private AdmUserService userService;
	
	@Inject
	private AdmMenuService menuService;
	
	@Inject	
	private AdmUserProfileService userProfileService;
	
	@Inject
	private AdmPageProfileService pageProfileService;
*/	
	public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
		log.info("Init " + this.getClass().getName());
/*		
		//============ PROFILE ================
		
		AdmProfile profile1 = profileService.insert(new AdmProfile("ADMIN", true, false));
		AdmProfile profile2 = profileService.insert(new AdmProfile("USER", false, true));
		
		Set<AdmProfile> profiles1 = new HashSet<AdmProfile>();
		profiles1.add(profile1);
		
		Set<AdmProfile> profiles2 = new HashSet<AdmProfile>();
		profiles2.add(profile2);
		
		//============ PAGE ================
		
		List<AdmPage> listaAdmPage = new ArrayList<AdmPage>();
		
		
		listaAdmPage.add(new AdmPage("admin/admParameterCategory/listAdmParameterCategory.xhtml", "Category of Configuration Parameters"));
		listaAdmPage.add(new AdmPage("admin/admParameterCategory/editAdmParameterCategory.xhtml", "Edit Category of Configuration Parameters"));
		listaAdmPage.add(new AdmPage("admin/admParameter/listAdmParameter.xhtml", "Configuration Parameters"));
		listaAdmPage.add(new AdmPage("admin/admParameter/editAdmParameter.xhtml", "Edit Configuration Parameters"));
		listaAdmPage.add(new AdmPage("admin/admProfile/listAdmProfile.xhtml", "Administer Profile"));
		listaAdmPage.add(new AdmPage("admin/admProfile/editAdmProfile.xhtml", "Edit Administer Profile"));
		listaAdmPage.add(new AdmPage("admin/admPage/listAdmPage.xhtml", "Administer Page"));
		listaAdmPage.add(new AdmPage("admin/admPage/editAdmPage.xhtml", "Edit Administer Page"));
		listaAdmPage.add(new AdmPage("admin/admMenu/listAdmMenu.xhtml", "Administer Menu"));
		listaAdmPage.add(new AdmPage("admin/admMenu/editAdmMenu.xhtml", "Edit Administer Menu"));
		listaAdmPage.add(new AdmPage("admin/admUser/listAdmUser.xhtml", "Administer User"));
		listaAdmPage.add(new AdmPage("admin/admUser/editAdmUser.xhtml", "Edit Administer User"));
		
		listaAdmPage.add(new AdmPage("/admin/admParameterCategory", "Category of Configuration Parameters"));
		listaAdmPage.add(new AdmPage("/admin/admParameterCategoryEdit", "Edit Category of Configuration Parameters"));
		listaAdmPage.add(new AdmPage("/admin/admParameter", "Configuration Parameters"));
		listaAdmPage.add(new AdmPage("/admin/admParameterEdit", "Edit Configuration Parameters"));
		listaAdmPage.add(new AdmPage("/admin/admProfile", "Administer Profile"));
		listaAdmPage.add(new AdmPage("/admin/admProfileEdit", "Edit Administer Profile"));
		listaAdmPage.add(new AdmPage("/admin/admPage", "Administer Page"));
		listaAdmPage.add(new AdmPage("/admin/admPageEdit", "Edit Administer Page"));
		listaAdmPage.add(new AdmPage("/admin/admMenu", "Administer Menu"));
		listaAdmPage.add(new AdmPage("/admin/admUser", "Administer User"));
		listaAdmPage.add(new AdmPage("/admin/admUserEdit", "Edit Administer User"));
		listaAdmPage.add(new AdmPage("/admin/changePasswordEdit", "Change Password"));
		
		pageService.insert(listaAdmPage);
		
		listaAdmPage.forEach(page1 -> {
			AdmPageProfile pageProfile = new AdmPageProfile(page1.getId(), profile1.getId());		
			pageProfileService.insert(pageProfile);			
		});
		
		//============ USER ================
		
		//senha = 123456
		AdmUser user0 = new AdmUser("admin", "$2a$10$nhU38YCtaWpLzTIeG/uAIeGnu7GItrvGsQAQrgsjM9hN19cGp25N6");
		user0.setName("Henrique");
		user0.setEmail("nao_responda@gmail.com");
				
		AdmUser user1 = userService.insert(user0);
		
		Set<AdmUser> users = new HashSet<AdmUser>();
		users.add(user1);

		AdmUserProfile userProfile = new AdmUserProfile(user1.getId(), profile1.getId());
		
		userProfileService.insert(userProfile);
		
		//============ PARAMETER CATEGORY ================
		
		List<AdmParameterCategory> listaAdmPC = new ArrayList<AdmParameterCategory>();
		
		listaAdmPC.add(new AdmParameterCategory("Court Parameters", 1L));
		listaAdmPC.add(new AdmParameterCategory("Login Parameters", 2L));
		listaAdmPC.add(new AdmParameterCategory("E-mail Parameters", 3L));
		listaAdmPC.add(new AdmParameterCategory("Network connection Parameters", 4L));
		listaAdmPC.add(new AdmParameterCategory("System Parameters", null));

		parameterCategoryService.insert(listaAdmPC);
		
		//============ PARAMETER ================
		
		List<AdmParameter> listaAdmParameter = new ArrayList<AdmParameter>();
		
		listaAdmParameter.add(new AdmParameter("Tribunal Regional do Trabalho da 1a. Região", "Nome do tribunal onde o sistema está instalado.", "NOME_TRIBUNAL", 1L));
		listaAdmParameter.add(new AdmParameter("TRT1", "Sigla do tribunal onde o sistema está instalado.", "SIGLA_TRIBUNAL", 1L));
		listaAdmParameter.add(new AdmParameter("080009", "Código númérico de 6 dígitos que identifica o órgão no SIAFI.", "CODIGO_UNIDADE_GESTORA", 1L));
		listaAdmParameter.add(new AdmParameter("102", "Código númérico de 3 dígitos da UG no código de barras da GRU.", "APELIDO_UNIDADE_GESTORA", 1L));
		listaAdmParameter.add(new AdmParameter("false", "Bloquear o sistema para que os usuários, exceto do administador, não façam login", "BLOQUEAR_LOGIN", 2L));
		listaAdmParameter.add(new AdmParameter("NOME_USUARIO", "Define o atributo usado para efetuar login no sistema. Este parâmetro pode ser preenchido com: NOME_USUARIO ou CPF", "ATRIBUTO_LOGIN", 2L));
		listaAdmParameter.add(new AdmParameter("smtp.trt1.jus.br", "Servidor SMTP para que o sistema envie e-mail.", "SMTP_SERVIDOR", 3L));
		listaAdmParameter.add(new AdmParameter("25", "Porta do servidor SMTP para que o sistema envie e-mail.", "SMTP_PORTA", 3L));
		listaAdmParameter.add(new AdmParameter(null, "Usuário para login no servidor SMTP.", "SMTP_USERNAME", 3L));
		listaAdmParameter.add(new AdmParameter(null, "Senha para login no servidor SMTP.", "SMTP_PASSWORD", 3L));
		listaAdmParameter.add(new AdmParameter("sistema@trt1.jus.br", "E-mail do sistema.", "SMTP_EMAIL_FROM", 3L));
		listaAdmParameter.add(new AdmParameter("bravo.trtrio.gov.br", "Servidor do Proxy.", "PROXY_SERVIDOR", 4L));
		listaAdmParameter.add(new AdmParameter("8080", "Porta do Proxy.", "PROXY_PORTA", 4L));
		listaAdmParameter.add(new AdmParameter("Produção", "Define o ambiente onde o sistema está instalado. Este parâmetro pode ser preenchido com: desenvolvimento, homologação ou produção", "AMBIENTE_SISTEMA", 2L));
		
		String json = "[ { \"ativo\": \"false\", \"login\" : \"rafael.remiro\", \"setor\" : \"ESACS RJ\", \"cargo\": \"15426\", \"loginVirtual\": \"\" }, "
				+ "{ \"ativo\": \"false\", \"login\" : \"fabricio.peres\", \"setor\" : \"CSEG\", \"cargo\": \"15426\", \"loginVirtual\": \"\" }, "
				+ "{ \"ativo\": \"false\", \"login\" : \"henrique.souza\", \"setor\" : \"SAM\", \"cargo\": \"15426\", \"loginVirtual\": \"\" }]";
		
		listaAdmParameter.add(new AdmParameter(json, 
				"Habilitar o modo de teste por login, esquema do json [  { \"ativo\": \"false\", \"login\" : \"fulano\", \"setor\" : \"DISAD\", \"cargo\": \"15426\" } ]", 
				"MODO_TESTE", 2L));
		
		parameterService.insert(listaAdmParameter);

		//============ MENU ================
		
		List<AdmMenu> listaAdmMenu = new ArrayList<AdmMenu>();
		
		listaAdmMenu.add(new AdmMenu("Administrative", null, null, 1));
		listaAdmMenu.add(new AdmMenu("Category of Configuration Parameters", 1L, 1L, 2));
		listaAdmMenu.add(new AdmMenu("Configuration Parameters", 1L, 3L, 3));
		listaAdmMenu.add(new AdmMenu("Administer Profile", 1L, 5L, 4));
		listaAdmMenu.add(new AdmMenu("Administer Page", 1L, 7L, 5));
		listaAdmMenu.add(new AdmMenu("Administer Menu", 1L, 9L, 6));
		listaAdmMenu.add(new AdmMenu("Administer User", 1L, 10L, 7));
		listaAdmMenu.add(new AdmMenu("Change Password", 1L, 12L, 8));
				
		menuService.insert(listaAdmMenu);
		
		*/
	}
	
	public void postConstruct(@Observes PostConstructApplicationEvent event){ 
		log.info("PostConstruct " + this.getClass().getName());
	}
	
	public void destroy(@Observes @Destroyed(ApplicationScoped.class) Object init) {
		log.info("Destroy " + this.getClass().getName());
	}
	
	
}
