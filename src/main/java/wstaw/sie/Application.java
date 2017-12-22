package wstaw.sie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import wstaw.sie.config.ConnectorConfig;
import wstaw.sie.config.HibernateConfig;
import wstaw.sie.config.StaticResourceConfig;

@ComponentScan({"wstaw.sie.controller", "wstaw.sie.service",
	"wstaw.sie.model.session", "wstaw.sie.pac4j", "wstaw.sie.util",
	"drawer.com.logic", "drawer.com.bean", "wstaw.sie.cron"})
@EntityScan("wstaw.sie.model.entity")
@Import({HibernateConfig.class, StaticResourceConfig.class, ConnectorConfig.class})
@EnableScheduling
@Configuration
@SpringBootApplication
public class Application extends SpringBootServletInitializer  {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
}
  
  @Override
  protected SpringApplicationBuilder configure(
          SpringApplicationBuilder application) {
      return application.sources(Application.class);
  }
  
}
