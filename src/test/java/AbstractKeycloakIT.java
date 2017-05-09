import org.json.JSONObject;

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

    protected final String host = getHost();

    protected final String USER_A_ID = "a0da83a0-7c8c-4d99-a16c-05612704f067";

    protected final String USER_B_ID = "002cefe6-0bb5-4276-ab6e-bbd6143e8a14";

    private final String port = "8080";

    /**
     * Auth URL
     */
    protected final String AUTH = host + ":" + port + "/auth/realms/demo/protocol/openid-connect/token";

    private final String resource = "/auth/realms/demo/profile";

    /**
     * Service URl
     */
    protected final String SERVICE = host + ":" + port + resource;

    private String getHost() {
        String env = "http://" + System.getenv("KEYCLOAK_SERVER_URI");
        if (System.getenv("KEYCLOAK_SERVER_URI") == null) {
            env = "http://localhost";
        }
        return env;
    }
}
