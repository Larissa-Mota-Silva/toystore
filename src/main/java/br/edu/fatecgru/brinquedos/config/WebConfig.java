package br.edu.fatecgru.brinquedos.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

	    String caminho = System.getProperty("user.dir") + "/uploads/";

	    registry.addResourceHandler("/img/**")
	            .addResourceLocations(
	                "classpath:/static/img/",
	                "file:" + caminho
	            );
	}
}