package wstaw.sie.pac4j;

import org.pac4j.core.client.Clients;
import org.pac4j.j2e.configuration.ClientsFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service(value = "myClientsFactory")
@PropertySource(value = { "classpath:application.properties" })
public class MyClientsFactory implements ClientsFactory {

	private Clients clients;

	public final static String FACEBOOK_FIELDS = "id,name,first_name,middle_name,last_name,gender,locale,languages,link,email";

	@Override
	public Clients build() {
		String facebookKey = System.getenv("FACEBOOK_KEY");
		String facebookSecret = System.getenv("FACEBOOK_SECRET");
		String googleKey = System.getenv("GOOGLE_KEY");
		String googleSecret = System.getenv("GOOGLE_SECRET");
		String oauthCallbackLink = System.getenv("OAUTH_CALLBACK_LINK");
		final MyFacebookClient facebookClient = new MyFacebookClient(facebookKey, facebookSecret);
		facebookClient.setFields(FACEBOOK_FIELDS);
		final MyGoogle2Client googleClient = new MyGoogle2Client(googleKey, googleSecret);
		clients = new Clients(oauthCallbackLink, googleClient, facebookClient);
		return clients;
	}

}