package wstaw.sie.scribe;

import org.scribe.builder.api.StateApi20;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

public class MyFacebookApi extends StateApi20 {

	private static final String AUTHORIZE_URL_WITH_STATE = "https://www.facebook.com/dialog/oauth?display=popup&client_id=%s&redirect_uri=%s&state=%s";
	private static final String SCOPED_AUTHORIZE_URL_WITH_STATE = AUTHORIZE_URL_WITH_STATE + "&scope=%s";

	@Override
	public AccessTokenExtractor getAccessTokenExtractor() {
		return new JsonTokenExtractor();
	}

	@Override
	public String getAccessTokenEndpoint() {
		return "https://graph.facebook.com/oauth/access_token";
	}

	@Override
	public String getAuthorizationUrl(final OAuthConfig config, final String state) {
		Preconditions.checkEmptyString(config.getCallback(),
				"Must provide a valid url as callback. Facebook does not support OOB");

		// Append scope if present
		if (config.hasScope()) {
			return String.format(SCOPED_AUTHORIZE_URL_WITH_STATE, config.getApiKey(),
					OAuthEncoder.encode(config.getCallback()), OAuthEncoder.encode(state),
					OAuthEncoder.encode(config.getScope()));
		} else {
			return String.format(AUTHORIZE_URL_WITH_STATE, config.getApiKey(),
					OAuthEncoder.encode(config.getCallback()), OAuthEncoder.encode(state));
		}
	}

}
