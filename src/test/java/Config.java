import java.util.logging.Logger;

/**
 * Created by mirco on 09.05.17.
 */
class Config {

    private Logger logger = Logger.getLogger("Config");

    private final String host = getHost();

    private String getHost() {
        String env = System.getenv("KEYCLOAK_SERVER_URI");
        if (env == null) {
            logger.info("No Env Variable, falling back to localhost");
            env = "http://localhost";
        }
        return env;
    }

    private final String port = "8080";

    private final String resource = "/auth/realms/demo/profile";

    /**
     * Service URl
     */
    public final String SERVICE = host + ":" + port + resource;

    /**
     * Auth URL
     */
    public final String AUTH = host + ":" + port + "/auth/realms/demo/protocol/openid-connect/token";

    public final String USER_A_ID = "a0da83a0-7c8c-4d99-a16c-05612704f067";

    public final String USER_B_ID = "002cefe6-0bb5-4276-ab6e-bbd6143e8a14";
}
