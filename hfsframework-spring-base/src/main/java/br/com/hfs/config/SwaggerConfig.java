package br.com.hfs.config;

//@EnableSwagger2
//@Configuration
public class SwaggerConfig {
	
	//http://localhost:8084/swagger-ui.html
	/*
	@Bean
    public Docket api() {		
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.hfs"))
                .paths(PathSelectors.ant("/**"))
                .build()
                .ignoredParameterTypes(AdmUser.class, HfsUserDetails.class)
                .globalRequestParameters(
                		Collections.singletonList(
                                new RequestParameterBuilder()
                                    .name("Authorization")
                                    .description("Header to Token JWT")
                                    .in(ParameterType.HEADER)
                                    .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
                                    .required(false)
                                    .build()));
    }
    */
}
