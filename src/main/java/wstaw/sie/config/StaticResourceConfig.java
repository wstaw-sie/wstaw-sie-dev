package wstaw.sie.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class StaticResourceConfig extends WebMvcConfigurerAdapter {

	@Value("${photo.path}")
	private String photoPath;
	
	@Value("${url.photo.path}")
	private String urlPhotoPath;
	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler(urlPhotoPath+"/**").addResourceLocations("file:" + photoPath + "/");

        super.addResourceHandlers(registry);
    }
}