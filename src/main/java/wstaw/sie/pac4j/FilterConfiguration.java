package wstaw.sie.pac4j;

import javax.annotation.Resource;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import org.pac4j.j2e.configuration.ClientsFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import wstaw.sie.util.PathBean;


@Configuration
public class FilterConfiguration {
	
	@Resource
	ClientsFactory clientsFactory;
	
	@Resource
	PathBean pathBean;
	
    @Bean
    public FilterRegistrationBean googlePlusFilterRegistration() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(facebookFilter());
        registration.addInitParameter("clientName", "MyGoogle2Client");
        registration.addUrlPatterns("/googleplus/*");
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.addInitParameter("clientsFactory", "wstaw.sie.pac4j.MyClientsFactory");
        registration.setName("MyGoogle2Client");
        return registration;
    }
    
    @Bean(name = "GooglePlusFilter")
    public Filter googlePlusFilter() {
        return new GooglePlusFilter();
    }
     
    @Bean
    public FilterRegistrationBean facebookFilterRegistration() {
    	
    	FilterRegistrationBean registration = new FilterRegistrationBean();
    	registration.setFilter(googlePlusFilter());
    	registration.addInitParameter("clientName", "MyFacebookClient");
    	registration.addUrlPatterns("/facebook/*");
    	registration.setDispatcherTypes(DispatcherType.REQUEST);
    	registration.addInitParameter("clientsFactory", "wstaw.sie.pac4j.MyClientsFactory");
    	registration.setName("MyFacebookClient");
    	return registration;
    }

    @Bean(name = "FacebookFilter")
    public Filter facebookFilter() {
    	return new FacebookFilter();
    }
    
    @Bean
    public FilterRegistrationBean callbackFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(callbackFilter());
        registration.addUrlPatterns("/oauth2callback");
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.addInitParameter("clientsFactory", "wstaw.sie.pac4j.MyClientsFactory");
        registration.addInitParameter("defaultUrl", pathBean.getPath("profile"));
        registration.setName("MyCallbackFilter");
        return registration;
    }

    @Bean(name = "MyCallbackFilter")
    public Filter callbackFilter() {
        return new MyCallbackFilter();
    }
	
}
