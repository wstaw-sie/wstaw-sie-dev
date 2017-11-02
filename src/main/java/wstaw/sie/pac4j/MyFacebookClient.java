package wstaw.sie.pac4j;

import java.io.OutputStream;

import org.apache.commons.lang3.StringUtils;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.oauth.client.FacebookClient;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;
import org.scribe.oauth.StateOAuth20ServiceImpl;

import wstaw.sie.scribe.MyFacebookApi;

class MyFacebookClient extends FacebookClient {

	public MyFacebookClient(String key, String secret) {
		super(key,secret);
	}
	
    @Override
    protected void internalInit() {
        super.internalInit();
        CommonHelper.assertNotBlank("fields", this.fields);
        this.api20 = new MyFacebookApi();
        if(StringUtils.isNotBlank(this.scope)) {
            this.service = new StateOAuth20ServiceImpl(this.api20, new OAuthConfig(this.key, this.secret, this.callbackUrl, SignatureType.Header, this.scope, (OutputStream)null), this.connectTimeout, this.readTimeout, this.proxyHost, this.proxyPort);
        } else {
            this.service = new StateOAuth20ServiceImpl(this.api20, new OAuthConfig(this.key, this.secret, this.callbackUrl, SignatureType.Header, (String)null, (OutputStream)null), this.connectTimeout, this.readTimeout, this.proxyHost, this.proxyPort);
        }

    }
}