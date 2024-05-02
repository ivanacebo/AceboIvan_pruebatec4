package com.AceboIvanPruebaTec.Agencia;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AgenciaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgenciaApplication.class, args);
	}
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().info(new Info()
				.title("API aGENCIA")
				.version("0.0.1")
				.description("Api sobre una agencia que se dedica a reservar hoteles y vuelos por el mundo")
				.contact(new Contact()
						.name("Iván Acebo González")
						.url("https://www.linkedin.com/public-profile/settings?lipi=urn%3Ali%3Apage%3Ad_flagship3_profile_self_edit_contact-info%3BZCbTZjWCQuC4CLEaj14HsQ%3D%3D")
						.email("ivan.acebo.glez@gmail.com")));

	}


}
