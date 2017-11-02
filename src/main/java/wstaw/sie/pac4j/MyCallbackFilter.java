package wstaw.sie.pac4j;

/*
Copyright 2013 - 2014 Jerome Leleu

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/
import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.pac4j.core.client.Client;
import org.pac4j.core.context.J2EContext;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.Credentials;
import org.pac4j.core.exception.RequiresHttpAction;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.j2e.configuration.ClientsConfiguration;
import org.pac4j.j2e.filter.ClientsConfigFilter;
import org.pac4j.j2e.filter.RequiresAuthenticationFilter;
import org.pac4j.j2e.util.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wstaw.sie.model.entity.User;
import wstaw.sie.service.FloodService;
import wstaw.sie.service.LoginService;

/**
 * COPIED FROM @see org.pac4j.j2e.filter.CallbackFilter and added login to "Wstaw się!"
 * 
* This filter handles the callback from the provider to finish the authentication process.
* 
* @author Jerome Leleu
* @since 1.0.0
*/
public class MyCallbackFilter extends ClientsConfigFilter {
	
  @Resource
  private LoginService loginService;
  
  @Resource
  private FloodService floodService;
  
  private static final Logger logger = LoggerFactory.getLogger(MyCallbackFilter.class);
  
  private String defaultUrl = "/";
  private String blockedUrl = "/loginBlocked";
  //private String profileUrl = "/profile"l
  
  @Override
  public void init(final FilterConfig filterConfig) throws ServletException {
      super.init(filterConfig);
      this.defaultUrl = filterConfig.getInitParameter("defaultUrl");
      CommonHelper.assertNotBlank("defaultUrl", this.defaultUrl);
  }
  
  @Override
  @SuppressWarnings({
      "unchecked", "rawtypes"
  })
  protected void internalFilter(final HttpServletRequest request, final HttpServletResponse response,
                                final HttpSession session, final FilterChain chain) throws IOException,
      ServletException {
	  
      if (floodService.isIPBlocked(request.getRemoteAddr()))
      {
        logger.info("IP blocked for request: "+request);
        response.sendRedirect(this.blockedUrl);
        return;
      }
      
      final WebContext context = new J2EContext(request, response);
      final Client client = ClientsConfiguration.getClients().findClient(context);
      logger.debug("client : {}", client);
      
      final Credentials credentials;
      try {
          credentials = client.getCredentials(context);
      } catch (final RequiresHttpAction e) {
          logger.debug("extra HTTP action required : {}", e.getCode());
          return;
      }
      logger.debug("credentials : {}", credentials);
      
      // get user profile
      final CommonProfile profile = (CommonProfile) client.getUserProfile(credentials, context);
      logger.debug("profile : {}", profile);
      
      if (profile != null) {
          // only save profile when it's not null
    	  logger.info("MyCallbackFilter, profile is: "+ profile.toString());
          UserUtils.setProfile(session, profile);
          loginService.login(profile);
      }
      
      final String requestedUrl = (String) session.getAttribute(RequiresAuthenticationFilter.ORIGINAL_REQUESTED_URL);
      logger.debug("requestedUrl : {}", requestedUrl);
      //if (CommonHelper.isNotBlank(requestedUrl)) {
      //    response.sendRedirect(requestedUrl);
      //} else {
          response.sendRedirect(this.defaultUrl);
      //}
  }
}

