import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.logging.Logger;

/**
 * Created by mirco on 09.05.17.
 */
class AbstractKeycloakIT {

    protected final String USER_A_ORIGINAL = new JSONObject()
            .put("given_name", "Auth Test")
            .put("family_name", "Testmann1")
            .put("preferred_username", "test0r222")
            .put("email", "test@test.testing")
            .put("avatar", "test.png")
            .toString();

    protected final String USER_A_NAME_CHANGE = "a0da83a0-7c8c-4d99-a16c-05612704f067";

    protected final String USER_B_NAME_CHANGE = "002cefe6-0bb5-4276-ab6e-bbd6143e8a14";

    protected final String USER_NO_NAME_CHANGE = "59c5663c-7017-4a22-9bc4-534a4714b928";

    private final Logger logger = Logger.getLogger("AbstractKeycloakIT");

    protected final String host = getHost();

    private final String port = "8080";

    /**
     * Auth URL
     */
    protected final String AUTH = host + ":" + port + "/auth/realms/demo/protocol/openid-connect/token";

    protected final String AUTH_NO_NAME_CHANGE = host + ":" + port + "/auth/realms/no-name-change/protocol/openid-connect/token";

    private final String resource = "/auth/realms/demo/profile";

    /**
     * Service URl
     */
    protected final String SERVICE = host + ":" + port + resource;

    private String noNameChange = "/auth/realms/no-name-change/profile";


    protected final String SERVICE_NO_NAME_CHANGE = host + ":" + port + noNameChange;

    private String getHost() {
        String env = "http://" + System.getenv("KEYCLOAK_SERVER_URI");
        logger.info("KEYCLOAK_SERVER_URI: " + env);
        if (System.getenv("KEYCLOAK_SERVER_URI") == null) {
            logger.info("Using local host");
            env = "http://localhost";
        }
        return env;
    }


    public String getBearer(String username, String password, String clientSecret, String authUrl) throws IOException {

        OkHttpClient client = new OkHttpClient();
        String data = MessageFormat.format("grant_type=password&client_id=account&username={0}&password={1}&client_secret={2}",
                username,
                password,
                clientSecret);
        logger.info("Getting Token from " + authUrl + " with " +
                data);
        RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"),
                data);
        Request req = new Request.Builder().post(body)
                .url(authUrl)
                .build();
        Response response = client.newCall(req)
                .execute();
        String jsonData = response.body()
                .string();
        JSONObject json = new JSONObject(jsonData);
        logger.info("Response " + json.toString());
        return json.get("access_token")
                .toString();
    }
}
