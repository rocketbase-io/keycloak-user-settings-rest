import org.junit.Ignore;
import org.junit.Test;
import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.authentication.ClientCredentialsProviderUtils;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.keycloak.util.JsonSerialization;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Form;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by mirco on 08.05.17.
 */
@Ignore
public class ProfileTest {

    @Test
    public void test() throws Exception {
        AdapterConfig cfg = new AdapterConfig();
        cfg.setRealm("demo");
        cfg.setAuthServerUrl("http://localhost:8080/auth");
        cfg.setResource("account");

        Map<String, Object> cred = new HashMap<>();
        cred.put("secret", "4ed92746-f74c-4813-945f-3fab61f74632");

        cfg.setCredentials(cred);

        KeycloakDeployment c = KeycloakDeploymentBuilder.build(cfg);
        Form params = new Form();
        params.param(OAuth2Constants.GRANT_TYPE, OAuth2Constants.PASSWORD);
        params.param(OAuth2Constants.CLIENT_ID, "account");
        params.param(OAuth2Constants.PASSWORD, "mirco");
        params.param("username", "Test0r");

        Map<String, String> reqHeaders = new HashMap<>();
        Map<String, String> reqParams = new HashMap<>();
        ClientCredentialsProviderUtils.setClientCredentials(c, reqHeaders, reqParams);

        Client client = ClientBuilder.newClient();
        Builder request = client.target((c.getTokenUrl()))
                .request();

        for (Map.Entry<String, String> header : reqHeaders.entrySet()) {
            request.header(header.getKey(), header.getValue());
        }
        for (Map.Entry<String, String> param : reqParams.entrySet()) {
            params.param(param.getKey(), param.getValue());
        }


        String json = request.post(Entity.form(params), String.class);

        AccessTokenResponse tokenResp = JsonSerialization.readValue(json, AccessTokenResponse.class);
        System.out.println(tokenResp.getIdToken());

    }
}
